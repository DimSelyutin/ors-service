package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserById(UUID id);

    Optional<User> findUserByEmail(String email);
}
