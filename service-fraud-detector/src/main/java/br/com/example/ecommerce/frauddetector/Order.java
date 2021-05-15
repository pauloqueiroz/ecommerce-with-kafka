package br.com.example.ecommerce.frauddetector;

import java.math.BigDecimal;

public class Order {
	
	private String orderId;
	
	private BigDecimal amount;
	
	private String email;
	
	public Order(String email, String orderId, BigDecimal amount) {
		super();
		this.email = email;
		this.orderId = orderId;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Order [email=" + email + ", orderId=" + orderId + ", amount=" + amount + "]";
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
