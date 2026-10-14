package com.example.demo.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.Column;


@Entity
@Table(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Customer name is required")
    private String customerName;
    
    @Positive(message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @Column(unique = true)
    private String idempotencyKey;
    
    

public Order(String customerName,BigDecimal amount,String idempotencyKey){
	this.customerName=customerName;
	this.amount=amount;
	this.idempotencyKey=idempotencyKey;
	}
public String getIdempotencyKey() {
	return idempotencyKey;
}

public Order() {
	
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getCustomerName() {
	return customerName;
}
public void setCustomerName(String customerName) {
	this.customerName = customerName;
}
public BigDecimal getAmount() {
	return amount;
}
public void setAmount(BigDecimal amount) {
	this.amount = amount;
}


}
