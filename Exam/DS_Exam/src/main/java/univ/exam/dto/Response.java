package univ.exam.dto;

import univ.exam.model.Entity;

import java.io.Serializable;
import java.util.List;

public interface Response<T extends Entity> extends Serializable {
    int getStatus();
    String getDetails();
    boolean isEmpty();
    T getFirst();
    List<T> getAll();
    void add(T entity);
}
