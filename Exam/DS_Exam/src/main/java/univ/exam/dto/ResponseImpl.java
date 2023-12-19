package univ.exam.dto;

import lombok.Data;
import univ.exam.model.Entity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
@Data
public class ResponseImpl<T extends Entity> implements Response<T> {
    private int status;
    private String details;
    private List<T> entities;
    @Serial
    private static final long serialVersionUID = 1L;

    public ResponseImpl(int status, String details) {
        this.status = status;
        this.details = details;
        this.entities = new ArrayList<>();
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public boolean isEmpty() {
        return status == Status.ERROR || status == Status.SUCCESS_EMPTY;
    }

    @Override
    public T getFirst() {
        return entities.get(0);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entities);
    }

    @Override
    public void add(T entity) {
        entities.add(entity);
    }

    public ResponseImpl(int status, String details, List<T> entities) {
        this.status = status;
        this.details = details;
        this.entities = entities;
    }

    public ResponseImpl(int status, String details, T train) {
        this.status = status;
        this.details = details;
        this.entities = new ArrayList<>();
        entities.add(train);
    }

    public ResponseImpl() {
        this.entities = new ArrayList<>();
    }
}
