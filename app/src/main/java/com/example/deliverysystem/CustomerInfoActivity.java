package com.example.deliverysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliverysystem.Database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerInfoActivity extends AppCompatActivity {

    TextView textViewDoNo, textViewCustomerName, textViewCustomerPhone, textViewCustomerAddress;
    Button statusButton;
    ImageView btnCamera, btnGps;
    DBHelper dbHelper;
    String doNo, customerName, customerPhone, customerAddress;
    int status;

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

        btnCamera.setEnabled(false);
        btnGps.setEnabled(false);

        // Retrieve data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doNo = extras.getString("DoNo");
            customerName = extras.getString("CustName");
            customerPhone = extras.getString("CustContact");
            customerAddress = extras.getString("CustAddress");

            // Set data to respective TextViews
            textViewDoNo.setText("DO No: " + doNo);
            textViewCustomerName.setText("Customer Name: " + customerName);
            textViewCustomerPhone.setText("Customer Phone: " + customerPhone);
            textViewCustomerAddress.setText("Customer Address: " + customerAddress);
        }

        dbHelper = new DBHelper(this);
        status = dbHelper.getTaskStatusByDo(doNo);
        if (status == ConstantValue.TASK_STATUS_UNDONE)
        {
            statusButton.setText("Start");
            statusButton.setBackgroundColor(Color.parseColor("#54FD04"));

        }
        else if (status == ConstantValue.TASK_STATUS_PROCESSING)
        {
            statusButton.setText("Complete");
            statusButton.setBackgroundColor(Color.parseColor("#0431FD"));
            btnCamera.setEnabled(true);
            btnGps.setEnabled(true);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerInfoActivity.this, CameraActivity.class);
                intent.putExtra("DoNo", doNo);
                startActivity(intent);
                finish();
            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerInfoActivity.this, MapsActivity.class);
                intent.putExtra("Address", customerAddress);
                startActivity(intent);
                finish();
            }
        });
    }

    public void btnStatusClicked(View view) {
        boolean result = true;
        if (status == ConstantValue.TASK_STATUS_UNDONE)
        {
            result = dbHelper.updateTaskProcessing(doNo);
            if(!result)
            {
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
        else if (status == ConstantValue.TASK_STATUS_PROCESSING)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
            String dateText = df.format(new Date());

            result = dbHelper.completeTask(doNo, dateText);
            if(!result)
            {
                Toast.makeText(this, "Failed to complete task", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent myIntent = new Intent(CustomerInfoActivity.this, MainActivity.class);
                // Switch to the AnotherActivity
                startActivity(myIntent);
            }
        }
    }
}