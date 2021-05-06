package br.com.example.ecommerce;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws ExecutionException, InterruptedException{
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties());
		Callback callback = (data, ex) -> {
			if(ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println("sucesso enviando:::Offset: "+data.offset()+" partition: "+data.partition()+ " offset: "+data.offset());
		};
		for (int i = 0; i < 10; i++) {
			String key = UUID.randomUUID().toString();
			String data = key+",1,1,50";
			ProducerRecord<String, String> recordNewOrder = new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", key, data);
			producer.send(recordNewOrder , callback).get();
			
			String email = "Bem-vindo! Estamos processando a sua compra.";
			ProducerRecord<String, String> recordEmail = new ProducerRecord<String, String>("ECOMMERCE_SEND_EMAIL", key, email);
			producer.send(recordEmail, callback).get();
			
		}
	}

	private static Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
}
