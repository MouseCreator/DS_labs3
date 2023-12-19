package univ.exam.controller;

public interface FilterFactory<T> {
    CustomFilter<T> toFilter(String details);
}
