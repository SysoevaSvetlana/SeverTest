package com.example.demo.service;

import com.example.demo.dto.CreateWarehouseRequest;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @PreAuthorize("hasRole('BUYER')")
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
}
