package com.example.demo.controller;

import com.example.demo.dto.SupplyReportRowDto;
import com.example.demo.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Отчёты по поставкам (только для BUYER)")
@SecurityRequirement(name = "Bearer Authentication")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/supplies")
    @Operation(
            summary = "Отчёт по поставкам за период",
            description = """
                    Отчёт по поступлению видов продукции по поставщикам за выбранный период для конкретного покупателя.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчёт успешно сформирован",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = SupplyReportRowDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный период (дата начала больше даты окончания)"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не BUYER)")
            }
    )
    public ResponseEntity<List<SupplyReportRowDto>> getSupplyReport(
            @Parameter(description = "Дата начала периода", example = "2026-02-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            
            @Parameter(description = "Дата окончания периода", example = "2026-02-28", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<SupplyReportRowDto> report = reportService.getSupplyReport(from, to);
        return ResponseEntity.ok(report);
    }
}

