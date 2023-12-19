package univ.exam.dto;

import univ.exam.model.Entity;

import java.io.Serializable;

public interface Request<T extends Entity> extends Serializable {
    int getType();
    String getDetails();
    T getBody();
}
