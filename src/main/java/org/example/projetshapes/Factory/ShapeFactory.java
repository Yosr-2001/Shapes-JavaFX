package org.example.projetshapes.Factory;

import org.example.projetshapes.Strategy.CircleShape;
import org.example.projetshapes.Strategy.RectangleShape;
import org.example.projetshapes.Strategy.ShapeDraw;

public class ShapeFactory {
    public static ShapeDraw createShape(String type, double x, double y){
        return switch(type.toLowerCase()){
            case "rectangle"-> new RectangleShape(x,y);
            case "cercle", "circle" -> new CircleShape(x, y);
            default -> throw new IllegalArgumentException("Type de forme non reconnu");
        };
    }
}
