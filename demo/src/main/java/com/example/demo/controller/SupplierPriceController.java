package com.example.demo.controller;

import com.example.demo.dto.CreateSupplierPriceRequest;
import com.example.demo.dto.SupplierPriceDto;
import com.example.demo.dto.UpdateSupplierPriceRequest;
import com.example.demo.service.SupplierPriceService;
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

@RestController
@RequestMapping("/api/supplier/prices")
@RequiredArgsConstructor
@Tag(name = "Supplier Prices", description = "Управление ценами поставщика (только для SUPPLIER)")
@SecurityRequirement(name = "Bearer Authentication")
public class SupplierPriceController {

    private final SupplierPriceService supplierPriceService;

    @PostMapping
    @Operation(
            summary = "Создать цену на продукт",
            description = "Поставщик заранее сообщает цену на продукт на определённый период. Доступно только для SUPPLIER.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Цена успешно создана",
                            content = @Content(schema = @Schema(implementation = SupplierPriceDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации или периоды цен пересекаются"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не SUPPLIER)")
            }
    )
    public ResponseEntity<SupplierPriceDto> createPrice(
            @Valid @RequestBody CreateSupplierPriceRequest request
    ) {
        SupplierPriceDto price = supplierPriceService.createPrice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(price);
    }

    @GetMapping
    @Operation(
            summary = "Получить список своих цен",
            description = "Получение списка цен текущего поставщика с пагинацией. Доступно только для SUPPLIER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список цен успешно получен"
                    ),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не SUPPLIER)")
            }
    )
    public ResponseEntity<Page<SupplierPriceDto>> getMyPrices(
            @Parameter(description = "Параметры пагинации (page, size, sort)")
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<SupplierPriceDto> prices = supplierPriceService.getMyPrices(pageable);
        return ResponseEntity.ok(prices);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить цену",
            description = "Обновление существующей цены (изменение цены за кг и периода действия). Продукт изменить нельзя. Доступно только владельцу цены.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Цена успешно обновлена",
                            content = @Content(schema = @Schema(implementation = SupplierPriceDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации или периоды цен пересекаются"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (чужая цена)"),
                    @ApiResponse(responseCode = "404", description = "Цена не найдена")
            }
    )
    public ResponseEntity<SupplierPriceDto> updatePrice(
            @Parameter(description = "ID цены", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateSupplierPriceRequest request
    ) {
        SupplierPriceDto price = supplierPriceService.updatePrice(id, request);
        return ResponseEntity.ok(price);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить цену",
            description = "Удаление цены. Доступно только владельцу цены (SUPPLIER).",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Цена успешно удалена"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (чужая цена)"),
                    @ApiResponse(responseCode = "404", description = "Цена не найдена")
            }
    )
    public ResponseEntity<Void> deletePrice(
            @Parameter(description = "ID цены", example = "1")
            @PathVariable Long id
    ) {
        supplierPriceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}

