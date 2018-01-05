package com.example.synerzip.snapxeats.ui.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.synerzip.snapxeats.BaseActivity;
import com.example.synerzip.snapxeats.R;

import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
    }
}
