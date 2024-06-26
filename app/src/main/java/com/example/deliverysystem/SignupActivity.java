package com.example.deliverysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverysystem.Database.DBHelper;

public class SignupActivity extends AppCompatActivity {
    EditText name_input, password_input, vehicle_input;
    Button add_button;
    RadioButton btn_Admin, btn_Driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name_input = findViewById(R.id.editTextName);
        password_input = findViewById(R.id.editTextPassword);
        vehicle_input = findViewById(R.id.editTextVehicle);
        add_button = findViewById(R.id.buttonSignUp);
        btn_Admin = findViewById(R.id.roleAdmin);
        btn_Driver = findViewById(R.id.roleDriver);

        btn_Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle_input.setVisibility(View.VISIBLE);
            }
        });
        btn_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle_input.setVisibility(View.INVISIBLE);
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper myDB = new DBHelper(SignupActivity.this);
                int role = btn_Admin.isChecked() ? ConstantValue.ROLE_ADMIN : ConstantValue.ROLE_DRIVER;
                long result = myDB.insertUser(name_input.getText().toString().trim(),
                        password_input.getText().toString().trim(), role, vehicle_input.getText().toString().trim());
                if(result == -1){
                    Toast.makeText(getBaseContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getBaseContext(), "New user added", Toast.LENGTH_SHORT).show();
                    //once click the Sign up button - redirect to LoginActivity
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
