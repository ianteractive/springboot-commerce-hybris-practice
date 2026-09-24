package com.juan.ordermanagement.mapper;

import com.juan.ordermanagement.dto.CustomerResponse;
import com.juan.ordermanagement.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer){
        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customer.getCustomerId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());

        return response;
    }

}
