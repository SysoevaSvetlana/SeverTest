package com.example.demo.controller;

import com.example.demo.dto.CreateWarehouseRequest;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.dto.WarehouseStockDto;
import com.example.demo.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouses", description = "Управление складами (только для BUYER)")
@SecurityRequirement(name = "Bearer Authentication")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(
            summary = "Создать склад",
            description = "Создание нового склада. Доступно только для пользователей с ролью BUYER.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Склад успешно создан",
                            content = @Content(schema = @Schema(implementation = WarehouseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не BUYER)")
            }
    )
    public ResponseEntity<WarehouseDto> createWarehouse(
            @Valid @RequestBody CreateWarehouseRequest request
    ) {
        WarehouseDto warehouse = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouse);
    }

    @GetMapping
    @Operation(
            summary = "Получить список своих складов",
            description = "Получение списка складов текущего покупателя с пагинацией. Доступно только для BUYER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список складов успешно получен"
                    ),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не BUYER)")
            }
    )
    public ResponseEntity<Page<WarehouseDto>> getMyWarehouses(
            @Parameter(description = "Параметры пагинации (page, size, sort)")
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<WarehouseDto> warehouses = warehouseService.getMyWarehouses(pageable);
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}/stock")
    @Operation(
            summary = "Получить остатки товаров на складе",
            description = "Получение списка всех товаров и их остатков на конкретном складе. Доступно только для владельца склада (BUYER).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Остатки успешно получены"
                    ),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не владелец склада)"),
                    @ApiResponse(responseCode = "404", description = "Склад не найден")
            }
    )
    public ResponseEntity<List<WarehouseStockDto>> getWarehouseStock(
            @Parameter(description = "ID склада")
            @PathVariable Long id
    ) {
        List<WarehouseStockDto> stock = warehouseService.getWarehouseStock(id);
        return ResponseEntity.ok(stock);
    }
}