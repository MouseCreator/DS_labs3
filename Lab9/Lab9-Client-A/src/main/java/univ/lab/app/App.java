package univ.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import univ.lab.client.ClientConnector;
import univ.lab.config.AppConfig;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ClientConnector connector = context.getBean(ClientConnector.class);
    }
}
