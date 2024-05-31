package com.demo.repository;

import com.demo.model.product.ProductEntity;
import com.demo.model.sale.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Sale findByEndDateBefore(LocalDateTime endDate);
}
