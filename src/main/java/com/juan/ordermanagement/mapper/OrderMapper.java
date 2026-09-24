package com.juan.ordermanagement.mapper;

import com.juan.ordermanagement.dto.OrderItemResponse;
import com.juan.ordermanagement.dto.OrderResponse;
import com.juan.ordermanagement.entity.CustomerOrder;
import com.juan.ordermanagement.entity.OrderItem;
import org.hibernate.query.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(CustomerOrder order){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setCustomerId(order.getCustomer().getCustomerId());
        orderResponse.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> items = new ArrayList<>();
        for(OrderItem item : order.getItems()){
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setProductCode(item.getProduct().getProductCode());
            orderItemResponse.setQuantity(item.getQuantity());
            orderItemResponse.setUnitPrice(item.getUnitPrice());
            BigDecimal subtotal = BigDecimal.valueOf(item.getQuantity()).multiply(item.getUnitPrice());
            orderItemResponse.setSubtotal(subtotal);
            items.add(orderItemResponse);
        }
        orderResponse.setItems(items);

        return orderResponse;
    }
}
