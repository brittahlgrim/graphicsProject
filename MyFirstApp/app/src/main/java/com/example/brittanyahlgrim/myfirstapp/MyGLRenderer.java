package com.example.brittanyahlgrim.myfirstapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by brittanyahlgrim on 10/29/17.
 */


public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Shape mShape;
    private volatile float mAngle;
    private int mShapeType;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    public MyGLRenderer() {
        mShapeType = 1;
    }

    public MyGLRenderer(int numSides) {
        mShapeType = numSides;
    }

    //called once to set up OpenGLES environment
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        //set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        float coords[];
        if(mShapeType == 3) //triangle
        {
            coords = new float[9];
            coords[0] = 347.0f;
            coords[1] = 312.0f;
            coords[2] = 0.0f;
            coords[3] = 311.0f;
            coords[4] = -406.0f;
            coords[5] = 0.0f;
            coords[6] = -424.0f;
            coords[7] = 36.0f;
            coords[8] = 0.0f;
        }else {
            coords = new float[12];
            coords[0] = 500.0f;
            coords[1] = 750.0f;
            coords[2] = 0.0f;
            coords[3] = -500.0f;
            coords[4] = 750.0f;
            coords[5] = 0.0f;
            coords[6] = -500.0f;
            coords[7] = -750.0f;
            coords[8] = 0.0f;
            coords[9] = 500.0f;
            coords[10] = -750.0f;
            coords[11] = 0.0f;
        }
        mShape = new Shape(coords);

    }

    //called on each redraw of the frame
    public void onDrawFrame(GL10 unused){
        float [] scratch = new float[16];
        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //set the camera position (view matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //create the rotation transformation for the shape
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 3.14f;//0.090f * ((int)time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        //Combine the rotation matrix with the projection and camera view
        //note the mMVPMatrix factor must be the first in order
        //for the matrix multiplication product to be correct
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        //draw shape
        mShape.draw(scratch);
    }

    //this is used for when the orientation of the screen changes
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float)width/height;
        //this projection matrix is applied to object coordinates
        //in the drawframe() method
        Matrix.frustumM(mProjectionMatrix, 0, -(width/2), (width/2), (-height/2), (height/2), 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float getAngle(){return mAngle;}
    public void setAngle(float angle){mAngle = angle;}

    public void updateShape(float[] coords){
        mShape.updateCoordinates(coords);
    }
}


