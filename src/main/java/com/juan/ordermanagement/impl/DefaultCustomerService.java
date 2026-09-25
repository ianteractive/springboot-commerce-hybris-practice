package com.juan.ordermanagement.impl;

import com.juan.ordermanagement.dto.CustomerRequest;
import com.juan.ordermanagement.dto.CustomerResponse;
import com.juan.ordermanagement.entity.Customer;
import com.juan.ordermanagement.exception.ResourceNotFoundException;
import com.juan.ordermanagement.mapper.CustomerMapper;
import com.juan.ordermanagement.repository.CustomerRepository;
import com.juan.ordermanagement.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultCustomerService implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    public DefaultCustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        //Check whether customerId already exists.
        if(customerRepository.existsByCustomerId(request.getCustomerId())){
            throw new ResourceNotFoundException("Customer id exists " + request.getCustomerId());
        }
        if(customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("Customer id exists " + request.getEmail());
        }
        //Check whether email already exists.
        //If either exists, throw an exception.
        //Otherwise save the customer.
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setEmail(request.getEmail());
        customer.setName(request.getName());

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerByCustomerId(String customerId) {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);//.orElseThrow(()-> new ResourceNotFoundException("text" + customerId));
        if(customer.isPresent()){
            Customer foundCustomer = customer.get();
            return customerMapper.toResponse(foundCustomer);
        }
        throw new ResourceNotFoundException(
                "Customer not found: " + customerId
        );
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        // 1. Get all customers from repository
        List<Customer> customerList = customerRepository.findAll();
        // 2. Create List<CustomerResponse>
        List<CustomerResponse> responses = new ArrayList<>();
        // 3. Loop through customers
        // 4. Convert Customer → CustomerResponse
        // 5. Add response to list
        for(Customer customer : customerList){
            responses.add(customerMapper.toResponse(customer));
        }
        // 6. Return list
        return responses;
    }

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerRequest request) {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if(customer.isPresent()){
            Customer foundCustomer = customer.get();
            if(customerRepository.existsByEmail(request.getEmail())
                    && !foundCustomer.getEmail().equals(request.getEmail())
            ){
                throw new ResourceNotFoundException("Email belongs to another customer " + request.getEmail());
            }
            foundCustomer.setName(request.getName());
            foundCustomer.setEmail(request.getEmail());
            customerRepository.save(foundCustomer);
            return customerMapper.toResponse(foundCustomer);
        } else {
            throw new ResourceNotFoundException("Customer not found " + customerId);
        }
    }

    @Override
    public void deleteCustomer(String customerId) {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if(customer.isPresent()){
        //Delete Customer
        customerRepository.delete(customer.get());
        } else {
            throw new ResourceNotFoundException("Customer not found " + customerId);
        }
    }
}
