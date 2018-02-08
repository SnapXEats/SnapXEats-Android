package com.snapxeats.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.snapxeats.ui.preferences.PreferenceActivity;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class LocationHelper {
    private static LocationManager mLocationManager;
     private Context mContext;
     private static Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    public LocationHelper(Context mContext) {
        this.mContext = mContext;
    }


    public static boolean checkPermission(Context context) {

        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION);
    }

    public static boolean isGpsEnabled(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }
}
