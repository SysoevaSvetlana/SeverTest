package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SupplyReportRowDto {

    private Long supplierId;
    private String supplierName;

    private Long productId;
    private String productName;

    private Long warehouseId;
    private String warehouseName;

    private BigDecimal totalWeight;   // суммарный вес за период
    private BigDecimal totalPrice;    // суммарная стоимость за период

    /**
     * Преобразование из проекции SupplyReportRow (JPQL/Native query)
     */
    public static SupplyReportRowDto from(SupplyReportRow row) {
        return new SupplyReportRowDto(
                row.getSupplierId(),
                row.getSupplierName(),
                row.getProductId(),
                row.getProductName(),
                row.getWarehouseId(),
                row.getWarehouseName(),
                row.getTotalWeight(),
                row.getTotalPrice()
        );
    }
}
