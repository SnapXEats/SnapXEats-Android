package com.snapxeats.ui.home.fragment.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NetworkCheckReceiver;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.RootInstagram;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.utilities.AppUtility;
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
import static com.snapxeats.ui.home.HomeActivity.PreferenceConstant.ACCESS_FINE_LOCATION;
import static com.snapxeats.ui.home.HomeActivity.PreferenceConstant.DEVICE_LOCATION;


public class HomeFragment extends BaseFragment implements
        HomeFgmtContract.HomeFgmtView,
        AppContract.SnapXResults,
        HomeFgmtAdapter.RecyclerViewClickListener {

    private static final float ALPHA_BLUR_BTN = (float) 0.4;
    private static final float ALPHA_BTN = (float) 1.0;
    @BindView(R.id.txt_place_name)
    protected TextView mTxtPlaceName;

    @Inject
    HomeFgmtContract.HomeFgmtPresenter presenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    //Location provided by google
    private Location mCurrentLocation;

    //Selected Location
    public static com.snapxeats.common.model.Location mSelectedLocation;

    private String mPlacename;

    private HomeFgmtAdapter mHomeFgmtAdapter;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.btn_cuisine_done)
    protected TextView mTxtCuisineDone;

    private SelectedCuisineList selectedCuisineList;

    private LocationCuisine mLocationCuisine;

    private Activity activity;
    private DrawerLayout mDrawerLayout;
    public SharedPreferences preferences;
    private RootCuisine mRootCuisine;

    @Inject
    public HomeFragment() {
    }

    @Override
    public void initView() {

        snapXDialog.setContext(getActivity());
        utility.setContext(getActivity());
        presenter.addView(this);
        mTxtPlaceName.setSingleLine();

        buildGoogleAPIClient();

        if (checkPermissions()) {
            mSelectedLocation = getSelectedLocation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, view);

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mRecyclerView.setNestedScrollingEnabled(false);

        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        //refresh facebook token
        AccessToken.refreshCurrentAccessTokenAsync();

        mRecyclerView.setNestedScrollingEnabled(false);

        selectedCuisineList = new SelectedCuisineList();

        preferences = utility.getSharedPreferences();

        Gson gson = new Gson();
        String json = preferences.getString(getString(R.string.selected_location), "");
        mSelectedLocation = gson.fromJson(json, com.snapxeats.common.model.Location.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.txt_place_name)
    public void presentLocationScreen() {
        if (NetworkUtility.isNetworkAvailable(activity)) {
            presenter.presentScreen(LOCATION);
        } else {

            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mTxtCuisineDone.setAlpha((float) 0.4);
        mTxtCuisineDone.setClickable(false);

        if (NetworkUtility.isNetworkAvailable(activity)) {
            //disable 'done' button till cuisines not selected
            mTxtCuisineDone.setAlpha((float) 0.4);
            mTxtCuisineDone.setClickable(false);

            if (mSelectedLocation != null) {
                //set latitude and longitude for selected cuisines
                mLocationCuisine = new LocationCuisine();
                mLocationCuisine.setLatitude(mSelectedLocation.getLat());
                mLocationCuisine.setLongitude(mSelectedLocation.getLng());
                selectedCuisineList.setLocation(mLocationCuisine);

                //Save data in shared preferences
                utility.saveObjectInPref(mSelectedLocation, getString(R.string.selected_location));

                mTxtPlaceName.setText(mSelectedLocation.getName());
                showProgressDialog();
                presenter.getCuisineList(mLocationCuisine);
            }
        }
    }

    @OnClick(R.id.btn_cuisine_done)
    public void btnCuisineDone() {
        //TODO for future reference network check
        if (mHomeFgmtAdapter.getSelectedItems().size() != 0) {
            if (mSelectedLocation != null) {
                //set selected cuisines
                selectedCuisineList.setSelectedCuisineList(mHomeFgmtAdapter.getSelectedItems());
                //TODO pass object as ENUM
                Intent intent = new Intent(activity, FoodStackActivity.class);
                selectedCuisineList.setSelectedCuisineList(mHomeFgmtAdapter.getSelectedItems());

                intent.putExtra(getString(R.string.data_selectedCuisineList), selectedCuisineList);
                activity.startActivity(intent);
            }
        }
    }

    /**
     * get food images
     *
     * @param value
     */
    @Override
    public void success(Object value) {
        dismissProgressDialog();

        if (value instanceof RootCuisine) {
            mRootCuisine = (RootCuisine) value;
            setRecyclerView();
        } else if (value instanceof SnapXUser) {
            SnapXUser snapXUser = (SnapXUser) value;

            SharedPreferences preferences = utility.getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.user_id), snapXUser.getUser_id());
            editor.apply();

          //  presenter.saveUserDataInDb(snapXUser);
        }
    }

    public void setRecyclerView() {
        List<Cuisines> selectableItems = mRootCuisine.getCuisineList();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity, 2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mHomeFgmtAdapter = new HomeFgmtAdapter(activity, selectableItems, mRootCuisine,
                selectedCuisineList -> {
                    if (selectedCuisineList.size() >= 0) {
                        mTxtCuisineDone.setAlpha(ALPHA_BTN);
                        mTxtCuisineDone.setClickable(true);
                    } else if (selectedCuisineList.size() == 0) {
                        mTxtCuisineDone.setAlpha(ALPHA_BLUR_BTN);
                        mTxtCuisineDone.setClickable(false);
                    }
                });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHomeFgmtAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DEVICE_LOCATION:
                checkLocalPermissions();
                break;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleLocationRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int index = 0; index < permissions.length; index++) {

            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {

                if (checkPermissions()) {
                    mSelectedLocation = getSelectedLocation();
                }

            } else if (!shouldShowRequestPermissionRationale(permissions[index])) {
                snapXDialog.showChangePermissionDialog();
            } else {
                presenter.presentScreen(LOCATION);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_FINE_LOCATION);
        }
    }

    private void checkLocalPermissions() {
        //Check device level location permission
        if (LocationHelper.isGpsEnabled(activity)) {
            if (LocationHelper.checkPermission(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            } else if (NetworkUtility.isNetworkAvailable(activity)) {
                mCurrentLocation = getLocation();
                if (mCurrentLocation != null) {

                    mPlacename = getPlaceName(mCurrentLocation);
                    mSelectedLocation =
                            new com.snapxeats.common.model.Location(mCurrentLocation.getLatitude(),
                                    mCurrentLocation.getLongitude(),
                                    mPlacename);
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
    public void error(Object value) {
        dismissProgressDialog();
    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {

        });
    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {

        });
    }

    @Override
    public void recyclerViewListClicked(List<String> selectedCuisineList) {

    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
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
        return mSelectedLocation;
    }

    public boolean checkPermissions() {

        //Check device level location permission
        if (LocationHelper.isGpsEnabled(activity)) {
            if (LocationHelper.checkPermission(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            } else if (NetworkUtility.isNetworkAvailable(activity)) {
                return true;
            } else {
                showNetworkErrorDialog((dialog, which) -> {
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
        if (!LocationHelper.isGpsEnabled(activity)) {
            showGpsPermissionDialog();
        }
    }

    /**
     * Show Gps permission not available dialog
     */
    public void showGpsPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.location_service_not_active));
        builder.setMessage(getActivity().getString(R.string.enable_gps));
        builder.setPositiveButton(getActivity().getString(R.string.action_settings), (dialogInterface, i) -> {
            getActivity().startActivityFromFragment(this,
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    ACCESS_FINE_LOCATION);
        });

        builder.setNegativeButton(getActivity().getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                mSelectedLocation = intent.getParcelableExtra(context.getString(R.string.selected_location));
            }
        }
    }

}
