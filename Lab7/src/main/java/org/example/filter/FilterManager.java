package org.example.filter;

import java.util.List;

/**
 * Filters collection of data
 * @param <T> - parametrized data
 */
public interface FilterManager<T> {
    List<T> filter(List<T> origin, String filterString);
}
