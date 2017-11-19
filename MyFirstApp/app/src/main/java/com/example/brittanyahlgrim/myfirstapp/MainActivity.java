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
    /** Called when the user taps the Show Triangles button */
    public void showTriangleDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20TriangleActivity.class);
        startActivity(intent);
    }
    /** Called when the user taps the Show Square button */
    public void showSquareDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20SquareActivity.class);
        startActivity(intent);
    }
    /** Called when the user taps the Show Square button */
    public void showGeneralDisplay(View view) {
        Intent intent = new Intent(this, OpenGLES20GeneralActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText2);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
