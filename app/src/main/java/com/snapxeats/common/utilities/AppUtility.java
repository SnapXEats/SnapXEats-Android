package com.snapxeats.common.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Pair;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.network.NetworkHelper;
import com.snapxeats.ui.preferences.PreferenceContract;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Singleton
public class AppUtility {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;


    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    @Inject
    public AppUtility() {
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public SharedPreferences getSharedPreferences() {
        preferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_name), mContext.MODE_PRIVATE);
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        editor = preferences.edit();
        return editor;
    }

    /**
     * Get user current location
     */

    public Location getLocation() {

        if (mContext != null &&
                NetworkUtility.isNetworkAvailable(mContext)) {
            mCurrentLocation = locationHelper();

        } /*else {
            preferencePresenter.response(SnapXResult.NONETWORK);
        }*/
        return mCurrentLocation;
    }

    private Location locationHelper() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        createLocationRequest();

        mCurrentLocation = createLocationCallback(mContext);

        if (mFusedLocationClient != null && !NetworkHelper.checkPermission(mContext)) {
//            preferenceView.showProgressDialog();
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());

        }
        return mCurrentLocation;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private Location createLocationCallback(Context context) {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

//                String place = getPlaceName(mCurrentLocation);

//                preferencePresenter.updatePlace(place, mCurrentLocation);

            }
        };
        return mCurrentLocation;
    }

    public String getPlaceName(Location mCurrentLocation) {
        String place = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.get(0).getSubLocality() != null) {
                place = addresses.get(0).getSubLocality();

            } else if (addresses.get(0).getThoroughfare() != null) {
                place = addresses.get(0).getThoroughfare();
            }
        }
        SnapXToast.debug("Address: AppUtility " + place);
        return place;
    }
}
