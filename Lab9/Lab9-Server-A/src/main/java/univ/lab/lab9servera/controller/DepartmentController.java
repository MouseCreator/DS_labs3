package univ.lab.lab9servera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import univ.lab.lab9servera.dto.DepartmentCreateDTO;
import univ.lab.lab9servera.dto.DepartmentResponseDTO;
import univ.lab.lab9servera.service.DepartmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    @GetMapping
    public List<DepartmentResponseDTO> findAll() {
        return departmentService.findAll();
    }

    @PostMapping
    public DepartmentResponseDTO save(@RequestBody DepartmentCreateDTO departmentCreateDTO) {
        return departmentService.save(departmentCreateDTO);
    }
}
