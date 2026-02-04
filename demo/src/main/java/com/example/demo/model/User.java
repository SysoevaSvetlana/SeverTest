package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.Collection;
import java.util.Collections;



@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Пользователь системы")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    @Schema(description = "Email пользователя", example = "buyer@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Пароль пользователя (хэш)", example = "hashedPassword")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Роль пользователя", example = "BUYER")
    private Role role;

    // Только для SUPPLIER, nullable для BUYER
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Schema(description = "Поставщик, которого представляет пользователь (только для SUPPLIER)")
    private SupplierEnum supplier;

    // --- UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
