package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void buttonLoginClicked(View view) {
        // Retrieve the email and password entered by the user
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if email and password are provided
        if (validateInput(username, password)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the provided credentials are valid
        if (dbHelper.checkUser(username, password)) {
            SharedPreferences sharedPreferences = getSharedPreferences("Credential", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();

            // If credentials are valid, switch to MainActivity
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish(); // Close the LoginActivity
        } else {
            // If credentials are invalid, show error message
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateInput(String username, String password)
    {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        return true;
    }

    public void buttonSignupClicked(View view) {
        Intent myIntent = new Intent(LoginActivity.this, SignupActivity.class);
        // Switch to the AnotherActivity
        startActivity(myIntent);
    }


    // logout action
    public void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Credential", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply(); // Apply the changes

        // redirect back to the login screen
        Intent intent = new Intent(context, LoginActivity.class);
        // Clear the back stack so that the user cannot navigate back to the MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

}
