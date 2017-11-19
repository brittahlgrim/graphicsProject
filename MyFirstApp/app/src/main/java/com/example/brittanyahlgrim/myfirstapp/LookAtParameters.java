package com.example.brittanyahlgrim.myfirstapp;

/**
 * Created by brittanyahlgrim on 11/18/17.
 */

public class LookAtParameters {
    public float eyeX;
    public float eyeY;
    public float eyeZ;
    public float centerX;
    public float centerY;
    public float centerZ;
    public float upX;
    public float upY;
    public float upZ;

    public LookAtParameters(float ex, float ey, float ez, float cx, float cy, float cz, float ux, float uy, float uz){
        eyeX = ex; eyeY = ey; eyeZ = ez;
        centerX = cx; centerY = cy; centerZ = cz;
        upX = ux; upY = uy; upZ = uz;
    }


}
