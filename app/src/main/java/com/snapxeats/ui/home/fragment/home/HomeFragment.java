package com.snapxeats.ui.home.fragment.home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;

import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.common.model.UserReward;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.model.preference.RootCuisine;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.LocationHelper;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;
import com.snapxeats.ui.foodstack.FoodStackActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.LOCATION;
import static com.snapxeats.common.constants.UIConstants.ACCESS_FINE_LOCATION;
import static com.snapxeats.common.constants.UIConstants.ACTION_LOCATION_GET;
import static com.snapxeats.common.constants.UIConstants.CHANGE_LOCATION_PERMISSIONS;
import static com.snapxeats.common.constants.UIConstants.DEVICE_LOCATION;
import static com.snapxeats.common.constants.UIConstants.LOGOUT_DIALOG_HEIGHT;
import static com.snapxeats.common.constants.UIConstants.SPAN_COUNT;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeFragment extends BaseFragment implements
        HomeFgmtContract.HomeFgmtView,
        AppContract.SnapXResults {

    protected NavigationView mNavigationView;

    @BindView(R.id.txt_place_name)
    protected TextView mTxtPlaceName;

    @BindView(R.id.layout_parent)
    protected LinearLayout mParentLayout;

    @Inject
    HomeFgmtContract.HomeFgmtPresenter presenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    //Location provided by google
    private Location mCurrentLocation;

    //Selected Location
    public com.snapxeats.common.model.location.Location mSelectedLocation;

    private String mPlacename;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.btn_cuisine_done)
    protected TextView mTxtCuisineDone;

    private SelectedCuisineList selectedCuisineList;
    private Snackbar mSnackBar;

    private Activity activity;
    private DrawerLayout mDrawerLayout;
    public SharedPreferences preferences;
    private RootCuisine mRootCuisine;
    private List<Cuisines> cuisinesList;
    private List<String> selectedList;
    private HomeAdapter adapter;
    private LocationCuisine mLocationCuisine;

    private ProgressDialog mDialog;
    private Dialog changePermissionDilog;
    private Dialog locationErrorDialog;
    private Dialog cuisineErrorDialog;

    @Inject
    public HomeFragment() {
    }

    @Inject
    DbHelper dbHelper;

    @Inject
    RootUserPreference mRootUserPreference;

    @Inject
    HomeFgmtHelper homeFgmtHelper;
    MyReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOCATION_GET);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void initView() {
        snapXDialog.setContext(getActivity());
        utility.setContext(getActivity());
        presenter.addView(this);
        dbHelper.setContext(getActivity());
        mTxtPlaceName.setSingleLine();
        cuisinesList = new ArrayList<>();
        selectedList = new ArrayList<>();

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getActivity().getString(R.string.please_wait));
        mDialog.setCanceledOnTouchOutside(false);

        if (null == mSelectedLocation) {
            mSelectedLocation = detectCurrentLocation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;

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
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mRecyclerView.setNestedScrollingEnabled(false);

        mNavigationView = activity.findViewById(R.id.nav_view);
        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        //refresh facebook token
        AccessToken.refreshCurrentAccessTokenAsync();

        selectedCuisineList = new SelectedCuisineList();

        preferences = utility.getSharedPreferences();

        mSelectedLocation = utility.getLocationfromPref();
        initView();

        //Modifying done button color
        ViewTreeObserver treeObserver = mRecyclerView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(() -> {
            List<Cuisines> list = getSelectedCuisineList();
            if (null != list && ZERO < list.size()) {
                mTxtCuisineDone.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mTxtCuisineDone.setClickable(true);
            } else {
                mTxtCuisineDone.setTextColor(ContextCompat.getColor(activity, R.color.text_color_tertiary));
                mTxtCuisineDone.setClickable(false);
            }
        });
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
        selectedList.clear();

        if (null != locationErrorDialog && locationErrorDialog.isShowing())
            locationErrorDialog.dismiss();

        if (NetworkUtility.isNetworkAvailable(activity)) {
            setLocation();
            if (mSnackBar != null) {
                mSnackBar.dismiss();
            }
        } else {
            showNetworkErrorDialog((dialog, which) -> {
                if (!NetworkUtility.isNetworkAvailable(activity)) {
                    AppContract.DialogListenerAction click = () ->
                            setLocation();
                    mSnackBar = showSnackBar(mParentLayout, setClickListener(click));
                } else {
                    setLocation();
                }
            });
        }
    }

    @OnClick(R.id.btn_cuisine_done)
    public void btnCuisineDone() {
        for (Cuisines cuisines : cuisinesList) {
            if (cuisines.is_cuisine_like() || cuisines.is_cuisine_favourite()) {
                selectedList.add(cuisines.getCuisine_info_id());
            }
        }

        if (ZERO != selectedList.size()) {
            if (null != mSelectedLocation) {
                selectedCuisineList = homeFgmtHelper.getSelectedCuisine(mLocationCuisine, selectedList);
                //set selected cuisines data
                Intent intent = new Intent(activity, FoodStackActivity.class);
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
            mDialog.dismiss();
            mRootCuisine = (RootCuisine) value;
            cuisinesList = mRootCuisine.getCuisineList();
            Collections.sort(cuisinesList);
            if (null != cuisinesList && ZERO != cuisinesList.size()) {
                setRecyclerView();
            } else if (null != getActivity()){
                showCuisineEmptyDialog();
            }
        } else if (value instanceof SnapXUser) {
            SnapXUser snapXUser = (SnapXUser) value;
            SharedPreferences preferences = utility.getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.user_id), snapXUser.getUser_id());
            editor.apply();
        } else if (value instanceof UserReward) {
            UserReward userReward = (UserReward) value;
            utility.updateRewardUI(mNavigationView, userReward);
        }
    }

    private void showCuisineEmptyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.cuisine_error));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        cuisineErrorDialog = builder.create();
        cuisineErrorDialog.show();
    }

    public void setRecyclerView() {
        getDataFromLocalDB();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity, SPAN_COUNT);

        adapter = new HomeAdapter(activity, cuisinesList, new OnDoubleTapListenr() {
            Cuisines cuisines;

            @Override
            public void onSingleTap(int position, boolean isLike) {
                cuisines = mRootCuisine.getCuisineList().get(position);
                if (cuisines.is_cuisine_like()) {
                    cuisines.set_cuisine_like(isLike);
                } else {
                    cuisines.set_cuisine_favourite(isLike);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDoubleTap(int position, boolean isSuperLike) {

            }
        });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private List<Cuisines> getSelectedCuisineList() {
        List<Cuisines> selectedCuisineList = new ArrayList<>();
        if (null != cuisinesList) {
            for (Cuisines cuisines : cuisinesList) {
                if (cuisines.is_cuisine_favourite() || cuisines.is_cuisine_like()) {
                    selectedCuisineList.add(cuisines);
                }
            }
        }
        return selectedCuisineList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mSnackBar)
            mSnackBar.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DEVICE_LOCATION:
                mSelectedLocation = detectCurrentLocation();
                break;
            case CHANGE_LOCATION_PERMISSIONS:
                mSelectedLocation = detectCurrentLocation();
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
        if (grantResults[ZERO] == PackageManager.PERMISSION_GRANTED) {
            mSelectedLocation = detectCurrentLocation();
        } else if (!shouldShowRequestPermissionRationale(permissions[ZERO])) {
            changePermissionDilog = snapXDialog.showChangePermissionDialogForFragment(this, CHANGE_LOCATION_PERMISSIONS);
        } else {
            presenter.presentScreen(LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
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
            if (!NetworkUtility.isNetworkAvailable(getActivity()) && null != mRootUserPreference) {
                AppContract.DialogListenerAction click = () -> setLocation();
                mSnackBar = showSnackBar(mParentLayout, setClickListener(click));
            } else {
                setLocation();
            }
        });
    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }

    private com.snapxeats.common.model.location.Location detectCurrentLocation() {
        if (LocationHelper.isGpsEnabled(activity)) {
            if (LocationHelper.checkPermission(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            } else if (NetworkUtility.isNetworkAvailable(activity)) {
                showProgressDialog();
                mCurrentLocation = utility.getLocation();
                if (null != mCurrentLocation) {
                    dismissProgressDialog();
                    mPlacename = utility.getPlaceName(mCurrentLocation);
                    mSelectedLocation = new com.snapxeats.common.model.location.Location(
                            mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude(),
                            mPlacename);
                    setLocation();
                }
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                    if (!NetworkUtility.isNetworkAvailable(activity)) {
                        AppContract.DialogListenerAction click = () ->
                                mSelectedLocation = detectCurrentLocation();
                        mSnackBar = showSnackBar(mParentLayout, setClickListener(click));
                    } else {
                        mSelectedLocation = detectCurrentLocation();
                    }
                });
            }
        } else {
            checkGpsPermission();
        }
        return mSelectedLocation;
    }

    private void setLocation() {
        if (null != mSelectedLocation && !mSelectedLocation.getName().isEmpty()) {
            if (null != locationErrorDialog && locationErrorDialog.isShowing())
                locationErrorDialog.dismiss();

            if (null != cuisineErrorDialog && cuisineErrorDialog.isShowing())
                cuisineErrorDialog.dismiss();

            //set latitude and longitude for selected cuisines
            mLocationCuisine = new LocationCuisine();
            mLocationCuisine.setLatitude(mSelectedLocation.getLat());
            mLocationCuisine.setLongitude(mSelectedLocation.getLng());

            //Save data in shared preferences
            if (utility.isLoggedIn()) {
                utility.saveObjectInPref(mSelectedLocation, getString(R.string.selected_location));
            } else {
                mRootUserPreference.setLocation(mSelectedLocation);
            }

            mTxtPlaceName.setText(mSelectedLocation.getName());
            if (null != getActivity() && isAdded()) {
                mDialog.show();
            }
            presenter.getCuisineList(mLocationCuisine);
        } else if (LocationHelper.isGpsEnabled(activity)) {
            showLocationErrorDialog();
        }
    }

    private void showLocationErrorDialog() {
        locationErrorDialog = new Dialog(getActivity());
        locationErrorDialog.setOnCancelListener(dialog -> locationErrorDialog.dismiss());
        locationErrorDialog.setContentView(R.layout.location_error_dialog_layout);
        locationErrorDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            locationErrorDialog.cancel();
        });

        Window window = locationErrorDialog.getWindow();
        if (null != window) {
            window.setLayout(UIConstants.CHECKIN_DIALOG_WIDTH, UIConstants.LOCATION_DIALOG_HEIGHT);
        }
        locationErrorDialog.show();
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
        builder.setPositiveButton(getActivity().getString(R.string.action_settings), (dialogInterface, i) ->
                getActivity().startActivityFromFragment(this,
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        DEVICE_LOCATION));

        builder.setNegativeButton(getActivity().getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void getDataFromLocalDB() {
        List<UserCuisinePreferences> userCuisinePreferencesList = mRootUserPreference.getUserCuisinePreferences();

        if (null != userCuisinePreferencesList) {
            for (int rowIndex = ZERO; rowIndex < userCuisinePreferencesList.size(); rowIndex++) {
                for (int colIndex = ZERO; colIndex < cuisinesList.size(); colIndex++) {
                    if (userCuisinePreferencesList.get(rowIndex).getCuisine_info_id().
                            equalsIgnoreCase(cuisinesList.get(colIndex).getCuisine_info_id())) {
                        cuisinesList.get(colIndex).set_cuisine_like(userCuisinePreferencesList.get(rowIndex)
                                .getIs_cuisine_like());
                        cuisinesList.get(colIndex).set_cuisine_favourite(userCuisinePreferencesList.get(rowIndex)
                                .getIs_cuisine_favourite());
                    }
                }
            }
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                HomeFragment.this.mSelectedLocation = intent.getParcelableExtra(context.getString(R.string.selected_location));
            }
        }
    }

}
