package com.snapxeats;

import android.app.Activity;

import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public interface BaseView<T> extends AppContract.DialogView{
    Activity getActivity();

    void initView();
}