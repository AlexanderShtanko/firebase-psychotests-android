package com.alexandershtanko.psychotests.utils;


import android.util.Log;

import rx.functions.Action1;

/**
 * Created by aleksandr on 17.06.16.
 */
public class ErrorUtils {
    public static Action1<Throwable> onError() {
        return throwable -> {
            Log.e("ErrorUtils","",throwable);
        };
    }
}
