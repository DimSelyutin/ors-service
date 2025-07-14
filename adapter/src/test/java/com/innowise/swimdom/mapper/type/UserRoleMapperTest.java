package com.innowise.swimdom.mapper.type;

import static org.junit.jupiter.api.Assertions.*;

import com.innowise.swimdom.openapi.model.UserRole;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class UserRoleMapperTest {

    private final UserRoleMapper mapper = Mappers.getMapper(UserRoleMapper.class);

    @Test
    void toUserRoleEntity_mapsCorrectly() {
        assertEquals(com.innowise.swimdom.enums.UserRole.ADMIN, mapper.toUserRoleEntity(UserRole.ADMIN));
        assertEquals(com.innowise.swimdom.enums.UserRole.USER, mapper.toUserRoleEntity(UserRole.USER));
        assertNull(mapper.toUserRoleEntity(null));
    }

    @Test
    void toUserRoleDto_mapsCorrectly() {
        assertEquals(UserRole.ADMIN, mapper.toUserRoleDto(com.innowise.swimdom.enums.UserRole.ADMIN));
        assertEquals(UserRole.USER, mapper.toUserRoleDto(com.innowise.swimdom.enums.UserRole.USER));
        assertNull(mapper.toUserRoleDto(null));
    }
}

