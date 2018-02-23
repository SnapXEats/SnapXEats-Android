package com.snapxeats.ui.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import com.snapxeats.LocationBaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeActivity extends LocationBaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeContract.HomeView {

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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame_layout, homeFragment);
        transaction.addToBackStack("HOME");
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                selectedFragment = homeFragment;
                SnapXToast.debug("Home");
                break;
            case R.id.nav_wishlist:
                SnapXToast.debug("WishList");
                break;
            case R.id.nav_preferences:
                selectedFragment = navPrefFragment;
                SnapXToast.debug("Preferences");
                break;
            case R.id.nav_food_journey:
                SnapXToast.debug("Food Journey");
                break;
            case R.id.nav_rewards:
                SnapXToast.debug("Rewards");
                break;
            case R.id.nav_logout:
                SnapXToast.debug("Logout");
                break;
        }

        if (selectedFragment != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
