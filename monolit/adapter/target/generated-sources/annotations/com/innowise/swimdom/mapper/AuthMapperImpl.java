package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.mapper.type.UserRoleMapper;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.openapi.model.UserResponseDTO;
import com.innowise.swimdom.openapi.model.UserUpdateRequestDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:59+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    private final UserRoleMapper userRoleMapper;

    @Autowired
    public AuthMapperImpl(UserRoleMapper userRoleMapper) {

        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public AuthResponse toAuthResponse(String token, User user) {
        if ( token == null && user == null ) {
            return null;
        }

        AuthResponse authResponse = new AuthResponse();

        authResponse.setToken( token );
        authResponse.setUser( toUserResponse( user ) );

        return authResponse;
    }

    @Override
    public UserResponseDTO toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setRole( userRoleMapper.toUserRoleDto( user.getRole() ) );
        userResponseDTO.setId( user.getId() );
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setName( user.getName() );
        userResponseDTO.setSurname( user.getSurname() );
        userResponseDTO.setPhone( user.getPhone() );
        userResponseDTO.setCreatedAt( user.getCreatedAt() );
        userResponseDTO.setUpdatedAt( user.getUpdatedAt() );

        return userResponseDTO;
    }

    @Override
    public User toUser(UserCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );
        user.setName( dto.getName() );
        user.setSurname( dto.getSurname() );
        user.setPhone( dto.getPhone() );
        user.setRole( userRoleMapper.toUserRoleEntity( dto.getRole() ) );

        return user;
    }

    @Override
    public void updateUserFromDto(UserUpdateRequestDTO dto, User user) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            user.setId( dto.getId() );
        }
        if ( dto.getEmail() != null ) {
            user.setEmail( dto.getEmail() );
        }
        if ( dto.getPassword() != null ) {
            user.setPassword( dto.getPassword() );
        }
        if ( dto.getName() != null ) {
            user.setName( dto.getName() );
        }
        if ( dto.getSurname() != null ) {
            user.setSurname( dto.getSurname() );
        }
        if ( dto.getPhone() != null ) {
            user.setPhone( dto.getPhone() );
        }
        if ( dto.getRole() != null ) {
            user.setRole( userRoleMapper.toUserRoleEntity( dto.getRole() ) );
        }
    }
}
