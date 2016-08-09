package com.alexandershtanko.psychotests.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.alexandershtanko.psychotests.receivers.TODAlarmReceiver;

import java.util.Calendar;

/**
 * Created by aleksandr on 08.08.16.
 */
public class AlarmHelper {

    public static final int REQUEST_CODE = 143534;
    public static final String ALARM_SET = "ALARM_SET";
    public static final String PREFS_ALARM = "ALARM";

    public static void setTestOfDayAlarm(Context context) {
        if (!isAlarmSet(context)) {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            PendingIntent pendingIntent = getPendingIntent(context);


            Calendar firingCal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();

            firingCal.set(Calendar.HOUR_OF_DAY, 9);
            firingCal.set(Calendar.MINUTE, 0);
            firingCal.set(Calendar.SECOND, 0);

            long intendedTime = firingCal.getTimeInMillis();
            long currentTime = currentCal.getTimeInMillis();

            if (intendedTime >= currentTime) {

                manager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {

                firingCal.add(Calendar.DAY_OF_MONTH, 1);
                intendedTime = firingCal.getTimeInMillis();

                manager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            SharedPreferences prefs = context.getSharedPreferences(PREFS_ALARM, Context.MODE_PRIVATE);
            prefs.edit().putBoolean(ALARM_SET, true).apply();
        }

    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent alarmIntent = new Intent(context, TODAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static boolean isAlarmSet(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ALARM, Context.MODE_PRIVATE);
        return prefs.contains(ALARM_SET);
    }

    public static void unsetAlarm(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ALARM, Context.MODE_PRIVATE);
        if (prefs.contains(ALARM_SET))
            prefs.edit().remove(ALARM_SET).commit();

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(getPendingIntent(context));
    }


}
