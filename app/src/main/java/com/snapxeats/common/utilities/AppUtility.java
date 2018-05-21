package com.snapxeats.common.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.foodGestures.DaoSession;
import com.snapxeats.common.model.location.Location;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import com.snapxeats.network.LocationHelper;
import com.snapxeats.ui.home.fragment.snapnshare.ViewPagerAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.snapxeats.common.constants.UIConstants.MARGIN;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Singleton
public class AppUtility {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private DaoSession daoSession;
    private SnapxDataDao snapxDataDao;
    private SnapxData snapxData;
    private ProgressDialog mDialog;
    private int dotsCount;
    private ImageView[] dots;

    @Inject
    public AppUtility() {
    }

    @Inject
    SnapXDialog snapXDialog;

    public void setContext(Context context) {
        this.mContext = context;
        snapXDialog.setContext((Activity) context);
        daoSession = ((SnapXApplication) context.getApplicationContext()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();
        if (snapxDataDao.loadAll() != null && snapxDataDao.loadAll().size() > 0) {
            snapxData = snapxDataDao.loadAll().get(0);
        }
    }

    public SharedPreferences getSharedPreferences() {
        preferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_name),
                mContext.MODE_PRIVATE);
        return preferences;
    }

    public void saveObjectInPref(Location location, String key) {
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        editor.putString(key, json);
        editor.apply();
    }

    public String getAuthToken(final Context context) {
        String token = null;
        SnapXApplication app = (SnapXApplication) context.getApplicationContext();
        if (null != app) {
            token = app.getToken();
            if (null != token && !token.isEmpty()) {
                return String.format("Bearer %s", token);
            } else {
                //TODO: fetch it from DB, assign it to app.token & return that token
                if (null != snapxData) {
                    token = snapxData.getToken(); // fetch it from DB
                    app.setToken(token);
                }
                return String.format("Bearer %s", token);
            }
        }
        return token;
    }

    public void setAuthToken(final Context context) {
        String token = null;
        SnapXApplication app = (SnapXApplication) context.getApplicationContext();
        if (null != app) {
            token = app.getToken();
            if (null != token && !token.isEmpty()) {
                //TODO: fetch it from DB, assign it to app.token & return that token
                if (snapxData != null && !snapxData.getToken().isEmpty()) {
                    token = snapxData.getToken(); // fetch it from DB
                }
                app.setToken(token);
            }
        }
    }

    public void hideKeyboard() {
        View view = ((Activity) mContext).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean checkPermissions() {
        //Check device level location permission
        if (LocationHelper.isGpsEnabled(mContext)) {
            if (LocationHelper.checkPermission(mContext)) {
                LocationHelper.requestPermission((Activity) mContext);
            } else if (NetworkUtility.isNetworkAvailable(mContext)) {
                return true;
            } else {
                snapXDialog.showNetworkErrorDialog((dialog, which) -> {
                });
                return false;
            }
        } else {
            checkGpsPermission();
        }
        return false;
    }

    /**
     * Show Enable Gps Permission dialog
     */
    public void checkGpsPermission() {
        if (!LocationHelper.isGpsEnabled(mContext)) {
            snapXDialog.showGpsPermissionDialog();
        }
    }

    public android.location.Location getLocation() {
        android.location.Location mCurrentLocation = null;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        LocationHelper.checkPermission(mContext);

        // Getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Getting passive status
        boolean isPassiveEnabled = locationManager
                .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

        // Getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (isPassiveEnabled) {
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        } else if (isNetworkEnabled) {
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else if (isGPSEnabled) {
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (mCurrentLocation != null) {
            double lat = mCurrentLocation.getLatitude();
            double lng = mCurrentLocation.getLongitude();
        }

        return mCurrentLocation;
    }

    public String getPlaceName(android.location.Location location) {
        String placeName = "";
        Address locationAddress = getAddress(location.getLatitude(), location.getLongitude());

        if (locationAddress != null) {

            if (locationAddress.getSubLocality() != null) {
                placeName = locationAddress.getSubLocality();
            } else if (locationAddress.getThoroughfare() != null) {
                placeName = locationAddress.getThoroughfare();
            }
        }
        return placeName;
    }

    private Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences();
        String serverUserId = preferences.getString(mContext.getString(R.string.user_id), "");
        return !serverUserId.isEmpty();
    }

    /**
     * view pager for restaurant images
     *
     * @param restaurant_pics
     * @param viewPager
     * @param layout
     */
    public void setViewPager(List<RestaurantPics> restaurant_pics, ViewPager viewPager, LinearLayout layout) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), restaurant_pics);
        viewPager.setAdapter(viewPagerAdapter);
        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int index = ZERO; index < dotsCount; index++) {
            dots[index] = new ImageView(getApplicationContext());
            dots[index].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(MARGIN, ZERO, MARGIN, ZERO);
            layout.addView(dots[index], params);
        }
        dots[ZERO].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }

    /**
     * Set corousal dots
     *
     * @param viewPager
     */
    public void setImagesCorousal(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int index = ZERO; index < dotsCount; index++) {
                    dots[index].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
