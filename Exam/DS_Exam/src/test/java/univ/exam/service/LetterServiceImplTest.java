package univ.exam.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import univ.exam.model.Letter;
import univ.exam.model.User;
import univ.exam.repository.ListLetterRepository;

import static org.junit.jupiter.api.Assertions.*;

class LetterServiceImplTest {

    private LetterService service;

    @BeforeEach
    void setUp() {
        service = new LetterServiceImpl(new ListLetterRepository());
    }

    @Test
    void add() {
        Letter letter = new Letter();
        letter.setCategory("Hello");
        letter.setFrom(new User("User", false));
        Letter add = service.add(letter);
        assertEquals(0L, add.getId());

        Letter letter2 = new Letter();
        letter2.setCategory("Hello");
        letter2.setFrom(new User("Sus", true));
        Letter addedSus = service.add(letter2);

        assertEquals(1L, addedSus.getId());
        assertEquals("Spam", addedSus.getCategory());
    }
}