package com.snapxeats.ui.restaurant;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.googleDirections.GoogleDirDest;
import com.snapxeats.common.model.googleDirections.GoogleDirOrigin;
import com.snapxeats.common.model.googleDirections.LocationGoogleDir;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import com.snapxeats.common.model.restaurantInfo.RestaurantSpeciality;
import com.snapxeats.common.model.restaurantInfo.RestaurantTimings;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.directions.DirectionsActivity;

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

import static com.snapxeats.common.constants.UIConstants.GOOGLE_DIR_NOT_FOUND;
import static com.snapxeats.common.constants.UIConstants.GOOGLE_DIR_NO_RESULTS;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.REST_CALL;
import static com.snapxeats.common.constants.UIConstants.SET_ALPHA_DISABLE;
import static com.snapxeats.common.constants.UIConstants.STRING_SPACE;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.UBER_PACKAGE;
import static com.snapxeats.common.constants.UIConstants.UBER_URI;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 05/02/18.
 */

public class RestaurantDetailsActivity extends BaseActivity implements RestaurantDetailsContract.RestaurantDetailsView,
        AppContract.SnapXResults {

    private List<RestaurantPics> mRestaurantPicsList;
    private List<RestaurantSpeciality> mRestaurantSpecialties;

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

    private RootRestaurantInfo mRootRestaurantInfo;

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

    @BindView(R.id.restaurant_details_parent_layout)
    protected LinearLayout mParentLayout;

    private RootGoogleDir mRootGoogleDir;
    private String restaurantId, lat, lng;

    @BindView(R.id.layout_dots)
    protected LinearLayout mSliderDotsPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        initView();
    }

    /*initialize views*/
    @Override
    public void initView() {
        mRestaurantPresenter.addView(this);
        snapXDialog.setContext(this);
        ButterKnife.bind(this);
        utility.setContext(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            builder.setMessage(getString(R.string.install_uber))
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
        restaurantId = getIntent().getStringExtra(getString(R.string.intent_restaurant_id));
        showProgressDialog();
        mRestaurantPresenter.getRestDetails(restaurantId);
        utility.setImagesCorousal(mRestviewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setGoogleDir() {
        final String destLat = mRootRestaurantInfo.getRestaurantDetails().getLocation_lat();
        final String destLng = mRootRestaurantInfo.getRestaurantDetails().getLocation_long();
        LocationGoogleDir locationGoogleDir = new LocationGoogleDir();
        GoogleDirOrigin googleDirOrigin = new GoogleDirOrigin();

        lat = String.valueOf(utility.setLatLng().latitude);
        lng = String.valueOf(utility.setLatLng().longitude);

        googleDirOrigin.setOriginLat(lat);
        googleDirOrigin.setOriginLng(lng);

        GoogleDirDest googleDirDest = new GoogleDirDest();
        googleDirDest.setDestinationLat(destLat);
        googleDirDest.setDestinationLng(destLng);

        locationGoogleDir.setGoogleDirOrigin(googleDirOrigin);
        locationGoogleDir.setGoogleDirDest(googleDirDest);
        mRestaurantPresenter.getGoogleDirections(locationGoogleDir);
    }

    @OnClick(R.id.img_rest_directions)
    public void imgRestDirections() {
        if (null != mRootRestaurantInfo && null != mRootGoogleDir) {
            Intent intent = new Intent(RestaurantDetailsActivity.this, DirectionsActivity.class);
            intent.putExtra(getString(R.string.intent_rest_details), mRootRestaurantInfo);
            intent.putExtra(getString(R.string.intent_google_dir), mRootGoogleDir);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.img_rest_call)
    public void imgRestCall() {
        if (null != mRootRestaurantInfo && !restContactNo.isEmpty()) {
            String contact = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_contact_no();
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
        if (value instanceof RootRestaurantInfo) {
            mRootRestaurantInfo = (RootRestaurantInfo) value;
            setUpRecyclerView();
            restaurantTimingsList();
            setGoogleDir();
        } else if (value instanceof RootGoogleDir) {
            dismissProgressDialog();
            mRootGoogleDir = (RootGoogleDir) value;
            setGoogleDirView();
        }
    }

    private void setGoogleDirView() {
        if (mRootGoogleDir.getStatus().equalsIgnoreCase(GOOGLE_DIR_NO_RESULTS) ||
                mRootGoogleDir.getStatus().equalsIgnoreCase(GOOGLE_DIR_NOT_FOUND)) {

        } else if (null != mRootGoogleDir
                && null != mRootGoogleDir.getRoutes()
                && ZERO != mRootGoogleDir.getRoutes().size()
                && null != mRootGoogleDir.getRoutes().get(ZERO).getLegs()
                && ZERO != mRootGoogleDir.getRoutes().get(ZERO).getLegs().size()
                && null != mRootGoogleDir.getRoutes().get(ZERO).getLegs().get(ZERO).getDuration()
                && null != mRootGoogleDir.getRoutes().get(ZERO).getLegs().get(ZERO).getDuration().getText()
                && !mRootGoogleDir.getRoutes().get(ZERO).getLegs().get(ZERO).getDuration().getText().isEmpty()) {
            mTxtRestDuration.setText(mRootGoogleDir.getRoutes().get(ZERO).getLegs().get(ZERO)
                    .getDuration().getText() + UIConstants.STRING_SPACE + getString(R.string.away));
        }
    }

    public void setUpRecyclerView() {
        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name().isEmpty()) {
            String restName = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name();
            mTxtRestName.setText(restName);
        }
        utility.setViewPager(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics(), mRestviewPager, mSliderDotsPanel);
        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address().isEmpty()) {
            String restAddress = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address();
            mTxtRestAddr.setText(restAddress);
        }

        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_contact_no().isEmpty()) {
            restContactNo = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_contact_no();
        } else {
            mImgCall.setClickable(false);
            mImgCall.setAlpha(SET_ALPHA_DISABLE);
        }

        List<RestaurantPics> restPics = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics();
        for (int INDEX_REST_PICS = ZERO;
             INDEX_REST_PICS < restPics.size();
             INDEX_REST_PICS++) {
            mRestaurantPicsList.add(restPics.get(INDEX_REST_PICS));
        }
        List<RestaurantSpeciality> restSpecialty = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_speciality();
        for (int INDEX_REST_SPECIALTIES = ZERO; INDEX_REST_SPECIALTIES < restSpecialty.size(); INDEX_REST_SPECIALTIES++) {
            mRestaurantSpecialties.add(restSpecialty.get(INDEX_REST_SPECIALTIES));
            View view = mInflater.inflate(R.layout.layout_rest_specialties,
                    mLayoutRestSpecialties, false);
            ImageView imageView = view.findViewById(R.id.img_restaurant_specialties);

            Glide.with(getApplicationContext())
                    .load(mRestaurantSpecialties.get(INDEX_REST_SPECIALTIES)
                            .getDish_image_url())
                    .placeholder(R.drawable.ic_rest_info_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(true)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(THUMBNAIL)
                    .into(imageView);
            mLayoutRestSpecialties.addView(view);
        }
        /*set adapter for restaurant images*/
        RestImagesAdapter mRestPicsAdapter = new RestImagesAdapter(RestaurantDetailsActivity.this,
                null, mRestaurantPicsList, null);
        mRestviewPager.setAdapter(mRestPicsAdapter);
    }

    private void restaurantTimingsList() {
        List<String> listTimings = new ArrayList<>();
        List<RestaurantTimings> restTimingList = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings();
        String isOpenNow = mRootRestaurantInfo.getRestaurantDetails().getIsOpenNow();
        if (ZERO != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().size()) {
            for (int row = ZERO; row < restTimingList.size(); row++) {
                listTimings.add(restTimingList.get(row).getDay_of_week() + " " + restTimingList.get(row).getRestaurant_open_close_time());
            }
            Comparator<String> dateComparator = (s1, s2) -> {
                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format_month));
                    Date d1 = format.parse(s1);
                    Date d2 = format.parse(s2);
                    if (d1.equals(d2)) {
                        return s1.substring(s1.indexOf(" ") + ONE).compareTo(s2.substring(s2.indexOf(" ") + ONE));
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
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    mRestaurantPresenter.getRestDetails(restaurantId);
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                mRestaurantPresenter.getRestDetails(restaurantId);
            }
        });
    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
    }
}