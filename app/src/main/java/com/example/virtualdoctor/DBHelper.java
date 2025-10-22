// 1. DBHelper.java

package com.example.virtualdoctor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "virtual_doctor.db";
    public static final int DB_VERSION = 3;  // incremented from 2 to 3
    private static final String TABLE_APPOINTMENTS = "appointments";
   //email added
    private String currentDoctorEmail;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "role TEXT)";
        db.execSQL(createUserTable);

        db.execSQL("CREATE TABLE IF NOT EXISTS user_details (" +
                "user_id INTEGER PRIMARY KEY, " +
                "full_name TEXT, date_of_birth TEXT, email TEXT, phone TEXT, address TEXT, " +
                "height INTEGER, weight INTEGER, blood_group TEXT, " +
                "existing_conditions TEXT, past_surgeries TEXT, current_medications TEXT, " +
                "allergies TEXT, family_history TEXT, smoker INTEGER, drinker INTEGER)");

        String createDoctorTable = "CREATE TABLE IF NOT EXISTS doctors (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "specialization TEXT, " +
                "licenseNumber TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "hospital TEXT, " +
                "experience INTEGER, " +
                "latitude REAL, " +
                "longitude REAL)";
        db.execSQL(createDoctorTable);

        db.execSQL("CREATE TABLE IF NOT EXISTS appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "doctor_name TEXT, " +
                "specialization TEXT, " +
                "email TEXT, " +
                "patient_email TEXT," +
                "symptoms TEXT, " +
                "duration TEXT, " +
                "triggers TEXT, " +
                "previous_treatments TEXT, " +
                "severity TEXT, " +
                "mode TEXT," +
                "status TEXT DEFAULT 'pending')");  // <-- add this line

//        db.execSQL("CREATE TABLE schedule_meeting (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "useremail TEXT, " +
//                "doctoremail TEXT, " +
//                "consultation_mode TEXT, " +
//                "consultation_date TEXT, " +
//                "prescription TEXT)");
//
        String createConsultationTable = "CREATE TABLE IF NOT EXISTS consultation_details (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userEmail TEXT, " +
                "doctorEmail TEXT, " +
                "mode TEXT, " +
                "gpayId TEXT, " +
                "date TEXT, " +
                "prescription TEXT, " +
                "meetLink TEXT, " +
                "location TEXT" +
                ")";
        db.execSQL(createConsultationTable);

    }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS user_details");
            db.execSQL("DROP TABLE IF EXISTS doctors");
            db.execSQL("DROP TABLE IF EXISTS appointments");
            db.execSQL("DROP TABLE IF EXISTS consultation_details");  // ✅ Add this line
            onCreate(db);
        }

    //NEWLY ADDEd
    public void setCurrentDoctorEmail(String email) {
        this.currentDoctorEmail = email;
    }

    public String getCurrentDoctorEmail() {
        return currentDoctorEmail;
    }


    public boolean registerUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("role", role);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
    }

    public boolean saveUserDetails(int userId, String name, String dob, String email, String phone, String address,
                                   int height, int weight, String bloodGroup, String conditions,
                                   String surgeries, String meds, String allergies,
                                   String familyHistory, boolean smoker, boolean drinker) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("full_name", name);
        values.put("date_of_birth", dob);
        values.put("email", email);
        values.put("phone", phone);
        values.put("address", address);
        values.put("height", height);
        values.put("weight", weight);
        values.put("blood_group", bloodGroup);
        values.put("existing_conditions", conditions);
        values.put("past_surgeries", surgeries);
        values.put("current_medications", meds);
        values.put("allergies", allergies);
        values.put("family_history", familyHistory);
        values.put("smoker", smoker ? 1 : 0);
        values.put("drinker", drinker ? 1 : 0);

        long result = db.insertWithOnConflict("user_details", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public HashMap<String, String> getUserDetailsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> userDetails = new HashMap<>();

        String query = "SELECT * FROM user_details WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            userDetails.put("user_id", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))));
            userDetails.put("full_name", cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            userDetails.put("date_of_birth", cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth")));
            userDetails.put("email", cursor.getString(cursor.getColumnIndexOrThrow("email")));
            userDetails.put("phone", cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            userDetails.put("address", cursor.getString(cursor.getColumnIndexOrThrow("address")));
            userDetails.put("height", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("height"))));
            userDetails.put("weight", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("weight"))));
            userDetails.put("blood_group", cursor.getString(cursor.getColumnIndexOrThrow("blood_group")));
            userDetails.put("existing_conditions", cursor.getString(cursor.getColumnIndexOrThrow("existing_conditions")));
            userDetails.put("past_surgeries", cursor.getString(cursor.getColumnIndexOrThrow("past_surgeries")));
            userDetails.put("current_medications", cursor.getString(cursor.getColumnIndexOrThrow("current_medications")));
            userDetails.put("allergies", cursor.getString(cursor.getColumnIndexOrThrow("allergies")));
            userDetails.put("family_history", cursor.getString(cursor.getColumnIndexOrThrow("family_history")));
            userDetails.put("smoker", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("smoker"))));
            userDetails.put("drinker", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("drinker"))));
            cursor.close();
        }

        return userDetails;
    }

    public Cursor getDoctorLocationByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT latitude, longitude FROM doctors WHERE email = ?", new String[]{email});
    }


    // Updates user profile details by user_id
    public boolean updateUserDetails(int user_id, String name, String dob, String phone, String address,
                                     int height, int weight, String bloodGroup, String conditions,
                                     String surgeries, String meds, String allergies,
                                     String familyHistory, boolean smoker, boolean drinker) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("full_name", name);
        values.put("date_of_birth", dob);
        values.put("phone", phone);
        values.put("address", address);
        values.put("height", height);
        values.put("weight", weight);
        values.put("blood_group", bloodGroup);
        values.put("existing_conditions", conditions);
        values.put("past_surgeries", surgeries);
        values.put("current_medications", meds);
        values.put("allergies", allergies);
        values.put("family_history", familyHistory);
        values.put("smoker", smoker ? 1 : 0);
        values.put("drinker", drinker ? 1 : 0);

        int rows = db.update("user_details", values, "user_id = ?", new String[]{String.valueOf(user_id)});
        return rows > 0;
    }

    public boolean saveDoctorDetails(int doctorId, String name, String specialization,
                                     String licenseNumber, String email, String phone,
                                     String hospital, int experience, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", doctorId);
        values.put("name", name);
        values.put("specialization", specialization);
        values.put("licenseNumber", licenseNumber);
        values.put("email", email);
        values.put("phone", phone);
        values.put("hospital", hospital);
        values.put("experience", experience);
        values.put("latitude", latitude);
        values.put("longitude", longitude);

        long result = db.insertWithOnConflict("doctors", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public ArrayList<String> getAllUniqueValues(String columnName) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT " + columnName + " FROM doctors", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }


    public Cursor getDoctorsByFilters(String specialization, String hospital) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM doctors WHERE specialization = ? AND hospital = ?",
                new String[]{specialization, hospital});
    }

    public Cursor getDoctorById(int doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM doctors WHERE id = ?", new String[]{String.valueOf(doctorId)});
    }

    // Checks if doctor details exist for the given doctor ID
    public boolean isDoctorDetailsFilled(int doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM doctors WHERE id = ?", new String[]{String.valueOf(doctorId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Checks if user details exist for the given user ID
    public boolean isUserDetailsFilled(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_details WHERE user_id = ?", new String[]{String.valueOf(userId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Insert appointment -new one
    public boolean insertAppointment(String doctorName, String specialization, String doctorEmail, String patientEmail,
                                     String symptoms, String duration, String triggers,
                                     String previousTreatments, String severity, String mode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("doctor_name", doctorName);
        cv.put("specialization", specialization);
        cv.put("email", doctorEmail);
        cv.put("patient_email", patientEmail);
        cv.put("symptoms", symptoms);
        cv.put("duration", duration);
        cv.put("triggers", triggers);
        cv.put("previous_treatments", previousTreatments);
        cv.put("severity", severity);
        cv.put("mode", mode);
        cv.put("status", "pending");  // new field

        long result = db.insert(TABLE_APPOINTMENTS, null, cv);
        return result != -1;
    }



    public Cursor getAppointmentsByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM appointments WHERE email = ?", new String[]{email});
    }


    public Cursor getAppointmentsForDoctor(String doctorEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        //new query to fetch status also
        String query = "SELECT a.id AS _id, a.symptoms, a.duration, a.severity, a.mode, a.status, " +
                "u.full_name AS patient_name, u.email AS patient_email, u.phone AS patient_phone, u.address AS patient_address " +
                "FROM appointments a " +
                "JOIN user_details u ON a.patient_email = u.email " +
                "WHERE a.email = ?";


        return db.rawQuery(query, new String[]{doctorEmail});
    }
    //to update status
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", status);  // "accepted" or "rejected"

        int rows = db.update(TABLE_APPOINTMENTS, cv, "id=?", new String[]{String.valueOf(appointmentId)});
        return rows > 0;
    }

    public boolean insertConsultationDetails(String userEmail, String doctorEmail, String mode,
                                             String gpayId, String date, String prescription,
                                             String meetLink, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userEmail", userEmail);
        values.put("doctorEmail", doctorEmail);
        values.put("mode", mode);
        values.put("gpayId", gpayId);
        values.put("date", date);

        // Optional fields (nullable)
        if (prescription != null) {
            values.put("prescription", prescription);
        }
        if (meetLink != null) {
            values.put("meetLink", meetLink);
        }
        if (location != null) {
            values.put("location", location);
        }

        long result = db.insert("consultation_details", null, values);
        return result != -1;
    }
    public ArrayList<HashMap<String, String>> getUserConsultationDetails(String userEmail) {
        ArrayList<HashMap<String, String>> consultationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT doctorEmail, date, mode FROM consultation_details WHERE userEmail = ?",
                new String[]{userEmail}
        );

        if (cursor.moveToFirst()) {
            do {
                String docEmail = cursor.getString(cursor.getColumnIndexOrThrow("doctorEmail"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String mode = cursor.getString(cursor.getColumnIndexOrThrow("mode"));
                Log.d("DBHelper", "Found consultation - Doctor: " + docEmail + ", Date: " + date + ", Mode: " + mode);
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHelper", "No consultations found for userEmail: " + userEmail);
        }
        cursor.close();

        db.close();

        return consultationList;
    }

    public ArrayList<HashMap<String, String>> getAppointmentsForUser(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> bookings = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM appointments WHERE patient_email = ?", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> booking = new HashMap<>();
                booking.put("doctor_name", cursor.getString(cursor.getColumnIndexOrThrow("doctor_name")));
                booking.put("specialization", cursor.getString(cursor.getColumnIndexOrThrow("specialization")));
                booking.put("symptoms", cursor.getString(cursor.getColumnIndexOrThrow("symptoms")));
                booking.put("duration", cursor.getString(cursor.getColumnIndexOrThrow("duration")));
                booking.put("triggers", cursor.getString(cursor.getColumnIndexOrThrow("triggers")));
                booking.put("previous_treatments", cursor.getString(cursor.getColumnIndexOrThrow("previous_treatments")));
                booking.put("severity", cursor.getString(cursor.getColumnIndexOrThrow("severity")));
                booking.put("mode", cursor.getString(cursor.getColumnIndexOrThrow("mode")));
                booking.put("status", cursor.getString(cursor.getColumnIndexOrThrow("status")));
                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookings;
    }


//    public boolean insertConsultationDetails(String userEmail, String doctorEmail, String mode,
//                                             String gpayId, String date, String prescription,
//                                             String meetLink, String location) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put("user_email", userEmail);
//        values.put("doctor_email", doctorEmail);
//        values.put("mode", mode);
//        values.put("consultation_date", date);
//        values.put("gpay_id", gpayId);
//
//        if (prescription != null && !prescription.trim().isEmpty()) {
//            values.put("prescription", prescription);
//        }
//        if (meetLink != null && !meetLink.trim().isEmpty()) {
//            values.put("meet_link", meetLink);
//        }
//        if (location != null && !location.trim().isEmpty()) {
//            values.put("location", location);
//        }
//
//        long result = db.insert("ConsultationDetails", null, values);
//        return result != -1;
//    }


//    public Cursor getConsultationDetailsForUser(String userEmail) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT * FROM consultation_details WHERE userEmail = ?", new String[]{userEmail});
//    }


//    public boolean scheduleMeeting(String userEmail, String doctorEmail, String consultationMode, String consultationDate, String prescription) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("useremail", userEmail);
//        cv.put("doctoremail", doctorEmail);
//        cv.put("consultation_mode", consultationMode);
//        cv.put("consultation_date", consultationDate);
//        cv.put("prescription", prescription);
//
//        long result = db.insert("schedule_meeting", null, cv);
//        return result != -1;
//    }

}