package com.example.brittanyahlgrim.myfirstapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by brittanyahlgrim on 10/29/17.
 */


public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Shape mShape;
    private volatile float mAngle;
    private float xRotate = 0.0f, yRotate = 0.0f, zRotate = 1.0f;
    private int mShapeType;
    private static float mWidth, mHeight, mDepth;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    public static LookAtParameters[] ConstantLookAtParams = new LookAtParameters[6];
    private Rotation currView;

    public MyGLRenderer(int numSides) {
        initializeLookAtConstants();
        currView = Rotation.FRONT;

        mShapeType = numSides;
    }

    //called once to set up OpenGLES environment
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        //set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        float coords[];
        if(mShapeType < 3)
            mShapeType = 3;
        if(mShapeType > 100)
            mShapeType = 100;
        coords = new float[mShapeType * 3];
        double angle = (2 * Math.PI)/mShapeType;
        int radius = 300;
        for(int i = 0; i < mShapeType; i ++){
            int pointIndex = i * 3;
            coords[pointIndex] = (float) (Math.sin(angle * i) * radius);
            coords[pointIndex + 1] = (float)(Math.cos(angle * i) * radius);
            coords[pointIndex + 2] = 0;
        }
        mShape = new Shape(coords);
    }

    //called on each redraw of the frame
    public void onDrawFrame(GL10 unused){
        float [] scratch = new float[16];
        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if(currView == null)
            currView = Rotation.FRONT;
        LookAtParameters currLookAt = ConstantLookAtParams[currView.ordinal()];
        //set the camera position (view matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                currLookAt.eyeX, currLookAt.eyeY, currLookAt.eyeZ,
                currLookAt.centerX, currLookAt.centerY, currLookAt.centerZ,
                currLookAt.upX, currLookAt.upY, currLookAt.upZ);

        //Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //create the rotation transformation for the shape
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, xRotate, yRotate, zRotate);

        //Combine the rotation matrix with the projection and camera view
        //note the mMVPMatrix factor must be the first in order
        //for the matrix multiplication product to be correct
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        //draw shape
        mShape.draw(scratch);
    }

    //this is used for when the orientation of the screen changes
    public void onSurfaceChanged(GL10 unused, int width, int height){
        mWidth = width; mHeight = height;
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

    public void setCoordinates(float[] coords){
        mShape.setCoordinates(coords);
    }

    public float[] getCoordinates(){
        return mShape.getCoordinates();
    }

    public void setRotationAmounts(float x, float y, float z){
        xRotate = x;
        yRotate = y;
        zRotate = z;
    }

    public void setCurrView(Rotation currView) {
        this.currView = currView;
    }

    //todo: fix this so that viewing the side view works
    public static void initializeLookAtConstants(){
        ConstantLookAtParams[Rotation.FRONT.ordinal()] = new LookAtParameters(0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        ConstantLookAtParams[Rotation.BACK.ordinal()] = new LookAtParameters(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        ConstantLookAtParams[Rotation.LEFT.ordinal()] = new LookAtParameters(-3, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        ConstantLookAtParams[Rotation.RIGHT.ordinal()] = new LookAtParameters(3, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        ConstantLookAtParams[Rotation.TOP.ordinal()] = new LookAtParameters(0f, 3, 0f, 0f, 0f, 0f, 0f, 0.0f, 1.0f);
        ConstantLookAtParams[Rotation.BOTTOM.ordinal()] = new LookAtParameters(0f, -3, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f);
    }

}


