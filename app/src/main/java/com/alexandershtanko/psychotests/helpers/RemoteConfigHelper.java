package com.alexandershtanko.psychotests.helpers;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexander on 18/08/16.
 */
public class RemoteConfigHelper {
    public static final String TOD_NOTIFICATION = "tod_notification";

    public static void updateValues() {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put(TOD_NOTIFICATION, true);
        config.setDefaults(map);
        config.fetch().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                config.activateFetched();
        });
    }

    public static boolean isNeedShowTODNotification() {


        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        boolean showNotification = true;

        FirebaseRemoteConfigValue value = config.getValue(TOD_NOTIFICATION);
        if (value != null) {
            showNotification = value.asBoolean();
        }


        return showNotification;

    }
}
