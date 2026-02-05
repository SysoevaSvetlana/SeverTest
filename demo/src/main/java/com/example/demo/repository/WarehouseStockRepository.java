package com.example.demo.repository;

import com.example.demo.model.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Long> {

    @Query("SELECT ws FROM WarehouseStock ws " +
           "WHERE ws.warehouse.id = :warehouseId AND ws.product.id = :productId")
    Optional<WarehouseStock> findByWarehouseAndProduct(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId
    );

    @Query("SELECT ws FROM WarehouseStock ws " +
           "WHERE ws.warehouse.id = :warehouseId " +
           "ORDER BY ws.product.name")
    List<WarehouseStock> findByWarehouseId(@Param("warehouseId") Long warehouseId);
}

