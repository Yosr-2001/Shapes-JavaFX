package org.example.projetshapes.entities;

public class Shape {
    private String type;
    private double x;
    private double y;

    public Shape(String type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Shape() {}

    public String getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

}
