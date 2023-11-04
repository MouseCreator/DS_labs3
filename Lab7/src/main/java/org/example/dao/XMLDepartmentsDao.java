package org.example.dao;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class XMLDepartmentsDao implements DepartmentsDAO {
    private final String inputFileXML;
    private final Parser<Departments> parser;
    private final Writer<Departments> writer;
    public XMLDepartmentsDao(String inputFileXML, Parser<Departments> parser, Writer<Departments> writer) {
        this.inputFileXML = inputFileXML;
        this.parser = parser;
        this.writer = writer;
    }
    private void writeFile(Departments parsed) {
        writer.write(inputFileXML, parsed);
    }

    private Departments readFile() {
        return parser.parse(inputFileXML);
    }

    private <V> V withReadWrite(Function<Departments, V> function) {
        Departments departments = readFile();
        V result =  function.apply(departments);
        writeFile(departments);
        return result;
    }

    private void withReadWrite(Consumer<Departments> consumer) {
        Departments departments = readFile();
        consumer.accept(departments);
        writeFile(departments);
    }
    @Override
    public List<Department> findAll() {
        return readFile().getDepartmentList();
    }

    @Override
    public Department create(Department object) {
        return withReadWrite(parsed->{
            List<Department> departmentList = parsed.getDepartmentList();
            if (object.getId()==null) {
                Long nextId = findNextId(departmentList);
                object.setId(nextId);
            }
            departmentList.add(object);
            return object;
        });
    }

    private Long findNextId(List<Department> departmentList) {
        OptionalLong max = departmentList.stream().mapToLong(Department::getId).max();
        if (max.isEmpty()) {
            return 1L;
        }
        return max.getAsLong()+1L;
    }

    @Override
    public Department update(Department object) {
        return withReadWrite(parsed -> {
            List<Department> departmentList = parsed.getDepartmentList();
            Long targetId = object.getId();
            if (targetId==null){
                throw new IllegalArgumentException("Not initialized department passed to update");
            }
            deleteById(departmentList, targetId);
            departmentList.add(object);
            return object;
        });
    }

    private void deleteById(List<Department> departmentList, Long targetId) {
        if(!departmentList.removeIf(d-> Objects.equals(d.getId(), targetId))){
            throw new NoSuchElementException("Called to update not existing department");
        }
    }

    @Override
    public void delete(Department object) {
        withReadWrite(departments -> {
            deleteById(departments.getDepartmentList(), object.getId());
        });

    }

    @Override
    public boolean delete(Long id) {
        return withReadWrite(departments -> {
            try {
                deleteById(departments.getDepartmentList(), id);
            } catch (NoSuchElementException e) {
                return false;
            }
            return true;
        });
    }

    @Override
    public Optional<Department> find(Long id) {
        return readFile().getDepartmentList().stream().filter(d->id.equals(d.getId())).findFirst();
    }
}
