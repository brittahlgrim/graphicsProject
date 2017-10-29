package com.example.brittanyahlgrim.myfirstapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by brittanyahlgrim on 10/29/17.
 */


public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriange;
    private Square mSquare;
    //called once to set up OpenGLES environment
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        //set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriange = new Triangle();
        mSquare = new Square();
    }

    //called on each redraw of the frame
    public void onDrawFrame(GL10 unused){
        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        mTriange.draw();
    }

    //this is used for when the orientation of the screen changes
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);
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
}


