package univ.exam.controller.ui;

import univ.exam.communicator.ClientCommunicator;
import univ.exam.dto.*;
import univ.exam.model.Letter;
import univ.exam.model.User;
import univ.exam.util.DateManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommonClientController implements ClientController{
    private final ClientCommunicator<Letter> communicator;
    private final LocalController ioManager;

    public CommonClientController(ClientCommunicator<Letter> communicator) {
        this.communicator = communicator;
        this.ioManager = LocalController.consoleController();
    }
    private String readCommand() {
        return ioManager.askString("Enter command");
    }
    @Override
    public void mainLoop() {

        while (true) {
            String command = readCommand();
            if (command.trim().equals("close")) {
                break;
            }
            try {
                commandProcessor(CommandStack.create(command));
            } catch (Exception e) {
                ioManager.print(e.getMessage());
            }
        }
    }

    private void commandProcessor(CommandStack commandStack) {
        String command = commandStack.getNextCommand();
        switch (command) {
            case "add" -> addTrain();
            case "get" -> getTrains(commandStack);
            case "remove" -> removeTrain(commandStack);
            case "update" -> updateTrain(commandStack);
            default -> ioManager.print("Unknown command " + command);
        }
    }
    private Letter createLetter() {
        Letter letter = new Letter();
        letter.setTitle(ioManager.askString("Enter letter title:"));
        User from = createUser("FROM");
        letter.setFrom(from);
        User to = createUser("TO");
        letter.setTo(to);

        letter.setSentDate(getTime());
        letter.setCategory(ioManager.askString("Enter category:"));
        List<String> tags = createTags(new ArrayList<>());
        letter.setTags(tags);
        return letter;
    }

    private List<String> createTags(List<String> origin) {
        String tags = ioManager.askStringOr("Enter tags:", "");
        if (tags.isEmpty()) {
            return origin;
        }
        String[] split = tags.split(",");
        List<String> tagsList = new ArrayList<>();
        for (String tag : split) {
            if (tag.isEmpty())
                continue;
            tagsList.add(tag.trim());
        }
        return tagsList;
    }

    private User createUser(String dest) {
        User from = new User();
        from.setName(ioManager.askString("Enter " + dest + " name:"));
        if (from.getName().contains("sus")) {
            from.setSuspicious(true);
        }
        return from;
    }

    private LocalDateTime getTime() {
        String s = ioManager.askStringOr("Enter date (YYYY-MM-DD HH:MM:SS):", null);
        LocalDateTime time;
        if (s == null) {
            time = LocalDateTime.now();
        } else {
            time = DateManager.stringToLocalDateTime(s);
        }
        return time;
    }

    private Letter createLetter(Letter origin) {
        Letter letter = new Letter();
        letter.setTitle(ioManager.askStringOr("Enter letter title:", origin.getTitle()));
        letter.setCategory(ioManager.askStringOr("Enter category:", origin.getCategory()));
        List<String> tags = createTags(origin.getTags());
        letter.setTags(tags);
        return letter;
    }
    private void updateTrain(CommandStack commandStack) {
        long targetId = Long.parseLong(commandStack.getNextValue());
        Request<Letter> requestG = Requests.getById(targetId);
        Response<Letter> response = communicator.sendAndReceive(requestG);
        validate(response);

        Letter toModify = response.getFirst();
        ioManager.print("Original train:\n" + toModify);
        Letter updated = createLetter(toModify);
        Request<Letter> requestU = Requests.update(updated);
        Response<Letter> responseF = communicator.sendAndReceive(requestU);
        print(responseF);
    }

    private void validate(Response<Letter> response) {
        if (response.getStatus()==Status.ERROR) {
            throw new IllegalStateException(response.getDetails());
        }
    }

    private void print(Response<Letter> response) {
        switch (response.getStatus()) {
            case Status.ERROR, Status.SUCCESS_EMPTY -> ioManager.print(response.getDetails());
            case Status.SUCCESS_MONO -> {
                ioManager.print(response.getDetails());
                ioManager.print(response.getFirst());
            }
            case Status.SUCCESS_MULTI -> {
                ioManager.print(response.getDetails());
                ioManager.print(response.getAll());
            }
            default -> ioManager.print("Unknown status returned: " + response.getStatus());
        }
    }

    private void removeTrain(CommandStack commandStack) {
        long targetId = Long.parseLong(commandStack.getNextValue());
        Request<Letter> requestG = Requests.remove(targetId);
        Response<Letter> response = communicator.sendAndReceive(requestG);
        print(response);
    }

    private void getTrains(CommandStack commandStack) {
        String details = commandStack.getRest();
        Request<Letter> requestG = Requests.get(details);
        Response<Letter> response = communicator.sendAndReceive(requestG);
        print(response);
    }

    private void addTrain() {
        Letter newTrain = createLetter();
        Request<Letter> requestG = Requests.post(newTrain);
        Response<Letter> response = communicator.sendAndReceive(requestG);
        print(response);
    }

    @Override
    public void close() throws Exception {
        communicator.close();
    }
}
