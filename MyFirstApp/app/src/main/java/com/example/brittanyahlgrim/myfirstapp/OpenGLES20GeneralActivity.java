package com.example.brittanyahlgrim.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class OpenGLES20GeneralActivity extends AppCompatActivity {
    private MyGLSurfaceView mGLView;
    private Spinner viewSpinner, rotationSpinner;

    private InteractionMode mMode = InteractionMode.DRAW;
    private Rotation mRotation = Rotation.FRONT;
    public static int numberOfVertices = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String vertText = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if(!vertText.isEmpty()) {
            numberOfVertices = Integer.parseInt(vertText);
        } else {
            numberOfVertices = 3;
        }
        setContentView(R.layout.activity_open_gles20_general);
        mGLView = (MyGLSurfaceView) findViewById(R.id.myGlSurfaceViewLayout);

        addListenerOnButton();
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        viewSpinner = (Spinner) findViewById(R.id.spinner);
        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                mMode = InteractionMode.DRAW.valueOf(selectedItem);
                mGLView.setmMode(mMode);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {            }
        });

        rotationSpinner = (Spinner) findViewById(R.id.spinner2);
        rotationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                mRotation = Rotation.valueOf(selectedItem);
                mGLView.setmRotation(mRotation);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {            }
        });
    }
}
