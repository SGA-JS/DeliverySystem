package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.deliverysystem.Database.DBHelper;

public class SignupActivity extends AppCompatActivity {
    EditText name_input, phone_input, email_input, address_input, password_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);

        name_input = findViewById(R.id.editTextName);
        phone_input = findViewById(R.id.editTextPhone);
        email_input = findViewById(R.id.editTextEmail);
        address_input = findViewById(R.id.editTextAddress);

        password_input = findViewById(R.id.editTextPassword);
        add_button = findViewById(R.id.buttonSignUp);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper myDB = new DBHelper(SignupActivity.this);
                myDB.insertCustomer(name_input.getText().toString().trim(),
                        phone_input.getText().toString().trim(),
                        email_input.getText().toString().trim(),
                        address_input.getText().toString().trim(),
                        password_input.getText().toString().trim());

                //once click the Sign up button - redirect to LoginActivity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }
}
