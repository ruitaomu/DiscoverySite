package com.jfighter.discoverysite.util;

public class Coordinate {
    private final double posX;
    private final double posY;

    public Coordinate(double y, double x) {
        this.posX = x;
        this.posY = y;
    }

    public double X() {
        return posX;
    }

    public double Y() {
        return posY;
    }
}