package com.alexandershtanko.psychotests.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alexandershtanko.psychotests.receivers.TestOfDayAlarmReceiver;

import java.util.Calendar;

/**
 * Created by aleksandr on 08.08.16.
 */
public class AlarmHelper {

    public static void setTestOfDayAlarm(Context context)
    {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);


        Intent alarmIntent = new Intent(context, TestOfDayAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 143532, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
        manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
