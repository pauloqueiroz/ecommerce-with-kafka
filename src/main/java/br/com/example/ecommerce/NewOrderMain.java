package br.com.example.ecommerce;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	private static KafkaDispatcher dispatcher;

	public static void main(String[] args) throws ExecutionException, InterruptedException{
		
		try {		
			dispatcher = new KafkaDispatcher();
			for (int i = 0; i < 10; i++) {
				String key = UUID.randomUUID().toString();
				String data = key+",1,1,50";
				dispatcher.send("ECOMMERCE_NEW_ORDER", key, data);
				
				String email = "Bem-vindo! Estamos processando a sua compra.";
				dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
