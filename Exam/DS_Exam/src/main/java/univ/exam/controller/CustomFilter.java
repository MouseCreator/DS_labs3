package univ.exam.controller;

import java.util.function.Predicate;

public class CustomFilter<T> {

    private final Predicate<T> predicate;

    public CustomFilter(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public CustomFilter(boolean b) {
        this.predicate = t -> b;
    }

    public CustomFilter<T> and(CustomFilter<T> other) {
        return new CustomFilter<>(predicate.and(other.predicate));
    }

    public Predicate<T> get() {
        return predicate;
    }
}
