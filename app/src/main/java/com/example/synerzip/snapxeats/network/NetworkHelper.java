package com.example.synerzip.snapxeats.network;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.example.synerzip.snapxeats.ui.preferences.PreferenceActivity;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class NetworkHelper {
    public static boolean checkPermission(Context context) {

        return  ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION);
    }
}
