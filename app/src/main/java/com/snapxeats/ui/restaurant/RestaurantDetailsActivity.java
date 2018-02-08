package com.snapxeats.ui.restaurant;

import android.os.Bundle;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;

import butterknife.ButterKnife;

/**
 * Created by Prajakta Patil on 05/02/18.
 */

public class RestaurantDetailsActivity  extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
