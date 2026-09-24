package com.juan.ordermanagement.controller;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;
import com.juan.ordermanagement.entity.Product;
import com.juan.ordermanagement.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //createProduct()	POST	/api/products
    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request){
        return productService.createProduct(request);
    }

    //getProductByProductCode()	GET	/api/products/{productCode}
    @GetMapping("/{productCode}")
    public ProductResponse getProductByProductCode(@PathVariable String productCode){
        return productService.getProductByProductCode(productCode);
    }

    //getAllProducts()	GET	/api/products
    @GetMapping
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    //updateProduct()	PUT	/api/products/{productCode}
    @PutMapping("/{productCode}")
    public ProductResponse updateProduct(@PathVariable String productCode,
                                        @RequestBody ProductRequest request){
        return productService.updateProduct(productCode, request);
    }

    //deleteProduct()	DELETE	/api/products/{productCode}
    @DeleteMapping("/{productCode}")
    public void deleteProduct(@PathVariable String productCode){
        productService.deleteProduct(productCode);
    }
}
