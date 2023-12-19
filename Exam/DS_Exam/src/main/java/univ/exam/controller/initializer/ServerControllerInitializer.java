package univ.exam.controller.initializer;

import univ.exam.controller.LetterFilterFactory;
import univ.exam.controller.LetterServiceController;
import univ.exam.repository.ListLetterRepository;
import univ.exam.repository.LetterRepository;
import univ.exam.service.LetterService;
import univ.exam.service.LetterServiceImpl;

public class ServerControllerInitializer {
    public static LetterServiceController getLetterServerController() {
        LetterRepository repository = new ListLetterRepository();
        LetterService service = new LetterServiceImpl(repository);
        LetterFilterFactory filterFactory = new LetterFilterFactory();
        return new LetterServiceController(service, filterFactory);
    }
}
