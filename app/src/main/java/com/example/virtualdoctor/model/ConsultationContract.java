package com.example.virtualdoctor.model;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ConsultationContract {
    private ConsultationContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.virtualdoctor.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CONSULTATIONS = "consultations";

    public static class ConsultationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONSULTATIONS);

        public static final String TABLE_NAME = "consultations";

        public static final String COLUMN_PATIENT_NAME = "patientName";
        public static final String COLUMN_DOCTOR_NAME = "doctorName";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_REASON = "reason";
    }
}

