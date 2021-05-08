package br.com.example.ecommerce;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService implements Consumer<String>{

	@SuppressWarnings({ "resource" })
	public static void main(String[] args) {
		LogService service = new LogService();
		Pattern topicPattern = Pattern.compile("ECOMMERCE.*");
		try {
			Map<String, String> overrideProperties = Collections.singletonMap(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
			KafkaService<String> kafkaService = new KafkaService<String>(LogService.class.getSimpleName(), topicPattern, service, String.class, overrideProperties);
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
