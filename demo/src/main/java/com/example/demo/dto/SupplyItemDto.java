package com.example.demo.dto;

import com.example.demo.model.SupplyItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "DTO элемента поставки")
public class SupplyItemDto {

    @Schema(description = "ID элемента поставки", example = "1")
    private Long id;

    @Schema(description = "ID продукта", example = "1")
    private Long productId;

    @Schema(description = "Название продукта", example = "Красное яблоко")
    private String productName;

    @Schema(description = "Вес в кг", example = "100.50")
    private BigDecimal weightKg;

    @Schema(description = "Цена за кг", example = "120.00")
    private BigDecimal pricePerKg;

    @Schema(description = "Общая стоимость", example = "12060.00")
    private BigDecimal totalPrice;

    public static SupplyItemDto from(SupplyItem entity) {
        return new SupplyItemDto(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getWeightKg(),
                entity.getPricePerKg(),
                entity.getTotalPrice()
        );
    }
}

