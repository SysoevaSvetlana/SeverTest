package com.example.demo.repository;


import com.example.demo.model.SupplierEnum;
import com.example.demo.model.SupplierPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface SupplierPriceRepository extends JpaRepository<SupplierPrice, Long> {

    Page<SupplierPrice> findBySupplier(SupplierEnum supplier, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM SupplierPrice p " +
           "WHERE p.supplier = :supplier " +
           "AND p.product.id = :productId " +
           "AND p.startDate <= :endDate " +
           "AND p.endDate >= :startDate")
    boolean existsOverlappingPrice(@Param("supplier") SupplierEnum supplier,
                                   @Param("productId") Long productId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM SupplierPrice p " +
           "WHERE p.supplier = :supplier " +
           "AND p.product.id = :productId " +
           "AND p.id <> :priceId " +
           "AND p.startDate <= :endDate " +
           "AND p.endDate >= :startDate")
    boolean existsOverlappingPriceExcludingSelf(@Param("supplier") SupplierEnum supplier,
                                                @Param("productId") Long productId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("priceId") Long priceId);

    @Query("SELECT p FROM SupplierPrice p " +
           "WHERE p.supplier = :supplier " +
           "AND p.product.id = :productId " +
           "AND :date BETWEEN p.startDate AND p.endDate")
    Optional<SupplierPrice> findActualPrice(@Param("supplier") SupplierEnum supplier,
                                            @Param("productId") Long productId,
                                            @Param("date") LocalDate date);
}

