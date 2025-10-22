package com.example.virtualdoctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.virtualdoctor.R;
import com.example.virtualdoctor.model.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private final List<Appointment> appointments;
    private final Context context;

    public AppointmentAdapter(List<Appointment> appointments, Context context) {
        this.appointments = appointments;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPatientName, textPatientEmail, textPatientPhone, textPatientAddress;
        TextView textSymptoms, textDuration, textSeverity, textMode;
        Button btnAccept;

        public ViewHolder(View itemView) {
            super(itemView);
            textPatientName = itemView.findViewById(R.id.textPatientName);
            textPatientEmail = itemView.findViewById(R.id.textPatientEmail);
            textPatientPhone = itemView.findViewById(R.id.textPatientPhone);
            textPatientAddress = itemView.findViewById(R.id.textPatientAddress);
            textSymptoms = itemView.findViewById(R.id.textSymptoms);
            textDuration = itemView.findViewById(R.id.textDuration);
            textSeverity = itemView.findViewById(R.id.textSeverity);
            textMode = itemView.findViewById(R.id.textMode);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }

    @NonNull
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.ViewHolder holder, int position) {
        Appointment appt = appointments.get(position);

        holder.textPatientName.setText("Name: " + appt.getPatientName());
        holder.textPatientEmail.setText("Email: " + appt.getPatientEmail());
        holder.textPatientPhone.setText("Phone: " + appt.getPatientPhone());
        holder.textPatientAddress.setText("Address: " + appt.getPatientAddress());
        holder.textSymptoms.setText("Symptoms: " + appt.getSymptoms());
        holder.textDuration.setText("Duration: " + appt.getDuration());
        holder.textSeverity.setText("Severity: " + appt.getSeverity());
        holder.textMode.setText("Mode: " + appt.getMode());

        holder.btnAccept.setOnClickListener(v -> {
            String mode = appt.getMode();
            String email = appt.getPatientEmail();
            String prescription = appt.getPrescription();

            if (prescription != null && !prescription.isEmpty()) {
                sendWhatsAppMessage(appt.getPatientPhone(), prescription);
            } else if (mode.equalsIgnoreCase("Online")) {
                sendEmail(email, "Google Meet Link",
                        "Your appointment is confirmed.\nMeet Link: https://meet.google.com/xyz-abcq\nTime: 5:00 PM\nStatus: Available");
            } else {
                sendEmail(email, "Offline Appointment",
                        "Your offline appointment is confirmed.\nClinic Location: 123 Clinic Street\nDate: 3rd June, 11:00 AM\nStatus: Available");
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    private void sendEmail(String to, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            context.startActivity(Intent.createChooser(intent, "Send email via..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendWhatsAppMessage(String phone, String prescription) {
        String message = "Hello! Your prescription is ready:\n" + prescription;
        String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + Uri.encode(message);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
