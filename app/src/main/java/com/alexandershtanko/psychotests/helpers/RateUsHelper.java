package com.alexandershtanko.psychotests.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.alexandershtanko.psychotests.BuildConfig;
import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.views.dialogs.YesNoDialog;

/**
 * Created by alexander on 09/08/16.
 */
public class RateUsHelper {
    private static final String PREFS_RATE_US = "Rate Us";
    private static final String DO_NOT_SHOW_AGAIN = "Do not show again";
    private static boolean doNotShowInThisSession = false;

    public static void doYouLikeApplication(Context context) {
        openDialog(context, R.string.text_rate_us_like_question, R.string.yes, R.string.no,
                (dialog, which) -> {
                    AmplitudeHelper.onRateUsResult(true);
                    rateUsInGooglePlay(context);
                },
                (dialog, which) -> {
                    AmplitudeHelper.onRateUsResult(false);
                    helpUs(context);
                }
        );

        AmplitudeHelper.onRateUsOpen();

    }


    public static void rateUsInGooglePlay(Context context) {
        openDialog(context, R.string.text_rate_us_google_play_question, R.string.want, R.string.next_time,
                (dialog, which) -> {
                    doNotShowAgain(context);
                    openGooglePlay(context);
                },
                (dialog, which) -> {
                    doNotShowInThisSession();
                }
        );

    }

    public static void helpUs(Context context) {
        openDialog(context, R.string.text_rate_us_help_question, R.string.give_feedback, R.string.no_thanks,
                (dialog, which) -> {
                    doNotShowAgain(context);
                    openFeedbackDialog(context);
                },
                (dialog, which) -> {
                    doNotShowAgain(context);
                }
        );

    }

    public static void doNotShowAgain(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_RATE_US, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(DO_NOT_SHOW_AGAIN, true).apply();
    }

    public static void openFeedbackDialog(Context context) {
        feedback(context,context.getString(R.string.developer_email));
    }


    private static void openDialog(Context context, int msgRes, int yesRes, int noRes, DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
        YesNoDialog.show(context, msgRes, yesRes, noRes, onYesListener, onNoListener);
    }

    public static void openGooglePlay(Context context) {
        String appPackageName = BuildConfig.APPLICATION_ID;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public synchronized static void doNotShowInThisSession() {
        doNotShowInThisSession = true;
    }

    public static Boolean needShowRateUs(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_RATE_US, Context.MODE_PRIVATE);

        return !prefs.contains(DO_NOT_SHOW_AGAIN) && !doNotShowInThisSession;
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
