package br.com.example.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.example.ecommerce.kafka.Consumer;
import br.com.example.ecommerce.kafka.KafkaDispatcher;
import br.com.example.ecommerce.kafka.KafkaService;

public class CreateUserService implements Consumer<Order>{

	private static KafkaService<Order> kafkaService;
	
	private static KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();
	
	private static List<User> users = new ArrayList<User>();

	public static void main(String[] args) {
		CreateUserService service = new CreateUserService();
		try {
			kafkaService = new KafkaService<Order>(CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", service, Order.class, new HashMap<String, String>());
			kafkaService.run();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void consume(ConsumerRecord<String, Order> record) {
		System.out.println("-------------------------");
		System.out.println("Inserindo usuario");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(2000);
		}catch(Exception e) {
			//ignoring
			e.printStackTrace();
		}
		Order order = record.value();
		if(isNewUser(order.getEmail())) {
			User user = new User(order.getUserId(), order.getEmail());
			insert(user);
		}
		
		System.out.println("Compra aprovada.");
		try {
			orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean isNewUser(String email) {
		boolean exists = users.stream().filter(u-> u.getEmail().equalsIgnoreCase(email)).findFirst().isPresent();
		if(exists) {
			return false;
		}
		return true;
	}

	private void insert(User user) {
		users.add(user);
	}

}
