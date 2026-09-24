package com.juan.ordermanagement.service;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductByProductCode(String productCode);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(String productCode, ProductRequest request);
    void deleteProduct(String productCode);
}