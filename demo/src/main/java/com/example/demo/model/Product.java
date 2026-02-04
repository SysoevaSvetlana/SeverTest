package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Продукт")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор продукта", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Название продукта", example = "Красное яблоко")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Категория продукта", example = "APPLE")
    private Category category;
}
