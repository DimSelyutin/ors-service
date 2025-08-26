package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.exception.AuthenticationException;
import com.innowise.swimdom.exception.IncorrectRefreshTokenException;
import com.innowise.swimdom.mapper.AuthMapper;
import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.JwtResponse;
import com.innowise.swimdom.openapi.model.RefreshJwtRequest;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.service.AuthenticationService;
import com.innowise.swimdom.service.UserService;
import com.innowise.swimdom.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;

import static com.innowise.swimdom.util.Constants.REFRESH_TOKEN;

/**
 * Service for authentication.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider tokenProvider;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Method for login user.
     *
     * @param loginRequest request with email and password.
     * @return AuthResponse.
     */
    @Override
    public AuthResponse login(AuthRequest loginRequest) {
        log.debug("Login - start: {}", loginRequest);

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User principal = (User) authentication.getPrincipal();

            String jwt =
                tokenProvider.generateAccessToken(authMapper.toUserResponse((User) authentication.getPrincipal()));
            log.debug("Login - end: {}", loginRequest);
            return authMapper.toAuthResponse(jwt, principal);
        } catch (BadCredentialsException | AuthenticationException ex) {
            log.debug("Login - exception: {}", loginRequest);
            throw new AuthenticationException(Constants.INVALID_CREDENTIAL);
        }
    }

    /**
     * Method for registering user.
     *
     * @param signupRequest request with email and password.
     * @return UserResponseDTO
     */
    @Override
    @Transactional
    public AuthResponse registerUser(UserCreateRequestDTO signupRequest) {
        log.debug("register user - start, {}", signupRequest.getEmail());
        User registeredUser = userService.createUser(signupRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateAccessToken(authMapper
            .toUserResponse((User) authentication.getPrincipal()));
        log.debug("register user - start, {}", registeredUser.getEmail());
        return authMapper.toAuthResponse(jwt, registeredUser);
    }

    public JwtResponse getNewAccessToken(RefreshJwtRequest refreshJwtRequest) {
        String refreshToken = refreshJwtRequest.getRefreshToken();
        if (tokenProvider.isRefreshTokenValid(refreshToken)) {
            String email = tokenProvider.getRefreshClaims(refreshToken).getSubject();
            String storedRefreshToken = (String) redisTemplate.opsForValue().get(email);

            if (storedRefreshToken != null && storedRefreshToken.equals(refreshToken)) {
                User principal = (User) userDetailsService.loadUserByUsername(email);
                String newAccessToken = tokenProvider.generateAccessToken(authMapper.toUserResponse(principal));
                String newRefreshToken = tokenProvider.generateRefreshToken(authMapper.toUserResponse(principal));

                saveRefreshTokenInRedis(Map.of(REFRESH_TOKEN, newRefreshToken), email);

                return new JwtResponse()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken);
            }
        }
        throw new IncorrectRefreshTokenException();
    }

    private Map<String, Object> saveRefreshTokenInRedis(Map<String, Object> map, String key) {
        String refreshToken = (String) map.get(REFRESH_TOKEN);
        Duration duration = tokenProvider.getRefreshTokenExpirationTime();
        redisTemplate.opsForValue().set(key, refreshToken, duration);
        return map;
    }
}
