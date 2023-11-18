package org.example.model.dto;

import lombok.Data;
import org.example.model.Departments;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int status;
    private String target;
    private String detailsString;
    private Departments departments;
}
