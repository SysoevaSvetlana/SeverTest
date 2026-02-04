package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Элемент поставки (товар и его количество)")
public class CreateSupplyItemRequest {

    @NotNull(message = "ID продукта обязателен")
    @Schema(description = "ID продукта", example = "1")
    private Long productId;

    @NotNull(message = "Вес обязателен")
    @DecimalMin(value = "0.01", message = "Вес должен быть больше 0")
    @Schema(description = "Вес продукта в кг", example = "100.50")
    private BigDecimal weightKg;
}

