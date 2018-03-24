package com.example.rajita.codefundo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button cam;
        Button notes;
        Button marks;
        Button pic;

        cam = (Button)findViewById(R.id.button1);
        cam.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(Start.this,
                        Student.class);
                startActivity(myIntent);
            }
        });

        notes = (Button)findViewById(R.id.button2);
        notes.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(Start.this,
                        Notes.class);
                startActivity(myIntent);
            }
        });

        marks = (Button)findViewById(R.id.button3);
        marks.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(Start.this,
                        Marks.class);
                startActivity(myIntent);
            }
        });

        pic = (Button)findViewById(R.id.button4);
        pic.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(Start.this,
                        Camera.class);
                startActivity(myIntent);
            }
        });
    }
}
