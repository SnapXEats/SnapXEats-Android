package com.snapxeats.ui.home.fragment.smartphotos.smart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snapxeats.BaseFragment;
import com.snapxeats.R;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 15/5/18.
 */

public class SmartFragment extends BaseFragment {

    @Inject
    public SmartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smart_photos, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
