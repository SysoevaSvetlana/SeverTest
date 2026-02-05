package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Запрос на создание цены поставщика")
public class CreateSupplierPriceRequest {

    @NotNull(message = "ID продукта обязателен")
    @Schema(description = "ID продукта", example = "1")
    private Long productId;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @Schema(description = "Цена за кг", example = "120.50")
    private BigDecimal pricePerKg;

    @NotNull(message = "Дата начала обязательна")
    @Schema(description = "Дата начала действия цены", example = "2026-02-01")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания обязательна")
    @Schema(description = "Дата окончания действия цены", example = "2026-02-28")
    private LocalDate endDate;
}
