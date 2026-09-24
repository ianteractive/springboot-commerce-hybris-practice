package com.juan.ordermanagement.service;

import com.juan.ordermanagement.dto.CreateOrderRequest;
import com.juan.ordermanagement.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest createOrderRequest);
}
