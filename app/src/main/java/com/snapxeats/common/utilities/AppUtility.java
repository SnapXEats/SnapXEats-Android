package com.snapxeats.common.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pkmmte.view.CircularImageView;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.location.Location;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import com.snapxeats.common.model.smartphotos.DaoSession;
import com.snapxeats.network.LocationHelper;
import com.snapxeats.ui.home.fragment.snapnshare.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.snapxeats.common.constants.UIConstants.MARGIN;
import static com.snapxeats.common.constants.UIConstants.MILLIES_TWO;
import static com.snapxeats.common.constants.UIConstants.MILLIS;
import static com.snapxeats.common.constants.UIConstants.MILLI_TO_SEC_CONVERSION;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.TEN;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Singleton
public class AppUtility {
    private SharedPreferences preferences;
    private Context mContext;
    private SnapXData snapXData;
    private int dotsCount;
    private ImageView[] dots;
    private CircularImageView imgUser;
    private TextView txtUserName;
    private TextView txtNotLoggedIn;
    private LinearLayout mLayoutUserData;

    @Inject
    DbHelper dbHelper;

    @Inject
    public AppUtility() {
    }

    @Inject
    SnapXDialog snapXDialog;

    public void setContext(Context context) {
        this.mContext = context;
        snapXDialog.setContext((Activity) context);
        DaoSession daoSession = ((SnapXApplication) context.getApplicationContext()).getDaoSession();
        SnapXDataDao snapxDataDao = daoSession.getSnapXDataDao();
        if (null != snapxDataDao.loadAll() && ZERO < snapxDataDao.loadAll().size()) {
            snapXData = snapxDataDao.loadAll().get(ZERO);
        }
    }

    public SharedPreferences getSharedPreferences() {
        preferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_name),
                Context.MODE_PRIVATE);
        return preferences;
    }

    public void saveObjectInPref(Location location, String key) {
        SharedPreferences.Editor editor = preferences.edit();
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
                if (null != snapXData) {
                    token = snapXData.getToken(); // fetch it from DB
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
                if (null != snapXData && !snapXData.getToken().isEmpty()) {
                    token = snapXData.getToken(); // fetch it from DB
                }
                app.setToken(token);
            }
        }
    }

    public void hideKeyboard() {
        View view = ((Activity) mContext).getCurrentFocus();
        if (null != view) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert null != imm;
            imm.hideSoftInputFromWindow(view.getWindowToken(), ZERO);
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

        if (null != mCurrentLocation) {
            double lat = mCurrentLocation.getLatitude();
            double lng = mCurrentLocation.getLongitude();
        }

        return mCurrentLocation;
    }

    public String getPlaceName(android.location.Location location) {
        String placeName = "";
        Address locationAddress = getAddress(location.getLatitude(), location.getLongitude());

        if (null != locationAddress) {

            if (null != locationAddress.getSubLocality()) {
                placeName = locationAddress.getSubLocality();
            } else if (null != locationAddress.getThoroughfare()) {
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
            addresses = geocoder.getFromLocation(latitude, longitude, ONE);
            return addresses.get(ZERO);

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

    /**
     * Get file real path from URI
     */
    public String getRealPathFromURIPath(Uri contentURI, Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (null == cursor) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    /**
     * Converts milliseconds to seconds
     */
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString;
        String secondsString;

        // Convert total duration into time
        int seconds = (int) ((milliseconds % (MILLIS)) % (MILLIES_TWO) / MILLI_TO_SEC_CONVERSION);

        // Prepending 0 to seconds if it is one digit
        if (TEN > seconds) {
            secondsString = ZERO + "" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = mContext.getString(R.string.str_timer) + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Reset media player
     */
    public void resetMediaPlayer(MediaPlayer mediaPlayer) {
        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    /**
     * initialize navigation drawer views
     */
    public void initNavHeaderViews(NavigationView mNavigationView) {
        View mNavHeader = mNavigationView.getHeaderView(ZERO);
        imgUser = mNavHeader.findViewById(R.id.img_user);
        txtUserName = mNavHeader.findViewById(R.id.txt_user_name);
        txtNotLoggedIn = mNavHeader.findViewById(R.id.txt_nav_not_logged_in);
        mLayoutUserData = mNavHeader.findViewById(R.id.layout_user_data);
    }

    /**
     * set user info in navigation drawer
     */
    public void setUserInfo(NavigationView mNavigationView) {
        initNavHeaderViews(mNavigationView);
        Menu menu = mNavigationView.getMenu();
        MenuItem menuLogoutItem = menu.findItem(R.id.nav_logout);
        List<SnapXData> data = dbHelper.getSnapxDataDao().loadAll();
        if (null != snapXData && ZERO < data.size()) {
            txtNotLoggedIn.setVisibility(View.GONE);
            mLayoutUserData.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(data.get(ZERO).getImageUrl()).placeholder(R.drawable.user_image).into(imgUser);
            txtUserName.setText(data.get(ZERO).getUserName());
            menuLogoutItem.setTitle(mContext.getString(R.string.log_out));
        } else {
            mLayoutUserData.setVisibility(View.GONE);
            txtNotLoggedIn.setVisibility(View.VISIBLE);
            if (!isLoggedIn()) {
                menuLogoutItem.setTitle(mContext.getString(R.string.log_in));
            }
        }
    }
}
