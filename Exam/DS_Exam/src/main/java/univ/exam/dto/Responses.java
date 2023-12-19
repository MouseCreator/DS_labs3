package univ.exam.dto;

import univ.exam.model.Entity;

import java.util.List;

public class Responses {
    public static <T extends Entity> Response<T> error(String errorMessage) {
        return new ResponseImpl<>(Status.ERROR, errorMessage);
    }

    public static <T extends Entity> Response<T> empty() {
        return new ResponseImpl<>(Status.SUCCESS_EMPTY, "Success");
    }

    public static <T extends Entity> Response<T> of(T entity) {
        return new ResponseImpl<>(Status.SUCCESS_MONO, "Success", entity);
    }

    public static <T extends Entity> Response<T> of(List<T> entities) {
        return new ResponseImpl<>(Status.SUCCESS_MULTI, "Success", entities);
    }
}
