package br.com.example.ecommerce;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaDispatcher implements Closeable{

	KafkaProducer<String, String> producer;

	public KafkaDispatcher() {
		super();
		this.producer = new KafkaProducer<String, String>(properties());
	}
	
	
	public void send(String topic, String key, String value) throws InterruptedException, ExecutionException {
		
		Callback callback = (data, ex) -> {
			if(ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println("sucesso enviando::: Topico: "+data.topic()+" Offset: "+data.offset()+" partition: "+data.partition()+ " offset: "+data.offset());
		};
		ProducerRecord<String, String> recordNewOrder = new ProducerRecord<String, String>(topic, key, value);
		producer.send(recordNewOrder , callback).get();
	}
	
	
	private Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}


	@Override
	public void close(){
		producer.close();
		
	}
	
}
