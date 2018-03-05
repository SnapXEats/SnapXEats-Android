package com.snapxeats.ui.foodstack;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 30/1/18.
 */

public class FoodStackActivity extends BaseActivity
        implements FoodStackContract.FoodStackView, AppContract.SnapXResults {

    @BindView(R.id.activity_main_card_stack_view)
    protected CardStackView cardStackView;

    private SwipeFoodStackAdapter mStackAdapter;

    @BindView(R.id.toolbar_foodstack)
    protected Toolbar mToolbar;

    @Inject
    FoodStackContract.FoodStackPresenter mFoodStackPresenter;

    @BindView(R.id.img_foodstack_placeholder)
    protected ImageView mImgFoodStackPlaceholder;

    private Map<String, List<String>> listHashMap;

    private RootCuisinePhotos rootCuisinePhotos;


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
        setup();

    }

    private void setup() {
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                switch (direction) {
                    case Top: {
//                        swipeTop();
                        break;
                    }
                    case Right: {
                        swipeRight();
                        break;
                    }
                    case Left: {
                        //  swipeLeft();
                        break;
                    }
                }
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
                Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());

                if (direction == SwipeDirection.Left) {

                }

                if (cardStackView.getTopIndex() == mStackAdapter.getCount() - 5) {
                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
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
                        rootCuisinePhotos.getDishesInfo().get(index).getRestaurant_info_id());
                startActivity(intent);
            }
        });
    }

    private void paginate() {
        cardStackView.setPaginationReserved();
        mStackAdapter.addAll(createFoodStack(listHashMap));
        mStackAdapter.notifyDataSetChanged();
    }

    public SwipeFoodStackAdapter createFoodStackAdapter(Map<String, List<String>> stringListMap) {
        final SwipeFoodStackAdapter adapter = new SwipeFoodStackAdapter(FoodStackActivity.this);
        adapter.addAll(createFoodStack(stringListMap));
        return adapter;
    }

    //fetch food images
    private LinkedList<FoodStackData> extractFoodStackImages() {
        LinkedList<FoodStackData> spots = new LinkedList<>();
        for (int i = cardStackView.getTopIndex(); i < mStackAdapter.getCount(); i++) {
            spots.add(mStackAdapter.getItem(i));
        }
        return spots;
    }

    //create foodstack for swiping photos
    private List<FoodStackData> createFoodStack(Map<String, List<String>> stringListMap) {
        List<FoodStackData> spots = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                spots.add(new FoodStackData(key, values.get(i),values.get(i)));
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

    //set foodstack adapter
    private void setStackAdapter() {
        int INDEX_DISH_INFO, INDEX_REST_DISH;
        List<DishesInfo> dishInfo = rootCuisinePhotos.getDishesInfo();
        listHashMap = new HashMap<>();
        List<String> stringsUrl = new ArrayList<>();
        for (INDEX_DISH_INFO = 0; INDEX_DISH_INFO < dishInfo.size(); INDEX_DISH_INFO++) {
            for (INDEX_REST_DISH = 0;
                 INDEX_REST_DISH < dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().size(); INDEX_REST_DISH++) {
                stringsUrl.add(dishInfo.get(INDEX_DISH_INFO).getRestaurantDishes().get(INDEX_REST_DISH).getDish_image_url());
            }
            listHashMap.put(dishInfo.get(INDEX_DISH_INFO).getRestaurant_name(), stringsUrl);
//            listHashMap.put(dishInfo.get(INDEX_DISH_INFO).getRestaurant_info_id(), stringsUrl);

            mStackAdapter = new SwipeFoodStackAdapter(FoodStackActivity.this);
            mStackAdapter = createFoodStackAdapter(listHashMap);
            cardStackView.setAdapter(mStackAdapter);
        }
    }

    //swipe LEFT
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
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
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

        Set<Map.Entry<String,List<String>>> mapSet = listHashMap.entrySet();
        Map.Entry<String,List<String>> elementAt = (new ArrayList<>(mapSet)).get(index);

        Log.v("--**keyyy",elementAt.getKey());

        Intent intent = new Intent(FoodStackActivity.this, RestaurantDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_foodstackRestDetailsId),
                rootCuisinePhotos.getDishesInfo().get(index).getRestaurant_info_id());
        startActivity(intent);
        gestureRight();
    }

    private void gestureRight() {
        View target = cardStackView.getTopView();
        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 0f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 500f,0f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
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
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 0f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 2000f, 0f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
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

    @OnClick(R.id.img_cuisine_reject)
    public void imgCuisineReject() {
        swipeLeft();
    }

    @OnClick(R.id.img_cuisine_wishlist)
    public void imgCuisineWishlist() {
        swipeTop();
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
        Glide.with(getApplicationContext()).pauseRequests();
    }
}
