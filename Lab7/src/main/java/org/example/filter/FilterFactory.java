package org.example.filter;

/**
 * Creates filters for data
 * @param <T> - expected filter type, for example SQL select query String or Predicate
 */
public interface FilterFactory<T> {
    T parse(String input);
}
