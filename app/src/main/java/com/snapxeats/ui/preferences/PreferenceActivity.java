package com.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.NetworkHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.FOODSTACK;
import static com.snapxeats.common.Router.Screen.LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION;

/**
 * Created by Snehal Tembare on 3/1/18.
 */


public class PreferenceActivity extends BaseActivity implements PreferenceContract.PreferenceView,
        AppContract.SnapXResults {

    public interface PreferenceConstant {
        int ACCESS_FINE_LOCATION = 1;
    }

    @BindView(R.id.txt_place_name)
    protected TextView mTxtPlaceName;

    @Inject
    PreferenceContract.PreferencePresenter presenter;

    @Inject
    SnapXDialog snapXDialog;
    private LocationManager mLocationManager;
    private Snackbar mSnackBar;
    private AppContract.DialogListenerAction denyAction = () -> NetworkHelper.requestPermission(this);
    private AppContract.DialogListenerAction allowAction = () -> presenter.presentScreen(LOCATION);
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.addView(this);
        snapXDialog.setContext(this);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initView();
    }

    /**
     * Check provider is enable or not
     */
    private void checkGpsPermission() {
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            snapXDialog.showGpsPermissionDialog();
        }
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        preferences = getSharedPreferences("SnapXEats", MODE_PRIVATE);
        editor = preferences.edit();

        initSnackBar();
    }

    /**
     * Initialize Snackbar
     */
    private void initSnackBar() {
        //Snackbar to show location permission error
        mSnackBar = Snackbar.make(findViewById(R.id.layout_parent), getString(R.string.location_permission_needed),
                Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.retry),
                v -> NetworkHelper.requestPermission(PreferenceActivity.this));
    }

    /**
     * Update user location
     *
     * @param placeName
     */
    @Override
    public void updatePlaceName(String placeName, Location location) {
        dismissProgressDialog();
        mLocation=location;
        if (mSnackBar != null) {
            mSnackBar.dismiss();
        }
        if (placeName.isEmpty()) {
            mTxtPlaceName.setText(preferences.getString(getString(R.string.last_location), ""));
        } else {
            mTxtPlaceName.setText(placeName);
            editor.putString(getString(R.string.last_location), placeName);
            editor.apply();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (NetworkHelper.checkPermission(this) &&
                    preferences.getBoolean(getString(R.string.isLocationPermissionGranted), true)) {
                NetworkHelper.requestPermission(this);
            } else {
                presenter.getLocation(this);
            }
        } else {
            checkGpsPermission();
        }

        if (NetworkHelper.checkPermission(this) &&
                preferences.getBoolean(getString(R.string.isLocationPermissionDenied), false)) {
            mSnackBar.show();
        }
        mTxtPlaceName.setText(preferences.getString(getString(R.string.last_location), ""));
    }

    @OnClick(R.id.txt_place_name)
    public void presentLocationScreen() {

        presenter.presentScreen(LOCATION);
    }

    @OnClick(R.id.txt_done)
    public void presentFoodStackScreen() {
        presenter.presentScreen(FOODSTACK);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_FINE_LOCATION:
                handleLocationRequest(permissions, grantResults);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleLocationRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SnapXToast.debug("Permissions granted");
            editor.putBoolean(getString(R.string.isLocationPermissionGranted), true);
            editor.putBoolean(getString(R.string.isLocationPermissionDenied), false);
            editor.apply();
            if (mSnackBar != null) {
                mSnackBar.dismiss();
            }

        } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
            SnapXToast.debug("Permissions denied check never ask again");
            editor.putBoolean(getString(R.string.isLocationPermissionGranted), false);
            editor.putBoolean(getString(R.string.isLocationPermissionDenied), true);

            editor.apply();
            if (mSnackBar != null) {
                mSnackBar.dismiss();
            }

            // User selected the Never Ask Again Option Change settings in app settings manually
            snapXDialog.showChangePermissionDialog();
        } else {
            editor.putBoolean(getString(R.string.isLocationPermissionGranted), false);
            editor.putBoolean(getString(R.string.isLocationPermissionDenied), true);

            editor.apply();
            showDenyDialog(setListener(denyAction), setListener(allowAction));
        }
    }

    @Override
    public void success() {
    }

    @Override
    public void error() {
    }

    @Override
    public void noNetwork() {
        //Set action as a finish() to close current activity
        showNetworkErrorDialog(setListener(() -> finish()));
    }

    @Override
    public void networkError() {
    }
}
