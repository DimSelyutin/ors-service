package com.innowise.swimdom.mapper.type;

import com.innowise.swimdom.enums.UserRole;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for UserRole.
 */
@Mapper(componentModel = SPRING)
public interface UserRoleMapper {

    UserRole toUserRoleEntity(com.innowise.swimdom.openapi.model.UserRole userRole);

    com.innowise.swimdom.openapi.model.UserRole toUserRoleDto(UserRole userRole);
}

