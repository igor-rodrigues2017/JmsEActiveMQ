package br.com.igor.jms.topic;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import br.com.igor.jms.model.Pedido;
import br.com.igor.jms.model.PedidoFactory;

public class TesteProducerTopic {

	public static void main(String[] args) throws Exception {
		// contexto para fazer o lookup da conexeção do ActieMQ -

		InitialContext context = new InitialContext();

		// nome definido na documentação do ActiveMQ
		ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = cf.createConnection();

		connection.start();

		// session necessária para criar o consumer
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// pegamos o topic que foi criada no MOM, configurado no jndi.properties
		Destination topico = (Destination) context.lookup("loja");

		// producer pega o topico e envia a menssagem
		MessageProducer producer = session.createProducer(topico);
		
		
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		
		// transforma o pedido em um xml
		StringWriter writer = new StringWriter();
		JAXB.marshal(pedido, writer);
		String xml = writer.toString();

		TextMessage message = session.createTextMessage(xml);
		producer.send(message);

		session.close();
		connection.close();
		context.close();
	}

}
