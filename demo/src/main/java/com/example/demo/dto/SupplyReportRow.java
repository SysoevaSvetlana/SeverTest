package com.example.demo.dto;

import com.example.demo.model.SupplierEnum;
import java.math.BigDecimal;

public interface SupplyReportRow {
    SupplierEnum getSupplierName();

    Long getProductId();
    String getProductName();

    Long getWarehouseId();
    String getWarehouseName();

    BigDecimal getTotalWeight();
    BigDecimal getTotalPrice();
}