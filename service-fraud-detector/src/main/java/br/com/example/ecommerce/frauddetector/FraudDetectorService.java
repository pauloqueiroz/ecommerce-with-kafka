package br.com.example.ecommerce.frauddetector;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.example.ecommerce.kafka.Consumer;
import br.com.example.ecommerce.kafka.KafkaDispatcher;
import br.com.example.ecommerce.kafka.KafkaService;

public class FraudDetectorService implements Consumer<Order>{
	
	private static KafkaService<Order> kafkaService;
	
	private static KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();

	public static void main(String[] args) {
		FraudDetectorService service = new FraudDetectorService();
		try {
			kafkaService = new KafkaService<Order>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", service, Order.class, new HashMap<String, String>());
			kafkaService.run();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void consume(ConsumerRecord<String, Order> record) {
		System.out.println("-------------------------");
		System.out.println("Verificando fraude em compra");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(5000);
		}catch(Exception e) {
			//ignoring
			e.printStackTrace();
		}
		Order order = record.value();
		if(isFraud(order)) {
			System.out.println("Compra é fraude. "+order);
			try {
				orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(), order);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		System.out.println("Compra aprovada.");
		try {
			orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private boolean isFraud(Order order) {
		return order.getAmount().compareTo(BigDecimal.valueOf(4500)) >= 0;
	}
}
