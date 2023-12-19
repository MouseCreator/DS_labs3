package univ.exam.service;


import univ.exam.controller.CustomFilter;

import java.util.List;

public interface CrudService<T> {
    T add(T letter);
    void remove(Long id);
    T getById(Long id);
    List<T> getAll();
    T update(T letter);
    List<T> getByFilter(CustomFilter<T> trainCustomFilter);
}
