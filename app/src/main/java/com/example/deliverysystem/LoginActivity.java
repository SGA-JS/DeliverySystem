package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deliverysystem.Database.DBHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        dbHelper = new DBHelper(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);


    }

    public void buttonLoginClicked(View view) {
//        Intent myIntent = new Intent(this, MainActivity.class);
//        // Switch to the AnotherActivity
//        startActivity(myIntent);

        // Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
        // Log.i("appinfo", "The user clicked the button");


        // Retrieve the email and password entered by the user
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if email and password are provided
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if the provided credentials are valid
        if (dbHelper.checkUser(email, password)) {
            // If credentials are valid, switch to MainActivity
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish(); // Close the LoginActivity
        } else {
            // If credentials are invalid, show error message
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }

    }

    public void buttonSignupClicked(View view) {
        Intent myIntent = new Intent(LoginActivity.this, SignupActivity.class);
        // Switch to the AnotherActivity
        startActivity(myIntent);

        //Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
        Log.i("appinfo", "The user clicked signup button");
    }
}
