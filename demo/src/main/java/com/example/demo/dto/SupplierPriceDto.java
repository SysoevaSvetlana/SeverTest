package com.example.demo.dto;

import com.example.demo.model.SupplierEnum;
import com.example.demo.model.SupplierPrice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "DTO цены поставщика")
public class SupplierPriceDto {

    @Schema(description = "ID цены", example = "1")
    private Long id;

    @Schema(description = "Поставщик", example = "SUPPLIER_A")
    private SupplierEnum supplier;

    @Schema(description = "ID продукта", example = "1")
    private Long productId;

    @Schema(description = "Название продукта", example = "Красное яблоко")
    private String productName;

    @Schema(description = "Цена за кг", example = "120.50")
    private BigDecimal pricePerKg;

    @Schema(description = "Дата начала действия цены", example = "2026-02-01")
    private LocalDate startDate;

    @Schema(description = "Дата окончания действия цены", example = "2026-02-28")
    private LocalDate endDate;

    public static SupplierPriceDto from(SupplierPrice entity) {
        return new SupplierPriceDto(
                entity.getId(),
                entity.getSupplier(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getPricePerKg(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }
}