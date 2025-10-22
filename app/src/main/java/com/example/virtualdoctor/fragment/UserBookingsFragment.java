package com.example.virtualdoctor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualdoctor.DBHelper;
import com.example.virtualdoctor.R;
import com.example.virtualdoctor.adapters.BookingAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_bookings, container, false);
        recyclerView = view.findViewById(R.id.recyclerBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());

        // Get user email from arguments
        int userId = getArguments().getInt("user_id", -1);
        String userEmail = dbHelper.getUserDetailsByUserId(userId).get("email");

        ArrayList<HashMap<String, String>> bookings = dbHelper.getAppointmentsForUser(userEmail);
        bookingAdapter = new BookingAdapter(bookings);
        recyclerView.setAdapter(bookingAdapter);

        return view;
    }
}
