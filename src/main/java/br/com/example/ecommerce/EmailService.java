package br.com.example.ecommerce;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements Consumer<Email>{

	private static KafkaService<Email> kafkaService;

	public static void main(String[] args) {
		EmailService emailService = new EmailService();
		try {
			kafkaService = new KafkaService<Email>(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL", emailService, Email.class, new HashMap<String, String>());
			kafkaService.run();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consume(ConsumerRecord<String, Email> record) {
		System.out.println("-------------------------");
		System.out.println("Enviando e-mail");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			//ignoring
			e.printStackTrace();
		}
		System.out.println("E-mail enviado.");
	}

}
