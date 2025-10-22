package com.example.virtualdoctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.virtualdoctor.fragment.UserProfileFragment;
import com.example.virtualdoctor.fragment.UserBookingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Userhome extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int userId;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        userId = getIntent().getIntExtra("user_id", -1);
        Toast.makeText(this, "Userhome received userId: " + userId, Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);

        // Load profile fragment by default
        UserProfileFragment profileFragment = new UserProfileFragment();
        profileFragment.setArguments(bundle);
        loadFragment(profileFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_book) {
                Intent intent = new Intent(Userhome.this, UserDashboardActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                UserProfileFragment fragment = new UserProfileFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_bookings) {
                UserBookingsFragment bookingsFragment = new UserBookingsFragment();
                bookingsFragment.setArguments(bundle);
                loadFragment(bookingsFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
