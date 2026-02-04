package com.example.demo.dto;

import com.example.demo.model.SupplierEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Запрос на создание поставки")
public class CreateSupplyRequest {

    @NotNull(message = "ID склада обязателен")
    @Schema(description = "ID склада, на который поступает поставка", example = "1")
    private Long warehouseId;

    @NotNull(message = "Поставщик обязателен")
    @Schema(description = "Поставщик", example = "SUPPLIER_A")
    private SupplierEnum supplier;

    @NotNull(message = "Дата поставки обязательна")
    @Schema(description = "Дата поставки", example = "2026-02-05")
    private LocalDate supplyDate;

    @NotEmpty(message = "Список товаров не может быть пустым")
    @Valid
    @Schema(description = "Список товаров в поставке")
    private List<CreateSupplyItemRequest> items;
}

