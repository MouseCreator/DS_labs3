package univ.exam.service;

import univ.exam.controller.CustomFilter;
import univ.exam.model.Entity;
import univ.exam.repository.CrudRepository;

import java.util.List;

public abstract class AbstractService<T extends Entity> implements CrudService<T> {

    protected final CrudRepository<T> repository;

    public AbstractService(CrudRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T add(T letter) {
        return repository.add(letter);
    }

    @Override
    public void remove(Long id) {
        repository.remove(id);
    }

    @Override
    public T getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<T> getAll() {
        return repository.getAll();
    }

    @Override
    public T update(T letter) {
        return repository.update(letter);
    }

    @Override
    public List<T> getByFilter(CustomFilter<T> trainCustomFilter) {
        return repository.getByFilter(trainCustomFilter);
    }
}
