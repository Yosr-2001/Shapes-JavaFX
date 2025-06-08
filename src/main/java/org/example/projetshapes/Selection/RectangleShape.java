package org.example.projetshapes.Selection;

import javafx.scene.canvas.GraphicsContext;

public class RectangleShape implements  ShapeDraw{

    private double x,y;

    public RectangleShape(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeRect(x, y, 100, 60);
    }
}
