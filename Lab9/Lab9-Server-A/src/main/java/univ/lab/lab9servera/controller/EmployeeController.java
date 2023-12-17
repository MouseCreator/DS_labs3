package univ.lab.lab9servera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import univ.lab.lab9servera.dto.*;
import univ.lab.lab9servera.service.EmployeeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    @GetMapping
    public List<EmployeeResponseDTO> findAll() {
        return employeeService.findAll();
    }

    @PostMapping
    public EmployeeResponseDTO save(@RequestBody EmployeeCreateDTO employee) {
        return employeeService.save(employee);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO findById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @GetMapping("/by-name")
    public List<EmployeeResponseDTO> getAllByName(@RequestParam String name) {
        return employeeService.getAllByName(name);
    }

    @GetMapping("/by-department")
    public List<EmployeeResponseDTO> getAllByName(@RequestParam Long departmentId) {
        return employeeService.getAllByDepartment(departmentId);
    }

    @PutMapping
    public EmployeeResponseDTO update(@RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        return employeeService.update(employeeUpdateDTO);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
