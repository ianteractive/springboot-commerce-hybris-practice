package com.juan.ordermanagement.impl;

import com.juan.ordermanagement.dto.CreateOrderItemRequest;
import com.juan.ordermanagement.dto.CreateOrderRequest;
import com.juan.ordermanagement.dto.OrderResponse;
import com.juan.ordermanagement.dto.UpdateOrderRequest;
import com.juan.ordermanagement.entity.Customer;
import com.juan.ordermanagement.entity.CustomerOrder;
import com.juan.ordermanagement.entity.OrderItem;
import com.juan.ordermanagement.entity.Product;
import com.juan.ordermanagement.exception.ResourceNotFoundException;
import com.juan.ordermanagement.mapper.OrderMapper;
import com.juan.ordermanagement.repository.CustomerOrderRepository;
import com.juan.ordermanagement.repository.CustomerRepository;
import com.juan.ordermanagement.repository.OrderItemRepository;
import com.juan.ordermanagement.repository.ProductRepository;
import com.juan.ordermanagement.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultOrderService implements OrderService {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private CustomerOrderRepository customerOrderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderMapper orderMapper;

    public DefaultOrderService(CustomerRepository customerRepository, ProductRepository productRepository, CustomerOrderRepository customerOrderRepository, OrderItemRepository orderItemRepository, OrderMapper orderMapper) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
    }

        @Transactional
        @Override
        public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
            //1. Find Customer
            Customer customer = customerRepository.findByCustomerId(createOrderRequest.getCustomerId())
                    .orElseThrow(()-> new ResourceNotFoundException("Customer doesn't exist: " + createOrderRequest.getCustomerId()));
            //2. Create CustomerOrder
            CustomerOrder order = new CustomerOrder();
            order.setCustomer(customer);
            //3. Generate orderNumber
            order.setOrderNumber(UUID.randomUUID().toString());

            List<OrderItem> orderItemList = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            //4. Loop through requested items
            for(CreateOrderItemRequest itemRequest : createOrderRequest.getItems()){
                //5. Find each Product
                Product product = productRepository.findByProductCode(itemRequest.getProductCode())
                        .orElseThrow(()-> new ResourceNotFoundException("Product code doesn't exist: " + itemRequest.getProductCode()));
                //6. Create OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                //7. Copy current Product price → unitPrice
                orderItem.setUnitPrice(product.getPrice());

                //8. Calculate subtotal
                BigDecimal subtotal = BigDecimal.valueOf(orderItem.getQuantity()).multiply(orderItem.getUnitPrice());
                //9. Calculate totalAmount
                totalAmount = totalAmount.add(subtotal);
                orderItemList.add(orderItem);
            }
            order.setItems(orderItemList);
            order.setTotalAmount(totalAmount);
            //10. Save CustomerOrder
            CustomerOrder customerOrder = customerOrderRepository.save(order);
            for(OrderItem item : orderItemList){
                item.setOrder(customerOrder);
            }
            //11. Save OrderItems
            orderItemRepository.saveAll(orderItemList);
            customerOrder.setItems(orderItemList);
            //12. Map → OrderResponse
            return  orderMapper.toResponse(customerOrder);
            //
        }

    @Override
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        CustomerOrder customerOrder = customerOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Order number doesn't exist: " + orderNumber));
        return orderMapper.toResponse(customerOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(CustomerOrder order : customerOrders){
            orderResponses.add(orderMapper.toResponse(order));
        }
        return orderResponses;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(String orderNumber, UpdateOrderRequest updateOrderRequest) {
        CustomerOrder customerOrder = customerOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Order number doesn't exist: " + orderNumber));
        orderItemRepository.deleteAll(customerOrder.getItems());
        customerOrder.setItems(new ArrayList<>());
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(CreateOrderItemRequest orderItemRequest : updateOrderRequest.getItems()){
            Product product = productRepository.findByProductCode(orderItemRequest.getProductCode())
                    .orElseThrow(()-> new ResourceNotFoundException("Product code doesn't exist: " + orderItemRequest.getProductCode()));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(customerOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            BigDecimal subTotal = BigDecimal.valueOf(orderItem.getQuantity()).multiply(orderItem.getUnitPrice());
            totalAmount = totalAmount.add(subTotal);
            orderItemList.add(orderItem);
        }
        orderItemRepository.saveAll(orderItemList);
        customerOrder.setItems(orderItemList);
        customerOrder.setTotalAmount(totalAmount);
        customerOrderRepository.save(customerOrder);
        return orderMapper.toResponse(customerOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(String orderNumber) {
        CustomerOrder customerOrder = customerOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Order number doesn't exist: " + orderNumber));
        orderItemRepository.deleteAll(customerOrder.getItems());
        customerOrderRepository.delete(customerOrder);
    }
}
