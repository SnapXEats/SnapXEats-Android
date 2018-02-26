package com.snapxeats.ui.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.RestaurantPics;
import com.snapxeats.common.model.RestaurantSpeciality;
import com.snapxeats.common.model.RootRestaurantDetails;
import com.snapxeats.common.model.googleDirections.GoogleDirDest;
import com.snapxeats.common.model.googleDirections.GoogleDirOrigin;
import com.snapxeats.common.model.googleDirections.LocationGoogleDir;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    private RootGoogleDir mRootGoogleDir;

    @BindView(R.id.txt_restaurant_name)
    protected TextView mTxtRestName;

    @BindView(R.id.txt_restaurant_addr)
    protected TextView mTxtRestAddr;

    @BindView(R.id.toolbar_rest_details)
    protected Toolbar mToolbar;

    private TextView mTxtRestDuration;

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

    public void initRestaurantData() {
        mRestaurantPicsList = new ArrayList<>();
        mRestaurantSpecialties = new ArrayList<>();
        mInflater = LayoutInflater.from(this);
        //get restaurant details
        String id = "12584d25-046b-45d3-8a5e-8a0516dd4c7a";
        showProgressDialog();
        mRestaurantPresenter.getRestDetails(id);
        //get google directions
        //TODO latlng are hardcoded for now
        final String srcLat = "18.497895";
        final String srcLng = "73.829229";
        final String destLat = "18.504373";
        final String destLng = " 73.830680";
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
        if (mRootRestaurantDetails.getRestaurantDetails().getRestaurant_contact_no() != null) {
            String contact = mRootRestaurantDetails.getRestaurantDetails().getRestaurant_contact_no();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact, null));
            startActivity(intent);
        } else {
            SnapXToast.showToast(this, "No phone");
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
        mRootRestaurantDetails = (RootRestaurantDetails) value;
        setUpRecyclerView();
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

        //TODO data will be used in future
        //google directions data
        //   mTxtRestDuration.setText(mRootGoogleDir.getRoutes().get(0).getLegs().get(0).getDuration().getValue());
        //  SnapXToast.showToast(this,"oooo"+mRootGoogleDir.getRoutes().get(0).getLegs().get(0).getDuration().getValue());
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
