//package com.example.virtualdoctor;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.*;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.HashMap;
//
//public class BookDoctorActivity extends AppCompatActivity {
//
//    EditText etSymptoms, etDuration, etTriggers, etPreviousTreatments;
//    Spinner spinnerSeverity, spinnerMode;
//    Button btnUploadReport, btnSubmit;
//    String reportFilePath = null;
//
//    DBHelper dbHelper;
//    String doctorName, doctorEmail, doctorHospital;
//    String meetLink;
//
//    ActivityResultLauncher<String> filePickerLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_doctor);
//
//        dbHelper = new DBHelper(this);
//
//        etSymptoms = findViewById(R.id.etSymptoms);
//        etDuration = findViewById(R.id.etDuration);
//        etTriggers = findViewById(R.id.etTriggers);
//        etPreviousTreatments = findViewById(R.id.etPreviousTreatments);
//        spinnerSeverity = findViewById(R.id.spinnerSeverity);
//        spinnerMode = findViewById(R.id.spinnerMode);
//        btnUploadReport = findViewById(R.id.btnUploadReport);
//        btnSubmit = findViewById(R.id.btnSubmit);
//
//        // Retrieve doctor data safely with fallback empty strings
//        doctorName = getIntent().getStringExtra("doctor_name");
//        if (doctorName == null) doctorName = "";
//
//        doctorEmail = getIntent().getStringExtra("doctor_email");
//        if (doctorEmail == null) doctorEmail = "";
//
//        doctorHospital = getIntent().getStringExtra("doctor_hospital");
//        if (doctorHospital == null) doctorHospital = "";
//
//        // Generate dummy Google Meet link (replace with real logic if needed)
//        meetLink = "https://meet.google.com/" + generateDummyMeetCode();
//
//        // Setup severity spinner
//        String[] severities = {"Mild", "Moderate", "Severe"};
//        spinnerSeverity.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, severities));
//
//        // Setup mode of consultation spinner
//        String[] modes = {"Video Call", "Chat", "Offline"};
//        spinnerMode.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modes));
//
//        // Setup file picker launcher
//        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
//            if (uri != null) {
//                reportFilePath = uri.toString();
//                Toast.makeText(this, "Report uploaded", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btnUploadReport.setOnClickListener(v -> filePickerLauncher.launch("*/*"));
//
//        btnSubmit.setOnClickListener(v -> submitBooking());
//    }
//
//    private void submitBooking() {
//        String symptoms = etSymptoms.getText().toString().trim();
//        String duration = etDuration.getText().toString().trim();
//        String triggers = etTriggers.getText().toString().trim();
//        String previousTreatments = etPreviousTreatments.getText().toString().trim();
//        String severity = spinnerSeverity.getSelectedItem() != null ? spinnerSeverity.getSelectedItem().toString() : "";
//        String mode = spinnerMode.getSelectedItem() != null ? spinnerMode.getSelectedItem().toString() : "";
//
//        // Basic validation
//        if (symptoms.isEmpty() || duration.isEmpty() || severity.isEmpty() || mode.isEmpty()) {
//            Toast.makeText(this, "Please fill all required fields (Symptoms, Duration, Severity, Mode)", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Prepare booking data
//        HashMap<String, String> bookingData = new HashMap<>();
//        bookingData.put("doctor_name", doctorName);
//        bookingData.put("doctor_email", doctorEmail);
//        bookingData.put("doctor_hospital", doctorHospital);
//        bookingData.put("symptoms", symptoms);
//        bookingData.put("duration", duration);
//        bookingData.put("severity", severity);
//        bookingData.put("triggers", triggers);
//        bookingData.put("previous_treatments", previousTreatments);
//        bookingData.put("report_path", reportFilePath != null ? reportFilePath : "");
//        bookingData.put("mode_of_consultation", mode);
//
//        // Insert booking in database
//        boolean inserted = dbHelper.insertBooking(bookingData);
//
//        if (inserted) {
//            Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show();
//            handleConsultationMode(mode);
//        } else {
//            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void handleConsultationMode(String mode) {
//        switch (mode) {
//            case "Video Call":
//                if (meetLink != null && !meetLink.isEmpty()) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meetLink));
//                    startActivity(browserIntent);
//                } else {
//                    Toast.makeText(this, "No Google Meet link available", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case "Chat":
//                // Chat option is currently unavailable; you could add email intent here if you want
//                Toast.makeText(this, "Chat option is currently unavailable", Toast.LENGTH_SHORT).show();
//                break;
//
//            case "Offline":
//                if (doctorHospital != null && !doctorHospital.isEmpty()) {
//                    String mapsUrl = "http://maps.google.com/maps?q=" + Uri.encode(doctorHospital);
//                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
//                    startActivity(mapsIntent);
//                } else {
//                    Toast.makeText(this, "No hospital address available", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            default:
//                Toast.makeText(this, "Unknown consultation mode", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        // Close activity after handling
//        finish();
//    }
//
//    private String generateDummyMeetCode() {
//        String chars = "abcdefghijklmnopqrstuvwxyz";
//        String numbers = "0123456789";
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < 3; i++) sb.append(chars.charAt((int)(Math.random() * chars.length())));
//        sb.append("-");
//        for (int i = 0; i < 4; i++) sb.append(numbers.charAt((int)(Math.random() * numbers.length())));
//        sb.append("-");
//        for (int i = 0; i < 3; i++) sb.append(chars.charAt((int)(Math.random() * chars.length())));
//
//        return sb.toString();
//    }
//}
