package org.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonControllerTest {

    private static class PrintingBufferedReader extends BufferedReader {
        private final BufferedReader reader;
        public PrintingBufferedReader(Reader in, OutputStream outputStream) {
            super(in);
            reader = new BufferedReader(in);
            this.outputStream = outputStream;
        }

        private final OutputStream outputStream;
        @Override
        public String readLine() throws IOException {
            String line = reader.readLine();
            if (line != null) {
                outputStream.write(line.getBytes());
                outputStream.write(System.lineSeparator().getBytes());
            }
            return line;
        }
    }

    private CommonController controller;
    private OutputStream outputStream;
    private void processInput(String[] lines) {
        List<String> input = toAnswers(lines);
        String inputString = toAnswersString(input);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        PrintingBufferedReader printingInputStream = new PrintingBufferedReader(new InputStreamReader(inputStream), outputStream);
        controller.supplyInput(printingInputStream);
    }

    private List<String> toAnswers(String[] lines) {
        List<String> answers = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith(">")) {
                answers.add(line.substring(2));
            }
        }
        return answers;
    }
    private String toAnswersString(List<String> answers) {
        StringBuilder builder = new StringBuilder();
        for (String line : answers) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        controller = new CommonController(System.in, printStream);
    }

    void testDialogue(String[] dialogueLines, Consumer<String> consumer) {
        int currentLine = 0;
        String s = "";
        while (currentLine < dialogueLines.length) {
            consumer.accept(dialogueLines[currentLine]);
            String out = outputStream.toString();
            while (out.startsWith(s) && !out.equals(s)) {
                s += dialogueLines[currentLine];
                s += "\r\n";
                currentLine++;
            }
            assertEquals(out.trim(), s.trim());
        }
    }
    @Test
    void askBoolean() {
        String[] expectedDialogue = {
                "Is it red?",
                "> y",
                "Is it blue?",
                "> s",
                "Boolean value is expected!",
                "> false"
        };
        processInput(expectedDialogue);
        testDialogue(expectedDialogue, s->controller.askBoolean(s));
    }

    @Test
    void askInteger() {
        String[] expectedDialogue = {
                "How old are you?",
                "> 12",
                "How many friends do you have?",
                "> 3",
                "Integer value is expected!",
                "> 10"
        };
        processInput(expectedDialogue);
        testDialogue(expectedDialogue, s->controller.askInteger(s));
    }

    @Test
    void askLong() {
        String[] expectedDialogue = {
                "Long time no see!",
                "> 1000",
                "Type a number bigger than that!",
                "> P",
                "Long value is expected!",
                "> 2000"
        };
        processInput(expectedDialogue);
        testDialogue(expectedDialogue, s->controller.askLong(s));
    }

    @Test
    void askString() {
        String[] expectedDialogue = {
                "Type a string",
                "> ",
                "String is expected!",
                ">    ",
                "String is expected!",
                "> Here it comes"
        };
        processInput(expectedDialogue);
        testDialogue(expectedDialogue, s->controller.askString(s));
    }

    @Test
    void print() {
        String[] expectedDialogue = {
                "I am writing",
                "Strings for you",
                "When you call me",
        };
        processInput(expectedDialogue);
        testDialogue(expectedDialogue, s->controller.print(s));
    }
}