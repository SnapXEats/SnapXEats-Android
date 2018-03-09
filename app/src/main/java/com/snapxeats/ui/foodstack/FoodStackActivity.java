package com.snapxeats.ui.foodstack;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.DishesInfo;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private static final float SET_ALPHA_UNDO = (float) 0.5;
    private static final float RIGHT_ROTATION = 10f;
    private static final float RIGHT_X_TRANSLATION = 2000f;
    private static final float RIGHT_Y_TRANSLATION = 500f;

    @BindView(R.id.activity_main_card_stack_view)
    protected CardStackView cardStackView;

    private SwipeFoodStackAdapter mStackAdapter;

    @BindView(R.id.toolbar_foodstack)
    protected Toolbar mToolbar;

    @Inject
    FoodStackContract.FoodStackPresenter mFoodStackPresenter;

    @BindView(R.id.img_foodstack_placeholder)
    protected ImageView mImgFoodStackPlaceholder;

    private LinkedHashMap<FoodStackData, List<String>> listHashMap;

    private RootCuisinePhotos rootCuisinePhotos;

    @BindView(R.id.img_cuisine_like)
    protected ImageView mImgLike;

    @BindView(R.id.img_cuisine_dislike)
    protected ImageView mImgDislike;

    @BindView(R.id.img_cuisine_undo)
    protected ImageView mImgUndo;

    @BindView(R.id.img_cuisine_wishlist)
    protected ImageView mImgLWishlist;

    private FoodStackData foodStackData;

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SelectedCuisineList selectedCuisineList;
        selectedCuisineList = getIntent().getExtras().getParcelable(getString(R.string.data_selectedCuisineList));

        if (selectedCuisineList.getSelectedCuisineList().size() != 0) {
            /*Enable button actions*/
            setButtonActions();
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

    public void setButtonActions() {
        mImgLike.setClickable(true);
        mImgLike.setAlpha(SET_ALPHA);

        mImgDislike.setClickable(true);
        mImgDislike.setAlpha(SET_ALPHA);

        mImgLWishlist.setClickable(true);
        mImgLWishlist.setAlpha(SET_ALPHA);
    }

    private void setupFoodStack() {
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {

                /*Enable Undo button action*/
                mImgUndo.setClickable(true);
                mImgUndo.setAlpha((float) 1.0);

                switch (direction) {
                    case Top: {
                        swipeTop();
                        break;
                    }
                    case Right: {
                        swipeRight();
                        break;
                    }
                    case Left: {
                        swipeLeft();
                        break;
                    }
                }

                if (cardStackView.getTopIndex() == mStackAdapter.getCount() - 5) {
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
                intent.putExtra(getString(R.string.intent_foodstackRestInfoId),
                        foodStackData.getId());
                startActivity(intent);
            }
        });
    }

    private void paginate() {
        cardStackView.setPaginationReserved();
        mStackAdapter.addAll(createFoodStack(listHashMap));
        mStackAdapter.notifyDataSetChanged();
    }

    public SwipeFoodStackAdapter createFoodStackAdapter(Map<FoodStackData, List<String>> stringListMap) {
        final SwipeFoodStackAdapter adapter = new SwipeFoodStackAdapter(FoodStackActivity.this);
        adapter.addAll(createFoodStack(stringListMap));
        return adapter;
    }

    /*fetch food images*/
    private LinkedList<FoodStackData> extractFoodStackImages() {
        LinkedList<FoodStackData> spots = new LinkedList<>();
        int INDEX_FOOD_ITEM;
        for (INDEX_FOOD_ITEM = cardStackView.getTopIndex(); INDEX_FOOD_ITEM < mStackAdapter.getCount(); INDEX_FOOD_ITEM++) {
            spots.add(mStackAdapter.getItem(INDEX_FOOD_ITEM));
        }
        return spots;
    }

    /*create foodstack for swiping photos*/
    private List<FoodStackData> createFoodStack(Map<FoodStackData, List<String>> stringListMap) {
        List<FoodStackData> spots = new ArrayList<>();
        for (Map.Entry<FoodStackData, List<String>> entry : stringListMap.entrySet()) {
            String key = entry.getKey().getName();
            List<String> values = entry.getValue();
            int INDEX_MAPLIST;
            for (INDEX_MAPLIST = 0; INDEX_MAPLIST < values.size(); INDEX_MAPLIST++) {
                spots.add(new FoodStackData(key, values.get(INDEX_MAPLIST)));
            }
        }
        return spots;
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
        int INDEX_DISH_INFO, INDEX_REST_DISH;
        List<DishesInfo> dishInfo = rootCuisinePhotos.getDishesInfo();
        listHashMap = new LinkedHashMap<>();
        List<String> stringsUrl = new ArrayList<>();
        for (INDEX_DISH_INFO = 0; INDEX_DISH_INFO < dishInfo.size(); INDEX_DISH_INFO++) {
            for (INDEX_REST_DISH = 0;
                 INDEX_REST_DISH < dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().size(); INDEX_REST_DISH++) {
                stringsUrl.add(dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().get(INDEX_REST_DISH).getDish_image_url());
                foodStackData = new FoodStackData(dishInfo.get(INDEX_DISH_INFO).getRestaurant_name(),
                        dishInfo.get(INDEX_DISH_INFO).getRestaurant_info_id());
            }
            listHashMap.put(foodStackData, stringsUrl);
            mStackAdapter = new SwipeFoodStackAdapter(FoodStackActivity.this);
            mStackAdapter = createFoodStackAdapter(listHashMap);
            cardStackView.setAdapter(mStackAdapter);
        }
    }

    /*swipe LEFT*/
    public void swipeLeft() {
        List<FoodStackData> data = extractFoodStackImages();
        if (data.isEmpty()) {
            return;
        }
        gestureLeft();
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

    //swipe RIGHT
    public void swipeRight() {
        List<FoodStackData> data = extractFoodStackImages();
        if (data.isEmpty()) {
            return;
        }
        int index = cardStackView.getTopIndex();
        Set keyset = listHashMap.keySet();

        Intent intent = new Intent(FoodStackActivity.this, RestaurantDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_foodstackRestDetailsId),
                foodStackData.getId());
        startActivity(intent);
        gestureRight();
    }

    private void gestureRight() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), RIGHT_ROTATION));
        rotation.setDuration(200);
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

    //swipe TOP
    public void swipeTop() {
        List<FoodStackData> data = extractFoodStackImages();
        if (data.isEmpty()) {
            return;
        }
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat(getString(R.string.rotation), DEFAULT_TRANSLATION));
        rotation.setDuration(200);
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

    private void addLast() {
        LinkedList<FoodStackData> spots = extractFoodStackImages();
        // spots.addLast(createFoodStack(listHashMap.get(0)));
        mStackAdapter.clear();
        mStackAdapter.addAll(spots);
        mStackAdapter.notifyDataSetChanged();
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
        mImgUndo.setClickable(false);
        mImgUndo.setAlpha(SET_ALPHA_UNDO);
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
        Glide.with(getApplicationContext()).pauseRequests();
    }
}
