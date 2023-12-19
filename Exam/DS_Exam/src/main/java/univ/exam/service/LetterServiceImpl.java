package univ.exam.service;

import univ.exam.model.Letter;
import univ.exam.repository.CrudRepository;

public class LetterServiceImpl extends AbstractService<Letter> implements LetterService {

    public LetterServiceImpl(CrudRepository<Letter> repository) {
        super(repository);
    }


}
