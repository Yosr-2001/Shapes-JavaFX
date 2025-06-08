package org.example.projetshapes.Selection;

import java.awt.*;

public class ShapeFactory {
    public static ShapeDraw createShape(String type, double x,double y){
        return switch(type.toLowerCase()){
            case "rectangle"-> new RectangleShape(x,y);
            case "cercle", "circle" -> new CircleShape(x, y);
            default -> throw new IllegalArgumentException("Type de forme non reconnu");
        };
    }
}
