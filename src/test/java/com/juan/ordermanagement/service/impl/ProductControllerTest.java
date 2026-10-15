package com.juan.ordermanagement.controller;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;
import com.juan.ordermanagement.exception.ResourceNotFoundException;
import com.juan.ordermanagement.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturnProductByProductCode() throws Exception {
        // Arrange
        ProductResponse response = new ProductResponse();
        response.setProductCode("P001");
        response.setName("Basketball");
        response.setPrice(BigDecimal.valueOf(1500));
        response.setStock(10);

        when(productService.getProductByProductCode("P001"))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/products/P001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode").value("P001"))
                .andExpect(jsonPath("$.name").value("Basketball"))
                .andExpect(jsonPath("$.price").value(1500))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {
        // Arrange
        when(productService.getProductByProductCode("P999"))
                .thenThrow(ResourceNotFoundException.class);

        // Act + Assert
        mockMvc.perform(get("/api/products/P999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProduct() throws Exception{
        //Arrange
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductCode("P002");
        productResponse.setName("Basketball");
        productResponse.setPrice(BigDecimal.valueOf(2000));
        productResponse.setStock(15);

        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(productResponse);

        //Act + Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                            "productCode": "P002",
                            "name": "Basketball",
                            "price": 2000,
                            "stock": 15
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode").value("P002"))
                .andExpect(jsonPath("$.name").value("Basketball"))
                .andExpect(jsonPath("$.price").value(2000))
                .andExpect(jsonPath("$.stock").value(15));
    }
}