package com.example.demo.repository;

import com.example.demo.dto.SupplyReportRow;
import com.example.demo.model.SupplyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

    // Отчёт за период по поставщикам и продуктам
    @Query("SELECT " +
           "s.supplier AS supplier, " +
           "si.product.name AS productName, " +
           "SUM(si.weightKg) AS totalWeight, " +
           "SUM(si.totalPrice) AS totalPrice " +
           "FROM SupplyItem si " +
           "JOIN si.supply s " +
           "WHERE s.supplyDate BETWEEN :from AND :to " +
           "GROUP BY s.supplier, si.product.name " +
           "ORDER BY s.supplier, si.product.name")
    List<SupplyReportRow> getReport(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
    /*
    SELECT
    s.supplier AS supplier,
    p.name AS productName,
    SUM(si.weight_kg) AS totalWeight,
    SUM(si.total_price) AS totalPrice
    FROM supply_items si
    JOIN supplies s ON si.supply_id = s.id
    JOIN products p ON si.product_id = p.id
    WHERE s.supply_date BETWEEN '2026-01-01' AND '2026-01-31'
    GROUP BY s.supplier, p.name
    ORDER BY s.supplier, p.name;
     */
}
