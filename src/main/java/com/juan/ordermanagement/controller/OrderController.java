package com.juan.ordermanagement.controller;

import com.juan.ordermanagement.dto.CreateOrderRequest;
import com.juan.ordermanagement.dto.OrderResponse;
import com.juan.ordermanagement.dto.UpdateOrderRequest;
import com.juan.ordermanagement.service.OrderService;
import jakarta.validation.Valid;
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
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest){
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

    @PutMapping("/{orderNumber}")
    public OrderResponse updateOrder(@PathVariable String orderNumber,
                                     @Valid @RequestBody UpdateOrderRequest updateOrderRequest){
        return orderService.updateOrder(orderNumber, updateOrderRequest);
    }

    @DeleteMapping("/{orderNumber}")
    public void deleteOrder(@PathVariable String orderNumber){
        orderService.deleteOrder(orderNumber);
    }
}
