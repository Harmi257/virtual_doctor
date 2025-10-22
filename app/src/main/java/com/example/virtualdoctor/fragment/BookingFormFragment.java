package com.example.virtualdoctor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;

import com.example.virtualdoctor.DBHelper;
import com.example.virtualdoctor.R;

public class BookingFormFragment extends Fragment {

    EditText etSymptoms, etDuration, etTriggers, etPreviousTreatments, etPatientEmail;
    Spinner spinnerSeverity, spinnerMode;
    Button btnSubmit;

    String doctorName, specialization, email;
    DBHelper dbHelper;

    public static BookingFormFragment newInstance(String doctorName, String specialization, String email) {
        BookingFormFragment fragment = new BookingFormFragment();
        Bundle args = new Bundle();
        args.putString("doctorName", doctorName);
        args.putString("specialization", specialization);
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_form, container, false);

        etSymptoms = view.findViewById(R.id.etSymptoms);
        etDuration = view.findViewById(R.id.etDuration);
        etTriggers = view.findViewById(R.id.etTriggers);
        etPreviousTreatments = view.findViewById(R.id.etPreviousTreatments);
        etPatientEmail = view.findViewById(R.id.etPatientEmail); // <-- new field
        spinnerSeverity = view.findViewById(R.id.spinnerSeverity);
        spinnerMode = view.findViewById(R.id.spinnerMode);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        dbHelper = new DBHelper(getActivity());

        if (getArguments() != null) {
            doctorName = getArguments().getString("doctorName");
            specialization = getArguments().getString("specialization");
            email = getArguments().getString("email");
        }

        // Setup spinners
        ArrayAdapter<CharSequence> severityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.severity_array, android.R.layout.simple_spinner_item);
        severityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeverity.setAdapter(severityAdapter);

        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.mode_array, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(modeAdapter);

        btnSubmit.setOnClickListener(v -> {
            String symptoms = etSymptoms.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();
            String triggers = etTriggers.getText().toString().trim();
            String previousTreatments = etPreviousTreatments.getText().toString().trim();
            String patientEmail = etPatientEmail.getText().toString().trim(); // <-- get email
            String severity = spinnerSeverity.getSelectedItem().toString();
            String mode = spinnerMode.getSelectedItem().toString();

            boolean inserted = dbHelper.insertAppointment(
                    doctorName, specialization, email, patientEmail,
                    symptoms, duration, triggers, previousTreatments, severity, mode
            );
  //new code
            if (inserted) {
                Toast.makeText(getActivity(), "Appointment booked successfully!", Toast.LENGTH_SHORT).show();

                // ✅ Navigate to UserDashboardActivity
                Intent intent = new Intent(getActivity(), com.example.virtualdoctor.UserDashboardActivity.class);
                startActivity(intent);
                requireActivity().finish(); // Optional: closes the current activity hosting the fragment
            } else {
                Toast.makeText(getActivity(), "Failed to book appointment.", Toast.LENGTH_SHORT).show();
            }
        });     //new code

        return view;
    }
}
