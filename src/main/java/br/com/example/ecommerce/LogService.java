package br.com.example.ecommerce;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {

	@SuppressWarnings({ "resource", "rawtypes" })
	public static void main(String[] args) {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties());
		consumer.subscribe(Pattern.compile("ECOMMERCE.*"));
		while(true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			if(!records.isEmpty()) {
				System.out.println("Mensagens encontradas: "+records.count());
				for (ConsumerRecord record : records) {
					System.out.println("-------------------------");
					System.out.println("LOG: "+ record.topic());
					System.out.println(record.key());
					System.out.println(record.value());
					System.out.println(record.partition());
					System.out.println(record.offset());
				}
				
			}
		}
	}

	private static Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());

		return properties;
	}

}
