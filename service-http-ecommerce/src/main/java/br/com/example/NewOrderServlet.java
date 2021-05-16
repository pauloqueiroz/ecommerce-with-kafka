package br.com.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import br.com.example.ecommerce.kafka.KafkaDispatcher;

public class NewOrderServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();;
	private static final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<Email>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			
			String orderId = UUID.randomUUID().toString();
			String emailString = req.getParameter("email");
			BigDecimal amount = new BigDecimal(req.getParameter("amount"));
			System.out.println("Valor compra: " + amount);
			Order order = new Order(orderId, amount, emailString);
			orderDispatcher.send("ECOMMERCE_NEW_ORDER", emailString, order);

			String emailBody = "Bem-vindo! Estamos processando a sua compra.";
			Email email = new Email(emailString, emailBody);
			emailDispatcher.send("ECOMMERCE_SEND_EMAIL", emailString, email);
			String msg = "Processo de nova compra terminado.";
			System.out.println(msg);
			resp.getWriter().println(msg);
			resp.setStatus(HttpStatus.OK_200);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		orderDispatcher.close();
		emailDispatcher.close();
	}

}
