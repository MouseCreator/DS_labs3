package univ.exam.service;

import univ.exam.model.Letter;

import java.util.List;


public interface LetterService extends CrudService<Letter> {
    List<Letter> getFrom(String username);
    List<Letter> getTo(String username);
    List<Letter> getByTitle(String title);
    List<Letter> getByCategory(String category);
    List<Letter> getByTag(String tag);
}
