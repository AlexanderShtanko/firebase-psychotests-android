package com.alexandershtanko.psychotests.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexandershtanko.psychotests.helpers.AlarmHelper;

/**
 * Created by aleksandr on 07.08.16.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmHelper.setTestOfDayAlarm(context);
    }

}
