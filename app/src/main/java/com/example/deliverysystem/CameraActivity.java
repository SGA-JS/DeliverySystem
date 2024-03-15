package com.example.deliverysystem;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.deliverysystem.Database.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera.PictureCallback mPictureCallback;
    private String doNo;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        doNo = getIntent().getStringExtra("DoNo");
        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        // Initialize the picture callback
        mPictureCallback = (data, camera) -> saveCapturedImage(data);

        // Request camera and storage permissions
        requestPermissions();

        // Button click listener for capturing the image
        Button btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(v -> {
            if (mCamera != null) {
                // Capture the image
                mCamera.takePicture(null, null, mPictureCallback);
            }
        });
    }

    private void initializeCamera() {
        mCamera = getCameraInstance();
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error starting camera preview", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        initializeCamera();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // No need to update camera parameters in this implementation
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        releaseCamera();
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
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
            boolean result = dbHelper.updateImage(doNo, filename);
            if(!result)
            {
                Toast.makeText(this, "Failed to update image path", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception error) {
            Log.d("CameraActivity", "File " + filename + " not saved: " + error.getMessage());
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        } else {
            // Permissions already granted, initialize the camera
            initializeCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, initialize the camera
                initializeCamera();
            } else {
                // Permissions denied, show a message or handle accordingly
                Toast.makeText(this, "Camera and storage permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }
}