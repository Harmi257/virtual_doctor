package com.example.virtualdoctor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDashboardActivity extends AppCompatActivity {

    Spinner specializationSpinner, hospitalSpinner;
    ListView doctorListView;
    DBHelper dbHelper;
    VideoView videoView;

    ArrayList<String> specializationList = new ArrayList<>();
    ArrayList<String> hospitalList = new ArrayList<>();

    ArrayList<HashMap<String, String>> doctorList = new ArrayList<>();
    DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        specializationSpinner = findViewById(R.id.spinnerSpecialization);
        hospitalSpinner = findViewById(R.id.spinnerHospital);
        doctorListView = findViewById(R.id.listDoctors);
        videoView = findViewById(R.id.videoView);

        dbHelper = new DBHelper(this);

        loadFilters();
        setupListeners();

        adapter = new DoctorAdapter(this, doctorList);
        doctorListView.setAdapter(adapter);

        setupVideo();
    }

    private void setupVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.consulting);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);  // Loop the video
            videoView.start();
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(this, "Error playing video", Toast.LENGTH_SHORT).show();
            return true;
        });
    }


    private void loadFilters() {
        specializationList = dbHelper.getAllUniqueValues("specialization");
        hospitalList = dbHelper.getAllUniqueValues("hospital");

        specializationList.add(0, "Select Specialization");
        hospitalList.add(0, "Select Hospital");

        specializationSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, specializationList));
        hospitalSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, hospitalList));
    }

    private void setupListeners() {
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterDoctors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };

        specializationSpinner.setOnItemSelectedListener(filterListener);
        hospitalSpinner.setOnItemSelectedListener(filterListener);
    }

    private void filterDoctors() {
        String specialization = specializationSpinner.getSelectedItem().toString();
        String hospital = hospitalSpinner.getSelectedItem().toString();

        if (specialization.equals("Select Specialization") || hospital.equals("Select Hospital")) {
            doctorList.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        Cursor cursor = dbHelper.getDoctorsByFilters(specialization, hospital);
        doctorList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String experience = cursor.getString(cursor.getColumnIndexOrThrow("experience"));

                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("specialization", specialization);
                map.put("details", "Email: " + email + " | Experience: " + experience + " yrs");

                doctorList.add(map);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No doctors found for selected filters.", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
}
