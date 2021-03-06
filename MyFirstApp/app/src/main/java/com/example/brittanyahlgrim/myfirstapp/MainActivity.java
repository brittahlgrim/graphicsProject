package com.example.brittanyahlgrim.myfirstapp;

import android.content.Intent;
import android.opengl.GLES20;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String INTERACTIONMODE = "com.example.myfirstapp.INTERACTIONMODE";
    public static final String DRAWINGMODE = "com.example.myfirstapp.DRAWINGMODE";
    public static final String VIEWINGANGLE = "com.example.myfirstapp.VIEWINGANGLE";
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
        intent.putExtra(VIEWINGANGLE, ViewingAngle.FRONT);
        startActivity(intent);
    }
}
