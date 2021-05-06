package br.com.example.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Consumer {

	public void consume(ConsumerRecord<String, String> record);
}
