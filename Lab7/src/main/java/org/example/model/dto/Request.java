package org.example.model.dto;

import lombok.Data;
import org.example.model.Departments;

@Data
public class Request {
    private String method;
    private String target;
    private Departments departments;
}
