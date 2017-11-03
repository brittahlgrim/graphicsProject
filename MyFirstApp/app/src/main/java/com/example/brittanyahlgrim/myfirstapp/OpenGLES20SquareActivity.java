package com.example.brittanyahlgrim.myfirstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLES20SquareActivity extends AppCompatActivity {
    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this activity
        mGLView = new OpenGLES20SquareActivity.MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    class MyGLSurfaceView extends GLSurfaceView{
        private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context){
            super(context);

            //create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer(2);//square

            //set the renderer for drawing on GLSurface view
            setRenderer(mRenderer);

            // Render the view only when there is a change in the drawing data
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        private int numTouchEvents = 0;
        private float[] squareCoords = new float[12];
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
                case MotionEvent.ACTION_UP:
                    int index = numTouchEvents % 4;
                    int coordNum = index * 3;
                    float nx, ny, nz;
                    nx = ((width/2) - x);
                    ny = ((height/2) - y);
                    nz = 0.0f;
                    squareCoords[(coordNum)] = nx;
                    squareCoords[(coordNum) + 1] = ny;
                    squareCoords[(coordNum) + 2] = nz;
                    if(index == 3){
                        mRenderer.updateShape(squareCoords);
                        requestRender();
                    }
                    numTouchEvents++;
            }
            return true;
        }
    }
}
