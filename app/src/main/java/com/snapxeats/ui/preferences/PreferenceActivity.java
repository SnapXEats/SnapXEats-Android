package com.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.snapxeats.LocationBaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.LocationHelper;
import com.snapxeats.ui.foodstack.FoodStackActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.CUSTOM_LOCATION;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceActivity extends LocationBaseActivity implements PreferenceContract.PreferenceView,
        AppContract.SnapXResults, PreferenceAdapter.RecyclerViewClickListener {

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

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    //Location provided by google
    private Location mCurrentLocation;

    //Selected Location
    private com.snapxeats.common.model.Location mSelectedLocation;

    private String mPlacename;

    private PreferenceAdapter mPreferenceAdapter;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.btn_cuisine_done)
    protected TextView mTxtCuisineDone;

    private SelectedCuisineList selectedCuisineList;

    private LocationCuisine mLocationCuisine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.addView(this);
        snapXDialog.setContext(this);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
        initView();
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void initView() {
        buildGoogleAPIClient();

        presenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        mRecyclerView.setNestedScrollingEnabled(false);
        preferences = utility.getSharedPreferences();
        editor = preferences.edit();

        selectedCuisineList = new SelectedCuisineList();
        mTxtPlaceName.setText(preferences.getString(getString(R.string.last_location), getString(R.string.select_location)));

        if (checkPermissions()) {
            mSelectedLocation = getSelectedLocation();
        } else {
            Gson gson = new Gson();
            String json = preferences.getString(getString(R.string.selected_location), "");
            mSelectedLocation = gson.fromJson(json, com.snapxeats.common.model.Location.class);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //disable 'done' button till cuisines not selected
        mTxtCuisineDone.setAlpha((float) 0.4);
        mTxtCuisineDone.setClickable(false);


        if (mSelectedLocation != null) {

            /**TODO set latitude and longitude*/
            mLocationCuisine = new LocationCuisine();
            mLocationCuisine.setLatitude(mSelectedLocation.getLat());
            mLocationCuisine.setLongitude(mSelectedLocation.getLng());
            selectedCuisineList.setLocation(mLocationCuisine);

            //Save data in shared preferences
            utility.saveObjectInPref(mSelectedLocation, getString(R.string.selected_location));

            mTxtPlaceName.setText(mSelectedLocation.getName());
            presenter.getCuisineList(this, mLocationCuisine);
        }
    }

    @OnClick(R.id.layout_location)
    public void presentLocationScreen() {
        if (NetworkUtility.isNetworkAvailable(this)) {
            presenter.presentScreen(LOCATION);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    @OnClick(R.id.btn_cuisine_done)
    public void btnCuisineDone() {
        //TODO for future reference network check
        if (mPreferenceAdapter.getSelectedItems().size() != 0) {
            if (mSelectedLocation != null) {
                //set selected cuisines
                selectedCuisineList.setSelectedCuisineList(mPreferenceAdapter.getSelectedItems());
                //TODO pass object as ENUM
                Intent intent = new Intent(this, FoodStackActivity.class);
                intent.putExtra(getString(R.string.data_selectedCuisineList), selectedCuisineList);
                startActivity(intent);
            }
        }
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

    private com.snapxeats.common.model.Location getSelectedLocation() {

        mCurrentLocation = getLocation();

        if (mCurrentLocation != null) {

            mPlacename = getPlaceName(mCurrentLocation);
            mSelectedLocation = new com.snapxeats.common.model.Location(
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude(),
                    mPlacename);
        }
        return mSelectedLocation != null ? mSelectedLocation : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleLocationRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SnapXToast.debug("Permissions granted");

                if (checkPermissions()) {
                    mSelectedLocation = getSelectedLocation();
                }

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
                checkLocalPermissions();
                break;

            case CUSTOM_LOCATION:
                if (data != null) {
                    com.snapxeats.common.model.Location location =
                            data.getParcelableExtra(getString(R.string.selected_location));
                    if (location != null) {
                        mSelectedLocation = location;
                        utility.saveObjectInPref(location, getString(R.string.selected_location));
                    }
                }
                break;
        }
    }

    private void checkLocalPermissions() {
        //Check device level location permission
        if (LocationHelper.isGpsEnabled(this)) {
            if (LocationHelper.checkPermission(this)) {
                LocationHelper.requestPermission(this);
            } else if (NetworkUtility.isNetworkAvailable(this)) {
                mCurrentLocation = getLocation();
                if (mCurrentLocation != null) {

                    mPlacename = getPlaceName(mCurrentLocation);
                    mSelectedLocation =
                            new com.snapxeats.common.model.Location(mCurrentLocation.getLatitude(),
                                    mCurrentLocation.getLongitude(),
                                    mPlacename);

                    mTxtPlaceName.setText(mPlacename);
                    presenter.getCuisineList(this, mLocationCuisine);
                }
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                });
            }
        } else {
            checkGpsPermission();
        }
    }

    @Override
    public void success(Object value) {
        RootCuisine rootCuisine = (RootCuisine) value;
        List<Cuisines> selectableItems = rootCuisine.getCuisineList();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(PreferenceActivity.this, 2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mPreferenceAdapter = new PreferenceAdapter(PreferenceActivity.this,
                selectableItems,
                rootCuisine, selectedCuisineList -> {
            if (selectedCuisineList.size() >= 0) {
                mTxtCuisineDone.setClickable(true);
                mTxtCuisineDone.setAlpha((float) 1.0);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPreferenceAdapter);
    }

    @Override
    public void error() {
    }

    @Override
    public void noNetwork(Object value) {
        //Set action as a finish() to close current activity
        showNetworkErrorDialog((dialog, which) -> {
        });
    }

    @Override
    public void networkError() {
    }

    @Override
    public void recyclerViewListClicked(List<String> selectedCuisineList) {

    }

    /**
     * on back pressed action
     */
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
