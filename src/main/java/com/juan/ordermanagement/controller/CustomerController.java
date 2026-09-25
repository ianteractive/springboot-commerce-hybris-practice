package com.juan.ordermanagement.controller;

import com.juan.ordermanagement.dto.CustomerRequest;
import com.juan.ordermanagement.dto.CustomerResponse;
import com.juan.ordermanagement.entity.Customer;
import com.juan.ordermanagement.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public CustomerResponse createCustomer(@RequestBody CustomerRequest request){
        return customerService.createCustomer(request);
    }

    @GetMapping("/{customerId}")
    public CustomerResponse getCustomerByCustomerId(@PathVariable String customerId){
        return customerService.getCustomerByCustomerId(customerId);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PutMapping("/{customerId}")
    public CustomerResponse updateCustomerByCustomerId(
            @PathVariable String customerId,
            @RequestBody CustomerRequest request
    ){
        return customerService.updateCustomer(customerId, request);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomerByCustomerId(@PathVariable String customerId){
        customerService.deleteCustomer(customerId);
    }

}
