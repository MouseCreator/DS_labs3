package univ.exam.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class PassableResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int status;
    private String details;
    private List<String> entities;

    public PassableResponse() {
        entities = new ArrayList<>();
    }
}
