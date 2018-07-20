package br.com.igor.jms.topic;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TesteConsumerMessageListenerTopicEstoque {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		//Configuração de quais pacotes podem ser serializaveis. Exigência do ActiveMQ
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,br.com.caelum.modelo");
		// contexto para fazer o lookup da conexeção do ActieMQ -
		// As propriedades são definidas no arquivo jndi.properties
		InitialContext context = new InitialContext();

		// nome definido na documentação do ActiveMQ
		ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = cf.createConnection();
		
		//identifico a conexão, assim o consumidor se identifica para o Topic que ele existe
		connection.setClientID("estoque");

		connection.start();

		// session necessária para criar o consumer
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// pegamos o topic que foi criada no MOM, configurado no jndi.properties
		// aqui no lugar da interface Destination entra a Topic
		Topic topico = (Topic) context.lookup("loja");

		// consumer pega o topic e coloca uma assinatura Durável para, caso offline, receber as mensagens.
		// Alem de selecinar as mensagens de acordo com um selector ebook
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura", "ebook is null OR ebook=false", false);

		consumer.setMessageListener(new MessageListener(){

		    @Override
		    public void onMessage(Message message){
		    	//capturar apenas o texto da menssagem
		    	TextMessage txtMessage = (TextMessage) message;
		    	try {
					System.out.println(txtMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
		    }
		});

		new Scanner(System.in).nextLine();
		session.close();
		connection.close();
		context.close();
	}

}