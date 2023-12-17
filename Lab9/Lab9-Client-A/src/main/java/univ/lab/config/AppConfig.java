package univ.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackages = "univ.lab")
public class AppConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient departmentWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://localhost:8080/departments").build();
    }

    @Bean
    public WebClient employeeWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://localhost:8080/employees").build();
    }
}
