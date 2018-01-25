package com.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import com.snapxeats.network.NetworkHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class PreferenceInteractor {

    private PreferenceContract.PreferencePresenter preferencePresenter;
    private Activity mContext;

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
    *  The desired interval for location updates. Inexact. Updates may be more or less frequent.
    */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    @Inject
    public PreferenceInteractor() {

    }

    public void setPreferencePresenter(PreferenceContract.PreferencePresenter presenter) {
        this.preferencePresenter = presenter;
    }

    private void locationHelper(PreferenceContract.PreferenceView preferenceView) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        createLocationRequest();

        createLocationCallback(mContext);

        if (mFusedLocationClient != null && !NetworkHelper.checkPermission(mContext)) {
            preferenceView.showProgressDialog();
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());

        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
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
    private void createLocationCallback(Context context) {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String place = "";
                if (addresses != null) {
                    if (addresses.get(0).getSubLocality() != null) {
                        place = addresses.get(0).getSubLocality();

                    } else if (addresses.get(0).getThoroughfare() != null) {
                        place = addresses.get(0).getThoroughfare();
                    }
                }
                preferencePresenter.updatePlace(place, mCurrentLocation);

                SnapXToast.debug("Address: PreferenceInteractor " + place);
            }
        };
    }

    /**
     * Get user current location
     */

    public void getLocation(PreferenceContract.PreferenceView preferenceView) {
        mContext = preferenceView.getActivity();
        if (mContext != null &&
                NetworkUtility.isNetworkAvailable(mContext)) {
            locationHelper(preferenceView);

        } else {
            preferencePresenter.response(SnapXResult.NONETWORK);
        }
    }

    /**
     * get cuisines list
     */
    public void getCuisineList() {

        ApiHelper apiHelper = ApiClient.getClient(mContext).create(ApiHelper.class);
        Call<RootCuisine> listCuisineCall = apiHelper.getCuisineList();
        listCuisineCall.enqueue(new Callback<RootCuisine>() {
            @Override
            public void onResponse(Call<RootCuisine> call, Response<RootCuisine> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null) {
                     RootCuisine rootCuisine=response.body();
                     preferencePresenter.setCuisineList(rootCuisine);
                    }
                }
            }

            @Override
            public void onFailure(Call<RootCuisine> call, Throwable t) {
                preferencePresenter.response(SnapXResult.ERROR);

            }
        });
    }

}
