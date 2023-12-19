package univ.exam.controller;

import univ.exam.model.Letter;
import univ.exam.util.DateManager;

import java.time.LocalDateTime;

public class LetterFilterFactory implements FilterFactory<Letter> {
    public CustomFilter<Letter> toFilter(String details) {
        String[] filterStrings = details.trim().split(",");
        CustomFilter<Letter> customFilter = new CustomFilter<>(true);
        for (String filterString : filterStrings) {
            if (filterString.isEmpty())
                continue;
            CustomFilter<Letter> filter = toSimpleFilter(filterString);
            if (filter == null)
                continue;
            customFilter = customFilter.and(filter);
        }
        return customFilter;
    }

    private CustomFilter<Letter> toSimpleFilter(String filterString) {
        String[] keyValue = filterString.split("=", 2);
        if (keyValue.length != 2) {
            return null;
        }
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();
        switch (key) {
            case "id" -> {
                long id = Long.parseLong(value);
                return new CustomFilter<>(t -> t.getId().equals(id));
            }
            case "title" -> {
                return new CustomFilter<>(t -> t.getTitle().contains(value));
            }
            case "tag" -> {
                return new CustomFilter<>(t->t.getTags().contains(value));
            }
            case "after" -> {
                LocalDateTime time = DateManager.stringToLocalDateTime(value);
                return new CustomFilter<>(t -> t.getSentDate().isAfter(time));
            }
            case "before" -> {
                LocalDateTime time = DateManager.stringToLocalDateTime(value);
                return new CustomFilter<>(t -> t.getSentDate().isBefore(time));
            }
            case "category" -> {
                return new CustomFilter<>(t -> value.equals(t.getCategory()));
            }
            case "from" -> {
                return new CustomFilter<>(t -> value.equals(t.getFrom().getName()));
            }
            case "to" -> {
                return new CustomFilter<>(t -> value.equals(t.getTo().getName()));
            }
            default -> {
                return new CustomFilter<>(false);
            }
        }
    }
}
