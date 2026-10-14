
package com.example.ordermanagement.service;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrder() {

        OrderRequest request = new OrderRequest();
        request.setCustomerName("Rahul");
        request.setAmount(new BigDecimal("2500.00"));

        String idempotencyKey = "order-1001";

        Order saved = new Order(
                "Rahul",
                new BigDecimal("2500.00"),
                idempotencyKey
        );

        when(orderRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());

        when(orderRepository.save(any(Order.class)))
                .thenReturn(saved);

        OrderResponse result =
                orderService.createOrder(request, idempotencyKey);

        assertEquals("Rahul", result.getCustomerName());
        assertEquals(
                0,
                new BigDecimal("2500.00").compareTo(result.getAmount())
        );

        verify(orderRepository).findByIdempotencyKey(idempotencyKey);
        verify(orderRepository).save(any(Order.class));
    }
}
