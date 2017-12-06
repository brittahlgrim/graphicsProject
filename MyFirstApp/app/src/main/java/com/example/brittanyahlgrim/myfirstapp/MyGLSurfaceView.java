package com.example.brittanyahlgrim.myfirstapp;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brittanyahlgrim on 11/18/17.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer mRenderer;
    public InteractionMode mMode = InteractionMode.DRAW;
    public int mDrawingMode = GLES20.GL_LINE_LOOP;

    private float mPreviousX, mPreviousY;
    private float mDensity;
    private List<Integer> moveVertices = new ArrayList<>();
    private int numVertices = 3;
    private ViewingAngle mViewingAngle = ViewingAngle.FRONT;

    public MyGLSurfaceView(Context context){
        super(context);
        init(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        //create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        numVertices = OpenGLES20GeneralActivity.numberOfVertices;
        mDrawingMode = OpenGLES20GeneralActivity.drawingMode;
        mViewingAngle = OpenGLES20GeneralActivity.mViewingAngle;

        mRenderer = new MyGLRenderer(numVertices, mDrawingMode);
        //set the renderer for drawing on GLSurface view
        setRenderer(mRenderer, OpenGLES20GeneralActivity.mDensity);
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        //MotionEvent reports input details from the touch screen
        //and other input controls. In this case, you are only
        //interested in events where the touch position changed
        float x = e.getX();
        float y = e.getY();
        float nx, ny, nz;
        int w = getWidth(); int h = getHeight();
        nx = ((w/2) - x)/(w/2);
        ny = ((h/2) - y)/(h/4);
        nz = 0.0f;

        switch(mMode) {
            case DRAW:
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moveVertices = touchOnVertex(nx, ny);
                        break;
                    case MotionEvent.ACTION_UP:
                        updateVertices(moveVertices, nx, ny);
                        moveVertices.clear();
                        break;
                }
                break;
            case ROTATE:
                if(e.getAction() == MotionEvent.ACTION_MOVE){
                    if (mRenderer != null)
                    {
                        float deltaX = (x - mPreviousX) / mDensity / 2f;
                        float deltaY = (y - mPreviousY) / mDensity / 2f;

                        mRenderer.mDeltaX += deltaX;
                        mRenderer.mDeltaY += deltaY;
                        requestRender();
                    }
                }
                mPreviousX = x;
                mPreviousY = y;
                return true;

            case MOVE:
                break;
        }
        return true;
    }

    private List<Integer> touchOnVertex(float x, float y){
        List<Integer> foundIndices = new ArrayList<>();
        float x1 =  x;
        float y1 = y;
        float z1 = 0;

        float[] currCoords = mRenderer.getCoordinates();

        switch(mViewingAngle){
            case FRONT:
                z1 = -1.0f;
                for(int i = 0; i < currCoords.length; i=i+3){
                    float vX = currCoords[i];
                    float vY = currCoords[i + 1];
                    float vZ = currCoords[i + 2];
                    if(x1 == vX && y1 == vY && z1 == vZ) {
                        foundIndices.add(i);
                    }
                    //count anything within 5 pixels to be 'touching' a vertex
                    else if(x1 >= (vX - .10) && x1 <= (vX + .10)
                            && y1 >= (vY - .10) && y1 <= (vY + .10)
                            && z1 >= (vZ - .10) && z1 <= (vZ + .10)){
                        foundIndices.add(i);
                    }
                }
                break;
            case LEFT:
                break;
            case BACK:
                z1 = 1.0f;
                x1 = x1 * -1; //we rotate about y axis so the x vertices are opposite
                for(int i = 0; i < currCoords.length; i=i+3){
                    float vX = currCoords[i];
                    float vY = currCoords[i + 1];
                    float vZ = currCoords[i + 2];
                    if(x1 == vX && y1 == vY && z1 == vZ) {
                        foundIndices.add(i);
                    }
                    //count anything within 5 pixels to be 'touching' a vertex
                    else if(x1 >= (vX - .10) && x1 <= (vX + .10)
                            && y1 >= (vY - .10) && y1 <= (vY + .10)
                            && z1 >= (vZ - .10) && z1 <= (vZ + .10)){
                        foundIndices.add(i);
                    }
                }
                break;
            case RIGHT:
                break;
            case TOP:
                break;
            case BOTTOM:
                break;
        }

        return foundIndices;
    }

    private void updateVertices(List<Integer> moveVertices, float nx, float ny){
        float [] triangleCoords = mRenderer.getCoordinates();
        if(moveVertices.isEmpty())
            return;
        switch(mViewingAngle){
            case FRONT:
                for(int i = 0; i < moveVertices.size(); i ++){
                    int moveVertex = moveVertices.get(i);
                    int coordNum = moveVertex;
                    triangleCoords[(coordNum)] = nx;
                    triangleCoords[(coordNum) + 1] = ny;
                    triangleCoords[(coordNum) + 2] = -1.0f;
                }
                break;
            case LEFT:
                break;
            case BACK:
                for(int i = 0; i < moveVertices.size(); i ++){
                    int moveVertex = moveVertices.get(i);
                    int coordNum = moveVertex;
                    triangleCoords[(coordNum)] = -1 * nx;
                    triangleCoords[(coordNum) + 1] = ny;
                    triangleCoords[(coordNum) + 2] = 1.0f;
                }
                break;
            case RIGHT:
                break;
            case TOP:
                break;
            case BOTTOM:
                break;
        }

        mRenderer.setCoordinates(triangleCoords);
        requestRender();
        moveVertices.clear();
    }

    public void setmMode(InteractionMode mode){
        mMode = mode;
    }
    public void setmViewingAngle(ViewingAngle angle){
        float deltaX = 0;
        float deltaY = 0;

        //get the rotation back to the front, then rotate to the actual choice
        switch(mViewingAngle){
            case FRONT:
                break;
            case LEFT:
                deltaX += -90;
                deltaY = 0;
                break;
            case BACK:
                deltaX += -180;
                deltaY = 0;
                break;
            case RIGHT:
                deltaX += 90;
                deltaY = 0;
                break;
            case TOP:
                deltaX = 0;
                deltaY += 90;
                break;
            case BOTTOM:
                deltaX = 0;
                deltaY += -90;
                break;
        }

        mViewingAngle = angle;
        switch(mViewingAngle){
            case FRONT:
                break;
            case LEFT:
                deltaX += 90;
                deltaY = 0;
                break;
            case BACK:
                deltaX += 180;
                deltaY = 0;
                break;
            case RIGHT:
                deltaX += -90;
                deltaY = 0;
                break;
            case TOP:
                deltaX = 0;
                deltaY += -90;
                break;
            case BOTTOM:
                deltaX = 0;
                deltaY += 90;
                break;
        }

        mRenderer.mDeltaX += deltaX;
        mRenderer.mDeltaY += deltaY;
        requestRender();
    }

    public void setRenderer(MyGLRenderer renderer, float density)
    {
        mRenderer = renderer;
        mDensity = density;
        super.setRenderer(renderer);
    }
}
