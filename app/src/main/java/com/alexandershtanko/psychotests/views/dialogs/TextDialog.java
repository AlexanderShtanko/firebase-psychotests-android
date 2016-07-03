package com.alexandershtanko.psychotests.views.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.utils.ErrorUtils;

/**
 * Created by aleksandr on 03.07.16.
 */
public class TextDialog {

    public static final String TAG = TextDialog.class.getSimpleName();

    public static void show(Context context, String title, String text, Boolean showOkButton, Runnable onDismissListener) {
        show(context, title, text, showOkButton, false, null, onDismissListener);
    }

    public static void show(Context context, String title, String text, Boolean showOkButton, Boolean showCancelButton, Runnable onOkClickListener, Runnable onDismissListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(text).setCancelable(true);
        if (onDismissListener != null)
            alertDialogBuilder.setOnDismissListener(dialog -> onDismissListener.run());
        if (title != null)
            alertDialogBuilder.setTitle(title);

        if (showOkButton)
            alertDialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                if (onOkClickListener != null)
                    onOkClickListener.run();
                dialog.dismiss();
            });

        if (showCancelButton)
            alertDialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }
}



