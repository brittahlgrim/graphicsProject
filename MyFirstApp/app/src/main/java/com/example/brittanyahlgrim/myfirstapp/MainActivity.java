package com.example.brittanyahlgrim.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /** Called when the user taps the Send button */
    public void showOpenGLDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20Activity.class);
        startActivity(intent);
    }
    /** Called when the user taps the Send button */
    public void showTriangleDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20TriangleActivity.class);
        startActivity(intent);
    }
    /** Called when the user taps the Send button */
    public void showSquareDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20SquareActivity.class);
        startActivity(intent);
    }
}
