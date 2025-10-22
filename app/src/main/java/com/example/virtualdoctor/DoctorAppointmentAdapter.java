package com.example.virtualdoctor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorAppointmentAdapter extends CursorAdapter {

    private DBHelper dbHelper;

    public DoctorAppointmentAdapter(Context context, Cursor c, int flags, DBHelper dbHelper) {
        super(context, c, flags);
        this.dbHelper = dbHelper;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.appointment_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textSymptoms = view.findViewById(R.id.textSymptoms);
        TextView textDuration = view.findViewById(R.id.textDuration);
        TextView textSeverity = view.findViewById(R.id.textSeverity);
        TextView textMode = view.findViewById(R.id.textMode);
        TextView textPatientName = view.findViewById(R.id.textPatientName);
        TextView textPatientEmail = view.findViewById(R.id.textPatientEmail);
        TextView textPatientPhone = view.findViewById(R.id.textPatientPhone);
        TextView textPatientAddress = view.findViewById(R.id.textPatientAddress);
        TextView textStatus = view.findViewById(R.id.textStatus);

        Button btnAccept = view.findViewById(R.id.btnAccept);
        Button btnReject = view.findViewById(R.id.btnReject);
        Button btnScheduleMeeting = view.findViewById(R.id.btnScheduleMeeting); // make sure it's in the XML

        // Extract data from cursor
        String symptoms = cursor.getString(cursor.getColumnIndexOrThrow("symptoms"));
        String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
        String severity = cursor.getString(cursor.getColumnIndexOrThrow("severity"));
        String mode = cursor.getString(cursor.getColumnIndexOrThrow("mode"));
        String patientName = cursor.getString(cursor.getColumnIndexOrThrow("patient_name"));
        String patientEmail = cursor.getString(cursor.getColumnIndexOrThrow("patient_email"));
        String patientPhone = cursor.getString(cursor.getColumnIndexOrThrow("patient_phone"));
        String patientAddress = cursor.getString(cursor.getColumnIndexOrThrow("patient_address"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
        int appointmentId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

        // Bind data to views
        textSymptoms.setText("Symptoms: " + symptoms);
        textDuration.setText("Duration: " + duration);
        textSeverity.setText("Severity: " + severity);
        textMode.setText("Mode: " + mode);
        textPatientName.setText("Patient: " + patientName);
        textPatientEmail.setText("Email: " + patientEmail);
        textPatientPhone.setText("Phone: " + patientPhone);
        textPatientAddress.setText("Address: " + patientAddress);
        textStatus.setText("Status: " + status);

        // Control buttons based on status
        if ("pending".equalsIgnoreCase(status)) {
            btnAccept.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
            btnScheduleMeeting.setVisibility(View.GONE);

            btnAccept.setOnClickListener(v -> {
                boolean updated = dbHelper.updateAppointmentStatus(appointmentId, "accepted");
                if (updated) {
                    Toast.makeText(context, "Appointment accepted", Toast.LENGTH_SHORT).show();
                    refreshCursor();
                } else {
                    Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            });

            btnReject.setOnClickListener(v -> {
                boolean updated = dbHelper.updateAppointmentStatus(appointmentId, "rejected");
                if (updated) {
                    Toast.makeText(context, "Appointment rejected", Toast.LENGTH_SHORT).show();
                    refreshCursor();
                } else {
                    Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            });

        } else if ("accepted".equalsIgnoreCase(status)) {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            btnScheduleMeeting.setVisibility(View.VISIBLE);

            btnScheduleMeeting.setOnClickListener(v -> {
                Intent intent = new Intent(context, ScheduleMeetingActivity.class);
                intent.putExtra("userEmail", patientEmail);
                intent.putExtra("doctorEmail", dbHelper.getCurrentDoctorEmail());
                intent.putExtra("mode", mode);
                context.startActivity(intent);
            });

        } else {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            btnScheduleMeeting.setVisibility(View.GONE);
        }
    }

    private void refreshCursor() {
        Cursor newCursor = dbHelper.getAppointmentsForDoctor(dbHelper.getCurrentDoctorEmail());
        swapCursor(newCursor);
    }
}
