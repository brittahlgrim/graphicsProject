package com.example.brittanyahlgrim.myfirstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context){
            super(context);

            //create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer();

            //set the renderer for drawing on GLSurface view
            setRenderer(mRenderer);

//            // Render the view only when there is a change in the drawing data
//            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }


}
