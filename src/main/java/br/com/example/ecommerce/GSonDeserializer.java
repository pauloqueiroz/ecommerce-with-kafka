package br.com.example.ecommerce;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonDeserializer<T> implements Deserializer<T>{
	
	public static final String TYPE_CONFIG = "br.com.example.ecommerce.type_config";
	private final Gson builder = new GsonBuilder().create();
	private Class<?> type;

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, byte[] bytes) {
		return (T) builder.fromJson(new String(bytes), type);
	}
	
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		Deserializer.super.configure(configs, isKey);
		String typeName = String.valueOf(configs.get(TYPE_CONFIG));
		try {
			this.type = Class.forName(typeName);
		}catch(Exception e) {
			throw new RuntimeException("Tipo para deserealização não existe no classpath", e);
		}
	}
	
}
