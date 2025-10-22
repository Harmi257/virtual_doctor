package com.example.virtualdoctor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    Button btnLogin, btnRegisterLink;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterLink = findViewById(R.id.btnRegisterLink);
        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Cursor cursor = dbHelper.loginUser(email, password);

                if (cursor.moveToFirst()) {
                    String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String useremail = cursor.getString(cursor.getColumnIndexOrThrow("email"));  // get email here
                    Toast.makeText(LoginActivity.this, "Login Success as " + role, Toast.LENGTH_SHORT).show();

                    Intent intent;

                    if (role.equals("doctor")) {
                        // Check if doctor details exist in DB
                        boolean doctorDetailsFilled = dbHelper.isDoctorDetailsFilled(userId);
                        if (doctorDetailsFilled) {
                            intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, DoctorDetailsActivity.class);
                        }
                        intent.putExtra("user_id", userId);
                        intent.putExtra("email", useremail);
                    } else {
                        // For user role, check if user details exist
                        boolean userDetailsFilled = dbHelper.isUserDetailsFilled(userId);
                        if (userDetailsFilled) {
                            intent = new Intent(LoginActivity.this, Userhome.class); // Launch Userhome (fragment-based)
                            intent.putExtra("user_id", userId);
                        } else {
                            intent = new Intent(LoginActivity.this, UserDetailsActivity.class);
                            intent.putExtra("user_id", userId);
                        }

                    }

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

        btnRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
