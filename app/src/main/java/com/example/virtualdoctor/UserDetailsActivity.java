package com.example.virtualdoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {

    EditText inputFullName, inputDob, inputEmail, inputPhone, inputAddress,
            inputHeight, inputWeight, inputBloodGroup, inputPastSurgeries,
            inputMedications, inputFamilyHistory, inputAllergies, inputConditions;
    CheckBox cbSmoker, cbDrinker;
    Button btnSubmit;
    DBHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        dbHelper = new DBHelper(this);
        userId = getIntent().getIntExtra("user_id", -1);

        inputFullName = findViewById(R.id.inputFullName);
        inputDob = findViewById(R.id.inputDob);
        inputEmail = findViewById(R.id.inputEmail);
        inputPhone = findViewById(R.id.inputPhone);
        inputAddress = findViewById(R.id.inputAddress);
        inputHeight = findViewById(R.id.inputHeight);
        inputWeight = findViewById(R.id.inputWeight);
        inputBloodGroup = findViewById(R.id.inputBloodGroup);
        inputPastSurgeries = findViewById(R.id.inputPastSurgeries);
        inputMedications = findViewById(R.id.inputMedications);
        inputFamilyHistory = findViewById(R.id.inputFamilyHistory);
        inputAllergies = findViewById(R.id.inputAllergies);
        inputConditions = findViewById(R.id.inputConditions);
        cbSmoker = findViewById(R.id.cbSmoker);
        cbDrinker = findViewById(R.id.cbDrinker);
        btnSubmit = findViewById(R.id.btnSubmitUser);

        btnSubmit.setOnClickListener(view -> {
            try {
                boolean saved = dbHelper.saveUserDetails(
                        userId,
                        inputFullName.getText().toString(),
                        inputDob.getText().toString(),
                        inputEmail.getText().toString(),
                        inputPhone.getText().toString(),
                        inputAddress.getText().toString(),
                        Integer.parseInt(inputHeight.getText().toString()),
                        Integer.parseInt(inputWeight.getText().toString()),
                        inputBloodGroup.getText().toString(),
                        inputConditions.getText().toString(),
                        inputPastSurgeries.getText().toString(),
                        inputMedications.getText().toString(),
                        inputAllergies.getText().toString(),
                        inputFamilyHistory.getText().toString(),
                        cbSmoker.isChecked(),
                        cbDrinker.isChecked()
                );

                if (saved) {
                    Toast.makeText(UserDetailsActivity.this, "Details saved!", Toast.LENGTH_SHORT).show();

                    // Navigate to UserDashboard after saving details
                    Intent intent = new Intent(UserDetailsActivity.this, Userhome.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserDetailsActivity.this, "Failed to save!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(UserDetailsActivity.this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
