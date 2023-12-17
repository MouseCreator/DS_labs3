package univ.lab.lab9_boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import univ.lab.lab9_boot.model.Employee;
import univ.lab.lab9_boot.service.EmployeeService;

@SpringBootApplication
public class Lab9BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab9BootApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Employee employee = new Employee();
            }
        };
    }

}
