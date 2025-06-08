package org.example.projetshapes.Logging;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements LoggerStrategy {
    private static final String FILE_NAME = "logs.txt";

    @Override
    public void log(String message) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
