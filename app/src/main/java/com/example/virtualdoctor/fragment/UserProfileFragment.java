package com.example.virtualdoctor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.virtualdoctor.DBHelper;
import com.example.virtualdoctor.R;

import java.util.HashMap;
public class UserProfileFragment extends Fragment {

    private int userId = -1;

    private TextView tvName, tvDOB, tvEmail, tvMobile, tvAddress,
            tvHeight, tvWeight, tvBloodGroup, tvExistingConditions,
            tvPastSurgeries, tvCurrentMedications, tvAllergies,
            tvFamilyHistory, tvSmoker, tvDrinker;

    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize all views
        tvName = view.findViewById(R.id.tvName);
        tvDOB = view.findViewById(R.id.tvDOB);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvBloodGroup = view.findViewById(R.id.tvBloodGroup);
        tvExistingConditions = view.findViewById(R.id.tvExistingConditions);
        tvPastSurgeries = view.findViewById(R.id.tvPastSurgeries);
        tvCurrentMedications = view.findViewById(R.id.tvCurrentMedications);
        tvAllergies = view.findViewById(R.id.tvAllergies);
        tvFamilyHistory = view.findViewById(R.id.tvFamilyHistory);
        tvSmoker = view.findViewById(R.id.tvSmoker);
        tvDrinker = view.findViewById(R.id.tvDrinker);

        dbHelper = new DBHelper(getActivity());

        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }

        if (userId != -1) {
            loadUserProfile(userId);
        } else {
            Toast.makeText(getActivity(), "No valid user ID found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadUserProfile(int userId) {
        HashMap<String, String> userDetails = dbHelper.getUserDetailsByUserId(userId);

        if (userDetails != null && !userDetails.isEmpty()) {
            tvName.setText(userDetails.getOrDefault("full_name", "N/A"));
            tvDOB.setText(userDetails.getOrDefault("date_of_birth", "N/A"));
            tvEmail.setText(userDetails.getOrDefault("email", "N/A"));
            tvMobile.setText(userDetails.getOrDefault("phone", "N/A"));
            tvAddress.setText(userDetails.getOrDefault("address", "N/A"));
            tvHeight.setText(userDetails.getOrDefault("height", "N/A"));
            tvWeight.setText(userDetails.getOrDefault("weight", "N/A"));
            tvBloodGroup.setText(userDetails.getOrDefault("blood_group", "N/A"));
            tvExistingConditions.setText(userDetails.getOrDefault("existing_conditions", "N/A"));
            tvPastSurgeries.setText(userDetails.getOrDefault("past_surgeries", "N/A"));
            tvCurrentMedications.setText(userDetails.getOrDefault("current_medications", "N/A"));
            tvAllergies.setText(userDetails.getOrDefault("allergies", "N/A"));
            tvFamilyHistory.setText(userDetails.getOrDefault("family_history", "N/A"));

            // Convert smoker/drinker int values to Yes/No
            tvSmoker.setText(convertFlagToYesNo(userDetails.get("smoker")));
            tvDrinker.setText(convertFlagToYesNo(userDetails.get("drinker")));

        } else {
            Toast.makeText(getActivity(), "User details not found", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertFlagToYesNo(String flag) {
        if (flag == null) return "N/A";
        try {
            int val = Integer.parseInt(flag);
            return val == 1 ? "Yes" : "No";
        } catch (NumberFormatException e) {
            return "N/A";
        }
    }
}
