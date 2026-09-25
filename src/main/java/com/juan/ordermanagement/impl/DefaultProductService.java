package com.juan.ordermanagement.impl;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;
import com.juan.ordermanagement.entity.Product;
import com.juan.ordermanagement.exception.ResourceNotFoundException;
import com.juan.ordermanagement.mapper.ProductMapper;
import com.juan.ordermanagement.repository.ProductRepository;
import com.juan.ordermanagement.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultProductService implements ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;

    public DefaultProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if(productRepository.existsByProductCode(request.getProductCode())){
            throw new ResourceNotFoundException("Product already exists: " + request.getProductCode());
        }
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);

    }

    @Override
    public ProductResponse getProductByProductCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(()-> new ResourceNotFoundException("Product Code doesn't exist: " + productCode));
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for(Product product : products){
            productResponses.add(productMapper.toResponse(product));
        }
        return productResponses;
    }

    @Override
    public ProductResponse updateProduct(String productCode, ProductRequest request) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(()-> new ResourceNotFoundException("Product code doesn't exist: " + productCode));
           if(productRepository.existsByProductCode(request.getProductCode())
                   && !product.getProductCode().equals(request.getProductCode())){
               throw new ResourceNotFoundException("Product code already exist: " + request.getProductCode());
           }
           product.setProductCode(request.getProductCode());
           product.setName(request.getName());
           product.setPrice(request.getPrice());
           product.setStock(request.getStock());
           productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    public void deleteProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(()-> new ResourceNotFoundException("Product code doesn't exist: " + productCode));
        productRepository.delete(product);
    }
}
