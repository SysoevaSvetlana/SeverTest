package com.example.demo.service;

import com.example.demo.dto.SupplyReportRowDto;
import com.example.demo.exeption.BusinessException;
import com.example.demo.repository.SupplyItemRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final SupplyItemRepository supplyItemRepository;

    @PreAuthorize("hasRole('BUYER')")
    public List<SupplyReportRowDto> getSupplyReport(LocalDate from, LocalDate to) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        if (from.isAfter(to)) {
            throw new BusinessException("Дата начала больше даты окончания");
        }

        return supplyItemRepository.getReport(from, to, currentUser.getUser().getId()).stream()
                .map(SupplyReportRowDto::from)
                .toList();
    }
}

