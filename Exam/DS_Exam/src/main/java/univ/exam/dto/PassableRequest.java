package univ.exam.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PassableRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int type;
    private String details;
    private String body;
}
