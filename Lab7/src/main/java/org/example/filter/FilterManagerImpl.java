package org.example.filter;

import java.util.List;
import java.util.function.Predicate;

public class FilterManagerImpl<T> implements FilterManager<T> {
    private final FilterFactory<Predicate<T>> filterFactory;

    public FilterManagerImpl(FilterFactory<Predicate<T>> filterFactory) {
        this.filterFactory = filterFactory;
    }
    @Override
    public List<T> filter(List<T> origin, String filterString) {
        Predicate<T> predicate = filterFactory.parse(filterString);
        return origin.stream().filter(predicate).toList();
    }
}
