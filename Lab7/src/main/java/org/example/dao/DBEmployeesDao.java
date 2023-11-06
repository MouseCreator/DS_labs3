package org.example.dao;

import org.example.model.Department;
import org.example.model.Employee;
import org.example.util.ConnectionPool;
import org.example.util.ConnectionWrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBEmployeesDao extends AbstractDBCrudDao<Employee> implements EmployeesDao {
    public DBEmployeesDao(ConnectionPool connectionPool) {
        super(connectionPool);
    }
    @Override
    protected String getTableName() {
        return "employees";
    }
    @Override
    protected String[] fields() {
        String[] arr = new String[6];
        arr[0] = "id";
        arr[1] = "name";
        arr[2] = "age";
        arr[3] = "role";
        arr[4] = "experience";
        arr[5] = "department";
        return arr;
    }

    @Override
    protected int applyCreation(PreparedStatement statement, Employee object) throws SQLException {
        statement.setString(1, object.getName());
        statement.setInt(2, object.getAge());
        statement.setString(3, object.getRole());
        statement.setInt(4, object.getExperienceYears());
        statement.setLong(5, object.getDepartmentId());
        return fields().length;
    }

    @Override
    protected Employee toInstance(ResultSet set) throws SQLException {
        Employee employee = new Employee();
        employee.setId(set.getObject("id", Long.class));
        employee.setName(set.getString("name"));
        employee.setAge(set.getObject("age", Integer.class));
        employee.setRole(set.getString("role"));
        employee.setExperienceYears(set.getObject("experience", Integer.class));
        employee.setDepartmentId(set.getObject("department", Long.class));
        return employee;
    }

    @Override
    public List<Employee> findAllEmployeesOfDepartment(Department department) {
        return findAllEmployeesOfDepartment(department.getId());
    }

    @Override
    public List<Employee> findAllEmployeesOfDepartment(Long id) {
        String sql = "SELECT * FROM employees where department = ?";
        List<Employee> resultList = new ArrayList<>();
        try(ConnectionWrapper connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.get().prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                resultList.add(toInstance(set));
            }
            set.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }


}
