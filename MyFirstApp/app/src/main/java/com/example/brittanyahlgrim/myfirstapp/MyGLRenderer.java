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
//    private int triangleToDraw = 0;
//    private Triangle [] mTriangles;
    private Triangle mTriangle;
    private Square mSquare;
    private volatile float mAngle;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    //called once to set up OpenGLES environment
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        //set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        float triangleCoords1[] = { //in counterclockwise order
                0.0f, 0.622008459f, 0.0f, //top
                -0.5f, -0.311004243f, 0.0f, // bottom left
                0.5f, -0.311004243f, 0.0f  // bottom right
        };
        mTriangle = new Triangle(triangleCoords1);
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

        //create the rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 3.14f;//0.090f * ((int)time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        //Combine the rotation matrix with the projection and camera view
        //note the mMVPMatrix factor must be the first in order
        //for the matrix multiplication product to be correct
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        //draw shape
        mTriangle.draw(scratch);
        //mTriangles[triangleToDraw].draw(scratch);
    }

    //this is used for when the orientation of the screen changes
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float)width/height;
        //this projection matrix is applied to object coordinates
        //in the drawframe() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
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

    public void updateTriangle(float[] coords){
        mTriangle.updateCoordinates(coords);
    }
}


