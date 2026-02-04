package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "supply_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Элемент поставки (конкретный продукт и количество)")
public class SupplyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор элемента поставки", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id", nullable = false)
    private Supply supply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @DecimalMin("0.0")
    @Column(nullable = false)
    @Schema(description = "Вес продукта в кг", example = "100")
    private BigDecimal weightKg;

    @DecimalMin("0.0")
    @Column(nullable = false)
    @Schema(description = "Фактическая цена за кг на момент поставки", example = "120")
    private BigDecimal pricePerKg;

    @DecimalMin("0.0")
    @Column(nullable = false)
    @Schema(description = "Общая стоимость позиции", example = "12000")
    private BigDecimal totalPrice;
}
