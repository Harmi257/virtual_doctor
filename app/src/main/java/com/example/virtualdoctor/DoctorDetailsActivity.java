package com.example.virtualdoctor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DoctorDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText inputName, inputSpecialization, inputLicenseNumber, inputEmail, inputPhone, inputHospital, inputYearsOfExperience;
    private Button btnSubmit;
    private DBHelper dbHelper;
    private int doctorId;
    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        dbHelper = new DBHelper(this);
        doctorId = getIntent().getIntExtra("user_id", -1);

        inputName = findViewById(R.id.inputDoctorName);
        inputSpecialization = findViewById(R.id.inputSpecialization);
        inputLicenseNumber = findViewById(R.id.inputLicenseNumber);
        inputEmail = findViewById(R.id.inputDoctorEmail);
        inputPhone = findViewById(R.id.inputDoctorPhone);
        inputHospital = findViewById(R.id.inputHospital);
        inputYearsOfExperience = findViewById(R.id.inputExperience);
        btnSubmit = findViewById(R.id.btnSubmitDoctor);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        btnSubmit.setOnClickListener(view -> {
            if (selectedLatLng == null) {
                Toast.makeText(this, "Please select your location", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean saved = dbHelper.saveDoctorDetails(
                    doctorId,
                    inputName.getText().toString(),
                    inputSpecialization.getText().toString(),
                    inputLicenseNumber.getText().toString(),
                    inputEmail.getText().toString(),
                    inputPhone.getText().toString(),
                    inputHospital.getText().toString(),
                    Integer.parseInt(inputYearsOfExperience.getText().toString()),
                    selectedLatLng.latitude,
                    selectedLatLng.longitude
            );

            if (saved) {
                Toast.makeText(DoctorDetailsActivity.this, "Doctor details saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoctorDetailsActivity.this, DoctorDashboardActivity.class);
                startActivity(intent);
                finish();
            }
             else {
                Toast.makeText(DoctorDetailsActivity.this, "Failed to save!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    selectedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                    mMap.addMarker(new MarkerOptions().position(selectedLatLng).title("Current Location"));
                }
            });
        }

        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // ✅ add this line

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location != null) {
                                selectedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                                mMap.addMarker(new MarkerOptions().position(selectedLatLng).title("Current Location"));
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}