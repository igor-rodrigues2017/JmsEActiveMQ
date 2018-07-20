package br.com.igor.jms.fila;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteConsumerReceive {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// contexto para fazer o lookup da conexeção do ActieMQ -
		// As propriedades são definidas no arquivo jndi.properties
		InitialContext context = new InitialContext();

																	// nome definido na documentação do ActiveMQ
		ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = cf.createConnection();

		connection.start();
		
		//session necessária para criar o consumer
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
															//pegamos a fila que foi criada no MOM, configurado no jndi.properties
		Destination fila = (Destination) context.lookup("financeiro");
		
		//consumer pega a fila e recebe a menssagem
		MessageConsumer consumer = session.createConsumer(fila);
		
		Message message = consumer.receive();
		System.out.println("Menssagem Recebida: " + message);
		
		session.close();
		
		new Scanner(System.in).nextLine();

		connection.close();
		context.close();
	}

}
