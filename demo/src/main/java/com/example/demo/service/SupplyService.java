package com.example.demo.service;

import com.example.demo.dto.CreateSupplyItemRequest;
import com.example.demo.dto.CreateSupplyRequest;
import com.example.demo.dto.SupplyDto;
import com.example.demo.exeption.AccessDeniedException;
import com.example.demo.exeption.BusinessException;
import com.example.demo.exeption.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SupplierPriceRepository;
import com.example.demo.repository.SupplyRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private final SupplyRepository supplyRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierPriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final WarehouseStockRepository warehouseStockRepository;

    @PreAuthorize("hasRole('BUYER')")
    @Transactional
    public SupplyDto createSupply(CreateSupplyRequest request) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Склад не найден"));

        if (!warehouse.getBuyer().getId().equals(currentUser.getUser().getId())) {
            throw new AccessDeniedException("Склад не принадлежит текущему BUYER");
        }

        Supply supply = new Supply();
        supply.setWarehouse(warehouse);
        supply.setSupplier(request.getSupplier());
        supply.setSupplyDate(request.getSupplyDate());
        supply.setCreatedBy(warehouse.getBuyer());

        List<SupplyItem> items = new ArrayList<>();

        for (CreateSupplyItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Продукт не найден"));

            SupplierPrice price = priceRepository.findActualPrice(
                    request.getSupplier(),
                    product.getId(),
                    request.getSupplyDate()
            ).orElseThrow(() -> new BusinessException(
                    "Нет цены на дату " + request.getSupplyDate() + " для продукта " + product.getName() +
                    " от поставщика " + request.getSupplier()
            ));

            SupplyItem item = new SupplyItem();
            item.setSupply(supply);
            item.setProduct(product);
            item.setWeightKg(itemRequest.getWeightKg());
            item.setPricePerKg(price.getPricePerKg());
            item.setTotalPrice(price.getPricePerKg().multiply(itemRequest.getWeightKg()));

            items.add(item);
        }

        supply.setItems(items);

        Supply savedSupply = supplyRepository.save(supply);

        for (SupplyItem item : savedSupply.getItems()) {
            updateWarehouseStock(warehouse, item.getProduct(), item.getWeightKg());
        }

        return SupplyDto.from(savedSupply);
    }

    private void updateWarehouseStock(Warehouse warehouse, Product product, java.math.BigDecimal weightKg) {
        WarehouseStock stock = warehouseStockRepository
                .findByWarehouseAndProduct(warehouse.getId(), product.getId())
                .orElse(WarehouseStock.builder()
                        .warehouse(warehouse)
                        .product(product)
                        .quantityKg(java.math.BigDecimal.ZERO)
                        .build());

        stock.setQuantityKg(stock.getQuantityKg().add(weightKg));
        warehouseStockRepository.save(stock);
    }


    @PreAuthorize("hasRole('BUYER')")
    @Transactional(readOnly = true)
    public Page<SupplyDto> getSupplies(Pageable pageable) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        return supplyRepository.findByWarehouseBuyerId(currentUser.getUser().getId(), pageable)
                .map(SupplyDto::from);
    }
}
