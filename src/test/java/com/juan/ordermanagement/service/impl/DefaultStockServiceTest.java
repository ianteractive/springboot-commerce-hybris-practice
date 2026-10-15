package com.juan.ordermanagement.service.impl;
import com.juan.ordermanagement.service.ProductStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultStockServiceTest {

    private final ProductStockService service
            = new ProductStockService();

    @Test
    void shouldReturnTrueIfStockIsPositive(){
        int stock = 10;
        boolean result = service.isInStock(stock);
        assertTrue(result);
    }
    @Test
    void shouldReturnFalseIfStockIsZero(){
        int stock = 0;
        boolean result = service.isInStock(stock);
        assertFalse(result);
    }
    @Test
    void shouldNotReturnIfStockIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> service.isInStock(-5));
    }

    @Test
    void verifyServiceInstanceNotNull(){
        assertNotNull(service);
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "5, true",
            "10, true",
            "0, false",
            "100, true"
    })
    void shouldCheckStockAvailability(
            int stock, boolean expected) {

        boolean result = service.isInStock(stock);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void shouldThrowExceptionWhenStockIsNegative(int stock) {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.isInStock(stock)
        );
    }


}
