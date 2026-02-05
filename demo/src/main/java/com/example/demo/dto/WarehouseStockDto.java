package com.example.demo.dto;

import com.example.demo.model.WarehouseStock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация об остатках товара на складе")
public class WarehouseStockDto {

    @Schema(description = "ID записи", example = "1")
    private Long id;

    @Schema(description = "ID склада", example = "1")
    private Long warehouseId;

    @Schema(description = "Название склада", example = "Склад №1 (Москва)")
    private String warehouseName;

    @Schema(description = "ID продукта", example = "1")
    private Long productId;

    @Schema(description = "Название продукта", example = "Красное яблоко")
    private String productName;

    @Schema(description = "Количество товара на складе (кг)", example = "1500.50")
    private BigDecimal quantityKg;

    public static WarehouseStockDto from(WarehouseStock entity) {
        return WarehouseStockDto.builder()
                .id(entity.getId())
                .warehouseId(entity.getWarehouse().getId())
                .warehouseName(entity.getWarehouse().getName())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getName())
                .quantityKg(entity.getQuantityKg())
                .build();
    }
}

