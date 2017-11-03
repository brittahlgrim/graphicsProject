package com.example.brittanyahlgrim.myfirstapp;

/**
 * Created by brittanyahlgrim on 11/1/17.
 */

public class Coordinates {
    private float x;
    private float y;
    private float z;

    public Coordinates(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public static float[] convertCoordsToArray(Coordinates[] c){
        float[] coordsArray = {
                c[0].getX(),
                c[0].getY(),
                c[0].getZ(),
                c[1].getX(),
                c[1].getY(),
                c[1].getZ(),
                c[2].getX(),
                c[2].getY(),
                c[2].getZ()
        };
        return coordsArray;
    }

}
