package com.snapxeats.ui.foodstack;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
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

    private HashMap<String, List<String>> listHashMap;

    @View(R.id.txtRestaurantName)
    private TextView mTxtDishName;

    private SwipePlaceHolderView mSwipeView;

    public TinderDirectionalCard(Context context, HashMap<String, List<String>> listHashMap,
                                 SwipePlaceHolderView mSwipeView) {
        this.mContext = context;
        this.listHashMap = listHashMap;
        this.mSwipeView = mSwipeView;
    }

    @Click(R.id.image_view)
    private void onClick() {
        Log.d("DEBUG", "profileImageView");
        mContext.startActivity(new Intent(mContext, RestaurantInfoActivity.class));
    }

    @Resolve
    private void onResolve() {
        for (Map.Entry<String, List<String>> entry : listHashMap.entrySet()) {
            String key = entry.getKey();
            mTxtDishName.setText(key);
            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                Picasso.with(mContext).load(values.get(i)).into(mImageView);
            }
        }
    }

    @SwipeOutDirectional
    private void onSwipeOutDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeOutDirectional " + direction.name());
        SnapXToast.showToast(mContext, "LEFT");
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState");
    }

    @SwipeInDirectional
    private void onSwipeInDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeInDirectional " + direction.name());
        SnapXToast.showToast(mContext, "RIGHT");
        mContext.startActivity(new Intent(mContext, RestaurantDetailsActivity.class));
    }

    @SwipingDirection
    private void onSwipingDirection(SwipeDirection direction) {
        Log.d("DEBUG", "SwipingDirection " + direction.name());
        if (direction.name() == "TOP") {
            SnapXToast.showToast(mContext, "WISHLIST");
        }

    }

    @SwipeTouch
    private void onSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {
        Log.d("DEBUG", "onSwipeTouch "
                + " xStart : " + xStart
                + " yStart : " + yStart
                + " xCurrent : " + xCurrent
                + " yCurrent : " + yCurrent
                + " distance : "
                + Math.sqrt(Math.pow(xCurrent - xStart, 2) + (Math.pow(yCurrent - yStart, 2)))
        );
    }
}
