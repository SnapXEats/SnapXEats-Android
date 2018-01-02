package com.example.synerzip.snapxeats;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.synerzip.snapxeats.common.Router;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public abstract class BaseActivity extends DaggerAppCompatActivity {

    /**
     * The singleton Navigator object of the application
     */
    @Inject
    Router router;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        router.setActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        router.setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        router.setActivity(this);
    }

}