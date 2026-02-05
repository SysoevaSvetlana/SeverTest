package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на авторизацию")
public class LoginRequest {

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя", example = "buyer@example.com")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;
}
