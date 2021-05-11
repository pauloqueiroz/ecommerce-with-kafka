package br.com.example.ecommerce.kafka;

import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializer<T> implements Serializer<T>{

	private final Gson builder = new GsonBuilder().create();

	@Override
	public byte[] serialize(String topic, T object) {
		return builder.toJson(object).getBytes();
	}
}
