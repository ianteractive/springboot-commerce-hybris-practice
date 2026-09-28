package com.juan.ordermanagement.service;

import com.juan.ordermanagement.dto.CreateOrderRequest;
import com.juan.ordermanagement.dto.OrderResponse;
import com.juan.ordermanagement.dto.UpdateOrderRequest;
import org.hibernate.query.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest createOrderRequest);
    OrderResponse getOrderByOrderNumber(String orderNumber);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrder(String orderNumber, UpdateOrderRequest updateOrderRequest);
    void deleteOrder(String orderNumber);
}
