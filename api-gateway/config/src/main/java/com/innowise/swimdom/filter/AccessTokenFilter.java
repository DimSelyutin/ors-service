package com.innowise.swimdom.filter;

import com.innowise.swimdom.exception.UnauthorizedException;
import com.innowise.swimdom.service.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * Request filter. Validates access token and, on success, adds user info headers
 * to the routed request.
 */
@Component
public class AccessTokenFilter extends AbstractGatewayFilterFactory<AccessTokenFilter.Config> {

    private static final String LOGIN_HEADER = "X-User-Login";

    private static final String ROLE_HEADER = "X-User-Roles";

    private static final String MASTER_ACCESS_TOKEN = "test_user";

    private static final String MASTER_ROLE = "ADMIN";

    private final JwtProvider jwtProvider;

    @Value("${api-gateway.isMasterTokenEnabled}")
    private boolean isMasterTokenEnabled;

    @Autowired
    public AccessTokenFilter(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authorization == null) {
                throw new UnauthorizedException("Authorization token not found");
            }
            String accessToken = authorization;
            if (authorization.startsWith("Bearer ")) {
                accessToken = authorization.substring("Bearer ".length());
            }
            String login;
            String role;
            if (jwtProvider.isAccessTokenValid(accessToken)) {
                login = jwtProvider.getAccessClaims(accessToken).getSubject();
                Object rolesClaim = jwtProvider.getAccessClaims(accessToken).get("roles");
                if (rolesClaim instanceof java.util.Collection<?> rolesCollection) {
                    role = String.join(",", rolesCollection.stream().map(String::valueOf).toList());
                } else if (rolesClaim == null) {
                    role = "";
                } else {
                    role = rolesClaim.toString();
                }
            } else {
                throw new UnauthorizedException("Access token not valid");
            }

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(LOGIN_HEADER, login)
                .header(ROLE_HEADER, role)
                .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    /**
     * Helper configuration object.
     */
    public static class Config {
    }
}
