package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "supplies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Поставка")
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор поставки", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Поставщик", example = "SUPPLIER_A")
    private SupplierEnum supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @Schema(description = "Склад, на который поступает поставка")
    private Warehouse warehouse;

    @NotNull
    @Schema(description = "Дата поставки", example = "2026-02-05")
    private LocalDate supplyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @Schema(description = "Пользователь-покупатель, создавший поставку")
    private User createdBy;

    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplyItem> items;
}
