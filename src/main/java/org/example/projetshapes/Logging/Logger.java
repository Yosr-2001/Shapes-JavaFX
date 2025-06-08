package org.example.projetshapes.Logging;

import java.util.ArrayList;
import java.util.List;
public class Logger {
    private static Logger instance;

    private List<String>logs=new ArrayList<>();

    private Logger() { 	}

    public static Logger getInstance() {
        if(instance==null)
            instance= new Logger();
        return instance;
    }
    public void log(String message) {
        logs.add(message);
        System.out.println("LOG: " + message);
    }


}
