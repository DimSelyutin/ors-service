package com.innowise.swimdom.mapper.type;

import com.innowise.swimdom.enums.UserRole;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for UserRole.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserRoleMapper {

    UserRole toUserRoleEntity(com.innowise.swimdom.openapi.model.UserRole userRole);

    com.innowise.swimdom.openapi.model.UserRole toUserRoleDto(UserRole userRole);
}

