package com.example.brittanyahlgrim.myfirstapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by brittanyahlgrim on 10/29/17.
 */


public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Shape mShape;
    private int mShapeType;
    private int mDrawingMode;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mscratch = new float[16];

    // These still work without volatile, but refreshes are not guaranteed to happen.
    public volatile float mDeltaX = 0;
    public volatile float mDeltaY = 0;

    private final float[] mAccumulatedRotation = new float[16];
    private final float[] mCurrentRotation = new float[16];

    public MyGLRenderer(int numSides, int drawingMode) {
        mShapeType = numSides;
        mDrawingMode = drawingMode;
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
        int radius = 1;

        GLFloatPoint[] topCoords = new GLFloatPoint[mShapeType + 1];
        GLFloatPoint[] bottomCoords = new GLFloatPoint[mShapeType + 1];
        for(int i = 0; i < mShapeType; i ++){
            float x = (float) (Math.sin(angle * i) * radius);
            float y = (float)(Math.cos(angle * i) * radius);

            topCoords[i] = new GLFloatPoint(x, y, -1);
            bottomCoords[i] = new GLFloatPoint(x, y, 1);
        }
        if(mDrawingMode == GLES20.GL_LINE_LOOP){
            coords = makeWireframeVertexArray(topCoords, bottomCoords);
        }else{
            coords = makeSolidVertexArray(topCoords, bottomCoords);
        }

        mShape = new Shape(coords);

        // Initialize the accumulated rotation matrix
        Matrix.setIdentityM(mAccumulatedRotation, 0);
    }

    //called on each redraw of the frame
    public void onDrawFrame(GL10 unused){
        //float [] scratch = new float[16];
        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        LookAtParameters currLookAt = new LookAtParameters(0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        //set the camera position (view matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                currLookAt.eyeX, currLookAt.eyeY, currLookAt.eyeZ,
                currLookAt.centerX, currLookAt.centerY, currLookAt.centerZ,
                currLookAt.upX, currLookAt.upY, currLookAt.upZ);

        //calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //BEGIN
        //create the rotation transformation for the shape
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;
        // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
        Matrix.multiplyMM(mscratch, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
        System.arraycopy(mscratch, 0, mAccumulatedRotation, 0, 16);
        //END

        //Combine the rotation matrix with the projection and camera view
        //note the mMVPMatrix factor must be the first in order
        //for the matrix multiplication product to be correct
        Matrix.multiplyMM(mscratch, 0, mMVPMatrix, 0, mAccumulatedRotation, 0);

        //draw shape
        mShape.draw(mscratch, mDrawingMode);
    }

    //this is used for when the orientation of the screen changes
    public void onSurfaceChanged(GL10 unused, int width, int height){
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 1000.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
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
    private class GLFloatPoint{
        float x; float y; float z;
        private GLFloatPoint(float xx, float yy, float zz){
            x = xx; y = yy; z = zz;
        }
    }
    private class vec3{
        float x; float y; float z;
        private vec3(float xx, float yy, float zz){
            x = xx; y = yy; z = zz;
        }
    }
    private class vec4{
        float x; float y; float z; float w;
        private vec4(float xx, float yy, float zz, float ww){
            x = xx; y = yy; z = zz; w= ww;
        }
    }

    private float[] makeWireframeVertexArray(GLFloatPoint[] topCoords, GLFloatPoint[] bottomCoords){
        float coords[];
        GLFloatPoint []tempCoords2 = new GLFloatPoint[(8 * mShapeType) + 2];
        //front face
        int currIndex2 = 0;
        for (int i = 0; i <= mShapeType; i++){
            tempCoords2[currIndex2] = topCoords[i % mShapeType];
            currIndex2++;
        }
        //bottom face
        for(int i = 0; i <= mShapeType; i ++){
            tempCoords2[currIndex2] = bottomCoords[i % mShapeType];
            currIndex2++;
        }
        //side faces
        for(int i = 0; i < mShapeType; i ++){
            tempCoords2[currIndex2] = topCoords[i % mShapeType];
            currIndex2++;
            tempCoords2[currIndex2] = topCoords[(i + 1) % mShapeType];
            currIndex2++;
            tempCoords2[currIndex2] = bottomCoords[(i + 1) % mShapeType];
            currIndex2++;
            tempCoords2[currIndex2] = bottomCoords[(i) % mShapeType];
            currIndex2++;
            tempCoords2[currIndex2] = topCoords[i % mShapeType];
            currIndex2++;
            tempCoords2[currIndex2] = topCoords[(i + 1) % mShapeType];
            currIndex2++;
        }
        coords = new float[currIndex2 * 3]; //3 points per vertex; each vertex is copied 6 times (3 for top face, 3 for bottom face)
        for(int i = 0; i < currIndex2; i ++){
            coords[i * 3] = tempCoords2[i].x;
            coords[(i * 3) + 1] = tempCoords2[i].y;
            coords[(i * 3) + 2] = tempCoords2[i].z;
        }

        return coords;
    }
    private float[] makeSolidVertexArray(GLFloatPoint[] topCoords, GLFloatPoint[] bottomCoords){
        float coords[];
        int currIndex3 = 0;
        GLFloatPoint [] tempCoords3 = new GLFloatPoint[(11 * mShapeType) - 10];//each vertex is represented 3 times
        //top face
        for(int i = 0; i < (mShapeType - 2); i ++){
            tempCoords3[(i * 3)] = topCoords[mShapeType - 1];
            tempCoords3[(i * 3) + 2] = topCoords[i];
            tempCoords3[(i * 3) + 1] = topCoords[i + 1];
        }
        currIndex3 = currIndex3 + (mShapeType - 2) * 3;

        //reset the point to start at the top of the shape
        tempCoords3[currIndex3] = topCoords[mShapeType - 1];
        currIndex3++;
        tempCoords3[currIndex3] = topCoords[0];
        currIndex3++;
        for(int i = 0; i < mShapeType; i ++){
            tempCoords3[currIndex3] = topCoords[i];
            currIndex3++;
            tempCoords3[currIndex3] = bottomCoords[i];
            currIndex3++;
            tempCoords3[currIndex3] = bottomCoords[(i + 1) % mShapeType];
            currIndex3++;
            tempCoords3[currIndex3] = topCoords[i];
            currIndex3++;
            tempCoords3[currIndex3] = topCoords[(i + 1) % mShapeType];
            currIndex3++;
        }

        //bottom face
        for(int i = 0; i < (mShapeType - 2); i ++){
            tempCoords3[currIndex3 + (i * 3)] = bottomCoords[mShapeType - 1];
            tempCoords3[currIndex3 + ((i * 3) + 2)] = bottomCoords[i];
            tempCoords3[currIndex3 + ((i * 3) + 1)] = bottomCoords[i + 1];
        }
        currIndex3 = currIndex3 + (mShapeType - 2) * 3;


        coords = new float[currIndex3 * 3]; //3 points per vertex; each vertex is copied 6 times (3 for top face, 3 for bottom face)
        for(int i = 0; i < currIndex3; i ++){
            coords[i * 3] = tempCoords3[i].x;
            coords[(i * 3) + 1] = tempCoords3[i].y;
            coords[(i * 3) + 2] = tempCoords3[i].z;
        }
        return  coords;
    }


    public float[] onTouch(float touchX, float touchY, float width, float height)
    {
        int ix = 0, iy = 1, iz = 2, iw = 3;
        float x = (2.0f * touchX) / width - 1.0f;
        float y = 1.0f - (2.0f * touchY) / height;
        float z = 1.0f;

        float[] verts1 = new float[3];
        verts1[ix] = x; verts1[iy] = y; verts1[iz] = 1000;

        float[] result1 = new float[16];
        Matrix.invertM(result1, 0, mMVPMatrix, 0);
        Matrix.multiplyMM(result1, 0, result1, 0, verts1, 0);

        float[] coords = mShape.getCoordinates();
        FloatBuffer vBuffer = mShape.getVertexBuffer();
        float[] fb = new float[vBuffer.remaining()];
        System.arraycopy(vBuffer.array(), 0, fb, 0, vBuffer.remaining());
        for(int i = 0; i < (fb.length/3); i ++){

        }

        return null;
    }

    public static float[] normalize(float a[]){
        float scale = 0 ;
        for(int k=0;k<a.length;k++){
            scale+=a[k]*a[k];
        }
        scale = (float) (1/Math.sqrt(scale));
        for(int k=0;k<a.length;k++){
            a[k]*=scale ;
        }
        return a;
    }
}


