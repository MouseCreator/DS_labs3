package org.example.activemq;

import org.example.communicator.ServerCommunicator;
import org.example.model.dto.Request;
import org.example.model.dto.Response;

import javax.jms.*;

public class ServerCommunicatorMQ implements ServerCommunicator {
    private final MessageConsumer consumer;
    private final MessageProducer producer;
    private final Session session;
    public ServerCommunicatorMQ(MessageConsumer consumer, MessageProducer producer, Session session) {
        this.consumer = consumer;
        this.producer = producer;
        this.session = session;
    }

    @Override
    public void send(Response response) {
        try {
            producer.send(session.createObjectMessage(response));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Request receive() {
        while (true) {
            try {
                ObjectMessage message = (ObjectMessage) consumer.receive(500);
                return (Request) message.getObject();
            } catch (JMSException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
