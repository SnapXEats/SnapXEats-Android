package com.snapxeats.ui.navpreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;


/**
 * Created by Snehal Tembare on 8/2/18.
 */
public class NavPreferenceActivity extends BaseActivity implements View.OnClickListener {


    private long mLastClickTime = 0;
    private SharedPreferences preferences;
    private com.snapxeats.common.model.Location mSelectedLocation;
    private int rating;
    private int pricing;
    private int distance;
    private boolean isSortByDistance;
    private boolean isSortByRating;

    @Inject
    AppUtility utility;

    @Inject
    SnapXDialog snapXDialog;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.sbg_pricing)
    protected SegmentedButtonGroup mSbgPricing;

    @BindView(R.id.sbg_distance)
    protected SegmentedButtonGroup mSbgDistance;

    @BindView(R.id.seg_txt_one)
    protected TextView mTxtSegOne;

    @BindView(R.id.seg_txt_two)
    protected TextView mTxtSegTwo;

    @BindView(R.id.seg_txt_three)
    protected TextView mTxtSegThree;

    @BindView(R.id.seg_txt_four)
    protected TextView mTxtSegFour;

    @BindView(R.id.txt_location)
    protected TextView mTxtLocation;

    @BindView(R.id.txt_rating_three)
    protected TextView mTxtRatingThree;

    @BindView(R.id.txt_rating_four)
    protected TextView mTxtRatingFour;

    @BindView(R.id.txt_rating_five)
    protected TextView mTxtRatingFive;

    @BindView(R.id.rating_bar_three)
    protected RatingBar mRatingThree;

    @BindView(R.id.rating_bar_four)
    protected RatingBar mRatingFour;

    @BindView(R.id.rating_bar_five)
    protected RatingBar mRatingFive;

    @BindView(R.id.view_rating_three)
    protected View mViewRatingThree;

    @BindView(R.id.view_rating_four)
    protected View mViewRatingFour;

    @BindView(R.id.view_rating_five)
    protected View mViewRatingFive;

    @BindView(R.id.layout_rating_three)
    protected LinearLayout mLayoutRatingThree;

    @BindView(R.id.layout_rating_four)
    protected LinearLayout mLayoutRatingFour;

    @BindView(R.id.layout_rating_five)
    protected LinearLayout mLayoutRatingFive;

    @BindView(R.id.seg_txt_auto)
    protected TextView mTxtSegDistanceAuto;

    @BindView(R.id.seg_txt_distance_one)
    protected TextView mTxtSegDistanceOne;

    @BindView(R.id.seg_txt_distance_two)
    protected TextView mTxtSegDistanceTwo;

    @BindView(R.id.seg_txt_distance_three)
    protected TextView mTxtSegDistanceThree;

    @BindView(R.id.seg_txt_distance_four)
    protected TextView mTxtSegDistanceFour;

    @BindView(R.id.seg_txt_distance_five)
    protected TextView mTxtSegDistanceFive;

    @BindView(R.id.radio_group_sort_by)
    protected RadioGroup mRadioGrpSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_preference);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        utility.setContext(this);
        snapXDialog.setContext(this);
        preferences = utility.getSharedPreferences();

        mSbgPricing.setOnClickedButtonListener(position -> {
            updateUI(position);
            SnapXToast.debug("NavPreferenceActivity Position" + position);
        });

        mSbgDistance.setOnClickedButtonListener(position -> updateDistanceUI(position));

        mRadioGrpSortBy.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_distance) {
                isSortByDistance = true;
            } else {
                isSortByRating = false;
            }

        });

        registerListeners();

        showLocation();

/*        Handler handler = new Handler();
        Runnable runnable = () -> mSbgPricing.setEnabled(true);
        handler.postDelayed(runnable, 500);*/


    }

    private void updateDistanceUI(int position) {
        switch (position) {
            case 0:
                distance = 0;
                mSbgDistance.setPosition(position);
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case 1:
                distance = 1;
                mSbgDistance.setPosition(position);
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case 2:
                distance = 2;
                mSbgDistance.setPosition(position);
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case 3:
                distance = 3;
                mSbgDistance.setPosition(position);
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case 4:
                distance = 4;
                mSbgDistance.setPosition(position);
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case 5:
                distance = 5;
                mSbgDistance.setPosition(position);
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
        }

    }

    private void registerListeners() {
        mTxtSegOne.setOnClickListener(this);
        mTxtSegTwo.setOnClickListener(this);
        mTxtSegThree.setOnClickListener(this);
        mTxtSegFour.setOnClickListener(this);
        mLayoutRatingThree.setOnClickListener(this);
        mLayoutRatingFour.setOnClickListener(this);
        mLayoutRatingFive.setOnClickListener(this);

        mTxtSegDistanceAuto.setOnClickListener(this);
        mTxtSegDistanceOne.setOnClickListener(this);
        mTxtSegDistanceTwo.setOnClickListener(this);
        mTxtSegDistanceThree.setOnClickListener(this);
        mTxtSegDistanceFour.setOnClickListener(this);
        mTxtSegDistanceFive.setOnClickListener(this);
    }

    private void showLocation() {
        Gson gson = new Gson();
        String json = preferences.getString(getString(R.string.selected_location), "");
        mSelectedLocation = gson.fromJson(json, com.snapxeats.common.model.Location.class);
        mTxtLocation.setSingleLine();
        mTxtLocation.setText(mSelectedLocation.getName());
    }


    private void updateUI(int position) {
        switch (position) {
            case 0:
//                updatePricing(position);
                mSbgPricing.setPosition(position);
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;

            case 1:
                mSbgPricing.setPosition(position);

                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
//                updatePricing(position);
                break;
            case 2:
//                updatePricing(position);
                mSbgPricing.setPosition(position);

                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case 3:
//                updatePricing(position);
                mSbgPricing.setPosition(position);

                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
        }


    }

    private void updatePricing(int position) {
        switch (position) {
            case 0:
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mSbgPricing.setPosition(0);
                break;

            case 1:
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mSbgPricing.setPosition(1);
                break;
            case 2:
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mSbgPricing.setPosition(2);
                break;
            case 3:
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mSbgPricing.setPosition(3);

                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.seg_txt_one:
                pricing = 1;
                mSbgPricing.setPosition(0);
//                updatePricing(0);

                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_two:
                pricing = 2;
                mSbgPricing.setPosition(1);

//                updatePricing(1);
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_three:
//                updatePricing(2);

                pricing = 3;
                mSbgPricing.setPosition(2);
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_four:
//                updatePricing(3);

                pricing = 4;
                mSbgPricing.setPosition(3);
                mTxtSegFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;

            case R.id.layout_rating_three:
                rating = 3;
                mViewRatingThree.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtRatingThree.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mRatingThree.setRating(3);

                mViewRatingFour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                mTxtRatingFour.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
                mRatingFour.setRating(0);

                mViewRatingFive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                mTxtRatingFive.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
                mRatingFive.setRating(0);
                break;

            case R.id.layout_rating_four:
                rating = 4;
                mViewRatingFour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtRatingFour.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mRatingFour.setRating(4);

                mViewRatingThree.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                mTxtRatingThree.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
                mRatingThree.setRating(0);

                mViewRatingFive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                mTxtRatingFive.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
                mRatingFive.setRating(0);
                break;

            case R.id.layout_rating_five:
                rating = 5;
                mViewRatingFive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtRatingFive.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mRatingFive.setRating(5);

                mViewRatingFour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                mTxtRatingFour.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
                mRatingFour.setRating(0);

                mViewRatingThree.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                mTxtRatingThree.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
                mRatingThree.setRating(0);
                break;

            case R.id.seg_txt_auto:
                distance = 0;
                mSbgDistance.setPosition(0);

                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;

            case R.id.seg_txt_distance_one:
                distance = 1;
                mSbgDistance.setPosition(1);
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_distance_two:
                distance = 2;
                mSbgDistance.setPosition(2);
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_distance_three:
                distance = 3;
                mSbgDistance.setPosition(3);
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_distance_four:
                distance = 4;
                mSbgDistance.setPosition(4);
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
            case R.id.seg_txt_distance_five:
                distance = 5;
                mSbgDistance.setPosition(5);
                mTxtSegDistanceFive.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_selected_color));
                mTxtSegDistanceOne.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceTwo.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceThree.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                mTxtSegDistanceAuto.setTextColor(ContextCompat.getColor(this, R.color.pref_txt_color));
                break;
        }
    }
}
