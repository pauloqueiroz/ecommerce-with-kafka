package br.com.example.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	private static KafkaDispatcher<Order> orderDispatcher;
	private static KafkaDispatcher<Email> emailDispatcher;

	public static void main(String[] args) throws ExecutionException, InterruptedException{
		
		try {		
			orderDispatcher = new KafkaDispatcher<Order>();
			emailDispatcher = new KafkaDispatcher<Email>();
			for (int i = 0; i < 10; i++) {
				String userId = UUID.randomUUID().toString();
				String orderId = UUID.randomUUID().toString();
				BigDecimal amount = new BigDecimal(Math.random()*5000+1);
				Order order = new Order(userId, orderId, amount);
				orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
				
				String emailBody = "Bem-vindo! Estamos processando a sua compra.";
				Email email = new Email(userId, emailBody);
				emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
