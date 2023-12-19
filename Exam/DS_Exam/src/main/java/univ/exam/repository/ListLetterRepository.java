package univ.exam.repository;

import univ.exam.model.Letter;


public class ListLetterRepository extends AbstractListRepository<Letter> implements LetterRepository {
    @Override
    protected void updateModel(Letter update, Letter prev) {
        prev.setTo(update.getTo());
        prev.setFrom(update.getFrom());
        prev.setTitle(update.getTitle());
        prev.setCategory(update.getCategory());
        prev.setSentDate(update.getSentDate());
        prev.setTags(update.getTags());
    }
}
