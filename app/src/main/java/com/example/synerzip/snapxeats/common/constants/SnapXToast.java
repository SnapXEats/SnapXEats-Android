package com.example.synerzip.snapxeats.common.constants;

import android.content.Context;
import android.util.Log;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class SnapXToast {
    public static final String LOG_TAG = "SnapXEats";

    /**
     * Method for showing toast
     *
     * @param context
     * @param toastMessage
     */
    public static void showToast(Context context, String toastMessage) {

        android.widget.Toast.makeText(context, toastMessage, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * show long toast
     *
     * @param context
     * @param toastMessage
     */
    public static void showLongToast(Context context, String toastMessage) {

        android.widget.Toast.makeText(context, toastMessage, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void info(String message) {
        Log.i(LOG_TAG, message);
    }

}
