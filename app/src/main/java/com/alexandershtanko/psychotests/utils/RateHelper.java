package com.alexandershtanko.psychotests.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alexandershtanko.psychotests.BuildConfig;
import com.alexandershtanko.psychotests.R;

/**
 * Created by aleksandr on 10.07.16.
 */
public class RateHelper {
    public static void rateTheApp(Context context) {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void shareTheApp(Context context) {
        try {
            final String appPackageName = BuildConfig.APPLICATION_ID;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            String sAux = context.getString(R.string.app_link);
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, context.getString(R.string.select)));
        } catch (Exception e) {
        }
    }


    public static void shareTheResult(Context context, String title, String desc) {
        try {
            final String appPackageName = BuildConfig.APPLICATION_ID;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));

            String sAux = "\n" +
                    title + "\n"
                    + desc + "\n\n" +
                    context.getString(R.string.app_link)+"\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, context.getString(R.string.select)));
        } catch (Exception e) {
        }
    }

    public static void feedback(Context context, String email) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_review)+context.getString(R.string.app_name));
        i.putExtra(Intent.EXTRA_TEXT   , context.getString(R.string.negative_review));
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.send_review)));
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }
}
