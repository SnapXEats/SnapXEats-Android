package com.snapxeats.common.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.pkmmte.view.CircularImageView;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.UserReward;
import com.snapxeats.common.model.location.Location;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import com.snapxeats.network.LocationHelper;
import com.snapxeats.ui.home.fragment.checkin.CheckInData;
import com.snapxeats.ui.home.fragment.checkin.CheckInDataDao;
import com.snapxeats.ui.home.fragment.checkin.DaoSession;
import com.snapxeats.ui.home.fragment.snapnshare.SnapNotificationReceiver;
import com.snapxeats.ui.home.fragment.snapnshare.ViewPagerAdapter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.snapxeats.common.constants.UIConstants.CHECKOUT_DURATION;
import static com.snapxeats.common.constants.UIConstants.MARGIN;
import static com.snapxeats.common.constants.UIConstants.MILLIES_TWO;
import static com.snapxeats.common.constants.UIConstants.MILLIS;
import static com.snapxeats.common.constants.UIConstants.MILLI_TO_SEC_CONVERSION;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.SECONDS;
import static com.snapxeats.common.constants.UIConstants.TEN;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
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
    private TextView mTxtRewardPoints;
    private TextView txtNotLoggedIn;
    private LinearLayout mLayoutUserData;
    private CheckInData checkInData;
    private MenuItem snapNShareMenu, foodJourneyMenu, checkInMenu, menuLogoutItem;

    @Inject
    RootUserPreference rootUserPreference;

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
        CheckInDataDao checkInDataDao = daoSession.getCheckInDataDao();
        if (null != snapxDataDao.loadAll() && ZERO < snapxDataDao.loadAll().size()) {
            snapXData = snapxDataDao.loadAll().get(ZERO);
        }
        if (null != checkInDataDao.loadAll() && ZERO < checkInDataDao.loadAll().size()) {
            checkInData = checkInDataDao.loadAll().get(ZERO);
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
        Menu menu = mNavigationView.getMenu();
        imgUser = mNavHeader.findViewById(R.id.img_user);
        txtUserName = mNavHeader.findViewById(R.id.txt_user_name);
        mTxtRewardPoints = mNavHeader.findViewById(R.id.txt_nav_rewards);
        txtNotLoggedIn = mNavHeader.findViewById(R.id.txt_nav_not_logged_in);
        mLayoutUserData = mNavHeader.findViewById(R.id.layout_user_data);

        snapNShareMenu = menu.findItem(R.id.nav_snap);
        foodJourneyMenu = menu.findItem(R.id.nav_food_journey);
        checkInMenu = menu.findItem(R.id.nav_check_in);
        menuLogoutItem = menu.findItem(R.id.nav_logout);
    }

    /**
     * set user info in navigation drawer
     */
    public void setUserInfo(NavigationView mNavigationView) {
        initNavHeaderViews(mNavigationView);
        if (null != snapXData && isLoggedIn()) {
            txtNotLoggedIn.setVisibility(View.GONE);
            mLayoutUserData.setVisibility(View.VISIBLE);

            Glide.with(mContext.getApplicationContext())
                    .load(snapXData.getImageUrl())
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(THUMBNAIL).into(imgUser);

            txtUserName.setText(snapXData.getUserName());
            mTxtRewardPoints.setText(snapXData.getUserRewardPoint());
            menuLogoutItem.setTitle(mContext.getString(R.string.log_out));
        } else {
            mLayoutUserData.setVisibility(View.GONE);
            txtNotLoggedIn.setVisibility(View.VISIBLE);
            if (!isLoggedIn()) {
                menuLogoutItem.setTitle(mContext.getString(R.string.log_in));
            }
        }
    }

    /*  public boolean getCheckedInTimeDiff() {
          Date date = new Date();
          @SuppressLint("SimpleDateFormat")
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.format_checkedIn));
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(date);
          String currentTime = simpleDateFormat.format(calendar.getTime());
          Date parsedCheckedIn = null;
          Date parsedCurrentTime = null;
          Date currentTime = Calendar.getInstance().getTime();
          int hours = ZERO;
          try {
              Date checkedInTime = new SimpleDateFormat(mContext.getString(R.string.checkin_time_format)).parse(checkInData.getCheckInTime());
              long mills = currentTime.getTime() - checkedInTime.getTime();
              hours = (int) (mills / (MILLIS));
              int mins = (int) ((mills / (MILLIES_TWO)) % SECONDS);

              String diff = hours + ":" + mins;
          } catch (ParseException e) {
              e.printStackTrace();
          }

          long timeDiff = parsedCheckedIn.getTime() - parsedCurrentTime.getTime();
          long mills = Math.abs(timeDiff);

          int checkoutTime = (int) (mills / (CHECKOUT_DURATION * MILLIS));

          if (checkoutTime > CHECKOUT_DURATION) {
              snapNShareMenu.setEnabled(false);
              if (checkInMenu.getTitle().toString().equals(mContext.getString(R.string.check_in))) {
                  checkInMenu.setTitle(mContext.getString(R.string.checkout));
              } else {
                  checkInMenu.setTitle(mContext.getString(R.string.check_in));
              }
          }
          return true;
          if (hours >= CHECKOUT_DURATION) {
              checkInMenu.setTitle(mContext.getString(R.string.check_in));
              return false;
          } else {
              return true;
          }
      }*/
    public boolean getCheckedInTimeDiff() {
        Date currentTime = Calendar.getInstance().getTime();
        int hours = ZERO;
        try {
            @SuppressLint("SimpleDateFormat")
            Date checkedInTime = new SimpleDateFormat(mContext.getString(R.string.checkin_time_format)).parse(checkInData.getCheckInTime());
            long mills = currentTime.getTime() - checkedInTime.getTime();
            hours = (int) (mills / (MILLIS));
            int mins = (int) ((mills / (MILLIES_TWO)) % SECONDS);

            //For debug purpose
            String diff = hours + ":" + mins;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (hours >= CHECKOUT_DURATION) {
            checkInMenu.setTitle(mContext.getString(R.string.check_in));
            return false;
        } else {
            return true;
        }
    }

    //Cancel notification for broadcast receiver
    public void cancelReminder(Intent intent) {
        ComponentName componentName = new ComponentName(mContext, SnapNotificationReceiver.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                ZERO, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        if (null != am)
            am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public void enableReceiver(ComponentName receiver) {
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public com.snapxeats.common.model.location.Location getLocationfromPref() {
        Gson gson = new Gson();
        String json = preferences.getString(mContext.getString(R.string.selected_location), "");
        com.snapxeats.common.model.location.Location mSelectedLocation = gson.fromJson(json, com.snapxeats.common.model.location.Location.class);
        return mSelectedLocation;
    }

    /* Set latitude and longitude as per login status */
    public LatLng setLatLng() {
        LatLng latLng = null;
        if (isLoggedIn()) {
            latLng = new LatLng(getLocationfromPref().getLat(), getLocationfromPref().getLng());
        } else if (null != rootUserPreference.getLocation()) {
            latLng = new LatLng(rootUserPreference.getLocation().getLat(), rootUserPreference.getLocation().getLng());
        }
        return latLng;
    }

    public void updateRewardUI(NavigationView navigationView, UserReward userReward) {
        View mNavHeader = navigationView.getHeaderView(ZERO);
        TextView mTxtRewardPoints = mNavHeader.findViewById(R.id.txt_nav_rewards);
        if (null != userReward && null != userReward.getUserRewardPoint()) {
            mTxtRewardPoints.setText(String.valueOf(userReward.getUserRewardPoint()));
            dbHelper.updateRewardPoint(userReward.getUserRewardPoint());
        }
    }

    /**
     * Delete local image and audio file
     */
    public void deleteLocalData(String image_path, String audio_path) {

        File imageFile = new File(Uri.parse(image_path).getPath());
        File audioFile = new File(audio_path);
        if (imageFile.exists()) {
            imageFile.delete();
            System.out.println(imageFile + " File deleted");
        } else {
            System.out.println("File not Deleted :" + imageFile);
        }

        if (audioFile.exists()) {
            audioFile.delete();
            System.out.println(audioFile + " File deleted");
        } else {
            System.out.println("File not Deleted :" + audioFile);
        }
    }
}
