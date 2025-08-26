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

    default UserRole toUserRoleEntity(com.innowise.swimdom.openapi.model.UserRole dtoRole) {
        if (dtoRole == null) {
            return null;
        }
        return UserRole.valueOf(dtoRole.getValue());
    }

    default com.innowise.swimdom.openapi.model.UserRole toUserRoleDto(UserRole entityRole) {
        if (entityRole == null) {
            return null;
        }
        for (com.innowise.swimdom.openapi.model.UserRole dtoRole :
            com.innowise.swimdom.openapi.model.UserRole.values()) {
            if (dtoRole.getValue().equals(entityRole.name())) {
                return dtoRole;
            }
        }
        throw new IllegalArgumentException("Unknown UserRole: " + entityRole);
    }
}

