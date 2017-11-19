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
    private int mShapeType;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    // These still work without volatile, but refreshes are not guaranteed to happen.
    public volatile float mDeltaX;
    public volatile float mDeltaY;

    private final float[] mAccumulatedRotation = new float[16];
    private final float[] mCurrentRotation = new float[16];

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

        double angle = (2 * Math.PI)/mShapeType;
        int radius = 300;

        GLFloatPoint[] topCoords = new GLFloatPoint[mShapeType + 1];
        GLFloatPoint[] bottomCoords = new GLFloatPoint[mShapeType + 1];
        for(int i = 0; i < mShapeType; i ++){
            float x = (float) (Math.sin(angle * i) * radius);
            float y = (float)(Math.cos(angle * i) * radius);

            topCoords[i] = new GLFloatPoint(x, y, 0);
            bottomCoords[i] = new GLFloatPoint(x, y, 250);
//            topCoords[i].x = (float) (Math.sin(angle * i) * radius);
//            topCoords[i].y = (float)(Math.cos(angle * i) * radius);
//            topCoords[i].z = 0;
//            bottomCoords[i].x = (float) (Math.sin(angle * i) * radius);
//            bottomCoords[i].y = (float)(Math.cos(angle * i) * radius);
//            bottomCoords[i].z = 250;
        }
        GLFloatPoint []tempCoords2 = new GLFloatPoint[(8 * mShapeType) + 2];
        //front face
        int currIndex = 0;
        for (int i = 0; i <= mShapeType; i++){
            tempCoords2[currIndex] = topCoords[i % mShapeType];
            currIndex++;
        }
        //bottom face
        for(int i = 0; i <= mShapeType; i ++){
            tempCoords2[currIndex] = bottomCoords[i % mShapeType];
            currIndex++;
        }
        //side faces
        for(int i = 0; i < mShapeType; i ++){
            tempCoords2[currIndex] = topCoords[i % mShapeType];
            currIndex++;
            tempCoords2[currIndex] = topCoords[(i + 1) % mShapeType];
            currIndex++;
            tempCoords2[currIndex] = bottomCoords[(i + 1) % mShapeType];
            currIndex++;
            tempCoords2[currIndex] = bottomCoords[(i) % mShapeType];
            currIndex++;
            tempCoords2[currIndex] = topCoords[i % mShapeType];
            currIndex++;
            tempCoords2[currIndex] = topCoords[(i + 1) % mShapeType];
            currIndex++;
        }
        coords = new float[currIndex * 3]; //3 points per vertex; each vertex is copied 6 times (3 for top face, 3 for bottom face)
        for(int i = 0; i < currIndex; i ++){
            coords[i * 3] = tempCoords2[i].x;
            coords[(i * 3) + 1] = tempCoords2[i].y;
            coords[(i * 3) + 2] = tempCoords2[i].z;
        }
        mShape = new Shape(coords);

        // Initialize the accumulated rotation matrix
        Matrix.setIdentityM(mAccumulatedRotation, 0);
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

        //BEGIN
        //create the rotation transformation for the shape
//        Matrix.setRotateM(mRotationMatrix, 0, mAngle, xRotate, yRotate, zRotate);
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;
        // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
        Matrix.multiplyMM(scratch, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
        System.arraycopy(scratch, 0, mAccumulatedRotation, 0, 16);
        //END

        //Combine the rotation matrix with the projection and camera view
        //note the mMVPMatrix factor must be the first in order
        //for the matrix multiplication product to be correct
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mAccumulatedRotation, 0);

        //draw shape
        mShape.draw(scratch);
    }

    //this is used for when the orientation of the screen changes
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float)width/height;
        //this projection matrix is applied to object coordinates
        //in the drawframe() method
        Matrix.frustumM(mProjectionMatrix, 0, -(width/2), (width/2), (-height/2), (height/2), 1, 150);
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

    public void setCoordinates(float[] coords){
        mShape.setCoordinates(coords);
    }

    public float[] getCoordinates(){
        return mShape.getCoordinates();
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

    private class GLFloatPoint{
        float x; float y; float z;
        private GLFloatPoint(float xx, float yy, float zz){
            x = xx; y = yy; z = zz;
        }
    }

}


