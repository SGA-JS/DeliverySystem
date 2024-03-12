package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerInfoActivity extends AppCompatActivity {

    TextView textViewDoNo, textViewCustomerName, textViewCustomerPhone, textViewCustomerAddress;
    Button statusButton;
    ImageView btnCamera, btnGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Initialize views
        textViewDoNo = findViewById(R.id.viewDoNo);
        textViewCustomerName = findViewById(R.id.viewCustomerName);
        textViewCustomerPhone = findViewById(R.id.viewCustomerPhone);
        textViewCustomerAddress = findViewById(R.id.viewCustomerAddress);
        statusButton = findViewById(R.id.statusButton);
        btnCamera = findViewById(R.id.btnCamera);
        btnGps = findViewById(R.id.btnGps);

        // Retrieve data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String doNo = extras.getString("DoNo");
            String customerName = extras.getString("CustName");
            String customerPhone = extras.getString("CustContact");
            String customerAddress = extras.getString("CustAddress");

            // Set data to respective TextViews
            textViewDoNo.setText("DO No: " + doNo);
            textViewCustomerName.setText("Customer Name: " + customerName);
            textViewCustomerPhone.setText("Customer Phone: " + customerPhone);
            textViewCustomerAddress.setText("Customer Address: " + customerAddress);
        }
    }
}