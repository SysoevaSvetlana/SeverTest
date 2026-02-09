package com.example.demo.service;

import com.example.demo.dto.SupplyReportRow;
import com.example.demo.dto.SupplyReportRowDto;
import com.example.demo.exeption.BusinessException;
import com.example.demo.model.Role;
import com.example.demo.model.SupplierEnum;
import com.example.demo.model.User;
import com.example.demo.repository.SupplyItemRepository;
import com.example.demo.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.utils.SecurityUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для ReportService")
class ReportServiceTest {

    @Mock
    private SupplyItemRepository supplyItemRepository;

    @InjectMocks
    private ReportService reportService;

    private User testBuyer;
    private CustomUserDetails testUserDetails;
    private LocalDate fromDate;
    private LocalDate toDate;

    @BeforeEach
    void setUp() {

        testBuyer = new User();
        testBuyer.setId(1L);
        testBuyer.setEmail("buyer@test.com");
        testBuyer.setRole(Role.BUYER);

        testUserDetails = new CustomUserDetails(testBuyer);


        fromDate = LocalDate.of(2026, 2, 1);
        toDate = LocalDate.of(2026, 2, 28);
    }

    @Test
    @DisplayName("Успешное получение отчёта с данными")
    void getSupplyReport_Success_WithData() {

        List<SupplyReportRow> mockReportRows = Arrays.asList(
                createMockReportRow(SupplierEnum.SUPPLIER_A, 1L, "Красное яблоко", 1L, "Склад №1",
                        new BigDecimal("500.00"), new BigDecimal("60000.00")),
                createMockReportRow(SupplierEnum.SUPPLIER_A, 2L, "Зелёная груша", 1L, "Склад №1",
                        new BigDecimal("300.00"), new BigDecimal("45000.00")),
                createMockReportRow(SupplierEnum.SUPPLIER_B, 1L, "Красное яблоко", 2L, "Склад №2",
                        new BigDecimal("400.00"), new BigDecimal("48000.00"))
        );

        when(supplyItemRepository.getReport(eq(fromDate), eq(toDate), eq(testBuyer.getId())))
                .thenReturn(mockReportRows);

        // Act
        List<SupplyReportRowDto> result;
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(testUserDetails);
            result = reportService.getSupplyReport(fromDate, toDate);
        }


        assertNotNull(result);
        assertEquals(3, result.size());


        SupplyReportRowDto firstRow = result.get(0);
        assertEquals("SUPPLIER_A", firstRow.getSupplierName());
        assertEquals(1L, firstRow.getProductId());
        assertEquals("Красное яблоко", firstRow.getProductName());
        assertEquals(1L, firstRow.getWarehouseId());
        assertEquals("Склад №1", firstRow.getWarehouseName());
        assertEquals(new BigDecimal("500.00"), firstRow.getTotalWeight());
        assertEquals(new BigDecimal("60000.00"), firstRow.getTotalPrice());


        SupplyReportRowDto secondRow = result.get(1);
        assertEquals("SUPPLIER_A", secondRow.getSupplierName());
        assertEquals(2L, secondRow.getProductId());
        assertEquals("Зелёная груша", secondRow.getProductName());


        SupplyReportRowDto thirdRow = result.get(2);
        assertEquals("SUPPLIER_B", thirdRow.getSupplierName());
        assertEquals(2L, thirdRow.getWarehouseId());
        assertEquals("Склад №2", thirdRow.getWarehouseName());


        verify(supplyItemRepository, times(1)).getReport(fromDate, toDate, testBuyer.getId());
    }

    @Test
    @DisplayName("Успешное получение пустого отчёта")
    void getSupplyReport_Success_EmptyData() {

        when(supplyItemRepository.getReport(eq(fromDate), eq(toDate), eq(testBuyer.getId())))
                .thenReturn(Collections.emptyList());


        List<SupplyReportRowDto> result;
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(testUserDetails);
            result = reportService.getSupplyReport(fromDate, toDate);
        }


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplyItemRepository, times(1)).getReport(fromDate, toDate, testBuyer.getId());
    }

    @Test
    @DisplayName("Ошибка: дата начала больше даты окончания")
    void getSupplyReport_ThrowsException_WhenFromDateAfterToDate() {

        LocalDate invalidFromDate = LocalDate.of(2026, 2, 28);
        LocalDate invalidToDate = LocalDate.of(2026, 2, 1);


        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(testUserDetails);

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                reportService.getSupplyReport(invalidFromDate, invalidToDate);
            });

            assertEquals("Дата начала больше даты окончания", exception.getMessage());
        }


        verify(supplyItemRepository, never()).getReport(any(), any(), any());
    }


    /**
     * Вспомогательный метод для создания мок-объекта SupplyReportRow
     */
    private SupplyReportRow createMockReportRow(
            SupplierEnum supplier,
            Long productId,
            String productName,
            Long warehouseId,
            String warehouseName,
            BigDecimal totalWeight,
            BigDecimal totalPrice
    ) {
        return new SupplyReportRow() {
            @Override
            public SupplierEnum getSupplierName() {
                return supplier;
            }

            @Override
            public Long getProductId() {
                return productId;
            }

            @Override
            public String getProductName() {
                return productName;
            }

            @Override
            public Long getWarehouseId() {
                return warehouseId;
            }

            @Override
            public String getWarehouseName() {
                return warehouseName;
            }

            @Override
            public BigDecimal getTotalWeight() {
                return totalWeight;
            }

            @Override
            public BigDecimal getTotalPrice() {
                return totalPrice;
            }
        };
    }
}

