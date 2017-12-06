package com.example.brittanyahlgrim.myfirstapp;

import android.content.Intent;
import android.opengl.GLES20;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import static com.example.brittanyahlgrim.myfirstapp.InteractionMode.MOVE;

public class Free_Draw_Activity extends AppCompatActivity {

    public static final String INTERACTIONMODE = "com.example.myfirstapp.INTERACTIONMODE";
    public static final String DRAWINGMODE = "com.example.myfirstapp.DRAWINGMODE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Show Square button */
    public void showWireframeDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20GeneralActivity.class);
        EditText interactionModeText = (EditText) findViewById(R.id.editText2);
        String message = interactionModeText.getText().toString();
        intent.putExtra(INTERACTIONMODE, message);
        int drawingMode = GLES20.GL_LINE_LOOP;
        intent.putExtra(DRAWINGMODE, drawingMode);
        startActivity(intent);
    }

    /** Called when the user taps the Show Square button */
    public void showSolidDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20GeneralActivity.class);
        EditText interactionModeText = (EditText) findViewById(R.id.editText2);
        String message = interactionModeText.getText().toString();
        intent.putExtra(INTERACTIONMODE, message);
        int drawingMode = GLES20.GL_TRIANGLE_STRIP;
        intent.putExtra(DRAWINGMODE, drawingMode);
        startActivity(intent);
    }

    int numTouches = 0;
    GLFloatPoint[] touchedVertices = new GLFloatPoint[100];
    @Override
    public boolean onTouchEvent(MotionEvent e){
        //MotionEvent reports input details from the touch screen
        //and other input controls. In this case, you are only
        //interested in events where the touch position changed
        if(numTouches == 100)
            return true;
        float x = e.getX();
        float y = e.getY();
        touchedVertices[numTouches] = new GLFloatPoint(x, y, 0.0f);
        numTouches++;
        return true;
    }

    private class GLFloatPoint{
        float x; float y; float z;
        private GLFloatPoint(float xx, float yy, float zz){
            x = xx; y = yy; z = zz;
        }
    }
}
