package br.com.example.ecommerce;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService {

	private final KafkaConsumer<String, String> consumer;
	private final Consumer serviceConsumer;
	private final String groupName;

	public KafkaService(String groupName, String topic, Consumer service) {
		this.groupName = groupName;
		this.serviceConsumer = service;
		this.consumer = new KafkaConsumer<String, String>(properties());
		consumer.subscribe(Collections.singletonList(topic));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void run() {
		while(true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			if(!records.isEmpty()) {
				System.out.println("Mensagens encontradas: "+records.count());
				for (ConsumerRecord record : records) {
					serviceConsumer.consume(record);
				}
				
			}
		}
	}
	
	private Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupName);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()+" - "+this.groupName);

		return properties;
	}

}
