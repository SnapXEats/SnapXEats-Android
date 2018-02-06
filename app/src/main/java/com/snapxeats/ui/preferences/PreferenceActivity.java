package com.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.NetworkHelper;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.FOODSTACK;
import static com.snapxeats.common.Router.Screen.LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.CUSTOM_LOCATION;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceActivity extends BaseActivity implements PreferenceContract.PreferenceView,
        AppContract.SnapXResults {

    public interface PreferenceConstant {
        int ACCESS_FINE_LOCATION = 1;
        int CUSTOM_LOCATION = 2;
    }

    @BindView(R.id.txt_place_name)
    protected TextView mTxtPlaceName;

    @Inject
    PreferenceContract.PreferencePresenter presenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    private LocationManager mLocationManager;

    private Snackbar mSnackBar;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Location mLocation;

    private PreferenceAdapter mPreferenceAdapter;
    private int selectedCardPos = 0;
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * Check provider is enable or not
     */
    private void checkGpsPermission() {
        if (!NetworkHelper.isGpsEnabled(this)) {
            snapXDialog.showGpsPermissionDialog();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        presenter.addView(this);
        snapXDialog.setContext(this);
        utility.setmContext(this);
        mRecyclerView.setNestedScrollingEnabled(false);
        preferences = utility.getSharedPreferences();
        editor = preferences.edit();
//        mTxtPlaceName.setText(preferences.getString(getString(R.string.last_location), getString(R.string.select_location)));
        checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkPermissions() {
        //Check device level location permission
        if (NetworkHelper.isGpsEnabled(this)) {
            if (NetworkHelper.checkPermission(this)) {
                NetworkHelper.requestPermission(this);
            } else if (NetworkUtility.isNetworkAvailable(this)) {
                presenter.getLocation(this);
                presenter.getCuisineList();
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                });
            }
        } else {
            checkGpsPermission();
        }
    }

    /**
     * Update user location
     *
     * @param placeName
     */
    @Override
    public void updatePlaceName(String placeName, Location location) {
        dismissProgressDialog();
        mLocation = location;
        if (mSnackBar != null) {
            mSnackBar.dismiss();
        }
        if (placeName.isEmpty()) {
//            mTxtPlaceName.setText(preferences.getString(getString(R.string.last_location), getString(R.string.select_location)));
        } else {
            mTxtPlaceName.setText(placeName);
//            editor.putString(getString(R.string.last_location), placeName);
            editor.apply();
        }
    }

    @Override
    public void getCuisineInfo(RootCuisine rootCuisine) {
        if (rootCuisine != null) {
            mPreferenceAdapter = new PreferenceAdapter(PreferenceActivity.this,
                    selectedCardPos,
                    rootCuisine.getCuisineList(),
                    rootCuisine,
                    (position, rootCuisineObject) -> {

                    });
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(PreferenceActivity.this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mPreferenceAdapter);
    }

    @OnClick(R.id.layout_location)
    public void presentLocationScreen() {
        presenter.presentScreen(LOCATION);
    }

    @OnClick(R.id.btn_cuisine_done)
    public void btnCuisineDone() {
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
            checkPermissions();
            //To add data in preferences
        } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
            SnapXToast.debug("Permissions denied check never ask again");
            snapXDialog.showChangePermissionDialog();
        } else {
            presenter.presentScreen(LOCATION);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION:
                checkPermissions();
                break;

            case CUSTOM_LOCATION:
                    com.snapxeats.common.model.Location location =
                            data.getParcelableExtra(getString(R.string.selected_location));
                    if (location != null) {
                        mTxtPlaceName.setText(location.getName());
                    }
                break;
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
        showNetworkErrorDialog((dialog, which) -> {
        });
    }

    @Override
    public void networkError() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
