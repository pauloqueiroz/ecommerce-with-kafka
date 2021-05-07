package br.com.example.ecommerce;

import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class LogService implements Consumer{

	@SuppressWarnings({ "resource" })
	public static void main(String[] args) {
		LogService service = new LogService();
		Pattern topicPattern = Pattern.compile("ECOMMERCE.*");
		try {
			KafkaService kafkaService = new KafkaService(LogService.class.getSimpleName(), topicPattern, service);
			kafkaService.run();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void consume(ConsumerRecord<String, String> record) {
		System.out.println("-------------------------");
		System.out.println("LOG: "+ record.topic());
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
	}

}
