package univ.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import univ.lab.config.AppConfig;
import univ.lab.ui.UserCommunicator;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserCommunicator userCommunicator = context.getBean(UserCommunicator.class);
        userCommunicator.mainLoop();
    }
}
