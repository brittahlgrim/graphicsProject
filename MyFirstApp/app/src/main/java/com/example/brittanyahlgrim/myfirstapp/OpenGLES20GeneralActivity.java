package com.example.brittanyahlgrim.myfirstapp;

import android.content.Intent;
import android.opengl.GLES20;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class OpenGLES20GeneralActivity extends AppCompatActivity {
    private MyGLSurfaceView mGLView;

    private InteractionMode mMode = InteractionMode.DRAW;
    public static int numberOfVertices = 3;
    public static int drawingMode = GLES20.GL_LINE_LOOP;
    public static float mDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String vertText = intent.getStringExtra(MainActivity.INTERACTIONMODE);
        drawingMode = intent.getIntExtra(MainActivity.DRAWINGMODE, GLES20.GL_LINE_LOOP);
        if(!vertText.isEmpty()) {
            numberOfVertices = Integer.parseInt(vertText);
        } else {
            numberOfVertices = 3;
        }
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mDensity = displayMetrics.density;
        setContentView(R.layout.activity_open_gles20_general);
        mGLView = (MyGLSurfaceView) findViewById(R.id.myGlSurfaceViewLayout);

        addListenerOnButton();
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        Spinner viewSpinner = (Spinner) findViewById(R.id.spinner);
        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                mMode = InteractionMode.valueOf(selectedItem);
                mGLView.setmMode(mMode);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {            }
        });
    }
}
