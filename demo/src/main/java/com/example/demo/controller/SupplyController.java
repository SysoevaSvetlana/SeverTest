package com.example.demo.controller;

import com.example.demo.dto.CreateSupplyRequest;
import com.example.demo.dto.SupplyDto;
import com.example.demo.service.SupplyService;
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
@RequestMapping("/api/supplies")
@RequiredArgsConstructor
@Tag(name = "Supplies", description = "Приёмка поставок от поставщиков (только для BUYER)")
@SecurityRequirement(name = "Bearer Authentication")
public class SupplyController {

    private final SupplyService supplyService;

    @PostMapping
    @Operation(
            summary = "Принять поставку от поставщика",
            description = """
               
                    В одной поставке может быть несколько видов продукции.
                    Для каждого продукта автоматически подбирается актуальная цена на дату поставки.
                    
                    Доступно только для BUYER на свои склады.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Поставка успешно принята",
                            content = @Content(schema = @Schema(implementation = SupplyDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации или нет цены на дату поставки"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не BUYER или чужой склад)"),
                    @ApiResponse(responseCode = "404", description = "Склад или продукт не найден")
            }
    )
    public ResponseEntity<SupplyDto> createSupply(
            @Valid @RequestBody CreateSupplyRequest request
    ) {
        SupplyDto supply = supplyService.createSupply(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(supply);
    }

    @GetMapping
    @Operation(
            summary = "Получить список поставок",
            description = "Получение списка всех поставок на склады текущего покупателя с пагинацией. Доступно только для BUYER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список поставок успешно получен"
                    ),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не BUYER)")
            }
    )
    public ResponseEntity<Page<SupplyDto>> getSupplies(
            @Parameter(description = "Параметры пагинации (page, size, sort)")
            @PageableDefault(size = 20, sort = "supplyDate") Pageable pageable
    ) {
        Page<SupplyDto> supplies = supplyService.getSupplies(pageable);
        return ResponseEntity.ok(supplies);
    }
}

