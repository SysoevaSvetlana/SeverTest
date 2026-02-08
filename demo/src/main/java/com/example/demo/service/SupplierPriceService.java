package com.example.demo.service;

import com.example.demo.dto.CreateSupplierPriceRequest;
import com.example.demo.dto.SupplierPriceDto;
import com.example.demo.dto.UpdateSupplierPriceRequest;
import com.example.demo.exeption.AccessDeniedException;
import com.example.demo.exeption.BusinessException;
import com.example.demo.exeption.NotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.SupplierPrice;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SupplierPriceRepository;
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
public class SupplierPriceService {

    private final SupplierPriceRepository priceRepository;
    private final ProductRepository productRepository;

    @PreAuthorize("hasRole('SUPPLIER')")
    @Transactional
    public SupplierPriceDto createPrice(CreateSupplierPriceRequest request) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Продукт не найден"));

        boolean overlap = priceRepository.existsOverlappingPrice(
                currentUser.getSupplier(),
                product.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (overlap) {
            throw new BusinessException("Периоды цен пересекаются");
        }

        SupplierPrice price = new SupplierPrice();
        price.setSupplier(currentUser.getSupplier());
        price.setProduct(product);
        price.setPricePerKg(request.getPricePerKg());
        price.setStartDate(request.getStartDate());
        price.setEndDate(request.getEndDate());

        return SupplierPriceDto.from(priceRepository.save(price));
    }

    @PreAuthorize("hasRole('SUPPLIER')")
    @Transactional
    public SupplierPriceDto updatePrice(Long priceId, UpdateSupplierPriceRequest request) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        SupplierPrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new NotFoundException("Цена не найдена"));

        if (!price.getSupplier().equals(currentUser.getSupplier())) {
            throw new AccessDeniedException("Чужая цена");
        }

        boolean overlap = priceRepository.existsOverlappingPriceExcludingSelf(
                currentUser.getSupplier(),
                price.getProduct().getId(),
                request.getStartDate(),
                request.getEndDate(),
                priceId
        );

        if (overlap) {
            throw new BusinessException("Периоды цен пересекаются");
        }

        price.setPricePerKg(request.getPricePerKg());
        price.setStartDate(request.getStartDate());
        price.setEndDate(request.getEndDate());

        return SupplierPriceDto.from(priceRepository.save(price));
    }

    @PreAuthorize("hasRole('SUPPLIER')")
    @Transactional
    public void deletePrice(Long priceId) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        SupplierPrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new NotFoundException("Цена не найдена"));

        if (!price.getSupplier().equals(currentUser.getSupplier())) {
            throw new AccessDeniedException("Чужая цена");
        }

        priceRepository.delete(price);
    }

    @PreAuthorize("hasRole('SUPPLIER')")
    @Transactional(readOnly = true)
    public Page<SupplierPriceDto> getMyPrices(Pageable pageable) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        return priceRepository.findBySupplier(currentUser.getSupplier(), pageable)
                .map(SupplierPriceDto::from);
    }
}
