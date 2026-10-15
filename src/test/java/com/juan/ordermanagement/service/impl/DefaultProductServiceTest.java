package com.juan.ordermanagement.service.impl;

import com.juan.ordermanagement.dto.ProductResponse;
import com.juan.ordermanagement.entity.Product;
import com.juan.ordermanagement.exception.ResourceNotFoundException;
import com.juan.ordermanagement.impl.DefaultProductService;
import com.juan.ordermanagement.mapper.ProductMapper;
import com.juan.ordermanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductMapper productMapper;
    @InjectMocks
    DefaultProductService defaultProductService;

    //ARRANGE
    //  Prepare product and DTO
    //  Configure repository mock
    //  Configure mapper mock
    //
    //ACT
    //  Call searchProductsByName()
    //
    //ASSERT
    //  Check returned DTO
    //  Verify repository call
    @Test
    //searchProductsByName(String name, Pageable pageable)
    void shouldSearchProductsByName() {
        // Arrange
        //Search Keyword
        String name = "phone";
        //Pagination Settings
        Pageable pageable = PageRequest.of(0,10);

        //Sample Entity
        Product product = new Product();
        //Expected DTO
        ProductResponse expectedResponse = new ProductResponse();

        //Tell Mockito what the repository returns
        Page<Product> productPage = new PageImpl<>(List.of(product));
        //Configure the mock repository
        //When the service searches for "phone" with this Pageable, return our sample page instead of querying MySQL.
        when(productRepository.findByNameContainingIgnoreCase(name, pageable))
                .thenReturn(productPage);
        //Whenever productMapper.toResponse() receives this sample product, return expectedResponse.
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        // Act
        Page<ProductResponse> result = defaultProductService.searchProductsByName(name, pageable);
        // Assert
        assertEquals(expectedResponse, result.getContent().get(0));
        verify(productRepository).findByNameContainingIgnoreCase(name, pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoProductsMatch() {
        // Arrange
        String name = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of());
        when(productRepository.findByNameContainingIgnoreCase(name, pageable))
                .thenReturn(productPage);
        //Act
        Page<ProductResponse> result = defaultProductService.searchProductsByName(name, pageable);
        //Assert
        assertTrue(result.isEmpty());
        verify(productRepository).findByNameContainingIgnoreCase(name, pageable);


    }

    @Test
    void shouldSearchProductsByStock() {
        // Arrange
        Integer stock = 10;
        Pageable pageable = PageRequest.of(0,10);
        Product product = new Product();
        ProductResponse response = new ProductResponse();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findByStockGreaterThanEqual(stock, pageable))
                .thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(response);
        // Act
        Page<ProductResponse> result = defaultProductService.searchProductByStock(stock, pageable);
        // Assert
        assertEquals(response, result.getContent().get(0));
        verify(productRepository).findByStockGreaterThanEqual(stock, pageable);
    }

    @Test
    void shouldSearchProductsByPrice() {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(100);
        Pageable pageable = PageRequest.of(0,10);
        Product product = new Product();
        ProductResponse response = new ProductResponse();

        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findByPriceGreaterThanEqual(price, pageable))
                .thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(response);
        // Act
        Page<ProductResponse> result = defaultProductService.searchProductByPrice(price, pageable);
        // Assert
        assertEquals(response, result.getContent().get(0));
        verify(productRepository).findByPriceGreaterThanEqual(price, pageable);
    }

    @Test
    void shouldSearchProductsByStockAndPrice() {
        // Arrange
        Integer stock = 10;
        BigDecimal price = BigDecimal.valueOf(500);
        Pageable pageable = PageRequest.of(0,10);
        Product product = new Product();
        ProductResponse response = new ProductResponse();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository
                .findByStockGreaterThanEqualAndPriceLessThanEqual(stock, price, pageable))
                .thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(response);

        // Act
        Page<ProductResponse> result =
                defaultProductService.searchProductByStockAndPrice(
                        stock, price, pageable);
        // Assert
        assertEquals(response, result.getContent().get(0));
        verify(productRepository)
                .findByStockGreaterThanEqualAndPriceLessThanEqual(
                        stock, price, pageable);
    }

    @Test
    void shouldGetProductByProductCode() {
        // Arrange
        String productCode = "P001";
        Product product = new Product();
        ProductResponse response = new ProductResponse();

        when(productRepository.findByProductCode(productCode))
                .thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);
        // Act
        ProductResponse result = defaultProductService.getProductByProductCode(productCode);
        // Assert
        assertEquals(response, result);
        verify(productRepository).findByProductCode(productCode);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {
        // Arrange
        String productCode = "P999";
        when(productRepository.findByProductCode(productCode))
                .thenReturn(Optional.empty());
        // Act & Assert
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> defaultProductService.getProductByProductCode(productCode));
        assertEquals("Product Code doesn't exist: " + productCode, exception.getMessage());
        verify(productRepository).findByProductCode(productCode);
    }
}
