package com.juan.ordermanagement.service;

import com.juan.ordermanagement.dto.CustomerRequest;
import com.juan.ordermanagement.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerByCustomerId(String customerId);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(String customerId, CustomerRequest request);

    void deleteCustomer(String customerId);
}
