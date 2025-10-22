package com.example.virtualdoctor.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import com.example.virtualdoctor.broadcastReceiver.ReminderBroadcastReceiver;

public class ScheduleConsultationService extends IntentService {

    public static final String ACTION_SCHEDULE = "com.example.virtualdoctor.action.SCHEDULE";

    public ScheduleConsultationService() {
        super("ScheduleConsultationService");
    }

    public static void startActionSchedule(android.content.Context context, String mode, long reminderTimeMillis, String date, String time) {
        Intent intent = new Intent(context, ScheduleConsultationService.class);
        intent.setAction(ACTION_SCHEDULE);
        intent.putExtra("mode", mode);
        intent.putExtra("reminderTimeMillis", reminderTimeMillis);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && ACTION_SCHEDULE.equals(intent.getAction())) {
            String mode = intent.getStringExtra("mode");
            long reminderTimeMillis = intent.getLongExtra("reminderTimeMillis", -1);
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");

            if (reminderTimeMillis > System.currentTimeMillis()) {
                Intent reminderIntent = new Intent(this, ReminderBroadcastReceiver.class);
                reminderIntent.putExtra("mode", mode);
                reminderIntent.putExtra("date", date);
                reminderIntent.putExtra("time", time);

                int requestCode = (mode + date + time).hashCode();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this, requestCode, reminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (alarmManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (alarmManager.canScheduleExactAlarms()) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
                        } else {
                            // Can't prompt user here since no UI; you might notify via Notification if desired
                        }
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
                    }
                }
            }
        }
    }
}
