package br.com.example.ecommerce.frauddetector;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.example.ecommerce.kafka.Consumer;
import br.com.example.ecommerce.kafka.KafkaService;

public class FraudDetectorService implements Consumer<Order>{
	
	private static KafkaService<Order> kafkaService;

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
		System.out.println("Compra processada.");
		
	}
}
