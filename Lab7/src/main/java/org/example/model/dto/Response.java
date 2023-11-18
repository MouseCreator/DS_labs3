package org.example.model.dto;

import lombok.Data;
import org.example.model.Departments;
@Data
public class Response {
    private int status;
    private String detailsString;
    private Departments departments;
}
