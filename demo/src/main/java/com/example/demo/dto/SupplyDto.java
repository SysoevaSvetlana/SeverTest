package com.example.demo.dto;

import com.example.demo.model.Supply;
import com.example.demo.model.SupplierEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Schema(description = "DTO поставки")
public class SupplyDto {

    @Schema(description = "ID поставки", example = "1")
    private Long id;

    @Schema(description = "Поставщик", example = "SUPPLIER_A")
    private SupplierEnum supplier;

    @Schema(description = "ID склада", example = "1")
    private Long warehouseId;

    @Schema(description = "Название склада", example = "Склад №1")
    private String warehouseName;

    @Schema(description = "Дата поставки", example = "2026-02-05")
    private LocalDate supplyDate;

    @Schema(description = "ID пользователя, создавшего поставку", example = "1")
    private Long createdById;

    @Schema(description = "Email пользователя, создавшего поставку", example = "buyer@example.com")
    private String createdByEmail;

    @Schema(description = "Список товаров в поставке")
    private List<SupplyItemDto> items;

    public static SupplyDto from(Supply entity) {
        return new SupplyDto(
                entity.getId(),
                entity.getSupplier(),
                entity.getWarehouse().getId(),
                entity.getWarehouse().getName(),
                entity.getSupplyDate(),
                entity.getCreatedBy().getId(),
                entity.getCreatedBy().getEmail(),
                entity.getItems() != null 
                    ? entity.getItems().stream()
                        .map(SupplyItemDto::from)
                        .collect(Collectors.toList())
                    : List.of()
        );
    }
}

