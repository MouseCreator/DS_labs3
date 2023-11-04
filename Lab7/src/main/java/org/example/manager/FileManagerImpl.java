package org.example.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileManagerImpl implements FileManager {
    @Override
    public void copyXML(String origin, String destination) {
        Path source = Paths.get(origin);
        Path destinationPath = Paths.get(destination);
        try {
            Files.copy(source, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearXML(String filename) {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String readRaw(String filename) {
        try {
            StringBuilder content = new StringBuilder();

            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
