package br.com.example.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService implements Consumer{
	
	public static void main(String[] args) {
		FraudDetectorService service = new FraudDetectorService();
		KafkaService kafkaService = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", service);
		kafkaService.run();
	}

	@Override
	public void consume(ConsumerRecord<String, String> record) {
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
