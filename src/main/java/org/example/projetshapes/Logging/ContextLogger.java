package org.example.projetshapes.Logging;

public class ContextLogger {
    private LoggerStrategy strategy;

    public void setStrategy(LoggerStrategy strategy) {
        this.strategy = strategy;
    }


    public void log(String message) {
        if (strategy != null) {
            strategy.log(message);
        } else {
            System.out.println("Aucune stratégie de logging définie.");
        }
    }
}
