package com.example.synerzip.snapxeats.common.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class NetworkUtility {
    /**
     * checks for network availability
     *
     * @return boolean variable indicating the status
     */
    public static  boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }
}
