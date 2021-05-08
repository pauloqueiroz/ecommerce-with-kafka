package br.com.example.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService<T> implements Closeable{

	private final KafkaConsumer<String, String> consumer;
	private final Consumer<?> serviceConsumer;
	private final String groupName;
	private final Class<T> type;
	
	public KafkaService(Consumer<?> serviceConsumer, String groupName, Class<T> clazz, Map<String, String> overrideProperties) {
		super();
		this.serviceConsumer = serviceConsumer;
		this.groupName = groupName;
		this.type = clazz;
		this.consumer = new KafkaConsumer<String, String>(properties(overrideProperties));
	}

	public KafkaService(String groupName, String topic, Consumer<?> service, Class<T> clazz, Map<String, String> overrideProperties) {
		this(service, groupName, clazz, overrideProperties);
		consumer.subscribe(Collections.singletonList(topic));
	}

	public KafkaService(String groupName, Pattern topicPattern, Consumer<?> service, Class<T> clazz, Map<String, String> overrideProperties) {
		this(service, groupName, clazz, overrideProperties);
		consumer.subscribe(topicPattern);
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
	
	private Properties properties(Map<String, String> overrideProperties) {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GSonDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupName);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()+" - "+this.groupName);
		properties.setProperty(GSonDeserializer.TYPE_CONFIG, this.type.getName());
		properties.putAll(overrideProperties);
		return properties;
	}

	@Override
	public void close() {
		consumer.close();
		
	}

}
