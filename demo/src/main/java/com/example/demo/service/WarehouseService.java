package com.example.demo.service;

import com.example.demo.dto.CreateWarehouseRequest;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.dto.WarehouseStockDto;
import com.example.demo.exeption.AccessDeniedException;
import com.example.demo.exeption.NotFoundException;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.repository.WarehouseStockRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseStockRepository warehouseStockRepository;

    @PreAuthorize("hasRole('BUYER')")
    @Transactional
    public WarehouseDto createWarehouse(CreateWarehouseRequest request) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        Warehouse warehouse = new Warehouse();
        warehouse.setName(request.getName());
        warehouse.setBuyer(currentUser.getUser());

        return WarehouseDto.from(warehouseRepository.save(warehouse));
    }

    @PreAuthorize("hasRole('BUYER')")
    @Transactional(readOnly = true)
    public Page<WarehouseDto> getMyWarehouses(Pageable pageable) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        return warehouseRepository.findByBuyerId(currentUser.getUser().getId(), pageable)
                .map(WarehouseDto::from);
    }

    @PreAuthorize("hasRole('BUYER')")
    @Transactional(readOnly = true)
    public List<WarehouseStockDto> getWarehouseStock(Long warehouseId) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("Склад не найден"));

        if (!warehouse.getBuyer().getId().equals(currentUser.getUser().getId())) {
            throw new AccessDeniedException("Склад не принадлежит текущему BUYER");
        }

        return warehouseStockRepository.findByWarehouseId(warehouseId).stream()
                .map(WarehouseStockDto::from)
                .toList();
    }
}
