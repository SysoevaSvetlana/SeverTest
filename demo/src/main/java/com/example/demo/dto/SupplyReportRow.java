package com.example.demo.dto;

import java.math.BigDecimal;

public interface SupplyReportRow {
    Long getSupplierId();
    String getSupplierName();

    Long getProductId();
    String getProductName();

    Long getWarehouseId();
    String getWarehouseName();

    BigDecimal getTotalWeight();
    BigDecimal getTotalPrice();
}