package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
	Optional<Order> findByIdempotencyKey(String idempotencyKey);
	
	List<Order> findByCustomerName(String customerName);
	
	// Naya method — Rahul ke total orders count karega  N+1
	long countByCustomerName(String customerName);

}
