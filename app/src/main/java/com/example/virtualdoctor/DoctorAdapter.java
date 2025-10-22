package com.example.virtualdoctor;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
public class DoctorAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> doctorList;

    public DoctorAdapter(Context context, ArrayList<HashMap<String, String>> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int i) {
        return doctorList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.doctor_list_item, parent, false);

        TextView nameText = convertView.findViewById(R.id.textDoctorName);
        TextView detailsText = convertView.findViewById(R.id.textDoctorDetails);
        Button consultButton = convertView.findViewById(R.id.btnConsult);

        HashMap<String, String> doctor = doctorList.get(position);

        nameText.setText(doctor.get("name"));
        detailsText.setText(doctor.get("details"));

        consultButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, ConsultNowActivity.class);
            intent.putExtra("name", doctor.get("name"));
            intent.putExtra("specialization", doctor.get("specialization"));
            intent.putExtra("email", doctor.get("email"));
            context.startActivity(intent);
        });

        return convertView;
    }
}
