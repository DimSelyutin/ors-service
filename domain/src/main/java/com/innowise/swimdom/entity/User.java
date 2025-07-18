package com.innowise.swimdom.entity;

import com.innowise.swimdom.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entity for User.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 100)
    private String email;

    @Column(nullable = false, length = 255)
    @Size(min = 8, max = 255, message = "Password length must be between 8 and 255 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, and one special character"
    )
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private String surname;

    @Size(max = 15)
    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UserRole role;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Getter GrantedAuthority.
     *
     * @return collection granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    /**
     * Getter email.
     *
     * @return email.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Getter isAccountNonExpired.
     *
     * @return isAccountNonExpired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Getter isAccountNonLocked.
     *
     * @return isAccountNonLocked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Getter isCredentialsNonExpired.
     *
     * @return isCredentialsNonExpired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Getter isEnabled.
     *
     * @return isEnabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setRole(UserRole userRole) {

    }
}
