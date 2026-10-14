package com.example.demo.dto;

import java.math.BigDecimal;

public class OrderResponse {
	 private Long id;
	    private String customerName;
	    private BigDecimal amount;

	    public OrderResponse() {
	    }

	    public OrderResponse(Long id, String customerName, BigDecimal amount) {
	        this.id = id;
	        this.customerName = customerName;
	        this.amount = amount;
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getCustomerName() {
	        return customerName;
	    }

	    public BigDecimal getAmount() {
	        return amount;
	    }
	}


