package br.com.igor.jms.fila;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;

import br.com.igor.jms.model.Pedido;
import br.com.igor.jms.model.PedidoFactory;

public class TesteProducer {

	public static void main(String[] args) throws Exception {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		// contexto para fazer o lookup da conexeção do ActieMQ -
		// Colocando o jndi.properties no código java

		Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		properties.setProperty("java.naming.provider.url", "tcp://10.0.0.10:61616");
		properties.setProperty("queue.financeiro", "fila.financeiro");

		InitialContext context = new InitialContext(properties);

		// nome definido na documentação do ActiveMQ
		ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = cf.createConnection();

		connection.start();

		// session necessária para criar o consumer
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// pegamos a fila que foi criada no MOM, configurado no jndi.properties
		Destination fila = (Destination) context.lookup("financeiro");

		// producer pega a fila e envia a menssagem
		MessageProducer producer = session.createProducer(fila);

		for (int i = 0; i < 20; i++) {
			Pedido pedido = new PedidoFactory().geraPedidoComValores();
			ObjectMessage message = session.createObjectMessage(pedido);
			producer.send(message);
		}

		session.close();
		connection.close();
		context.close();
	}

}
