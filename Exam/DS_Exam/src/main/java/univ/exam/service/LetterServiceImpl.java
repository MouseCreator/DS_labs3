package univ.exam.service;

import univ.exam.model.Letter;
import univ.exam.repository.CrudRepository;

import java.util.List;
import java.util.function.Predicate;

public class LetterServiceImpl extends AbstractService<Letter> implements LetterService {

    public LetterServiceImpl(CrudRepository<Letter> repository) {
        super(repository);
    }

    @Override
    public Letter add(Letter letter) {
        if (letter.getFrom() != null && letter.getFrom().isSuspicious()) {
            letter.setCategory("Spam");
        }
        return repository.add(letter);
    }


    @Override
    public List<Letter> getFrom(String username) {
        return filtered(t -> t.getFrom() != null &&t.getFrom().getName().equals(username));
    }

    private List<Letter> filtered(Predicate<Letter> p) {
        return repository.getAll().stream().filter(p).toList();
    }

    @Override
    public List<Letter> getTo(String username) {
        return filtered(t -> t.getTo() != null && t.getTo().getName().equals(username));
    }

    @Override
    public List<Letter> getByTitle(String title) {
        return filtered(t -> t.getTitle().contains(title));
    }

    @Override
    public List<Letter> getByCategory(String category) {
        return filtered(t -> category.equals(t.getCategory()));
    }

    @Override
    public List<Letter> getByTag(String tag) {
        return filtered(t -> t.getTags().contains(tag));
    }
}
