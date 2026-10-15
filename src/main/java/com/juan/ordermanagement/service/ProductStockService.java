package com.juan.ordermanagement.service;

public class ProductStockService {

    public boolean isInStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative"
            );
        }

        return stock > 0;
    }

}
