package com.example.demo.repository;

import com.example.demo.dto.SupplyReportRow;
import com.example.demo.model.SupplyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

    @Query("SELECT " +
           "CAST(s.supplier AS string) AS supplierId, " +
           "CAST(s.supplier AS string) AS supplierName, " +
           "si.product.id AS productId, " +
           "si.product.name AS productName, " +
           "s.warehouse.id AS warehouseId, " +
           "s.warehouse.name AS warehouseName, " +
           "SUM(si.weightKg) AS totalWeight, " +
           "SUM(si.totalPrice) AS totalPrice " +
           "FROM SupplyItem si " +
           "JOIN si.supply s " +
           "WHERE s.supplyDate BETWEEN :from AND :to " +
           "AND s.warehouse.buyer.id = :buyerId " +
           "GROUP BY s.supplier, si.product.id, si.product.name, s.warehouse.id, s.warehouse.name " +
           "ORDER BY s.supplier, si.product.name, s.warehouse.name")
    List<SupplyReportRow> getReport(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("buyerId") Long buyerId
    );
}
