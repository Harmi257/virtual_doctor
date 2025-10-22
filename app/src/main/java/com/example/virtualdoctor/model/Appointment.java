package com.example.virtualdoctor.model;

public class Appointment {
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private String symptoms;
    private String duration;
    private String severity;
    private String mode;
    private String prescription;

    // Add all necessary getters
    public String getPatientName() { return patientName; }
    public String getPatientEmail() { return patientEmail; }
    public String getPatientPhone() { return patientPhone; }
    public String getPatientAddress() { return patientAddress; }
    public String getSymptoms() { return symptoms; }
    public String getDuration() { return duration; }
    public String getSeverity() { return severity; }
    public String getMode() { return mode; }
    public String getPrescription() { return prescription; }
}

