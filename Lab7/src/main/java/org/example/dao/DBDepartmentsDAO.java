package org.example.dao;

import org.example.filter.DepartmentsSQLFilter;
import org.example.filter.FilterFactory;
import org.example.model.Department;
import org.example.util.ConnectionProvider;
import org.example.util.ConnectionWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBDepartmentsDAO extends AbstractDBCrudDao<Department> implements DepartmentsDAO {
    private final FilterFactory<String> filterFactory = new DepartmentsSQLFilter();
    @Override
    protected String getTableName() {
        return "departments";
    }
    @Override
    protected String[] fields() {
        String[] arr = new String[2];
        arr[0] = "id";
        arr[1] = "name";
        return arr;
    }

    @Override
    protected int applyCreation(PreparedStatement statement, Department object) throws SQLException{
        statement.setString(1, object.getName());
        return fields().length;
    }

    public DBDepartmentsDAO(ConnectionProvider provider) {
        super(provider);
    }


    @Override
    protected Department toInstance(ResultSet set) throws SQLException {
        return toDepartment(set);
    }

    private Department toDepartment(ResultSet set) throws SQLException {
        Department department = new Department();
        department.setId(set.getObject("id", Long.class));
        department.setName(set.getString("name"));
        return department;
    }

    @Override
    public List<Department> findByFilter(String filterString) {
        String sql = filterFactory.parse(filterString);
        List<Department> resultList = new ArrayList<>();
        try(ConnectionWrapper connection = provider.getConnection()) {
            Statement statement = connection.get().createStatement();
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                resultList.add(toDepartment(set));
            }
            set.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}
