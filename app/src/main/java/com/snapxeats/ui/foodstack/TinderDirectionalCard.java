package com.snapxeats.ui.foodstack;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.mindorks.placeholderview.SwipeDirection;
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
import com.snapxeats.common.utilities.SnapXDialog;
import com.squareup.picasso.Picasso;

/**
 * Created by Snehal Tembare on 24/1/18.
 */

@NonReusable
@Layout(R.layout.tinder_card_view)
public class TinderDirectionalCard {


    @View(R.id.image_view)
    private ImageView mImageView;

    private Context mContext;

    public TinderDirectionalCard(Context context) {
        this.mContext = context;
    }

    @Click(R.id.image_view)
    private void onClick() {
        Log.d("DEBUG", "profileImageView");
    }

    @Resolve
    private void onResolve() {
        Picasso.with(mContext).load("https://s3.us-east-2.amazonaws.com/snapxeats/english.jpg").into(mImageView);

    }

    @SwipeOutDirectional
    private void onSwipeOutDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeOutDirectional " + direction.name());
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState");
    }

    @SwipeInDirectional
    private void onSwipeInDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeInDirectional " + direction.name());
    }

    @SwipingDirection
    private void onSwipingDirection(SwipeDirection direction) {
        Log.d("DEBUG", "SwipingDirection " + direction.name());
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
