package com.jfighter.discoverysite.util;

public class Coordinate {
    private final float posX;
    private final float posY;

    public Coordinate(float y, float x) {
        this.posX = x;
        this.posY = y;
    }

    public float X() {
        return posX;
    }

    public float Y() {
        return posY;
    }
}