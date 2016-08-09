package com.alexandershtanko.psychotests.views.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.utils.ErrorUtils;

/**
 * Created by aleksandr on 21.01.16.
 */
public class YesNoDialog {
    public static void show(Context context, String text, DialogInterface.OnClickListener onOkClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(text);
        alertDialogBuilder.setPositiveButton(R.string.yes, onOkClickListener);
        alertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            ErrorUtils.log("YesNoDialog",e);
        }
    }

    public static void show(Context context, int msgRes, int yesRes, int noRes, DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
        show(context, -1, msgRes, yesRes, noRes, onYesListener, onNoListener);
    }

    public static void show(Context context, int titleRes, int msgRes, int yesRes, int noRes, DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msgRes);
        if (titleRes != -1)
            alertDialogBuilder.setTitle(titleRes);
        alertDialogBuilder.setPositiveButton(yesRes, onYesListener);
        alertDialogBuilder.setNegativeButton(noRes, onNoListener);
        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            ErrorUtils.log("YesNoDialog",e);
        }
    }
}
