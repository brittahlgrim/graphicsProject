package com.example.brittanyahlgrim.myfirstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLES20Activity extends AppCompatActivity {
    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this activity
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    class MyGLSurfaceView extends GLSurfaceView{
        private float mPreviousX;
        private float mPreviousY;
        private final MyGLRenderer mRenderer;
        private final float TOUCH_SCALE_FACTOR = 180.0f/320;

        public MyGLSurfaceView(Context context){
            super(context);

            //create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer();

            //set the renderer for drawing on GLSurface view
            setRenderer(mRenderer);

            // Render the view only when there is a change in the drawing data
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        private int numTouchEvents = 0;
        private float[] shapeCoords = new float[9];
        @Override
        public boolean onTouchEvent(MotionEvent e){
            //MotionEvent reports input details from the touch screen
            //and other input controls. In this case, you are only
            //interested in events where the touch position changed
            float x = e.getX();
            float y = e.getY();
            final float width = 1080;
            final float height = 1536;
            switch(e.getAction()){
                case MotionEvent.ACTION_MOVE:
                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;

                    //reverse direction of the rotation above the mid-line
                    if(y > getHeight()/2){
                        dx = dx * -1;
                    }
                    if(x < getWidth()/2)
                        dy = dy * -1;

                    mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                    requestRender();
                    break;
                case MotionEvent.ACTION_UP:
                    int index = numTouchEvents % 3;
                    int coordNum = index * 3;
                    float nx, ny, nz;
                    nx = ((width/2) - x);
                    ny = ((height/2) - y);
                    nz = 0.0f;
                    shapeCoords[(coordNum)] = nx;
                    shapeCoords[(coordNum) + 1] = ny;
                    shapeCoords[(coordNum) + 2] = nz;
                    if(index == 2){
                        mRenderer.updateShape(shapeCoords);
                        requestRender();
                    }
                    numTouchEvents++;
            }
            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    }
}
