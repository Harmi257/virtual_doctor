package com.example.virtualdoctor;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorDashboardActivity extends AppCompatActivity {

    ListView listViewAppointments;
    DBHelper dbHelper;
    String doctorEmail;
    DoctorAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        listViewAppointments = findViewById(R.id.listViewAppointments);
        dbHelper = new DBHelper(this);

        doctorEmail = getIntent().getStringExtra("email");
        if (doctorEmail == null) {
            doctorEmail = "doctor@example.com";
        }

        dbHelper.setCurrentDoctorEmail(doctorEmail);

        loadAppointments();
    }

    private void loadAppointments() {
        Cursor cursor = dbHelper.getAppointmentsForDoctor(doctorEmail);

        if (cursor != null && cursor.getCount() > 0) {
            adapter = new DoctorAppointmentAdapter(this, cursor, 0, dbHelper);
            listViewAppointments.setAdapter(adapter);
        } else {
            // You can show a "No appointments found" message here
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
