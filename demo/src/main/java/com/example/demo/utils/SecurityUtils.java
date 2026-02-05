package com.example.demo.utils;

import com.example.demo.model.Role;
import com.example.demo.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {}

    public static CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new com.example.demo.exeption.AccessDeniedException("Пользователь не аутентифицирован");
        }

        return user;
    }

    public static void requireRole(Role role) {
        CustomUserDetails user = getCurrentUser();
        if (user.getRole() != role) {
            throw new com.example.demo.exeption.AccessDeniedException("Требуется роль: " + role);
        }
    }
}

