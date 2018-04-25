package com.snapxeats.ui.foodstack;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.DishesInfo;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.foodGestures.FoodDislikes;
import com.snapxeats.common.model.foodGestures.FoodLikes;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.restaurantDetails.RestaurantDishes;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.maps.MapsActivity;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import static com.snapxeats.common.constants.UIConstants.SET_ALPHA;

/**
 * Created by Prajakta Patil on 30/1/18.
 */

/**
 * This activity is not yet functionally completed
 */

public class FoodStackActivity extends BaseActivity
        implements FoodStackContract.FoodStackView, AppContract.SnapXResults {

    @BindView(R.id.activity_main_card_stack_view)
    protected CardStackView cardStackView;

    private FoodStackAdapter mStackAdapter;

    @BindView(R.id.toolbar_foodstack)
    protected Toolbar mToolbar;

    @Inject
    FoodStackContract.FoodStackPresenter mFoodStackPresenter;

    @BindView(R.id.img_foodstack_placeholder)
    protected ImageView mImgFoodStackPlaceholder;

    private RootCuisinePhotos rootCuisinePhotos;

    @BindView(R.id.img_cuisine_like)
    protected ImageView mImgLike;

    @BindView(R.id.img_cuisine_dislike)
    protected ImageView mImgDislike;

    @BindView(R.id.img_cuisine_undo)
    protected ImageView mImgUndo;

    @BindView(R.id.img_cuisine_wishlist)
    protected ImageView mImgWishlist;

    private ArrayList<FoodStackData> foodStackDataList;

    private List<String> stringsUrl;

    @Inject
    AppUtility mAppUtility;

    private List<FoodWishlists> foodGestureWishlist;

    private List<FoodDislikes> foodGestureDislike;

    private List<FoodLikes> foodLikes;

    private LinkedList<FoodStackData> foodDataList;

    private AnimatorSet mAnimatorSet;

    @Inject
    DbHelper mDbHelper;

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    @BindView(R.id.img_foodstack_map)
    protected ImageView mImgMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        mFoodStackPresenter.addView(this);
        stringsUrl = new ArrayList<>();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        foodStackDataList = new ArrayList<>();
        foodGestureWishlist = new ArrayList<>();
        foodGestureDislike = new ArrayList<>();
        foodLikes = new ArrayList<>();
        foodDataList = new LinkedList<>();
        mAnimatorSet = new AnimatorSet();
        foodGestureDislike = foodStackDbHelper.getFoodDislikes();

        //Api call for saved food preferences
        foodGestures();

        SelectedCuisineList selectedCuisineList = getIntent().getExtras()
                .getParcelable(getString(R.string.data_selectedCuisineList));

        assert null != selectedCuisineList;
        if (0 != selectedCuisineList.getSelectedCuisineList().size()) {
            enableGestureActions();
        }

        if (NetworkUtility.isNetworkAvailable(this)) {
            showProgressDialog();
            mFoodStackPresenter.getCuisinePhotos(selectedCuisineList);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    private void foodGestures() {
        foodGestureWishlist.addAll(foodStackDbHelper.getFoodWishList());

        RootFoodGestures mRootFoodGestures = new RootFoodGestures();
        mRootFoodGestures.setWishlist_dish_array(foodStackDbHelper.getFoodWishList());
        mRootFoodGestures.setDislike_dish_array(foodStackDbHelper.getFoodDislikes());
        mRootFoodGestures.setLike_dish_array(foodStackDbHelper.getFoodLikes());

        //If all list are empty API shouldn't call
        if (0 != mRootFoodGestures.getDislike_dish_array().size()
                || 0 != mRootFoodGestures.getWishlist_dish_array().size()
                || 0 != mRootFoodGestures.getLike_dish_array().size()) {
            mFoodStackPresenter.foodstackGestures(mRootFoodGestures);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFoodStack();
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = mAppUtility.getSharedPreferences();
        String serverUserId = preferences.getString(getString(R.string.user_id), "");
        return !serverUserId.isEmpty();
    }

    public void enableGestureActions() {
        mImgLike.setClickable(true);
        mImgLike.setAlpha(SET_ALPHA);

        mImgDislike.setClickable(true);
        mImgDislike.setAlpha(SET_ALPHA);

        mImgWishlist.setClickable(true);
        mImgWishlist.setAlpha(SET_ALPHA);
    }

    public void disableGestureActions() {
        mImgLike.setClickable(false);
        mImgLike.setAlpha(UIConstants.SET_ALPHA_DISABLE);

        mImgDislike.setClickable(false);
        mImgDislike.setAlpha(UIConstants.SET_ALPHA_DISABLE);

        mImgWishlist.setClickable(false);
        mImgWishlist.setAlpha(UIConstants.SET_ALPHA_DISABLE);
    }

    /*Enable Undo button action*/
    public void enableUndo() {
        mImgUndo.setClickable(true);
        mImgUndo.setAlpha((float) 1.0);
    }

    /*Disable Undo button action*/
    public void disableUndo() {
        mImgUndo.setClickable(false);
        mImgUndo.setAlpha(UIConstants.SET_ALPHA_DISABLE);
    }

    private void setupFoodStack() {
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                /*disable gestures buttons if stack is empty*/
                if (cardStackView.getTopIndex() == foodStackDataList.size()) {
                    disableGestureActions();
                }
                if (0 == foodGestureDislike.size()) {
                    disableUndo();
                }
                switch (direction) {
                    case Top: {
                        swipeTop(cardStackView.getTopIndex() - 1);
                        break;
                    }
                    case Right: {
                        swipeRight(cardStackView.getTopIndex() - 1);
                        break;
                    }
                    case Left: {
                        break;
                    }
                }
            }

            @Override
            public void onCardReversed() {
            }

            @Override
            public void onCardMovedToOrigin() {
            }

            @Override
            public void onCardClicked(int index) {
                Intent intent = new Intent(FoodStackActivity.this,
                        RestaurantInfoActivity.class);
                intent.putExtra(getString(R.string.intent_foodstackRestInfoId), foodStackDataList.get(index).getId());
                startActivity(intent);
            }
        });
    }

    private LinkedList<FoodStackData> extractRemainingCards() {
        foodDataList.clear();
        for (int row = cardStackView.getTopIndex(); row < foodStackDataList.size(); row++) {
            foodDataList.add(foodStackDataList.get(row));
        }
        return foodDataList;
    }

    /**
     * get dishes info
     *
     * @param value
     */
    public void success(Object value) {
        mImgFoodStackPlaceholder.setVisibility(View.GONE);
        dismissProgressDialog();
        rootCuisinePhotos = (RootCuisinePhotos) value;
        setStackAdapter();
    }

    /*set foodstack adapter*/
    private void setStackAdapter() {
        int rowIndex, colIndex;
        List<DishesInfo> dishInfo = rootCuisinePhotos.getDishesInfo();
        if (0 != dishInfo.size()) {
            for (rowIndex = 0; rowIndex < dishInfo.size(); rowIndex++) {
                DishesInfo dish = dishInfo.get(rowIndex);
                for (colIndex = 0; colIndex < dish.getRestaurantDishes().size(); colIndex++) {
                    RestaurantDishes restaurantDishes = dish.getRestaurantDishes().get(colIndex);
                    stringsUrl.add(restaurantDishes.getDish_image_url());
                    FoodStackData foodStackData = new FoodStackData(dish.getRestaurant_name(),
                            dish.getRestaurant_info_id(), stringsUrl,
                            dish.getRestaurantDishes().get(colIndex).getRestaurant_dish_id());
                    foodStackDataList.add(foodStackData);
                }
            }
            mStackAdapter = new FoodStackAdapter(FoodStackActivity.this, foodStackDataList);
            cardStackView.setAdapter(mStackAdapter);
        }
    }

    /*swipe LEFT*/
    public void swipeLeft(int index) {
        List<FoodStackData> data = extractRemainingCards();
        if (data.isEmpty()) {
            return;
        }
        enableUndo();
        FoodDislikes foodDislikeItem;
        if (isLoggedIn()) {
            foodDislikeItem = new FoodDislikes();
            foodDislikeItem.setRestaurant_dish_id(foodStackDataList.get(index).getDishId());
            foodGestureDislike.add(foodDislikeItem);
            mFoodStackPresenter.saveDislikeToDb(foodGestureDislike);
        }
        gestureLeft();
    }

    /*swipe TOP*/
    public void swipeTop(int index) {
        disableUndo();
        List<FoodStackData> data = extractRemainingCards();
        if (data.isEmpty()) {
            return;
        }
        FoodWishlists foodGestureWishItem;
        if (isLoggedIn()) {
            foodGestureWishItem = new FoodWishlists();
            foodGestureWishItem.setRestaurant_dish_id(foodStackDataList.get(index).getDishId());
            foodGestureWishlist.add(foodGestureWishItem);
            mFoodStackPresenter.saveWishlistToDb(foodGestureWishlist);
        }
        gestureTop();
    }

    //swipe RIGHT
    public void swipeRight(int index) {
        List<FoodStackData> data = extractRemainingCards();
        if (data.isEmpty()) {
            return;
        }
        FoodLikes foodGestureLikesItem;
        if (isLoggedIn()) {
            foodGestureLikesItem = new FoodLikes();
            foodGestureLikesItem.
                    setRestaurant_dish_id(foodStackDataList.get(cardStackView.getTopIndex()).getDishId());
            foodLikes.add(foodGestureLikesItem);
            mFoodStackPresenter.saveLikesToDb(foodLikes);
        }
        Intent intent = new Intent(FoodStackActivity.this, RestaurantDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_restaurant_id), foodStackDataList.get(index).getId());
        startActivity(intent);
        gestureRight();
    }

    private void gestureRight() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), UIConstants.RIGHT_ROTATION));
        rotation.setDuration(UIConstants.SET_ROTATION_DURATION);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationX), UIConstants.DEFAULT_TRANSLATION,
                        UIConstants.RIGHT_X_TRANSLATION));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationY), UIConstants.DEFAULT_TRANSLATION,
                        UIConstants.RIGHT_Y_TRANSLATION));
        translateX.setStartDelay(UIConstants.SET_START_DELAY);
        translateY.setStartDelay(UIConstants.SET_START_DELAY);
        translateX.setDuration(UIConstants.SET_DURATION);
        translateY.setDuration(UIConstants.SET_DURATION);
        mAnimatorSet.playTogether(rotation, translateX, translateY);
    }

    private void gestureLeft() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), UIConstants.LEFT_ROTATION));
        rotation.setDuration(UIConstants.SET_ROTATION_DURATION);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationX), UIConstants.DEFAULT_TRANSLATION,
                        UIConstants.LEFT_X_TRANSLATION));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationY), UIConstants.DEFAULT_TRANSLATION,
                        UIConstants.LEFT_Y_TRANSLATION));
        translateX.setStartDelay(UIConstants.SET_START_DELAY);
        translateY.setStartDelay(UIConstants.SET_START_DELAY);
        translateX.setDuration(UIConstants.SET_DURATION);
        translateY.setDuration(UIConstants.SET_DURATION);
        mAnimatorSet.playTogether(rotation, translateX, translateY);
        cardStackView.swipe(SwipeDirection.Left, mAnimatorSet);
    }

    private void gestureTop() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), UIConstants.DEFAULT_TRANSLATION));
        rotation.setDuration(UIConstants.SET_ROTATION_DURATION);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationX), UIConstants.DEFAULT_TRANSLATION,
                        UIConstants.DEFAULT_TRANSLATION));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationY), UIConstants.DEFAULT_TRANSLATION,
                        UIConstants.TOP_Y_TRANSLATION));
        translateX.setStartDelay(UIConstants.SET_START_DELAY);
        translateY.setStartDelay(UIConstants.SET_START_DELAY);
        translateX.setDuration(UIConstants.SET_DURATION);
        translateY.setDuration(UIConstants.SET_DURATION);
        mAnimatorSet.playTogether(rotation, translateX, translateY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.img_foodstack_map)
    public void imgMaps() {
        if (mStackAdapter.getCount() != 0) {
            mImgMap.setAlpha(SET_ALPHA);
            Intent intent = new Intent(FoodStackActivity.this, MapsActivity.class);
            intent.putExtra(getString(R.string.intent_root_cuisine), rootCuisinePhotos);
            startActivity(intent);
        }
    }

    @OnClick(R.id.img_cuisine_like)
    public void imgCuisineLike() {
        swipeRight(cardStackView.getTopIndex());
    }

    @OnClick(R.id.img_cuisine_dislike)
    public void imgCuisineReject() {
        swipeLeft(cardStackView.getTopIndex());
    }

    @OnClick(R.id.img_cuisine_wishlist)
    public void imgCuisineWishlist() {
        swipeTop(cardStackView.getTopIndex());
    }

    @OnClick(R.id.img_cuisine_undo)
    public void imgCuisineUndo() {
        cardStackView.reverse();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
