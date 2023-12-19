package univ.exam.repository;

import univ.exam.controller.CustomFilter;
import univ.exam.model.Entity;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractListRepository<T extends Entity> implements CrudRepository<T> {
    protected final List<T> trainList;
    protected long idCount = 0;
    public AbstractListRepository() {
        this.trainList = new ArrayList<>();
    }

    protected abstract void updateModel(T newT, T prevT);

    @Override
    public T add(T model) {
        if (model.getId()==null) {
            model.setId(idCount++);
            trainList.add(model);
            return model;
        }
        return this.update(model);
    }

    @Override
    public void remove(Long id) {
        boolean wasRemoved = trainList.removeIf(findById(id));
        if (!wasRemoved) {
            throw new NoSuchElementException("No train with id " + id);
        }
    }

    @Override
    public T getById(Long id) {
        return trainList.stream()
                .filter(findById(id))
                .findFirst()
                .orElseThrow();
    }

    private Predicate<T> findById(Long id) {
        return t -> Objects.equals(t.getId(), id);
    }

    public T update(T train) {
        if (train.getId() == null) {
            return add(train);
        }
        Optional<T> trainOptional = trainList.stream().filter(findById(train.getId())).findFirst();
        if (trainOptional.isPresent()) {
            T prev = trainOptional.get();
            updateModel(train, prev);
            return prev;
        } else {
            throw new NoSuchElementException("No train with id " + train.getId());
        }

    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(trainList);
    }

    @Override
    public List<T> getByFilter(CustomFilter<T> trainCustomFilter) {
        return trainList.stream().filter(trainCustomFilter.get()).toList();
    }
}
