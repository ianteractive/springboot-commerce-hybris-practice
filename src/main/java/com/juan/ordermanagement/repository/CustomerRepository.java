package com.juan.ordermanagement.repository;

import com.juan.ordermanagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(String customerId);
    boolean existsByEmail(String email);
    boolean existsByCustomerId(String customerId);
}
