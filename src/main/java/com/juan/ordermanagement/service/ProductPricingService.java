package com.juan.ordermanagement.service;

import org.springframework.stereotype.Service;

@Service
public class ProductPricingService {

    public double calculateFinalPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative"
            );
        }

        if (price >= 100_000) {
            return price * 0.90;
        }

        return price;
    }
}
