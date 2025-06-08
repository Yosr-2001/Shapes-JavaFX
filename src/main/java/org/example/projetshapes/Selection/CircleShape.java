package org.example.projetshapes.Selection;

import javafx.scene.canvas.GraphicsContext;

public class CircleShape implements ShapeDraw{
    private double x,y;

    public CircleShape(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeOval(x, y, 60, 60);}

}
