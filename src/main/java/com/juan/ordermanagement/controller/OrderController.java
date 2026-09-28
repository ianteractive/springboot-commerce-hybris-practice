package com.juan.ordermanagement.controller;

import com.juan.ordermanagement.dto.CreateOrderRequest;
import com.juan.ordermanagement.dto.OrderResponse;
import com.juan.ordermanagement.service.OrderService;
import org.hibernate.query.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest){
        return orderService.createOrder(createOrderRequest);
    }

    @GetMapping("/{orderNumber}")
    public OrderResponse getOrderByOrderNumber(@PathVariable String orderNumber){
        return orderService.getOrderByOrderNumber(orderNumber);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }
}
