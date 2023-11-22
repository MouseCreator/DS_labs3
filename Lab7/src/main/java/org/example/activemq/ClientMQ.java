package org.example.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.communicator.ClientCommunicator;
import org.example.model.dto.Request;
import org.example.model.dto.Response;

import javax.jms.*;

public class ClientMQ implements ExceptionListener, ClientCommunicator {
    public Session session = null;
    private MessageConsumer consumer = null;
    private MessageProducer producer = null;
    private Connection connection = null;
    public void start(Destination declareDestination) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
            connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            String clientQueueName = "CLIENT." + connection.getClientID();
            String serverQueueName = "SERVER." + connection.getClientID();
            Destination clientDestination = session.createQueue(clientQueueName);
            Destination serverDestination = session.createQueue(serverQueueName);
            connect(clientQueueName, serverQueueName, declareDestination);
            consumer = session.createConsumer(serverDestination);
            producer = session.createProducer(clientDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e);
            e.printStackTrace();
        }
    }

    private void connect(String clientQueueName, String serverQueueName, Destination declareDestination) throws JMSException {
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        MessageProducer declareProducer = session.createProducer(declareDestination);
        declareProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        Message message = session.createTextMessage(clientQueueName + " " + serverQueueName);
        declareProducer.send(message);
    }

    @Override
    public Response sendAndReceive(Request request) {
        try {
            producer.send(session.createObjectMessage(request));
            return (Response) ((ObjectMessage) consumer.receive(1000)).getObject();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            consumer.close();
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onException(JMSException e) {
        System.out.println("JMS Exception occurred. " + e.getMessage());
    }
}
