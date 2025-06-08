package org.example.projetshapes.entities;

public class Shape {
    private String type;
    private double x;
    private double y;
    private int idDessin;


    public Shape(String type, double x, double y, int idDessin) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.idDessin = idDessin;
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


    public int getIdDessin() {
    return this.idDessin;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", idDessin=" + idDessin +
                '}';
    }
}
