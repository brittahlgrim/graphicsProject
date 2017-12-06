package com.example.brittanyahlgrim.myfirstapp;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by brittanyahlgrim on 11/3/17.
 */

public class Shape {
    private static final int COORDS_PER_VERTEX = 3;
    private int mPositionHandle;
    private int mColorHandle;
    private final int vertexStride = COORDS_PER_VERTEX / 4; //4 bytes per vertex
    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    protected final int mProgram = GLES20.glCreateProgram();
    protected FloatBuffer vertexBuffer;
    public FloatBuffer getVertexBuffer(){
        return vertexBuffer;
    }
    protected static float color[] = {0.63671875f, 0.76953125f, 0.22265652f, 1.0f};

    public float coordinates[];

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";


    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Shape(){
        this(null);
    }

    public Shape(float coords []) {
        coordinates = new float[coords.length];
        if(coords != null) {
            int i;
            for (i = 0; i < coords.length; i++) {
                coordinates[i] = coords[i];
            }
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                coordinates.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coordinates);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //add the vertex shader to the program
        GLES20.glAttachShader(mProgram, vertexShader);

        //add the fragment shader to the program
        GLES20.glAttachShader(mProgram, fragmentShader);

        //creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix, int mode){
        //add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        //get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        //enable a handle to the shape vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //prepare the shape coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        //get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        //set color for drawing the shape
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glLineWidth(10.0f);
        //draw the shape
        GLES20.glDrawArrays(mode,0, (coordinates.length/COORDS_PER_VERTEX));

        //disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public float[] getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(float[] coords) {
        if(coords != null) {
            int i;
            for (i = 0; i < coords.length; i++) {
                coordinates[i] = coords[i];
            }
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                coordinates.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coordinates);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //add the vertex shader to the program
        GLES20.glAttachShader(mProgram, vertexShader);

        //add the fragment shader to the program
        GLES20.glAttachShader(mProgram, fragmentShader);

        //creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }
}
