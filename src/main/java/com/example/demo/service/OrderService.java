
package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.globalExceptionHandler.OrderNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ATOMICITY: Order creation runs in a transaction
    @Transactional
    public OrderResponse createOrder(
            OrderRequest request, String idempotencyKey) {

        Optional<Order> existing =
                orderRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            Order order = existing.get();

            if (!order.getCustomerName().equals(
                    request.getCustomerName())
                    || order.getAmount().compareTo(
                    request.getAmount()) != 0) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Idempotency key already used for another request"
                );
            }

            return new OrderResponse(
                    order.getId(),
                    order.getCustomerName(),
                    order.getAmount()
            );
        }

        Order order = new Order(
                request.getCustomerName(),
                request.getAmount(),
                idempotencyKey
        );

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getAmount()
        );
    }

    // READ: Fetch all orders
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // READ: Fetch order by ID
    @Cacheable(value = "orders", key = "#id")
    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
//    @Cacheable(value = "orders", key = "#id")
//    @Transactional(readOnly = true)
//    public Order getOrderById(Long id) {
//        return orderRepository.findById(id)
//                .orElseThrow(() -> new OrderNotFoundException(id));
//    }

    // TRANSACTION: Update an order
    @CacheEvict(value = "orders", key = "#id")
    @Transactional
    public Order updateOrder(Long id, Order request) {
        Order existing = getOrderById(id);

        existing.setCustomerName(request.getCustomerName());
        existing.setAmount(request.getAmount());

        return orderRepository.save(existing);
    }

    // TRANSACTION: Delete an order
    @CacheEvict(value = "orders", key = "#id")
    @Transactional
    public void deleteOrder(Long id) {
        Order existing = getOrderById(id);
        orderRepository.delete(existing);
    }
}
