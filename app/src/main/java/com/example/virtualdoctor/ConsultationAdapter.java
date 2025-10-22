//package com.example.virtualdoctor;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class ConsultationAdapter extends BaseAdapter {
//
//    Context context;
//    ArrayList<HashMap<String, String>> consultations;
//    LayoutInflater inflater;
//
//    public ConsultationAdapter(Context context, ArrayList<HashMap<String, String>> consultations) {
//        this.context = context;
//        this.consultations = consultations;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return consultations.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return consultations.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    static class ViewHolder {
//        TextView doctorEmail, mode, date, info;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_consultation, parent, false);
//            holder = new ViewHolder();
//            holder.doctorEmail = convertView.findViewById(R.id.tvDoctorEmail);
//            holder.mode = convertView.findViewById(R.id.tvMode);
//            holder.date = convertView.findViewById(R.id.tvDate);
//            holder.info = convertView.findViewById(R.id.tvInfo);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        HashMap<String, String> map = consultations.get(position);
//        holder.doctorEmail.setText("Doctor: " + map.get("doctorEmail"));
//        holder.mode.setText("Mode: " + map.get("mode"));
//        holder.date.setText("Date: " + map.get("date"));
//        holder.info.setText(map.get("info"));
//
//        return convertView;
//    }
//}
