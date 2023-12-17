package univ.lab.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import univ.lab.dto.*;

@Service
public class EmployeeClientHandler {
    private final WebClient webClient;
    @Autowired
    public EmployeeClientHandler(@Qualifier("employeeWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<EmployeeResponseDTO> findAllEmployees() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(EmployeeResponseDTO.class);
    }

    public Mono<EmployeeResponseDTO> saveEmployee(EmployeeCreateDTO departmentCreateDTO) {
        return webClient.post()
                .bodyValue(departmentCreateDTO)
                .retrieve()
                .bodyToMono(EmployeeResponseDTO.class);
    }

    public Mono<EmployeeResponseDTO> updateEmployee(EmployeeUpdateDTO updateDTO) {
        return webClient.put()
                .bodyValue(updateDTO)
                .retrieve()
                .bodyToMono(EmployeeResponseDTO.class);
    }

    public Mono<EmployeeResponseDTO> getEmployeeById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeResponseDTO.class);
    }

    public Mono<Void> deleteEmployee(Long id) {
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<EmployeeResponseDTO> findEmployeeByName(String name) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/by-name")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .bodyToFlux(EmployeeResponseDTO.class);
    }

    public Flux<EmployeeResponseDTO> findEmployeeByDepartment(Long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/by-department")
                        .queryParam("departmentId", id)
                        .build())
                .retrieve()
                .bodyToFlux(EmployeeResponseDTO.class);
    }
}
