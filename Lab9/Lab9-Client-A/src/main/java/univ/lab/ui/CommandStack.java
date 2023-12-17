package univ.lab.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandStack {
    private final List<String> commandStack;

    private CommandStack(List<String> commandStack) {
        this.commandStack = commandStack;
    }

    public static CommandStack create(String source) {
        source = source.replace(" +", " ");
        String[] parts = source.trim().split(" ");
        List<String> stream = new ArrayList<>(Arrays.asList(parts));
        return new CommandStack(stream);
    }

    private String formatCommand(String command) {
        return command.toLowerCase().trim();
    }
    public String getNextCommand() {
        return formatCommand(getNextValue());
    }
    public String getNextValue() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot get parameter, because command stack is empty!");
        }
        return commandStack.remove(0);
    }

    public boolean isEmpty() {
        return commandStack.size() == 0;
    }

    public boolean hasCommand() {
        return commandStack.size() != 0;
    }
}
