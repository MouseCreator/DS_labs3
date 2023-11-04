package org.example.model;

import lombok.Data;
import org.example.dao.IdIterable;

@Data
public class Department implements IdIterable {
    private Long id;
    private String name;
}
