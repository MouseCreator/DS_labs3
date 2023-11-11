package org.example.controller;

import org.example.dao.IdIterable;

import java.util.List;

public interface ModelController<T extends IdIterable> {
    List<T> getAll();
    List<T> filter(List<T> result, String fString);
    void remove(Long ln);
    void update(T model);
    void create(T model);
    List<T> filter(String filterString);
    T get(Long id);

}
