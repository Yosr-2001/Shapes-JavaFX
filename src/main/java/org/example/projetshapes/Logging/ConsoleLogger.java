package org.example.projetshapes.Logging;

public class ConsoleLogger implements  LoggerStrategy{
    @Override
    public void log(String message) {
        System.out.println("Console log: " + message);
    }

}
