package com.juan.ordermanagement.service.impl;

import com.juan.ordermanagement.dto.ProductRequest;
import com.juan.ordermanagement.dto.ProductResponse;
import com.juan.ordermanagement.entity.Product;
import com.juan.ordermanagement.exception.ResourceNotFoundException;
import com.juan.ordermanagement.impl.DefaultProductService;
import com.juan.ordermanagement.mapper.ProductMapper;
import com.juan.ordermanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExtraProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private DefaultProductService productService;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void shouldReturnProductWhenProductCodeExist(){
        //arrange
        String productCode = "P001";

        Product product = new Product();
        product.setProductCode(productCode);
        product.setName("Basketball");
        product.setPrice(BigDecimal.valueOf(1500));
        product.setStock(10);

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setProductCode(productCode);
        expectedResponse.setName("Basketball");
        expectedResponse.setPrice(BigDecimal.valueOf(1500));
        expectedResponse.setStock(10);

        when(productRepository.findByProductCode(productCode))
                .thenReturn(Optional.of(product));
        when(productMapper.toResponse(product))
                .thenReturn(expectedResponse);


        //act
        ProductResponse result = productService.getProductByProductCode(productCode);

        //assert
        assertNotNull(result);
        assertEquals(productCode, result.getProductCode());
        assertEquals("Basketball", result.getName());
        assertEquals(BigDecimal.valueOf(1500), result.getPrice());
        assertEquals(10, result.getStock());

        verify(productRepository).findByProductCode(productCode);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowErrorWhenMissingProductCode(){
        when(productRepository.findByProductCode("P999"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductByProductCode("P999"));

        verify(productRepository).findByProductCode("P999");
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void shouldSaveProductAndReturnResponse(){
        // Arrange
        ProductRequest request = new ProductRequest();
        // TODO: Set the request's product code, name, price, and stock.
        request.setProductCode("P001");
        request.setName("Basketball");
        request.setPrice(BigDecimal.valueOf(1500));
        request.setStock(10);

        Product product = new Product();
        // TODO: Set the matching entity values.
        product.setProductCode("P001");
        product.setName("Basketball");
        product.setPrice(BigDecimal.valueOf(1500));
        product.setStock(10);

        Product savedProduct = new Product();
        // TODO: Set the saved entity values.
        savedProduct.setProductCode(product.getProductCode());
        savedProduct.setName(product.getName());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setStock(product.getStock());

        ProductResponse expectedResponse = new ProductResponse();
        // TODO: Set expected response values.
        expectedResponse.setProductCode(savedProduct.getProductCode());
        expectedResponse.setName(savedProduct.getName());
        expectedResponse.setPrice(savedProduct.getPrice());
        expectedResponse.setStock(savedProduct.getStock());

        // TODO: Stub the mapper to convert request -> product.
        when(productMapper.toEntity(request))
                .thenReturn(product);
        // TODO: Stub the repository to save product -> savedProduct.
        when(productRepository.save(product))
                .thenReturn(savedProduct);
        // TODO: Stub the mapper to convert savedProduct -> expectedResponse.
        when(productMapper.toResponse(savedProduct))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        // TODO: Assert the returned response matches expectedResponse.
        assertEquals(expectedResponse.getProductCode(), result.getProductCode());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getPrice(), result.getPrice());
        assertEquals(expectedResponse.getStock(), result.getStock());

        // TODO: Capture the Product passed to repository.save().
        // TODO: Assert its productCode, name, price, and stock.
        verify(productRepository).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertEquals("P001", capturedProduct.getProductCode());
        assertEquals("Basketball", capturedProduct.getName());
        assertEquals(BigDecimal.valueOf(1500), capturedProduct.getPrice());
        assertEquals(10, capturedProduct.getStock());

    }

    @Test
    void shouldThrowExceptionWhenProductCodeAlreadyExists(){
        ProductRequest request = new ProductRequest();
        request.setProductCode("P001");
        request.setName("Basketball");

        when(productRepository.existsByProductCode("P001"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));

        verify(productRepository, never()).save(any(Product.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"P001", "P002", "P003"})
    void shouldSearchUsingProvidedProductCode(String productCode) {
        // Arrange: stub the repository and mapper as needed
        Product savedProduct = new Product();
        savedProduct.setProductCode(productCode);
        savedProduct.setName("Basketball");

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setProductCode(productCode);
        expectedResponse.setName("Basketball");

        when(productRepository.findByProductCode(productCode))
                .thenReturn(Optional.of(savedProduct));
        when(productMapper.toResponse(savedProduct))
                .thenReturn(expectedResponse);

        // Act: call getProductByProductCode(productCode)
        ProductResponse product = productService.getProductByProductCode(productCode);

        // Assert: verify the repository received the correct code
        assertEquals(productCode, product.getProductCode());
        verify(productRepository).findByProductCode(productCode);
        verify(productMapper).toResponse(savedProduct);
    }
}
