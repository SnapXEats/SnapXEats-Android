package com.snapxeats.ui.restaurantInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.restaurantDetails.RestaurantPics;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prajakta Patil on 05/02/18.
 */
public class RestaurantInfoActivity extends BaseActivity implements RestaurantInfoContract.RestaurantInfoView,
        AppContract.SnapXResults {

    @Inject
    RestaurantInfoContract.RestaurantInfoPresenter mRestaurantPresenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @BindView(R.id.toolbar_rest_info)
    protected Toolbar mToolbar;

    @BindView(R.id.viewPager_rest_info)
    protected ViewPager mRestInfoViewPager;

    private List<RestaurantPics> mRestaurantPicsList;

    private RootRestaurantInfo mRootRestaurantInfo;

    @BindView(R.id.txt_rest_info_name)
    protected TextView mTxtRestName;

    @BindView(R.id.txt_rest_info_addr)
    protected TextView mTxtRestAddr;

    @BindView(R.id.spinner_rest_info_timings)
    protected Spinner mSpinner;

    @BindView(R.id.list_rest_aminities)
    protected ListView mListRestAminities;

    @BindView(R.id.txt_rest_info_open)
    protected TextView mTxtRestOpen;

    @BindView(R.id.txt_rest_info_close)
    protected TextView mTxtRestClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        mRestaurantPresenter.addView(this);
        snapXDialog.setContext(this);
        ButterKnife.bind(this);
        utility.setContext(this);
        //set mToolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initRestaurantInfo();
    }

    public void initRestaurantInfo() {
        mRestaurantPicsList = new ArrayList<>();
        //get restaurant details'
        String id = getIntent().getStringExtra(getString(R.string.intent_foodstackRestInfoId));
        showProgressDialog();
        mRestaurantPresenter.getRestInfo(id);
    }

    //set restaurant timings
    private void setRestaurantTimingsList() {
        ArrayAdapter<String> adapter;
        List<String> listTimings = new ArrayList<>();
        String isOpenNow = mRootRestaurantInfo.getRestaurantDetails().getIsOpenNow();

        if (mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().size() != 0) {
            for (int i = 0; i < mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().size(); i++) {
                listTimings.add(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().get(i).getDay_of_week()
                        + " - " +
                        mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().get(i).getRestaurant_open_close_time());
            }
            Comparator<String> dateComparator = (s1, s2) -> {
                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("EEE");
                    Date d1 = format.parse(s1);
                    Date d2 = format.parse(s2);
                    if (d1.equals(d2)) {
                        return s1.substring(s1.indexOf(" ") + 1).compareTo(s2.substring(s2.indexOf(" ") + 1));
                    } else {
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(d1);
                        cal2.setTime(d2);
                        return cal1.get(Calendar.DAY_OF_WEEK) - cal2.get(Calendar.DAY_OF_WEEK);
                    }
                } catch (ParseException pe) {
                    throw new RuntimeException(pe);
                }
            };
            Collections.sort(listTimings, dateComparator);
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listTimings);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            mTxtRestOpen.setVisibility(View.VISIBLE);
        } else if (isOpenNow.equalsIgnoreCase("true")) {
            mSpinner.setVisibility(View.GONE);
            mTxtRestOpen.setVisibility(View.VISIBLE);

        } else {
            mSpinner.setVisibility(View.GONE);
            mTxtRestClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        mRootRestaurantInfo = (RootRestaurantInfo) value;
        setUpRestaurantData();
        setRestaurantTimingsList();
        setRestaurantAminities();
    }

    //set restaurant aminities
    public void setRestaurantAminities() {
        List<String> strings = new ArrayList<>();
        if (mRootRestaurantInfo.getRestaurantDetails().getRestaurant_aminities().size() != 0) {
            for (int i = 0; i < mRootRestaurantInfo.getRestaurantDetails().getRestaurant_aminities().size(); i++) {
                strings.add(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_aminities().get(i));
            }
            AminitiesAdapter adapter = new AminitiesAdapter(getActivity(),
                    R.id.txt_rest_aminities, strings);
            mListRestAminities.setAdapter(adapter);
        }
    }

    //list adapter for restaurant aminities
    public class AminitiesAdapter extends ArrayAdapter<String> {
        List<String> list;
        Context mContext;

        public AminitiesAdapter(Context context, int textViewResourceId,
                                List<String> objects) {
            super(context, textViewResourceId, objects);
            this.mContext = context;
            list = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(R.layout.list_restaurant_aminities, parent, false);
            }
            TextView tv = row.findViewById(R.id.txt_rest_aminities);
            tv.setText(list.get(position));
            return row;

        }
    }

    //set restaurant data
    public void setUpRestaurantData() {
        //restaurants details
        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name().isEmpty()) {
            String restName = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name();
            mTxtRestName.setText(restName);
        }
        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address().isEmpty()) {
            String restAddress = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address();
            mTxtRestAddr.setText(restAddress);
        }

        for (int INDEX_REST_PICS = 0;
             INDEX_REST_PICS < mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics().size();
             INDEX_REST_PICS++) {
            mRestaurantPicsList.add(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics().get(INDEX_REST_PICS));
        }

        //set adapter for restaurant images
        RestaurantInfoAdapter mRestInfoAdapter = new RestaurantInfoAdapter(RestaurantInfoActivity.this, mRestaurantPicsList);
        mRestInfoViewPager.setAdapter(mRestInfoAdapter);
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        showNetworkErrorDialog((dialog, which) -> {
        });
        dismissProgressDialog();
    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
    }
}