package org.example.model.dto;

import lombok.Data;
import org.example.model.Departments;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String method;
    private String target;
    private String details;
    private Departments departments;


}
