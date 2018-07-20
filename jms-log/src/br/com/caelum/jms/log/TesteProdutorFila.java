package br.com.caelum.jms.log;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteProdutorFila {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection(); 
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination fila = (Destination) context.lookup("LOG");
		
		MessageProducer producer = session.createProducer(fila);
		
		Message message = session.createTextMessage("WARN | Apache ActiveMQ 5.12.0 (localhost, ID:Mac-mini-de-Caelum.local-49701-1443131721783-0:1) is starting");
		producer.send(message,DeliveryMode.NON_PERSISTENT,9,80000);
		
		session.close();
		connection.close();
		context.close();
	}
}
