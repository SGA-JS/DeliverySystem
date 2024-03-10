package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TaskAssignment extends AppCompatActivity {

    ImageButton qrCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addtask);
        qrCamera = findViewById(R.id.qrCamera);
        qrCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TaskAssignment.this, ScanQRActivity.class);
                // Switch to the AnotherActivity
                startActivity(myIntent);
            }
        });
    }
}