package com.juan.ordermanagement.dto;

import java.util.List;

public class UpdateOrderRequest {
    private List<CreateOrderItemRequest> items;

    public List<CreateOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateOrderItemRequest> items) {
        this.items = items;
    }
}
