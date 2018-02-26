package com.snapxeats.ui.foodstack;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;
import com.snapxeats.R;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Snehal Tembare on 24/1/18.
 */

@NonReusable
@Layout(R.layout.tinder_card_view)
public class TinderDirectionalCard {

    @View(R.id.image_view)
    private ImageView mImageView;

    private Context mContext;

    private Map<String, List<String>> listHashMap;

    @View(R.id.txtRestaurantName)
    private TextView mTxtDishName;

    private SwipePlaceHolderView mSwipeView;

    public TinderDirectionalCard(Context context, Map<String, List<String>> listHashMap,
                                 SwipePlaceHolderView mSwipeView) {
        this.mContext = context;
        this.listHashMap = listHashMap;
        this.mSwipeView = mSwipeView;
    }

    @Click(R.id.image_view)
    private void onClick() {
        mContext.startActivity(new Intent(mContext, RestaurantInfoActivity.class));
    }

    @Resolve
    private void onResolve() {
        for (Map.Entry<String, List<String>> entry : listHashMap.entrySet()) {
            String key = entry.getKey();
            mTxtDishName.setText(key);
            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                Picasso.with(mContext).load(values.get(i)).placeholder(R.drawable.ic_cuisine_placeholder).into(mImageView);
            }
        }
    }

    @SwipeOutDirectional
    private void onSwipeOutDirectional(SwipeDirection direction) {
    }

    @SwipeCancelState
    private void onSwipeCancelState() {

    }

    @SwipeInDirectional
    private void onSwipeInDirectional(SwipeDirection direction) {
        mContext.startActivity(new Intent(mContext, RestaurantDetailsActivity.class));
    }

    @SwipingDirection
    private void onSwipingDirection(SwipeDirection direction) {
    }

    @SwipeTouch
    private void onSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {

    }
}
