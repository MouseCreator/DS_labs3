package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface GenericCrudDao<T> {
    List<T> findAll();
    T create(T object);
    T update(T object);
    void delete(T object);
    boolean delete(Long id);
    Optional<T> find(Long id);
}
