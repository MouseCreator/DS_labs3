package univ.exam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import univ.exam.model.Entity;

import java.io.Serial;
@Data
@NoArgsConstructor
public class RequestImpl<T extends Entity> implements Request<T> {
    private int type;
    private String details;
    private T body;
    @Serial
    private static final long serialVersionUID = 1L;

    public RequestImpl(int type, String s) {
        this.type = type;
        this.details = s;
        this.body = null;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public T getBody() {
        return body;
    }

    public RequestImpl(int type, String details, T body) {
        this.type = type;
        this.details = details;
        this.body = body;
    }
}
