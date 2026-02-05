package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Supply Chain Management API",
                version = "1.0",
                description = """
                        API для управления поставками продукции от поставщиков на склады.
                        
                        ## Основной функционал:
                        - Регистрация и аутентификация пользователей (BUYER и SUPPLIER)
                        - Управление складами (только для BUYER)
                        - Управление ценами на продукцию (только для SUPPLIER)
                        - Приёмка поставок от поставщиков (только для BUYER)
                        - Отчёты по поставкам за период
                        
                        ## Роли пользователей:
                        - **BUYER** - покупатель, владелец складов, принимает поставки
                        - **SUPPLIER** - поставщик (SUPPLIER_A, SUPPLIER_B, SUPPLIER_C), устанавливает цены
                        
                        ## Аутентификация:
                        Используется JWT токен. Для доступа к защищённым эндпоинтам:
                        1. Зарегистрируйтесь через POST /api/auth/register
                        2. Войдите через POST /api/auth/login и получите токен
                        3. Нажмите кнопку "Authorize" и введите токен в формате: Bearer <ваш_токен>
                        """,
                contact = @Contact(
                        name = "API Support",
                        email = "support@example.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local server")
        },
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Введите JWT токен в формате: Bearer <токен>"
)
public class OpenApiConfig {
}

