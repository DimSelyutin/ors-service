package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.mapper.type.UserRoleMapper;
import com.innowise.swimdom.mapper.type.UserRoleMapperImpl;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.openapi.model.UserResponseDTO;
import com.innowise.swimdom.openapi.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthMapperTest {

    @Mock
    private UserRoleMapper userRoleMapper;

    private AuthMapper authMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authMapper = new AuthMapperImpl(userRoleMapper);
    }

    @Test
    void toAuthResponse_bothNull_returnsNull() {
        AuthResponse result = authMapper.toAuthResponse(null, null);
        assertNull(result);
    }

    @Test
    void toAuthResponse_tokenOnly_setsTokenAndNullUser() {
        String token = "token123";
        AuthResponse result = authMapper.toAuthResponse(token, null);

        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertNull(result.getUser());
    }

    @Test
    void toAuthResponse_userOnly_setsUserAndNullToken() {
        User user = createUser();
        UserResponseDTO userDto = createUserResponseDto();

        when(userRoleMapper.toUserRoleDto(user.getRole())).thenReturn(userDto.getRole());

        AuthResponse result = authMapper.toAuthResponse(null, user);

        assertNotNull(result);
        assertNull(result.getToken());
        assertNotNull(result.getUser());
        assertEquals(userDto.getEmail(), result.getUser().getEmail());
        assertEquals(userDto.getId(), result.getUser().getId());
        verify(userRoleMapper).toUserRoleDto(user.getRole());
    }

    @Test
    void toAuthResponse_tokenAndUser_setsBoth() {
        String token = "token123";
        User user = createUser();
        UserResponseDTO userDto = createUserResponseDto();

        when(userRoleMapper.toUserRoleDto(user.getRole())).thenReturn(userDto.getRole());

        AuthResponse result = authMapper.toAuthResponse(token, user);

        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertNotNull(result.getUser());
        assertEquals(userDto.getEmail(), result.getUser().getEmail());
        verify(userRoleMapper).toUserRoleDto(user.getRole());
    }

    @Test
    void toUserResponse_nullUser_returnsNull() {
        assertNull(authMapper.toUserResponse(null));
    }

    @Test
    void toUserResponse_validUser_mapsCorrectly() {
        User user = createUser();
        UserRole roleDto = com.innowise.swimdom.openapi.model.UserRole.fromValue("USER");

        when(userRoleMapper.toUserRoleDto(user.getRole())).thenReturn(roleDto);

        UserResponseDTO dto = authMapper.toUserResponse(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getSurname(), dto.getSurname());
        assertEquals(user.getPhone(), dto.getPhone());
        assertEquals(roleDto, dto.getRole());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());

        verify(userRoleMapper).toUserRoleDto(user.getRole());
    }

    @Test
    void toUser_nullDto_returnsNull() {
        assertNull(authMapper.toUser(null));
    }

    @Test
    void toUser_validDto_mapsCorrectly() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("pass123");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setPhone("+1234567890");
        UserRole roleDto = UserRole.fromValue("ADMIN");
        dto.setRole(roleDto);

        com.innowise.swimdom.enums.UserRole entityRole = com.innowise.swimdom.enums.UserRole.valueOf("ADMIN");

        when(userRoleMapper.toUserRoleEntity(roleDto)).thenReturn(entityRole);

        User user = authMapper.toUser(dto);

        assertNotNull(user);
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getSurname(), user.getSurname());
        assertEquals(dto.getPhone(), user.getPhone());

        verify(userRoleMapper).toUserRoleEntity(roleDto);
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.fromString("c9260c5c-e8e6-4491-950b-9cdfcf6f234a"));
        user.setEmail("user@example.com");
        user.setName("Alice");
        user.setSurname("Smith");
        user.setPhone("+111111111");
        user.setRole(com.innowise.swimdom.enums.UserRole.USER);
        user.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        user.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 12, 0));
        return user;
    }

    private UserResponseDTO createUserResponseDto() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(UUID.fromString("c9260c5c-e8e6-4491-950b-9cdfcf6f234a"));
        dto.setEmail("user@example.com");
        dto.setName("Alice");
        dto.setSurname("Smith");
        dto.setPhone("+111111111");
        dto.setRole(com.innowise.swimdom.openapi.model.UserRole.fromValue("USER"));
        dto.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        dto.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 12, 0));
        return dto;
    }
}

