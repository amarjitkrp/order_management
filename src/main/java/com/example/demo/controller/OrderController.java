package com.example.demo.controller;


import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;



@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService=orderService;
	}	
	
	


@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public OrderResponse createOrder(
        @Valid @RequestBody OrderRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {

    return orderService.createOrder(request,idempotencyKey);
}

		    @GetMapping
		    public List<Order> getAllOrders() {
		        return orderService.getAllOrders();
		    }

		    @GetMapping("/{id}")
		    public Order getOrderById(@PathVariable Long id) {
		        return orderService.getOrderById(id);
		    }

		    @PutMapping("/{id}")
		    public Order updateOrder(
		            @PathVariable Long id,
		            @Valid @RequestBody Order order) {
		        return orderService.updateOrder(id, order);
		    }

		    @DeleteMapping("/{id}")
		    @ResponseStatus(HttpStatus.NO_CONTENT)
		    public void deleteOrder(@PathVariable Long id) {
		        orderService.deleteOrder(id);
	}

}
