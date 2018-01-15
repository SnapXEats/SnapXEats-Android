package com.example.synerzip.snapxeats;

import android.app.Activity;
import android.content.DialogInterface;

import com.example.synerzip.snapxeats.dagger.AppContract;
import com.example.synerzip.snapxeats.ui.preferences.PreferenceContract;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public interface BaseView<T> extends AppContract.DialogView{
    Activity getActivity();

    void initView();
}