package br.com.example.ecommerce.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Consumer<T> {

	public void consume(ConsumerRecord<String, T> record);
}
