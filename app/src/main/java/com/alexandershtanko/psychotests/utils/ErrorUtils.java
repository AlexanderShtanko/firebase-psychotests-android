package com.alexandershtanko.psychotests.utils;


import android.support.design.BuildConfig;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import rx.functions.Action1;

/**
 * Created by aleksandr on 17.06.16.
 */
public class ErrorUtils {
    public static Action1<Throwable> onError() {
        return throwable -> log("ErrorUtils", throwable);
    }

    public static void log(String tag, Throwable e) {
        if (BuildConfig.DEBUG)
            Log.e(tag, "", e);
        FirebaseCrash.report(e);
    }
}
