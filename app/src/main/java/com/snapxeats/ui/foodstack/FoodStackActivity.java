package com.snapxeats.ui.foodstack;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.DishesInfo;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.foodGestures.FoodGestureDislike;
import com.snapxeats.common.model.foodGestures.FoodGestureLike;
import com.snapxeats.common.model.foodGestures.FoodGestureWishlist;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.restaurantDetails.RestaurantDishes;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 30/1/18.
 */

/**
 * This activity is not yet functionally completed
 */

public class FoodStackActivity extends BaseActivity
        implements FoodStackContract.FoodStackView, AppContract.SnapXResults {

    private static final long SET_START_DELAY = 100;
    private static final long SET_DURATION = 500;
    private static final long SET_ROTATION_DURATION = 200;
    private static final float SET_ALPHA = (float) 1.0;
    private static final float LEFT_ROTATION = -10f;
    private static final float LEFT_X_TRANSLATION = -2000f;
    private static final float LEFT_Y_TRANSLATION = 500f;
    private static final float DEFAULT_TRANSLATION = 0f;
    private static final float TOP_Y_TRANSLATION = -500f;
    private static final float SET_ALPHA_DISABLE = (float) 0.5;
    private static final float RIGHT_ROTATION = 10f;
    private static final float RIGHT_X_TRANSLATION = 2000f;
    private static final float RIGHT_Y_TRANSLATION = 500f;

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

    private List<FoodStackData> foodStackDataList;

    private RootFoodGestures mRootFoodGestures;

    private List<String> stringsUrl;

    private int wishListCount;

    @Inject
    AppUtility mAppUtility;

    private List<FoodGestureWishlist> foodGestureWishlist;
    private List<FoodGestureDislike> foodGestureDislike;
    private List<FoodGestureLike> foodGestureLike;
    private LinkedList<FoodStackData> foodDataList;

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
        mRootFoodGestures = new RootFoodGestures();
        stringsUrl = new ArrayList<>();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        foodStackDataList = new ArrayList<>();
        foodGestureWishlist = new ArrayList<>();
        foodGestureDislike = new ArrayList<>();
        foodGestureLike = new ArrayList<>();
        foodDataList = new LinkedList<>();

        SelectedCuisineList selectedCuisineList = getIntent().getExtras().getParcelable(getString(R.string.data_selectedCuisineList));
        assert selectedCuisineList != null;
        if (selectedCuisineList.getSelectedCuisineList().size() != 0) {
            enableGestureActions();
        } else {
            disableGestureActions();
        }

        if (NetworkUtility.isNetworkAvailable(this)) {
            showProgressDialog();
            mFoodStackPresenter.getCuisinePhotos(selectedCuisineList);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
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
        mImgLike.setAlpha(SET_ALPHA_DISABLE);

        mImgDislike.setClickable(false);
        mImgDislike.setAlpha(SET_ALPHA_DISABLE);

        mImgWishlist.setClickable(false);
        mImgWishlist.setAlpha(SET_ALPHA_DISABLE);
    }

    /*Enable Undo button action*/
    public void setUndoEnable() {
        mImgUndo.setClickable(true);
        mImgUndo.setAlpha((float) 1.0);
    }

    /*Disable Undo button action*/
    public void setUndoDisable() {
        mImgUndo.setClickable(false);
        mImgUndo.setAlpha(SET_ALPHA_DISABLE);
    }

    private void setupFoodStack() {
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {

                switch (direction) {
                    case Top: {
                        break;
                    }
                    case Right: {
                        swipeRight();
                        break;
                    }
                    case Left: {
                        setUndoEnable();
                        break;
                    }
                }

                if (cardStackView.getTopIndex() < mStackAdapter.getCount()) {
                    paginate();
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
                Intent intent = new Intent(FoodStackActivity.this, RestaurantInfoActivity.class);
                intent.putExtra(getString(R.string.intent_foodstackRestInfoId), foodStackDataList.get(index).getId());
                startActivity(intent);
            }
        });
    }


    private void paginate() {
        cardStackView.setPaginationReserved();
        mStackAdapter.notifyDataSetChanged();
    }

    private LinkedList<FoodStackData> extractRemainingCards() {
        foodDataList.clear();
        for (int row = cardStackView.getTopIndex(); row < foodStackDataList.size(); row++) {
            foodDataList.add(foodStackDataList.get(row));
        }
        return foodDataList;
    }

    //TODO Undo functionality
    private void addFirst() {
        LinkedList<FoodStackData> spots = extractRemainingCards();
        List<FoodStackData> dislikeList = new ArrayList<>();
        FoodStackData foodStackData;
        for (int row = 0; row < foodGestureDislike.size(); row++) {
            for (int col = 0; col < foodStackDataList.size(); col++) {
                if (foodGestureDislike.get(row).getRestaurant_dish_id().equalsIgnoreCase(foodStackDataList.get(col).getDishId())) {

                    foodStackData = new FoodStackData(foodStackDataList.get(col).getName(),
                            foodStackDataList.get(col).getId(),
                            foodStackDataList.get(col).getUrl(),
                            foodStackDataList.get(col).getDishId());

                    dislikeList.add(foodStackData);
                }
            }
        }
        /*for (int row = dislikeList.size(); row > 0; row--) {
            spots.addFirst(foodStackDataList.get(row));
            mStackAdapter.clear();
            mStackAdapter.addAll(foodDataList);
            mStackAdapter.notifyDataSetChanged();
            return;
        }*/
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
        if (dishInfo.size() != 0) {
            for (rowIndex = 0; rowIndex < dishInfo.size(); rowIndex++) {
                DishesInfo dish = dishInfo.get(rowIndex);
                for (colIndex = 0; colIndex < dish.getRestaurantDishes().size(); colIndex++) {
                    RestaurantDishes restaurantDishes = dish.getRestaurantDishes().get(colIndex);
                    stringsUrl.add(restaurantDishes.getDish_image_url());
                    FoodStackData foodStackData = new FoodStackData(dish.getRestaurant_name(),
                            dish.getRestaurant_info_id(), stringsUrl, dish.getRestaurantDishes().get(colIndex).getRestaurant_dish_id());
                    foodStackDataList.add(foodStackData);

                }
            }
            mStackAdapter = new FoodStackAdapter(FoodStackActivity.this, foodStackDataList);
            cardStackView.setAdapter(mStackAdapter);
        }
    }

    /*swipe LEFT*/
    public void swipeLeft() {
        setUndoEnable();
        int index = cardStackView.getTopIndex();
        FoodGestureDislike foodDislikeItem;
        if (isLoggedIn()) {
            foodDislikeItem = new FoodGestureDislike();
            foodDislikeItem.setRestaurant_dish_id(foodStackDataList.get(index).getDishId());
            foodGestureDislike.add(foodDislikeItem);
        }
        gestureLeft();
    }

    //swipe TOP
    public void swipeTop() {
        int index = cardStackView.getTopIndex();
        wishListCount++;
        FoodGestureWishlist foodGestureWishItem;
        if (isLoggedIn()) {
            foodGestureWishItem = new FoodGestureWishlist();
            foodGestureWishItem.setRestaurant_dish_id(foodStackDataList.get(index).getDishId());
            foodGestureWishlist.add(foodGestureWishItem);
            mRootFoodGestures.setWishlist_dish_array(foodGestureWishlist);
            mFoodStackPresenter.saveGesturesToDb(Integer.toString(wishListCount), mRootFoodGestures);
        }
        gestureTop();
    }

    //swipe RIGHT
    public void swipeRight() {
        List<FoodStackData> data = extractRemainingCards();
        if (data.isEmpty()) {
            return;
        }
        int index = cardStackView.getTopIndex();
        Intent intent = new Intent(FoodStackActivity.this, RestaurantDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_foodstackRestDetailsId),
                foodStackDataList.get(index).getId());
        startActivity(intent);
        gestureRight();
    }

    private void gestureRight() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), RIGHT_ROTATION));
        rotation.setDuration(SET_ROTATION_DURATION);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationX), DEFAULT_TRANSLATION, RIGHT_X_TRANSLATION));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationY), DEFAULT_TRANSLATION, RIGHT_Y_TRANSLATION));
        translateX.setStartDelay(SET_START_DELAY);
        translateY.setStartDelay(SET_START_DELAY);
        translateX.setDuration(SET_DURATION);
        translateY.setDuration(SET_DURATION);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);
    }

    private void gestureLeft() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), LEFT_ROTATION));
        rotation.setDuration(SET_ROTATION_DURATION);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationX), DEFAULT_TRANSLATION, LEFT_X_TRANSLATION));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationY), DEFAULT_TRANSLATION, LEFT_Y_TRANSLATION));
        translateX.setStartDelay(SET_START_DELAY);
        translateY.setStartDelay(SET_START_DELAY);
        translateX.setDuration(SET_DURATION);
        translateY.setDuration(SET_DURATION);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);
        cardStackView.swipe(SwipeDirection.Left, set);
    }

    private void gestureTop() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), DEFAULT_TRANSLATION));
        rotation.setDuration(SET_ROTATION_DURATION);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationX), DEFAULT_TRANSLATION, DEFAULT_TRANSLATION));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.translationY), DEFAULT_TRANSLATION, TOP_Y_TRANSLATION));
        translateX.setStartDelay(SET_START_DELAY);
        translateY.setStartDelay(SET_START_DELAY);
        translateX.setDuration(SET_DURATION);
        translateY.setDuration(SET_DURATION);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);
        cardStackView.swipe(SwipeDirection.Top, set);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.img_foodstack_map)
    public void imgMaps() {
    }

    @OnClick(R.id.img_cuisine_like)
    public void imgCuisineLike() {
        swipeRight();
    }

    @OnClick(R.id.img_cuisine_dislike)
    public void imgCuisineReject() {
        swipeLeft();
    }

    @OnClick(R.id.img_cuisine_wishlist)
    public void imgCuisineWishlist() {
        swipeTop();
    }

    @OnClick(R.id.img_cuisine_undo)
    public void imgCuisineUndo() {
        setUndoDisable();
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
