package univ.lab.ui;

import univ.lab.dto.DepartmentCreateDTO;
import univ.lab.dto.DepartmentUpdateDTO;
import univ.lab.model.Department;

public class DepartmentCreator {
    private final CommonController ioManager;
    public DepartmentCreator(CommonController ioManager) {
        this.ioManager = ioManager;
    }

    public DepartmentCreateDTO create() {
        DepartmentCreateDTO department = new DepartmentCreateDTO();
        department.setName(ioManager.askString("Enter department name"));
        return department;
    }

    public DepartmentUpdateDTO update(Department origin) {
        DepartmentUpdateDTO department = new DepartmentUpdateDTO();
        department.setId(origin.getId());
        department.setName(ioManager.askStringOr("Enter department name", origin.getName()));
        return department;
    }
}
