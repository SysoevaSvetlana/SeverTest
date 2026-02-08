package com.example.demo.dto;

import com.example.demo.model.SupplierEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SupplyReportRowDto {

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
        SupplierEnum supplierEnum = row.getSupplierName();
        String supplierName = supplierEnum == null ? null : supplierEnum.name();

        return new SupplyReportRowDto(
                supplierName,
                row.getProductId(),
                row.getProductName(),
                row.getWarehouseId(),
                row.getWarehouseName(),
                row.getTotalWeight(),
                row.getTotalPrice()
        );
    }
}
