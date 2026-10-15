package com.juan.ordermanagement.service.impl;

import com.juan.ordermanagement.service.ProductPricingService;
import com.juan.ordermanagement.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultPricingServiceTest {

    private int setupCount = 0;
    private ProductPricingService service;

    @BeforeEach
    void setUp(){
        service = new ProductPricingService();
        setupCount++;
    }

    @Test
    void shouldApplyDiscountWhenPriceIsAt100000(){
        double price = 100_000;
        double result = service.calculateFinalPrice(price);
        assertEquals(90_000, result);
    }

    @Test
    void shouldReturnOriginalPriceWhenBelowDiscountThreshold50000(){
        double price = 50_000;
        double result = service.calculateFinalPrice(price);
        assertEquals(50_000, result);
    }
    @Test
    void shouldReturnOriginalPriceWhenBelowDiscountThreshold99999(){
        double price = 99_999;
        double result = service.calculateFinalPrice(price);
        assertEquals(99_999, result);
    }
    @Test
    void shouldApplyDiscountWhenPriceIsInvalid(){
        assertThrows(IllegalArgumentException.class, () -> service.calculateFinalPrice(-1));
    }

    @Test
    void shouldRunSetupBeforeEachTest(){
        assertEquals(1, setupCount);
    }
}
