package univ.exam.dto;

import univ.exam.model.Entity;

public class Requests {
    public static <T extends Entity> Request<T> getById(Long id) {
        return get("id="+id);
    }

    public static <T extends Entity>  Request<T> update(T updated) {
        return new RequestImpl<>(RequestType.UPDATE, "", updated);
    }

    public static <T extends Entity>  Request<T>  post(T created) {
        return new RequestImpl<>(RequestType.POST, "", created);
    }

    public static <T extends Entity> Request<T> remove(long targetId) {
        return new RequestImpl<>(RequestType.DELETE, ""+targetId);
    }

    public static <T extends Entity> Request<T> get(String details) {
        return new RequestImpl<>(RequestType.GET, details);
    }
}
