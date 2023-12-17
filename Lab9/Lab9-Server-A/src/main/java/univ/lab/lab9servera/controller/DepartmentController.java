package univ.lab.lab9servera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import univ.lab.lab9servera.model.Department;
import univ.lab.lab9servera.service.DepartmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    @GetMapping
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    @PostMapping
    public Department save(@RequestBody Department department) {
        return departmentService.save(department);
    }
}
