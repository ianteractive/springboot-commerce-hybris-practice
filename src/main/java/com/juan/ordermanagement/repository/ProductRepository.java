package com.juan.ordermanagement.repository;

import com.juan.ordermanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    List<Product> findByStockGreaterThan(Integer stock);
    boolean existsByProductCode(String productCode);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
