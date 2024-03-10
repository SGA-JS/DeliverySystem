package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deliverysystem.Database.DBHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        dbHelper = new DBHelper(this);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void buttonLoginClicked(View view) {
        // Retrieve the email and password entered by the user
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if email and password are provided
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the provided credentials are valid
        if (dbHelper.checkUser(username, password)) {
            // If credentials are valid, switch to MainActivity
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            myIntent.putExtra("username", username);
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
    }
}
