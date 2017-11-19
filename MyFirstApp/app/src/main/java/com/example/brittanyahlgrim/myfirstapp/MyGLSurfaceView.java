package com.example.brittanyahlgrim.myfirstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Created by brittanyahlgrim on 11/18/17.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer mRenderer;
    public InteractionMode mMode = InteractionMode.DRAW;
    private Rotation mRotation = Rotation.FRONT;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX, mPreviousY;
    private float mDensity;
    private int moveVertex = -1;
    private int numVertices = 3;

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
        mRenderer = new MyGLRenderer(numVertices);
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
        nx = ((getWidth()/2) - x);
        ny = ((getHeight()/2) - y);
        nz = 0.0f;
        float [] triangleCoords = mRenderer.getCoordinates();

        switch(mMode) {
            //if draw mode is on, the user is able to drag and place vertices in different places
            //todo: allow the user to add another vertex if no vertex is selected, but the user selects a point on a line
            case DRAW:
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moveVertex = touchOnVertex(nx, ny, nz);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (moveVertex > -1) {
                            int coordNum = moveVertex * 3;
                            triangleCoords[(coordNum)] = nx;
                            triangleCoords[(coordNum) + 1] = ny;
                            triangleCoords[(coordNum) + 2] = nz;

                            mRenderer.setCoordinates(triangleCoords);
                            requestRender();
                        }
                        moveVertex = -1;
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

    private int touchOnVertex(float x, float y, float z){
        int foundIndex = -1;
        int x1 = Math.round(x);
        int y1 = Math.round(y);
        int z1 = Math.round(z);
        float[] currCoords = mRenderer.getCoordinates();
        for(int i = 0; i < numVertices; i ++){
            if(foundIndex >= 0)
                break;
            int vX = Math.round(currCoords[(i * 3)]);
            int vY = Math.round(currCoords[(i * 3) + 1]);
            int vZ = Math.round(currCoords[(i * 3) + 2]);
            if(x1 == vX && y1 == vY && z1 == vZ) {
                foundIndex = i;
            }
            //count anything within 5 pixels to be 'touching' a vertex
            else if(x1 >= (vX - 10) && x1 <= (vX + 10)
                    && y1 >= (vY - 10) && y1 <= (vY + 10)
                    && z1 >= (vZ - 10) && z1 <= (vZ + 10)){
                foundIndex = i;
            }
        }
        return foundIndex;
    }

    public void setmMode(InteractionMode mode){
        mMode = mode;
    }
    public void setmRotation(Rotation rotation){
        mRotation = rotation;
        mRenderer.setCurrView(rotation);
        requestRender();
    }

    public void setRenderer(MyGLRenderer renderer, float density)
    {
        mRenderer = renderer;
        mDensity = density;
        super.setRenderer(renderer);
    }
}
