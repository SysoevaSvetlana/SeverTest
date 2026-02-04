package com.example.demo.repository;

import com.example.demo.model.Supply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepository extends JpaRepository<Supply, Long> {

    // Получение поставок для складов покупателя с пагинацией
    Page<Supply> findByWarehouseBuyerId(Long buyerId, Pageable pageable);

}
