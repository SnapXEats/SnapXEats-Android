package com.snapxeats.ui.foodstack;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.Utils;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.DishesInfo;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.dagger.AppContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.snapxeats.common.Router.Screen.RESTAURANT_DETAILS;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
public class FoodStackActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FoodStackContract.FoodStackView,
        AppContract.SnapXResults {

    @BindView(R.id.swipe_view)
    protected SwipeDirectionalView mSwipeView;

    private DrawerLayout mDrawerLayout;

    @Inject
    FoodStackContract.FoodStackPreseneter mFoodStackPreseneter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stack);
        ButterKnifeLite.bind(this);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public void initView() {
        mFoodStackPreseneter.addView(this);
        SelectedCuisineList selectedCuisineList;
        selectedCuisineList = getIntent().getExtras().getParcelable("key");

        //get cuisine photos
        mFoodStackPreseneter.getCuisinePhotos(this, selectedCuisineList);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mSwipeView.getBuilder()
                .setIsUndoEnabled(true)
                .setDisplayViewCount(20)//stack will have max 10 images
                .setSwipeVerticalThreshold(Utils.dpToPx(50))
                .setSwipeHorizontalThreshold(Utils.dpToPx(50))
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        mSwipeView.addItemRemoveListener(count -> {
            SnapXToast.showToast(this, "count--->" + count);
            //TODO load more images from server when count is zero
        });
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_wishlist:
                SnapXToast.debug("WishList");
                break;
            case R.id.nav_preferences:
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

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void success() {

    }

    @Override
    public void error() {

    }

    @Override
    public void noNetwork() {

    }

    @Override
    public void networkError() {

    }

    /**
     * get foodstack photos
     *
     * @param rootCuisinePhotos
     */
    @Override
    public void getCuisinePhotoInfo(RootCuisinePhotos rootCuisinePhotos) {
        dismissProgressDialog();
        int INDEX_DISH_INFO, INDEX_REST_DISH;
        List<DishesInfo> dishInfo = rootCuisinePhotos.getDishesInfo();
        HashMap<String, List<String>> listHashMap = new HashMap<>();
        List<String> strings = new ArrayList<>();
        for (INDEX_DISH_INFO = 0; INDEX_DISH_INFO < dishInfo.size(); INDEX_DISH_INFO++) {
            for (INDEX_REST_DISH = 0; INDEX_REST_DISH < dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().size(); INDEX_REST_DISH++) {
                strings.add(dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().get(INDEX_REST_DISH).getDish_image_url());
            }
            listHashMap.put(dishInfo.get(INDEX_DISH_INFO).getRestaurant_name(), strings);
            //set dishname and image in swipeview
            mSwipeView.addView(new TinderDirectionalCard(this, listHashMap, mSwipeView));
        }
    }

    @OnClick(R.id.img_cuisine_like)
    public void imgCuisineLike() {
        mSwipeView.doSwipe(true);
        mFoodStackPreseneter.presentScreen(RESTAURANT_DETAILS);
    }

    @OnClick(R.id.img_cuisine_reject)
    public void imgCuisineReject() {
        mSwipeView.doSwipe(false);
    }

    @OnClick(R.id.img_cuisine_wishlist)
    public void imgCuisineWishlist() {
        //TODO favourite items to save
        SnapXToast.showToast(this, "Wishlist");
    }

    @OnClick(R.id.img_cuisine_undo)
    public void imgCuisineUndo() {
        //TODO reload image after undo action
        mSwipeView.undoLastSwipe();
        SnapXToast.showToast(this, "Undo");
    }

}
