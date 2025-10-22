package com.example.virtualdoctor;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.virtualdoctor.fragment.BookingFormFragment;

public class ConsultNowActivity extends AppCompatActivity {

    TextView textDoctorName, textSpecialization, textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_now);

        textDoctorName = findViewById(R.id.textDoctorName);
        textSpecialization = findViewById(R.id.textSpecialization);
        textEmail = findViewById(R.id.textEmail);

        String name = getIntent().getStringExtra("name");
        String specialization = getIntent().getStringExtra("specialization");
        String email = getIntent().getStringExtra("email");

        textDoctorName.setText("Doctor: " + name);
        textSpecialization.setText("Specialization: " + specialization);
        textEmail.setText("Email: " + email);

        // Now load the fragment here
        FragmentManager fm = getSupportFragmentManager();
        BookingFormFragment fragment = BookingFormFragment.newInstance(name, specialization, email);

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)  // Make sure activity_consult_now.xml has this container
                .addToBackStack(null)
                .commit();
    }
}
