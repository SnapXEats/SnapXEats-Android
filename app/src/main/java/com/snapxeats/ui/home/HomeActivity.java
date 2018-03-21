package com.snapxeats.ui.home;

import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.snapxeats.LocationBaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.mUserPreference;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeActivity extends LocationBaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeContract.HomeView,
        AppContract.SnapXResults {

    public interface PreferenceConstant {
        int ACCESS_FINE_LOCATION = 1;
        int DEVICE_LOCATION = 2;
    }

    @Inject
    HomeFragment homeFragment;

    @Inject
    NavPrefFragment navPrefFragment;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    private FragmentTransaction transaction;

    private FragmentManager fragmentManager;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

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
    RootUserPreference mRootUserPreference;

    private CircularImageView imgUser;
    private View mNavHeader;
    private TextView txtUserName;
    private TextView txtNotLoggedIn;
    private TextView txtRewards;
    private LinearLayout mLayoutUserData;

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
        preferences = utility.getSharedPreferences();
        mNavigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

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
    }

    private void setWishlistCount() {
        LinearLayout linearLayout = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                .getActionView().findViewById(R.id.layout_wishlist_count);
        if (null != mSnapxData && mSnapxData.size() > 0 && isLoggedIn()) {
            linearLayout.setVisibility(View.VISIBLE);
            TextView view = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                    .getActionView().findViewById(R.id.txt_count_wishlist);
            view.setText(getString(R.string.zero));
            if (null != mSnapxData.get(0).getFoodWishlistCount() && !mSnapxData.get(0).getFoodWishlistCount().isEmpty()) {
                view.setText(mSnapxData.get(0).getFoodWishlistCount());
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    break;
                case R.id.nav_preferences:
                    selectedFragment = navPrefFragment;
                    break;
                case R.id.nav_food_journey:
                    break;
                case R.id.nav_rewards:
                    break;
                case R.id.nav_logout:
                    break;
            }

            if (null != selectedFragment) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }

            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.preference_save_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.apply), (dialog, which) -> {

                        if (null != userId && !userId.isEmpty()) {

                            if (null != mUserPreference) {
                                showProgressDialog();
                                mPresenter.saveLocalData(mUserPreference);
                                if (preferences.getBoolean(getString(R.string.isFirstTimeUser), false)) {
                                    mPresenter.savePreferences(mUserPreference);
                                } else {
                                    mPresenter.updatePreferences(mUserPreference);
                                }
                            }
                        } else {
                            //For non logged in user
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_layout, homeFragment);
                            mNavigationView.setCheckedItem(R.id.nav_home);
                            isDirty = false;
                            transaction.commit();
                        }
                    });
            alertDialog.show();
        }
        return true;
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
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.isFirstTimeUser), false);
            editor.apply();
            transaction.commit();
        }
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {

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
