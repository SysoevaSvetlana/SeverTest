package com.example.demo.dto;

import com.example.demo.model.Warehouse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO склада")
public class WarehouseDto {

    @Schema(description = "ID склада", example = "1")
    private Long id;

    @Schema(description = "Название склада", example = "Склад №1")
    private String name;

    @Schema(description = "ID покупателя-владельца", example = "1")
    private Long buyerId;

    @Schema(description = "Email покупателя-владельца", example = "buyer@example.com")
    private String buyerEmail;

    public static WarehouseDto from(Warehouse entity) {
        return new WarehouseDto(
                entity.getId(),
                entity.getName(),
                entity.getBuyer().getId(),
                entity.getBuyer().getEmail()
        );
    }
}

