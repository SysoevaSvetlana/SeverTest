package com.example.demo.dto;

import java.math.BigDecimal;

public interface SupplyReportRow {
    String getSupplier();      // поставщик
    String getProductName();   // название продукта
    BigDecimal getTotalWeight();  // сумма веса
    BigDecimal getTotalPrice();   // сумма стоимости
}