package com.snapxeats.ui.restaurantInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.DownloadTask;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
import com.snapxeats.common.model.smartphotos.SmartPhotoResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.HomeDbHelper;
import com.snapxeats.ui.restaurant.RestImagesAdapter;

import java.text.DateFormat;
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

import static com.snapxeats.common.constants.UIConstants.STORAGE_REQUEST_PERMISSION;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 05/02/18.
 */
public class RestaurantInfoActivity extends BaseActivity implements RestaurantInfoContract.RestaurantInfoView,
        AppContract.SnapXResults, View.OnClickListener, DownloadTask.OnDownloadCompleted, MediaPlayer.OnCompletionListener {

    @Inject
    RestaurantInfoContract.RestaurantInfoPresenter mRestaurantPresenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    HomeDbHelper homeDbHelper;

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

    @BindView(R.id.txt_rest_info_open)
    protected TextView mTxtRestOpen;

    @BindView(R.id.txt_rest_info_close)
    protected TextView mTxtRestClose;

    @BindView(R.id.restaurant_info_parent_layout)
    protected LinearLayout mParentLayout;

    private String restaurantId;

    @BindView(R.id.txt_rest_aminities)
    protected TextView mTxtRestAminities;

    @BindView(R.id.txt_photo_taken)
    protected TextView mTxtPhotoTaken;

    @BindView(R.id.layout_dots)
    protected LinearLayout mSliderDotsPanel;

    private boolean isImageTap;
    private RestaurantPics mRestaurantPic;
    private ImageView mImgTextReview;
    private ImageView mImgAudioReview;
    private ImageView mImgDownload;
    private ImageView mImgPlayAudio;
    private TextView mTxtTimeOfAudio;
    private LinearLayout mLayoutControls;
    private Dialog mDialog;
    private Window mWindow;
    private MediaPlayer mMediaPlayer;
    private boolean isAudioPlayTap;
    private Handler mHandler = new Handler();
    private RestImagesAdapter mRestInfoAdapter;
    private SmartPhotoResponse mSmartPhoto;

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.toolbar_rest_info));
        initRestaurantInfo();
        mDialog = new Dialog(this);
    }

    public void initRestaurantInfo() {
        mRestaurantPicsList = new ArrayList<>();
        restaurantId = getIntent().getStringExtra(getString(R.string.intent_foodstackRestInfoId));
        showProgressDialog();
        mRestaurantPresenter.getRestInfo(restaurantId);
        utility.setImagesCorousal(mRestInfoViewPager);
    }

    /*set restaurant timings*/
    private void setRestaurantTimingsList() {
        ArrayAdapter<String> adapter;
        List<String> listTimings = new ArrayList<>();
        String isOpenNow = mRootRestaurantInfo.getRestaurantDetails().getIsOpenNow();

        if (ZERO != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().size()) {
            for (int row = ZERO; row < mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().size(); row++) {
                listTimings.add(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().get(row).getDay_of_week()
                        + " - " +
                        mRootRestaurantInfo.getRestaurantDetails().getRestaurant_timings().get(row).getRestaurant_open_close_time());
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

    /*set restaurant aminities values*/
    public void setRestaurantAminities() {
        List<String> list = new ArrayList<>();
        if (ZERO != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities().size()) {
            for (int row = ZERO; row < mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities().size(); row++) {
                list.add(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities().get(row));
            }

            StringBuilder builder = new StringBuilder();
            for (String details : mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities()) {
                builder.append(details + "\n");
            }
            mTxtRestAminities.setText(builder.toString());
            setRestPhotoDate();
        }
    }

    private void setRestPhotoDate() {
        DateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.yyyy_MM_dd));
        Date date = null;
        String stringDate = null;
        try {
            date = simpleDateFormat.parse(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics().get(ZERO).getCreated_date());
            SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.dd_MMM_yyyy));
            stringDate = dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTxtPhotoTaken.setText(getString(R.string.photo_taken) + " " + stringDate);
    }

    /*set restaurant data*/
    public void setUpRestaurantData() {
        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name().isEmpty()) {
            String restName = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name();
            mTxtRestName.setText(restName);
        }
        utility.setViewPager(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics(), mRestInfoViewPager, mSliderDotsPanel);
        if (!mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address().isEmpty()) {
            String restAddress = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address();
            mTxtRestAddr.setText(restAddress);
        }

        for (int INDEX_REST_PICS = ZERO;
             INDEX_REST_PICS < mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics().size();
             INDEX_REST_PICS++) {
            mRestaurantPicsList.add(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_pics().get(INDEX_REST_PICS));
        }

        /*set adapter for restaurant images*/
        mRestInfoAdapter = new RestImagesAdapter(RestaurantInfoActivity.this, homeDbHelper,
                mRestaurantPicsList,
                (restaurantPic, itemView) -> {
                    mRestaurantPic = restaurantPic;
                    mImgTextReview = itemView.findViewById(R.id.img_text_review);
                    mImgAudioReview = itemView.findViewById(R.id.img_audio_review);
                    mImgDownload = itemView.findViewById(R.id.img_download);

                    mImgTextReview.setOnClickListener(this);
                    mImgAudioReview.setOnClickListener(this);
                    mImgDownload.setOnClickListener(this);

                    mLayoutControls = itemView.findViewById(R.id.layout_controls);
                    isImageTap = !isImageTap;
                    if (isImageTap) {
                        setVisibilityForDownload();

                        mLayoutControls.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutControls.setVisibility(View.GONE);
                    }
                });

        mRestInfoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (null != mLayoutControls) {
                    isImageTap = false;
                    mLayoutControls.setVisibility(View.GONE);
                }
                setVisibilityForDownload();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRestInfoViewPager.setAdapter(mRestInfoAdapter);
    }

    private void setVisibilityForDownload() {
        //Check duplicate entry for dish to download
        if ((null != homeDbHelper) && (null != mRestaurantPic)
                && homeDbHelper.isDuplicateSmartPhoto(mRestaurantPic.getRestaurant_dish_id())
                && (null != mImgDownload)) {
            mImgDownload.setVisibility(View.GONE);
        } else if (null != mImgDownload) {
            mImgDownload.setVisibility(View.VISIBLE);
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
                    mRestaurantPresenter.getRestInfo(restaurantId);
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                mRestaurantPresenter.getRestInfo(restaurantId);
            }
        });
    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_text_review:
                setTextReview();
                break;

            case R.id.img_audio_review:
                setAudioReview();
                break;
            case R.id.btn_okay:
                if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                mDialog.dismiss();
                break;

            case R.id.img_play_audio:
                isAudioPlayTap = !isAudioPlayTap;

                if (isAudioPlayTap) {
                    mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_pause));

                    mMediaPlayer.start();
                    mUpdateTimeTask.run();
                } else {
                    mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }
                break;

            case R.id.img_download:
                checkStoragePermission();
                break;
        }
    }

    /**
     * Check permissions for external storage
     */
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_PERMISSION);
        } else {
            startDownloadingTask();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int index = ZERO; index < permissions.length; index++) {
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                startDownloadingTask();
            } else if (!shouldShowRequestPermissionRationale(permissions[index])) {
                snapXDialog.showChangePermissionDialog(STORAGE_REQUEST_PERMISSION);
            } else {
                SnapXToast.showToast(this, getString(R.string.enable_storage_permissions));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case STORAGE_REQUEST_PERMISSION:
                startDownloadingTask();
                break;
        }
    }

    private void startDownloadingTask() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (NetworkUtility.isNetworkAvailable(this)) {

                mapRestInfoToSmartPhoto();

                new DownloadTask(RestaurantInfoActivity.this, this, mSmartPhoto)
                        .execute(mRestaurantPic.getDish_image_url(), mRestaurantPic.getAudio_review_url());
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                });
            }
        }
    }

    private void mapRestInfoToSmartPhoto() {
        mSmartPhoto = new SmartPhotoResponse();

        mSmartPhoto.setRestaurant_dish_id(mRestaurantPic.getRestaurant_dish_id());
        mSmartPhoto.setPic_taken_date(mRestaurantPic.getCreated_date());
        mSmartPhoto.setText_review(mRestaurantPic.getText_review());

        mSmartPhoto.setRestaurant_name(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name());
        mSmartPhoto.setRestaurant_address(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address());
        mSmartPhoto.setRestaurant_aminities(mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities());
        mSmartPhoto.setDish_image_url(mRestaurantPic.getDish_image_url());
        mSmartPhoto.setAudio_review_url(mRestaurantPic.getAudio_review_url());
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (null != mMediaPlayer) {
                long currentDuration = mMediaPlayer.getCurrentPosition();

                // Displaying time completed playing
                mTxtTimeOfAudio.setText("" + utility.milliSecondsToTimer(currentDuration));

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, UIConstants.PERCENTAGE);
            }
        }
    };

    /**
     * Set Text review view
     */
    private void setTextReview() {
        mDialog.setContentView(R.layout.rest_smart_text_review_layout);
        mWindow = mDialog.getWindow();
        if (null != mWindow) {
            mWindow.setLayout(UIConstants.NO_DATA_DIALOG_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT);
            mWindow.setGravity(Gravity.CENTER);
        }
        mDialog.show();
        TextView textView = mDialog.findViewById(R.id.txt_review_contents);
        textView.setText(mRestaurantPic.getText_review());
        mDialog.findViewById(R.id.btn_okay).setOnClickListener(this);
    }

    /**
     * Set Audio review view
     */
    private void setAudioReview() {
        mDialog.setContentView(R.layout.rest_smart_audio_layout);
        mDialog.setOnCancelListener(dialog -> utility.resetMediaPlayer(mMediaPlayer));

        mWindow = mDialog.getWindow();
        if (null != mWindow) {
            mWindow.setLayout(UIConstants.NO_DATA_DIALOG_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT);
            mWindow.setGravity(Gravity.CENTER);
        }
        mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mRestaurantPic.getAudio_review_url()));
        mMediaPlayer.setOnCompletionListener(this);
        mTxtTimeOfAudio = mDialog.findViewById(R.id.timer_play);
        mImgPlayAudio = mDialog.findViewById(R.id.img_play_audio);
        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
        mDialog.show();
        mDialog.findViewById(R.id.btn_okay).setOnClickListener(this);
        mDialog.findViewById(R.id.img_play_audio).setOnClickListener(this);
    }


    @Override
    public void isDownloadComplete(boolean isComplete, SmartPhotoResponse smartPhotoResponse) {
        if (isComplete) {
            mDialog.setContentView(R.layout.rest_photo_download_success_layout);
            mWindow = mDialog.getWindow();
            if (null != mWindow) {
                mWindow.setLayout(UIConstants.NO_DATA_DIALOG_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT);
                mWindow.setGravity(Gravity.CENTER);
            }
            mDialog.show();
            mDialog.findViewById(R.id.btn_okay).setOnClickListener(this);
            mRestaurantPic.setAudio_review_url(smartPhotoResponse.getAudio_review_url());

            mSmartPhoto.setDish_image_url(smartPhotoResponse.getDish_image_url());
            mSmartPhoto.setAudio_review_url(smartPhotoResponse.getAudio_review_url());
            homeDbHelper.saveSmartPhotoDataInDb(mSmartPhoto);
            if (null != mLayoutControls) {
                mLayoutControls.setVisibility(View.GONE);
            }
            mRestInfoAdapter.notifyDataSetChanged();
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
        mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mRestaurantPic.getAudio_review_url()));
        mMediaPlayer.setOnCompletionListener(this);
        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
    }
}