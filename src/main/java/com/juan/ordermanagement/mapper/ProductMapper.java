package com.juan.ordermanagement.mapper;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;
import com.juan.ordermanagement.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product){
        ProductResponse response = new ProductResponse();

        response.setProductCode(product.getProductCode());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());

        return response;

    }

    public Product toEntity(ProductRequest productRequest){
        Product product = new Product();
        product.setProductCode(productRequest.getProductCode());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());

        return product;
    }
}
