package com.example.deliverysystem;

import androidx.fragment.app.FragmentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    String doNo, customerName, customerPhone, customerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doNo = extras.getString("DoNo");
            customerName = extras.getString("CustName");
            customerPhone = extras.getString("CustContact");
            customerAddress = extras.getString("CustAddress");
        }
        requestLocationPermission();
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

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with setting up the map
            setUpMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with setting up the map
                setUpMap();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private void setUpMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double latitude, longitude;

        // Use Geocoder to convert address to coordinates
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(customerAddress, 1);
            if (addresses != null && !addresses.isEmpty()) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
                // Add a marker and move the camera
                LatLng destination = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 12.0f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}