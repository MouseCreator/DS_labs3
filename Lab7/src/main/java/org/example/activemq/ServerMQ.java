package org.example.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.server.CommonServerController;
import org.example.server.ThreadPool;
import org.example.server.ThreadPoolImpl;

import javax.jms.*;

public class ServerMQ implements ExceptionListener {
    public Session session = null;
    private MessageConsumer declarationConsumer = null;
    private final ThreadPool threadPool = new ThreadPoolImpl(4);
    public void initialize(String declareDestinationStr) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination declareDestination = session.createQueue(declareDestinationStr);
            declarationConsumer = session.createConsumer(declareDestination);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e);
            e.printStackTrace();
        }
    }

    public void runServer() {
        while (true) {
            try {
                Message message = declarationConsumer.receive(1000);
                if (message instanceof TextMessage textMessage) {
                    String[] parts = textMessage.getText().split(" ");
                    if (parts.length != 2)
                        continue;
                    createNewCommunicator(parts[0], parts[1]);
                }
            } catch (JMSException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void createNewCommunicator(String clientName, String serverName) throws JMSException {
        Destination clientDestination = session.createQueue(clientName);
        Destination serverDestination = session.createQueue(serverName);
        MessageConsumer consumer = session.createConsumer(clientDestination);
        MessageProducer producer = session.createProducer(serverDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        ServerCommunicatorMQ serverCommunicatorMQ = new ServerCommunicatorMQ(consumer, producer, session);
        CommonServerController controller = new CommonServerController();
        threadPool.submit(new ServerRunnable(serverCommunicatorMQ, controller));
    }

    @Override
    public void onException(JMSException e) {
        System.out.println("Server exception: " + e);
    }
}
