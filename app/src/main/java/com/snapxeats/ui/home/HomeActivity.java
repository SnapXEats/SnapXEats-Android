package com.snapxeats.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.OnRecyclerItemClickListener;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.checkin.CheckInResponse;
import com.snapxeats.common.model.checkin.CheckInRestaurants;
import com.snapxeats.common.model.checkin.RestaurantInfo;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.foodstack.FoodStackDbHelper;
import com.snapxeats.ui.home.fragment.checkin.CheckInFragment;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyFragment;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import com.snapxeats.ui.home.fragment.rewards.RewardsFragment;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoFragment;
import com.snapxeats.ui.home.fragment.snapnshare.CheckInAdapter;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareFragment;
import com.snapxeats.ui.home.fragment.wishlist.WishlistDbHelper;
import com.snapxeats.ui.home.fragment.wishlist.WishlistFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.Router.Screen.LOGIN;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isCuisineDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isFoodDirty;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeContract.HomeView,
        AppContract.SnapXResults, View.OnClickListener {


    @Inject
    HomeFragment homeFragment;

    @Inject
    NavPrefFragment navPrefFragment;

    @Inject
    WishlistFragment wishlistFragment;

    @Inject
    CheckInFragment checkInFragment;

    @Inject
    FoodJourneyFragment foodJourneyFragment;

    @Inject
    SmartPhotoFragment smartPhotoFragment;

    @Inject
    SnapShareFragment snapShareFragment;

    @Inject
    RewardsFragment rewardsFragment;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    private FragmentTransaction transaction;

    private FragmentManager fragmentManager;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.parent_layout)
    protected View mParentLayout;

    //CheckIndialog
    protected LinearLayout mSingleRestLayout;
    protected LinearLayout mNoRestLayout;
    protected LinearLayout mBtnLayout;
    protected LinearLayout mMultipleRestLayout;
    protected RecyclerView mRecyclerView;
    protected CircularImageView mImgRestaurant;
    protected TextView mTxtRestName;
    protected TextView mTxtCancel;
    protected Button mBtnCheckIn;

    @Inject
    HomeContract.HomePresenter mPresenter;

    private List<SnapxData> mSnapxData;

    @Inject
    AppUtility mAppUtility;

    private SharedPreferences preferences;
    private String userId;

    @Inject
    AppUtility utility;

    @Inject
    HomeDbHelper homeDbHelper;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    RootUserPreference mRootUserPreference;

    private CircularImageView imgUser;
    private View mNavHeader;
    private TextView txtUserName;
    private TextView txtNotLoggedIn;
    private TextView txtRewards;
    private LinearLayout mLayoutUserData;
    private UserPreference mUserPreference;
    private boolean isCheckIn = true;
    private Dialog mCheckInDialog;
    private Dialog mRewardDialog;

    private double lattitude = 40.7014;
    private double longitude = -74.0151;
    private List<RestaurantInfo> mRestaurantList;

    private CheckInAdapter mAdapter;

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    @Inject
    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        mPresenter.addView(this);
        utility.setContext(this);
        wishlistDbHelper.setContext(this);
        preferences = utility.getSharedPreferences();
        mNavigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        foodStackDbHelper.setContext(this);

        List<SnapxData> snapxData = mPresenter.getUserDataFromDb();
        userId = preferences.getString(getString(R.string.user_id), "");
        mRootUserPreference = mPresenter.getUserPreferenceFromDb();

        if (null != snapxData && snapxData.size() > 0) {
            if (snapxData.get(0).getIsFirstTimeUser()) {
                transaction.replace(R.id.frame_layout, navPrefFragment);
            } else {
                transaction.replace(R.id.frame_layout, homeFragment);
            }
        } else {
            transaction.replace(R.id.frame_layout, homeFragment);
        }
        mSnapxData = mPresenter.getUserDataFromDb();
        setUserInfo();
        if (null != mSnapxData && mSnapxData.size() > 0) {
            if (mSnapxData.get(0).getIsFirstTimeUser()) {
                transaction.replace(R.id.frame_layout, navPrefFragment);
            } else {
                transaction.replace(R.id.frame_layout, homeFragment);
            }
        } else
            transaction.replace(R.id.frame_layout, homeFragment);
        transaction.commit();
        mNavigationView.setCheckedItem(R.id.nav_home);

        changeItems();
    }

    private void changeItems() {
        if (isCheckIn) {
            Menu menu = mNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.nav_snap);
            menuItem.setTitle(getString(R.string.check_in));
        }
    }

    private void setWishlistCount() {
        LinearLayout linearLayout = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                .getActionView().findViewById(R.id.layout_wishlist_count);

        if (null != mSnapxData && mSnapxData.size() > 0 && isLoggedIn()) {
            linearLayout.setVisibility(View.VISIBLE);

            TextView view = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                    .getActionView().findViewById(R.id.txt_count_wishlist);

            view.setText(getString(R.string.zero));

            dbHelper.setContext(this);
            if (0 != homeDbHelper.getWishlistCount()) {
                view.setText(String.valueOf(homeDbHelper.getWishlistCount()));
            } else {
                view.setText(getString(R.string.zero));
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = mAppUtility.getSharedPreferences();
        String serverUserId = preferences.getString(getString(R.string.user_id), "");
        return !serverUserId.isEmpty();
    }

    public void initNavHeaderViews() {
        mNavHeader = mNavigationView.getHeaderView(0);
        imgUser = mNavHeader.findViewById(R.id.img_user);
        txtUserName = mNavHeader.findViewById(R.id.txt_user_name);
        txtNotLoggedIn = mNavHeader.findViewById(R.id.txt_nav_not_logged_in);
        txtRewards = mNavHeader.findViewById(R.id.txt_nav_rewards);
        mLayoutUserData = mNavHeader.findViewById(R.id.layout_user_data);
    }

    private void setUserInfo() {
        initNavHeaderViews();
        if (isLoggedIn() && null != mSnapxData && mSnapxData.size() > 0) {
            Picasso.with(this).load(mSnapxData.get(0).getImageUrl()).into(imgUser);
            txtUserName.setText(mSnapxData.get(0).getUserName());
        } else {
            mLayoutUserData.setVisibility(View.GONE);
            txtNotLoggedIn.setVisibility(View.VISIBLE);
            if (!isLoggedIn()) {
                Menu menu = mNavigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.nav_logout);
                menuItem.setTitle(getString(R.string.log_in));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = (float) 1.0;
        this.getWindow().setAttributes(params);

        setWishlistCount();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (!isDirty) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = homeFragment;
                    break;
                case R.id.nav_wishlist:
                    if (0 != homeDbHelper.getWishlistCount()) {
                        selectedFragment = wishlistFragment;
                    }
                    break;
                case R.id.nav_preferences:
                    selectedFragment = navPrefFragment;
                    break;
                case R.id.nav_food_journey:
                    selectedFragment = foodJourneyFragment;
                    break;

                case R.id.nav_smart_photos:
                    selectedFragment = smartPhotoFragment;
                    break;
                case R.id.nav_snap:
                   /* mDrawerLayout.closeDrawer(GravityCompat.START);
                    WindowManager.LayoutParams params = this.getWindow().getAttributes();
                    params.alpha = (float) 0.2;
                   this.getWindow().setAttributes(params);
                    mPresenter.presentScreen(CHECKIN);*/
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    showCheckInDialog();


                    break;
                case R.id.nav_rewards:
                    selectedFragment = rewardsFragment;
                    break;
                case R.id.nav_logout:
                  /*  Menu menu = mNavigationView.getMenu();
                    MenuItem menuItem = menu.findItem(R.id.nav_logout);
                    if (menuItem.getTitle().equals("Log out")) {*/
                    showLogoutDialog();
                   /* } else {
                        mPresenter.presentScreen(LOGIN);
                    }*/
                    break;
            }

            if (null != selectedFragment) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(getString(R.string.preference_save_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.apply), (dialog, which) -> {

                        if (null != userId && !userId.isEmpty()) {

                            mUserPreference = homeDbHelper.mapLocalObject(mRootUserPreference);
                            postOrPutUserPreferences();
                        } else {
                            //For non logged in user
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_layout, homeFragment);
                            mNavigationView.setCheckedItem(R.id.nav_home);
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            isDirty = false;
                            isCuisineDirty = false;
                            isFoodDirty = false;
                            transaction.commit();
                        }
                    });
            alertDialog.show();
        }
        return true;
    }

    public void checkIn() {
        showProgressDialog();
        CheckInRequest checkInRequest = null;
        for (RestaurantInfo restaurantInfo : mRestaurantList) {
            if (restaurantInfo.isSelected()) {
                checkInRequest = new CheckInRequest();
                checkInRequest.setRestaurant_info_id(restaurantInfo.getRestaurant_info_id());
                checkInRequest.setReward_type(getString(R.string.reward_type_check_in));
            }
        }
        mPresenter.checkIn(checkInRequest);
    }

    public void dismissCheckInDialog() {
        mCheckInDialog.dismiss();
    }

    /**
     * Show logout dialog
     */
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.log_out));
        builder.setMessage(getString(R.string.logout_message));

        builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            //Clear local db
            showProgressDialog();
            mPresenter.sendUserGestures(wishlistDbHelper.getFoodGestures());
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof UserPreference) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, homeFragment);
            mNavigationView.setCheckedItem(R.id.nav_home);
            mDrawerLayout.closeDrawer(GravityCompat.START);
            isDirty = false;
            isCuisineDirty = false;
            isFoodDirty = false;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.isFirstTimeUser), false);
            editor.apply();
            transaction.commit();
        } else if (value instanceof Boolean) {
            if ((boolean) value) {
                mPresenter.presentScreen(LOGIN);
            }
        } else if (value instanceof CheckInRestaurants) {
            mCheckInDialog.show();
            mRestaurantList = ((CheckInRestaurants) value).getRestaurants_info();
            if (1 == mRestaurantList.size()) {
                mNoRestLayout.setVisibility(View.GONE);
                mSingleRestLayout.setVisibility(View.VISIBLE);
                mMultipleRestLayout.setVisibility(View.GONE);
                mBtnLayout.setVisibility(View.VISIBLE);
                setSingleCheckInView(mRestaurantList.get(0));
            } else if (0 < mRestaurantList.size()) {
                mNoRestLayout.setVisibility(View.GONE);
                mSingleRestLayout.setVisibility(View.GONE);
                mMultipleRestLayout.setVisibility(View.VISIBLE);
                mBtnLayout.setVisibility(View.VISIBLE);
                setupRecyclerView();
            } else {
                mNoRestLayout.setVisibility(View.VISIBLE);
                mBtnLayout.setVisibility(View.GONE);
                mSingleRestLayout.setVisibility(View.GONE);
                mMultipleRestLayout.setVisibility(View.GONE);
            }
        } else if (value instanceof CheckInResponse) {
            mRewardDialog = new Dialog(this);
            mRewardDialog.setContentView(R.layout.layout_reward_message);
            Window window = mRewardDialog.getWindow();
            if (null != window) {
                window.setLayout(800, 1100);
                window.setBackgroundDrawable(getDrawable(R.drawable.checkin_background));
            }
            mCheckInDialog.dismiss();
            mRewardDialog.show();
            TextView mTxtRewards = mRewardDialog.findViewById(R.id.txt_reward_points);
            String rewards = ((CheckInResponse) value).getReward_point() + " " + getString(R.string.reward_points);
            mTxtRewards.setText(rewards);
        }
    }


    /**
     * Show check-In dialog
     */
    private void showCheckInDialog() {
        mCheckInDialog = new Dialog(this);
        mCheckInDialog.setContentView(R.layout.manual_checkin_dialog);
        mCheckInDialog.setCancelable(true);
        mCheckInDialog.onBackPressed();
        Window window = mCheckInDialog.getWindow();
        if (null != window) {
            window.setLayout(950, 1250);
            window.setBackgroundDrawable(getDrawable(R.drawable.checkin_background));
        }

        mSingleRestLayout = mCheckInDialog.findViewById(R.id.single_restaurant_layout);
        mNoRestLayout = mCheckInDialog.findViewById(R.id.layout_no_restaurants);
        mBtnLayout = mCheckInDialog.findViewById(R.id.buttons_layout);
        mMultipleRestLayout = mCheckInDialog.findViewById(R.id.multiple_restaurants_layout);
        mRecyclerView = mCheckInDialog.findViewById(R.id.recyclerview);

        mImgRestaurant = mCheckInDialog.findViewById(R.id.img_restaurant);
        mTxtRestName = mCheckInDialog.findViewById(R.id.txt_rest_name);
        mTxtCancel = mCheckInDialog.findViewById(R.id.txt_cancel);
        mBtnCheckIn = mCheckInDialog.findViewById(R.id.btn_check_in);

        mCheckInDialog.findViewById(R.id.btn_check_in).setOnClickListener(this);
        mCheckInDialog.findViewById(R.id.txt_cancel).setOnClickListener(this);
        mCheckInDialog.findViewById(R.id.btn_okay).setOnClickListener(this);

        ViewTreeObserver viewTreeObserver = mRecyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            for (RestaurantInfo restaurantInfo : mRestaurantList) {
                if (restaurantInfo.isSelected()) {
                    mBtnCheckIn.setBackground(getDrawable(R.drawable.custom_button_selected));
                    mBtnCheckIn.setEnabled(true);
                }
            }
        });

        showProgressDialog();
        mPresenter.getNearByRestaurantToCheckIn(lattitude, longitude);

        /**
         Check In button
         */
        mBtnCheckIn.setOnClickListener(v -> {
            showProgressDialog();
            CheckInRequest checkInRequest = null;
            for (RestaurantInfo restaurantInfo : mRestaurantList) {
                if (restaurantInfo.isSelected()) {
                    checkInRequest = new CheckInRequest();
                    checkInRequest.setRestaurant_info_id(restaurantInfo.getRestaurant_info_id());
                    checkInRequest.setReward_type(getString(R.string.reward_type_check_in));
                }
            }
            mPresenter.checkIn(checkInRequest);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_in:
                checkIn();
                break;
            case R.id.txt_cancel:
                dismissCheckInDialog();
                break;
            case R.id.btn_okay:
                dismissCheckInDialog();
                break;
        }
    }

    private void setupRecyclerView() {

        if (null != mRestaurantList) {
            mAdapter = new CheckInAdapter(this, mRestaurantList, (position, isSelected) -> {
                for (RestaurantInfo restaurantInfo : mRestaurantList) {
                    restaurantInfo.setSelected(false);
                }
                mRestaurantList.get(position).setSelected(isSelected);
                mAdapter.notifyDataSetChanged();
            });

            LinearLayoutManager manager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    /**
     * Set view if only one restaurant is available
     */
    private void setSingleCheckInView(RestaurantInfo restaurantInfo) {
        if (null != restaurantInfo) {
            if (null != restaurantInfo.getRestaurant_logo() && !restaurantInfo.getRestaurant_logo().isEmpty()) {
                Picasso.with(this).load(restaurantInfo.getRestaurant_logo())
                        .placeholder(R.drawable.user_image).into(mImgRestaurant);
            }
            if (null != restaurantInfo.getRestaurant_name() && !restaurantInfo.getRestaurant_name().isEmpty()) {
                mTxtRestName.setText(restaurantInfo.getRestaurant_name());
            }
        }
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {

            if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                AppContract.DialogListenerAction click = () -> {
                    postOrPutUserPreferences();
                };
                showSnackBar(mParentLayout, setClickListener(click));
            }
        });
    }

    private void postOrPutUserPreferences() {
        if (null != mUserPreference) {
            showProgressDialog();
            if (preferences.getBoolean(getString(R.string.isFirstTimeUser), false)) {
                mPresenter.savePreferences(mUserPreference);
            } else {
                mPresenter.updatePreferences(mUserPreference);
            }
        }
    }

    @Override
    public void networkError(Object value) {
        showNetworkErrorDialog((dialog, which) -> {

        });
    }

    /**
     * on back pressed action
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        isDirty = false;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
