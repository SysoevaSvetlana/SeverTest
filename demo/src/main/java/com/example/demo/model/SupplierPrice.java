package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "supplier_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Цена на продукт от поставщика на определенный период")
public class SupplierPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор цены", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Поставщик", example = "SUPPLIER_A")
    private SupplierEnum supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @DecimalMin("0.0")
    @Column(nullable = false)
    @Schema(description = "Цена за кг", example = "120.5")
    private BigDecimal pricePerKg;

    @NotNull
    @Schema(description = "Дата начала действия цены", example = "2026-02-01")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "Дата окончания действия цены", example = "2026-02-10")
    private LocalDate endDate;
}
