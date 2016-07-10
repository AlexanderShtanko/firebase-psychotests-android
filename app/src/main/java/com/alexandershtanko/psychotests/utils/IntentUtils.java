package com.alexandershtanko.psychotests.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alexandershtanko.psychotests.BuildConfig;
import com.alexandershtanko.psychotests.R;

/**
 * Created by aleksandr on 10.07.16.
 */
public class IntentUtils {
    public static void rateTheApp(Context context) {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void shareTheApp(Context context)
    {
        try
        {
            final String appPackageName = BuildConfig.APPLICATION_ID;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            String sAux = "\nСсылка на приложение\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName +"\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, "Выберите"));
        }
        catch(Exception e)
        {
        }
    }
}
