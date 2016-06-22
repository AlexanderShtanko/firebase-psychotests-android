package com.alexandershtanko.psychotests.utils;

import android.content.Context;

/**
 * Created by aleksandr on 25.11.15.
 */
public class DisplayUtils {
    public static int getSizeInPx(Context context,int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)((float)dp*density);
    }
}
