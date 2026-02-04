package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание склада")
public class CreateWarehouseRequest {

    @NotBlank(message = "Название склада обязательно")
    @Size(min = 3, max = 100, message = "Название склада должно быть от 3 до 100 символов")
    @Schema(description = "Название склада", example = "Склад №1")
    private String name;
}

