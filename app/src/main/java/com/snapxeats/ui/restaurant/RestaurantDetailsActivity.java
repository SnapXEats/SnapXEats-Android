package com.snapxeats.ui.restaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.googleDirections.GoogleDirDest;
import com.snapxeats.common.model.googleDirections.GoogleDirOrigin;
import com.snapxeats.common.model.googleDirections.LocationGoogleDir;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.model.restaurantDetails.RestaurantPics;
import com.snapxeats.common.model.restaurantDetails.RestaurantSpeciality;
import com.snapxeats.common.model.restaurantDetails.RootRestaurantDetails;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.squareup.picasso.Picasso;

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
import butterknife.OnClick;

/**
 * Created by Prajakta Patil on 05/02/18.
 */

public class RestaurantDetailsActivity extends BaseActivity implements RestaurantDetailsContract.RestaurantDetailsView,
        AppContract.SnapXResults {

    private static final String UBER_URI = "https://play.google.com/store/apps/details?id=com.ubercab";
    private static final String UBER_PACKAGE = "com.ubercab";
    private static final String REST_CALL = "tel";
    List<RestaurantPics> mRestaurantPicsList;

    List<RestaurantSpeciality> mRestaurantSpecialties;

    RestImagesAdapter mRestPicsAdapter;

    @Inject
    RestaurantDetailsContract.RestaurantDetailsPresenter mRestaurantPresenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @BindView(R.id.viewPager)
    protected ViewPager mRestviewPager;

    private LayoutInflater mInflater;

    @BindView(R.id.layout_rest_specialties)
    protected LinearLayout mLayoutRestSpecialties;

    private RootRestaurantDetails mRootRestaurantDetails;

    @BindView(R.id.txt_restaurant_name)
    protected TextView mTxtRestName;

    @BindView(R.id.txt_restaurant_addr)
    protected TextView mTxtRestAddr;

    @BindView(R.id.toolbar_rest_details)
    protected Toolbar mToolbar;

    @BindView(R.id.txt_google_duration)
    protected TextView mTxtRestDuration;

    @BindView(R.id.spinner_rest_timings)
    protected Spinner mSpinner;

    @BindView(R.id.img_rest_call)
    protected ImageView mImgCall;

    private String restContactNo;

    @BindView(R.id.txt_rest_details_open)
    protected TextView mTxtRestOpen;

    @BindView(R.id.txt_rest_details_close)
    protected TextView mTxtRestClose;

    private RootGoogleDir mRootGoogleDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        initView();
    }

    //initialize views
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
        initRestaurantData();
    }

    //uber button click
    @OnClick(R.id.layout_uber)
    public void layoutUber() {
        boolean isAppInstalled = appInstalledOrNot(UBER_PACKAGE);
        if (isAppInstalled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.book_uber_ride))
                    .setPositiveButton(getString(R.string.confirm_uber), (dialog, which) -> {
                        Intent LaunchIntent = getPackageManager()
                                .getLaunchIntentForPackage(UBER_PACKAGE);
                        startActivity(LaunchIntent);
                    })
                    .setNegativeButton(getString(R.string.not_now), (dialog, which) -> {
                    })
                    .show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.install_uber))
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(UBER_URI))))
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    })
                    .show();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public void initRestaurantData() {
        mRestaurantPicsList = new ArrayList<>();
        mRestaurantSpecialties = new ArrayList<>();
        mInflater = LayoutInflater.from(this);
        //get restaurant details
        String id = getIntent().getStringExtra(getString(R.string.intent_foodstackRestDetailsId));
        showProgressDialog();
        mRestaurantPresenter.getRestDetails(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setGoogleDir() {
        //get google directions
        //TODO latlng are hardcoded for now
        final String srcLat = "40.4862157";
        final String srcLng = "-74.4518188";
        final String destLat = mRootRestaurantDetails.getRestaurantDetails().getLocation_lat();
        final String destLng = mRootRestaurantDetails.getRestaurantDetails().getLocation_long();
        LocationGoogleDir locationGoogleDir = new LocationGoogleDir();
        GoogleDirOrigin googleDirOrigin = new GoogleDirOrigin();
        googleDirOrigin.setOriginLat(srcLat);
        googleDirOrigin.setOriginLng(srcLng);
        GoogleDirDest googleDirDest = new GoogleDirDest();
        googleDirDest.setDestinationLat(destLat);
        googleDirDest.setDestinationLng(destLng);
        locationGoogleDir.setGoogleDirOrigin(googleDirOrigin);
        locationGoogleDir.setGoogleDirDest(googleDirDest);
        mRestaurantPresenter.getGoogleDirections(locationGoogleDir);
    }


    @OnClick(R.id.img_rest_directions)
    public void imgRestDirections() {
        //TODO navigate to directions screen
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.img_rest_call)
    public void imgRestCall() {
        if (null!=mRootRestaurantDetails && !restContactNo.isEmpty()) {
            String contact = mRootRestaurantDetails.getRestaurantDetails().getRestaurant_contact_no();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(REST_CALL, contact, null));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof RootRestaurantDetails) {
            mRootRestaurantDetails = (RootRestaurantDetails) value;
            setUpRecyclerView();
            restaurantTimingsList();
            setGoogleDir();
        } else if (value instanceof RootGoogleDir) {
            mRootGoogleDir = (RootGoogleDir) value;
            setGoogleDirView();
        }

    }

    private void setGoogleDirView() {
        mTxtRestDuration.setText(mRootGoogleDir.getRoutes().get(0).getLegs().get(0)
                .getDuration().getText() + getString(R.string.away));
    }

    public void setUpRecyclerView() {
        //restaurants details
        if (!mRootRestaurantDetails.getRestaurantDetails().getRestaurant_name().isEmpty()) {
            String restName = mRootRestaurantDetails.getRestaurantDetails().getRestaurant_name();
            mTxtRestName.setText(restName);
        }
        if (!mRootRestaurantDetails.getRestaurantDetails().getRestaurant_address().isEmpty()) {
            String restAddress = mRootRestaurantDetails.getRestaurantDetails().getRestaurant_address();
            mTxtRestAddr.setText(restAddress);
        }

        if (!mRootRestaurantDetails.getRestaurantDetails().getRestaurant_contact_no().isEmpty()) {
            restContactNo = mRootRestaurantDetails.getRestaurantDetails().getRestaurant_contact_no();
        } else {
            mImgCall.setClickable(false);
            mImgCall.setAlpha((float) 0.5);
        }

        for (int INDEX_REST_PICS = 0;
             INDEX_REST_PICS < mRootRestaurantDetails.getRestaurantDetails().getRestaurant_pics().size();
             INDEX_REST_PICS++) {
            mRestaurantPicsList.add(mRootRestaurantDetails.getRestaurantDetails().getRestaurant_pics().get(INDEX_REST_PICS));
        }
        for (int INDEX_REST_SPECIALTIES = 0;
             INDEX_REST_SPECIALTIES < mRootRestaurantDetails.getRestaurantDetails().getRestaurant_speciality().size();
             INDEX_REST_SPECIALTIES++) {
            mRestaurantSpecialties.add(mRootRestaurantDetails.getRestaurantDetails().getRestaurant_speciality().get(INDEX_REST_SPECIALTIES));
            View view = mInflater.inflate(R.layout.layout_rest_specialties,
                    mLayoutRestSpecialties, false);
            ImageView imageView = view.findViewById(R.id.img_restaurant_specialties);

            Picasso.with(this).load(mRestaurantSpecialties.get(INDEX_REST_SPECIALTIES).getDish_image_url())
                    .placeholder(R.drawable.ic_cuisine_placeholder).into(imageView);
            mLayoutRestSpecialties.addView(view);
        }

        //set adapter for restaurant images
        mRestPicsAdapter = new RestImagesAdapter(RestaurantDetailsActivity.this, mRestaurantPicsList);
        mRestviewPager.setAdapter(mRestPicsAdapter);
    }

    private void restaurantTimingsList() {
        List<String> listTimings = new ArrayList<>();
        String isOpenNow = mRootRestaurantDetails.getRestaurantDetails().getIsOpenNow();
        if (mRootRestaurantDetails.getRestaurantDetails().getRestaurant_timings().size() != 0) {
            for (int i = 0; i < mRootRestaurantDetails.getRestaurantDetails().getRestaurant_timings().size(); i++) {
                listTimings.add(mRootRestaurantDetails.getRestaurantDetails().getRestaurant_timings().get(i).getDay_of_week() +
                        "         " +
                        mRootRestaurantDetails.getRestaurantDetails().getRestaurant_timings().get(i).getRestaurant_open_close_time());
            }
            Comparator<String> dateComparator = (s1, s2) -> {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("EEEE");
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
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listTimings);
            mSpinner.setAdapter(adapter);
        } else if (isOpenNow.equalsIgnoreCase("true")) {
            mSpinner.setVisibility(View.GONE);
            mTxtRestOpen.setVisibility(View.VISIBLE);
        } else {
            mSpinner.setVisibility(View.GONE);
            mTxtRestClose.setVisibility(View.VISIBLE);
        }
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