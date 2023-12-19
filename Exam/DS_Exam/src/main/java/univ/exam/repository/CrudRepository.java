package univ.exam.repository;

import univ.exam.controller.CustomFilter;

import java.util.List;

public interface CrudRepository<T> {
    T add(T entity);
    void remove(Long id);
    List<T> getByFilter(CustomFilter<T> trainCustomFilter);
    T update(T entity);
    List<T> getAll();
    T getById(Long id);
}
