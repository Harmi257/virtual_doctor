package com.example.virtualdoctor;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.virtualdoctor.broadcastReceiver.ReminderBroadcastReceiver;
import com.example.virtualdoctor.service.ScheduleConsultationService;

import java.util.Calendar;

public class ScheduleMeetingActivity extends AppCompatActivity {

    private EditText editPrescription, editGpayId, editDate, editTime, editMeetLink;
    private Button btnSubmit, btnOpenMeet;
    private String userEmail, doctorEmail, mode;
    private TextView textLocation;
    private DBHelper dbHelper;
    private Calendar selectedDate;

    private int selectedHour = -1;
    private int selectedMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);

        // Initialize views
        editPrescription = findViewById(R.id.editPrescription);
        editGpayId = findViewById(R.id.editGpayId);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editMeetLink = findViewById(R.id.editMeetLink);
        textLocation = findViewById(R.id.textLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnOpenMeet = findViewById(R.id.btnOpenMeet);

        TextView textUserEmail = findViewById(R.id.textUserEmail);
        TextView textDoctorEmail = findViewById(R.id.textDoctorEmail);
        TextView textMode = findViewById(R.id.textMode);

        // Get data from intent
        userEmail = getIntent().getStringExtra("userEmail");
        doctorEmail = getIntent().getStringExtra("doctorEmail");
        mode = getIntent().getStringExtra("mode");

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(doctorEmail) || TextUtils.isEmpty(mode)) {
            Toast.makeText(this, "Missing booking details, redirecting...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        textUserEmail.setText("User Email: " + userEmail);
        textDoctorEmail.setText("Doctor Email: " + doctorEmail);
        textMode.setText("Mode: " + mode);

        dbHelper = new DBHelper(this);
        selectedDate = Calendar.getInstance();

        // Adjust UI based on mode
        if ("online".equalsIgnoreCase(mode)) {
            editPrescription.setVisibility(View.GONE);
            editMeetLink.setVisibility(View.VISIBLE);
            textLocation.setVisibility(View.GONE);
            btnOpenMeet.setVisibility(View.VISIBLE);
        } else if ("only prescription".equalsIgnoreCase(mode)) {
            editPrescription.setVisibility(View.VISIBLE);
            editMeetLink.setVisibility(View.GONE);
            textLocation.setVisibility(View.GONE);
            btnOpenMeet.setVisibility(View.GONE);
        } else if ("In-person".equalsIgnoreCase(mode)) {
            editPrescription.setVisibility(View.GONE);
            editMeetLink.setVisibility(View.GONE);
            btnOpenMeet.setVisibility(View.GONE);
            textLocation.setVisibility(View.VISIBLE);

            Cursor cursor = dbHelper.getDoctorLocationByEmail(doctorEmail);
            if (cursor != null && cursor.moveToFirst()) {
                int latIndex = cursor.getColumnIndex("latitude");
                int lonIndex = cursor.getColumnIndex("longitude");
                if (latIndex != -1 && lonIndex != -1) {
                    double latitude = cursor.getDouble(latIndex);
                    double longitude = cursor.getDouble(lonIndex);
                    String locationUrl = "https://maps.google.com/?q=" + latitude + "," + longitude;
                    textLocation.setText("Clinic Location:\n" + locationUrl);
                } else {
                    textLocation.setText("Location data not found.");
                }
            } else {
                textLocation.setText("Location not available.");
            }
        } else {
            Toast.makeText(this, "Invalid mode. Redirecting...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Date picker dialog
        editDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(year, month, dayOfMonth);
                        editDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        // Time picker dialog
        editTime.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();

            int hour = selectedHour != -1 ? selectedHour : now.get(Calendar.HOUR_OF_DAY);
            int minute = selectedMinute != -1 ? selectedMinute : now.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ScheduleMeetingActivity.this,
                    (view, hourOfDay, minute1) -> {
                        selectedHour = hourOfDay;
                        selectedMinute = minute1;

                        String timeFormatted = String.format("%02d:%02d", hourOfDay, minute1);
                        editTime.setText(timeFormatted);

                        // Update selectedDate with time chosen
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDate.set(Calendar.MINUTE, minute1);
                        selectedDate.set(Calendar.SECOND, 0);
                        selectedDate.set(Calendar.MILLISECOND, 0);
                    },
                    hour,
                    minute,
                    true
            );

            timePickerDialog.show();
        });

        // Open Google Meet
        btnOpenMeet.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://meet.google.com/"));
            startActivity(browserIntent);
        });

        // Submit button logic
        btnSubmit.setOnClickListener(v -> {
            String gpayId = editGpayId.getText().toString().trim();
            String date = editDate.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String prescription = editPrescription.getText().toString().trim();
            String meetLink = editMeetLink.getText().toString().trim();
            String location = textLocation.getText().toString().trim();

            if (TextUtils.isEmpty(gpayId) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                Toast.makeText(this, "Please enter GPay ID, consultation date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("online".equalsIgnoreCase(mode) && TextUtils.isEmpty(meetLink)) {
                Toast.makeText(this, "Google Meet link required", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("only prescription".equalsIgnoreCase(mode) && TextUtils.isEmpty(prescription)) {
                Toast.makeText(this, "Prescription required", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("In-person".equalsIgnoreCase(mode) && TextUtils.isEmpty(location)) {
                Toast.makeText(this, "Clinic location missing", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertConsultationDetails(
                    userEmail, doctorEmail, mode, gpayId, date,
                    "only prescription".equalsIgnoreCase(mode) ? prescription : null,
                    "online".equalsIgnoreCase(mode) ? meetLink : null,
                    "In-person".equalsIgnoreCase(mode) ? location : null
            );

            if (inserted) {
                String dateTimeText = date + " " + time;

                if ("online".equalsIgnoreCase(mode)) {
                    addEventToCalendar(dateTimeText, meetLink, location);
                    sendEmailInvite(dateTimeText, meetLink);
                    sendWhatsAppMessage(dateTimeText, null);
                    Toast.makeText(this, "Online consultation booked & event added!", Toast.LENGTH_SHORT).show();
                } else if ("In-person".equalsIgnoreCase(mode)) {
                    sendEmailInvite(dateTimeText, null);
                    sendWhatsAppMessage(dateTimeText, location);
                    Toast.makeText(this, "Consultation booked & notifications sent!", Toast.LENGTH_SHORT).show();
                } else if ("only prescription".equalsIgnoreCase(mode)) {
                    sendEmailInvite(dateTimeText, null);
                    sendWhatsAppMessage(dateTimeText, null);
                    Toast.makeText(this, "Prescription saved & notifications sent!", Toast.LENGTH_SHORT).show();
                }

                // Schedule alarm using the background service instead of direct AlarmManager call
                ScheduleConsultationService.startActionSchedule(
                        this,
                        mode,
                        selectedDate.getTimeInMillis(),
                        date,
                        time
                );

                finish();
            } else {
                Toast.makeText(this, "Failed to save consultation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEventToCalendar(String dateTime, String meetLink, String location) {
        long startMillis = selectedDate.getTimeInMillis();
        long endMillis = startMillis + (60 * 60 * 1000);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(CalendarContract.Events.TITLE, "Consultation with Doctor")
                .putExtra(CalendarContract.Events.DESCRIPTION,
                        mode.equalsIgnoreCase("online") ? "Join via Meet: " + meetLink : "Visit at: " + location)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail, doctorEmail});
        startActivity(intent);
    }

    private void sendEmailInvite(String dateTime, String meetLink) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail, doctorEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Consultation Invite");

        StringBuilder message = new StringBuilder("Dear all,\n\nYour consultation is scheduled on ")
                .append(dateTime);

        if (mode.equalsIgnoreCase("online") && meetLink != null) {
            message.append("\nGoogle Meet Link: ").append(meetLink);
        } else if (mode.equalsIgnoreCase("only prescription")) {
            message.append("\nPrescription Details: ").append(editPrescription.getText().toString());
        }

        message.append("\n\n- Virtual Doctor App");

        emailIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void sendWhatsAppMessage(String dateTime, String locationUrl) {
        try {
            StringBuilder message = new StringBuilder("Dear Patient,\n\nYour consultation is on ")
                    .append(dateTime)
                    .append("\nMode: ").append(mode);

            if (mode.equalsIgnoreCase("online")) {
                message.append("\nMeet Link: ").append(editMeetLink.getText().toString());
            } else if (mode.equalsIgnoreCase("In-person") && locationUrl != null) {
                message.append("\nLocation: ").append(locationUrl);
            } else if (mode.equalsIgnoreCase("only prescription")) {
                message.append("\nPrescription: ").append(editPrescription.getText().toString());
            }

            message.append("\n\n- Virtual Doctor App");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://wa.me/?text=" + Uri.encode(message.toString())));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
