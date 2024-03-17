package com.example.deliverysystem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.deliverysystem.Database.DBHelper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private CameraSource cameraSource;
    private SurfaceView surfaceView;
    String doNo, customerName, customerPhone, customerAddress;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doNo = extras.getString("DoNo");
            customerName = extras.getString("CustName");
            customerPhone = extras.getString("CustContact");
            customerAddress = extras.getString("CustAddress");
        }

        surfaceView = findViewById(R.id.cameraPreview);
        createCameraSource();

        // Button click listener for capturing the image
        Button btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(v -> {
            if (cameraSource != null) {
                // Capture the image
                cameraSource.takePicture(null, bytes -> saveCapturedImage(bytes));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CustomerInfoActivity.class);
        intent.putExtra("DoNo", doNo);
        intent.putExtra("CustName", customerName);
        intent.putExtra("CustAddress", customerAddress);
        intent.putExtra("CustContact", customerPhone);
        startActivity(intent);
    }

    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 123);
                        return;
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    private void saveCapturedImage(byte[] data) {
        File pictureFileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (pictureFileDir == null) {
            Log.d("CameraActivity", "Error creating media file directory");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = dateFormat.format(new Date());
        String photoFile = "IMG_" + doNo + "_" + timeStamp + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;
        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(this, "Image saved: " + filename, Toast.LENGTH_SHORT).show();
            dbHelper = new DBHelper(this);
            boolean result = dbHelper.updateImage(Integer.valueOf(doNo), photoFile);
            if(!result)
            {
                Toast.makeText(this, "Failed to update image path", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, CustomerInfoActivity.class);
                intent.putExtra("DoNo", doNo);
                intent.putExtra("CustName", customerName);
                intent.putExtra("CustAddress", customerAddress);
                intent.putExtra("CustContact", customerPhone);
                startActivity(intent);
            }
        } catch (Exception error) {
            Log.d("CameraActivity", "File " + filename + " not saved: " + error.getMessage());
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraSource.start(surfaceView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
