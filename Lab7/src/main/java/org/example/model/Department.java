package org.example.model;

import lombok.Data;
import org.example.dao.IdIterable;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Department implements IdIterable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
}
