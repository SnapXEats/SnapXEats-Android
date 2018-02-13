package com.snapxeats.ui.foodstack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

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
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.snapxeats.common.Router.Screen.PREFERENCE;
import static com.snapxeats.common.Router.Screen.RESTAURANT_DETAILS;

/**
 * Created by Prajakta Patil on 30/1/18.
 */

public class FoodStackActivity extends BaseActivity
        implements FoodStackContract.FoodStackView, AppContract.SnapXResults {

    @BindView(R.id.swipe_view)
    protected SwipeDirectionalView mSwipeView;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.img_foodstack_placeholder)
    protected ImageView mImgFoodStackPlaceholder;

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        SelectedCuisineList selectedCuisineList;
        selectedCuisineList = getIntent().getExtras().getParcelable(getString(R.string.data_selectedCuisineList));

        if (NetworkUtility.isNetworkAvailable(this)) {
            mFoodStackPreseneter.getCuisinePhotos(this, selectedCuisineList);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
        mSwipeView.getBuilder()
                .setIsUndoEnabled(true)
                .setDisplayViewCount(5)//stack will show max 10 images
                .setSwipeVerticalThreshold(Utils.dpToPx(50))
                .setSwipeHorizontalThreshold(Utils.dpToPx(50))
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        mSwipeView.addItemRemoveListener(count -> {
            //TODO load more images from server when count is zero
        });

        mToolbar.setNavigationOnClickListener(v -> mFoodStackPreseneter.presentScreen(PREFERENCE));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /**
     * get dishes info
     *
     * @param value
     */
    public void success(Object value) {
        mImgFoodStackPlaceholder.setVisibility(View.GONE);
        RootCuisinePhotos rootCuisinePhotos = (RootCuisinePhotos) value;
        dismissProgressDialog();
        int INDEX_DISH_INFO, INDEX_REST_DISH;
        List<DishesInfo> dishInfo = rootCuisinePhotos.getDishesInfo();
        Map<String, List<String>> listHashMap = new HashMap<>();
        List<String> strings = new ArrayList<>();
        for (INDEX_DISH_INFO = 0; INDEX_DISH_INFO < dishInfo.size(); INDEX_DISH_INFO++) {
            for (INDEX_REST_DISH = 0;
                 INDEX_REST_DISH < dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().size(); INDEX_REST_DISH++) {
                strings.add(dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().get(INDEX_REST_DISH).getDish_image_url());
            }
            listHashMap.put(dishInfo.get(INDEX_DISH_INFO).getRestaurant_name(), strings);
            //set dishname and image in swipeview
            mSwipeView.addView(new TinderDirectionalCard(this, listHashMap, mSwipeView));
        }
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        showNetworkErrorDialog((dialog, which) -> {
        });
    }

    @Override
    public void networkError(Object value) {

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
