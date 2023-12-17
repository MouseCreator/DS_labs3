package univ.lab.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import univ.lab.dto.DepartmentCreateDTO;
import univ.lab.dto.DepartmentResponseDTO;
import univ.lab.dto.DepartmentUpdateDTO;

@Component
public class DepartmentClientHandler {
    private final WebClient webClient;
    @Autowired
    public DepartmentClientHandler(@Qualifier("departmentWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<DepartmentResponseDTO> findAllDepartments() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(DepartmentResponseDTO.class);
    }

    public Mono<DepartmentResponseDTO> save(DepartmentCreateDTO departmentCreateDTO) {
        return webClient.post()
                .bodyValue(departmentCreateDTO)
                .retrieve()
                .bodyToMono(DepartmentResponseDTO.class);
    }

    public Mono<DepartmentResponseDTO> update(DepartmentUpdateDTO updateDTO) {
        return webClient.put()
                .bodyValue(updateDTO)
                .retrieve()
                .bodyToMono(DepartmentResponseDTO.class);
    }

    public Mono<DepartmentResponseDTO> getDepartmentById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(DepartmentResponseDTO.class);
    }

    public Mono<Void> deleteDepartment(Long id) {
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<DepartmentResponseDTO> findDepartmentByName(String name) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/by-name")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .bodyToFlux(DepartmentResponseDTO.class);
    }
}
