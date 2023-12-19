package univ.exam.service;


import univ.exam.controller.CustomFilter;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudService<T> {
    T add(T train);
    void remove(Long id);
    T getById(Long id);
    List<T> getAll();
    T update(T train);
    List<T> getByFilter(CustomFilter<T> trainCustomFilter);
}
