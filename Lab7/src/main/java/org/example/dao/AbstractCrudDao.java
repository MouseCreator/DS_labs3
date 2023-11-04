package org.example.dao;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudDao<T> implements GenericCrudDao<T> {

    @Override
    public List<T> findAll() {
        return null;
    }
    @Override
    public T create(T object) {
        return null;
    }
    @Override
    public T update(T object) {
        return null;
    }
    @Override
    public void delete(T object) {

    }
    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Optional<T> find(Long id) {
        return Optional.empty();
    }
}
