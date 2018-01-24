package com.snapxeats.ui.foodstack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.Utils;
import com.snapxeats.R;

import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 23/1/18.
 */
public class FoodStackActivity extends AppCompatActivity {

    @BindView(R.id.swipe_view)
    protected SwipeDirectionalView mSwipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stack);
        ButterKnifeLite.bind(this);
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setSwipeVerticalThreshold(Utils.dpToPx(50))
                .setSwipeHorizontalThreshold(Utils.dpToPx(50))
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
        initView();
    }

    private void initView() {

        mSwipeView.addView(new TinderDirectionalCard(this))
                .addView(new TinderDirectionalCard(this))
                .addView(new TinderDirectionalCard(this))
                .addView(new TinderDirectionalCard(this))
                .addView(new TinderDirectionalCard(this));
    }
}
