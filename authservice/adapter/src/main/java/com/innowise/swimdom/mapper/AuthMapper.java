package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.mapper.type.UserRoleMapper;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.openapi.model.UserResponseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for auth.
 *
 * @author DimSelyutin
 */
@Mapper(componentModel = SPRING, uses = UserRoleMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "token", source = "token")
    AuthResponse toAuthResponse(String token, User user);

    @Mapping(target = "role", source = "user.role")
    UserResponseDTO toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreateRequestDTO dto);

}
