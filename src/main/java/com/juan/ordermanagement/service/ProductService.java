package com.juan.ordermanagement.service;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductByProductCode(String productCode);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse updateProduct(String productCode, ProductRequest request);
    void deleteProduct(String productCode);
    Page<ProductResponse> searchProductsByName(String name, Pageable pageable);
    Page<ProductResponse> searchProductByStock(Integer stock, Pageable pageable);
    Page<ProductResponse> searchProductByPrice(BigDecimal price, Pageable pageable);
    Page<ProductResponse> searchProductByStockAndPrice(Integer stock, BigDecimal price, Pageable pageable);
}