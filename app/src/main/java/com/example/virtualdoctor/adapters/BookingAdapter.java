package com.example.virtualdoctor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualdoctor.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final ArrayList<HashMap<String, String>> bookings;

    public BookingAdapter(ArrayList<HashMap<String, String>> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        HashMap<String, String> booking = bookings.get(position);
        holder.doctorName.setText("Dr. " + booking.get("doctor_name"));
        holder.specialization.setText("Specialist: " + booking.get("specialization"));
        holder.mode.setText("Mode: " + booking.get("mode"));
        holder.status.setText("Status: " + booking.get("status"));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, specialization, mode, status;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.txtDoctorName);
            specialization = itemView.findViewById(R.id.txtSpecialization);
            mode = itemView.findViewById(R.id.txtMode);
            status = itemView.findViewById(R.id.txtStatus);
        }
    }
}
