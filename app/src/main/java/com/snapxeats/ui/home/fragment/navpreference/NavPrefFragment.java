package com.snapxeats.ui.home.fragment.navpreference;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.fragment.home.HomeFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.CUISINE_PREF;
import static com.snapxeats.common.Router.Screen.FOOD_PREF;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.DISTANCE.DISTANCE_FIVE;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.DISTANCE.DISTANCE_FOUR;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.DISTANCE.DISTANCE_ONE;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.DISTANCE.DISTANCE_THREE;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.DISTANCE.DISTANCE_TWO;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.PRICING.PRICE_AUTO;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.PRICING.PRICE_FOUR;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.PRICING.PRICE_ONE;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.PRICING.PRICE_THREE;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.PRICING.PRICE_TWO;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.RATINGS.FIVE_STAR;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.RATINGS.FOUR_STAR;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.RATINGS.THREE_STAR;

/**
 * Created by Snehal Tembare on 17/2/18.
 */


public class NavPrefFragment extends BaseFragment implements
        NavPrefContract.NavPrefView,
        View.OnClickListener,
        AppContract.SnapXResults {
    private RootUserPreference userPreferenceObject;

    public interface RATINGS {
        int THREE_STAR = 3;
        int FOUR_STAR = 4;
        int FIVE_STAR = 5;
    }

    public interface DISTANCE {
        int DISTANCE_ONE = 1;
        int DISTANCE_TWO = 2;
        int DISTANCE_THREE = 3;
        int DISTANCE_FOUR = 4;
        int DISTANCE_FIVE = 5;
    }

    public interface PRICING {
        int PRICE_AUTO = 0;
        int PRICE_ONE = 1;
        int PRICE_TWO = 2;
        int PRICE_THREE = 3;
        int PRICE_FOUR = 4;
    }

    private DrawerLayout mDrawerLayout;
    private Activity activity;
    private SharedPreferences preferences;
    private int userRating;
    private int pricing;

    //Default distance
    private int distance = DISTANCE_ONE;
    private boolean isSortByDistance;
    private boolean isSortByRating;

    @Inject
    AppUtility utility;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    NavPrefContract.NavPrefPresenter presenter;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    //View for Rating

    @BindView(R.id.rating_three)
    protected ImageView mImgRatingThree;

    @BindView(R.id.rating_four)
    protected ImageView mImgRatingFour;

    @BindView(R.id.rating_five)
    protected ImageView mImgRatingFive;

    //Pricing
    @BindView(R.id.txt_price_auto)
    protected TextView mTxtPriceAuto;

    @BindView(R.id.txt_price_one)
    protected TextView mTxtPriceOne;

    @BindView(R.id.txt_price_two)
    protected TextView mTxtpriceTwo;

    @BindView(R.id.txt_price_three)
    protected TextView mTxtPriceThree;

    @BindView(R.id.txt_price_four)
    protected TextView mTxtPriceFour;

    //Distance

    @BindView(R.id.txt_distance_one)
    protected TextView mTxtDistanceOne;

    @BindView(R.id.txt_distance_two)
    protected TextView mTxtDistanceTwo;

    @BindView(R.id.txt_distance_three)
    protected TextView mTxtDistanceThree;

    @BindView(R.id.txt_distance_four)
    protected TextView mTxtDistanceFour;

    @BindView(R.id.txt_pref_apply)
    protected TextView mTxtApply;

    @BindView(R.id.txt_distance_five)
    protected TextView mTxtDistanceFive;

    @BindView(R.id.radio_group_sort_by)
    protected RadioGroup mRadioGrpSortBy;

    @BindView(R.id.check_food)
    protected CheckBox mFoodCheckBox;

    @BindView(R.id.check_cuisines)
    protected CheckBox mCuisineCheckBox;

    @BindView(R.id.layout_parent)
    protected LinearLayout mParentLayout;

    @BindView(R.id.btn_price_auto)
    protected Button mBtnPriceAuto;

    @BindView(R.id.btn_price_one)
    protected Button mBtnPriceOne;

    @BindView(R.id.rd_price_two)
    protected Button mBtnPriceTwo;

    @BindView(R.id.btn_price_three)
    protected Button mBtnPriceThree;

    @BindView(R.id.btn_price_four)
    protected Button mBtnPriceFour;

    @BindView(R.id.btn_distance_one)
    protected Button mBtnDistanceOne;

    @BindView(R.id.btn_distance_two)
    protected Button mBtnDistanceTwo;

    @BindView(R.id.btn_distance_three)
    protected Button mBtnDistanceThree;

    @BindView(R.id.btn_distance_four)
    protected Button mBtnDistanceFour;

    @BindView(R.id.radio_distance)
    protected RadioButton mRdSortByDistance;

    @BindView(R.id.radio_ratings)
    protected RadioButton mRdSortByRatings;

    @BindView(R.id.btn_distance_five)
    protected Button mBtnDistanceFive;

    private List<UserCuisinePreferences> cuisinedPrefList;
    private List<UserFoodPreferences> foodPrefList;
    private String userId;
    protected NavigationView mNavigationView;

    public static boolean isDirty;
    public static boolean isCuisineDirty;
    public static boolean isFoodDirty;
    public UserPreference mUserPreference;

    @Inject
    RootUserPreference mRootUserPreference;

    @Inject
    public NavPrefFragment() {
    }

    @Inject
    HomeFragment homeFragment;

    @Inject
    PrefDbHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    public void initView() {
        utility.setContext(activity);
        snapXDialog.setContext(activity);
        preferences = utility.getSharedPreferences();
        presenter.addView(this);

        SharedPreferences preferences = utility.getSharedPreferences();
        userId = preferences.getString(getString(R.string.user_id), "");
        presenter.saveUserData();
    }


    private void updateDistanceUI(int position) {
        switch (position) {

            case DISTANCE_ONE:
                distance = DISTANCE_ONE;

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));

                mBtnDistanceOne.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnDistanceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFive.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;
            case DISTANCE_TWO:
                distance = DISTANCE_TWO;

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));

                mBtnDistanceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnDistanceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFive.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;
            case DISTANCE_THREE:
                distance = DISTANCE_THREE;

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));

                mBtnDistanceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceThree.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnDistanceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFive.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;
            case DISTANCE_FOUR:
                distance = DISTANCE_FOUR;

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));

                mBtnDistanceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFour.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnDistanceFive.setBackground(getResources().getDrawable(R.drawable.segmented_bg));

                break;

            case DISTANCE_FIVE:
                distance = DISTANCE_FIVE;

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));

                mBtnDistanceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnDistanceFive.setBackground(getResources().getDrawable(R.drawable.segmented_fg));

                break;
        }

    }

    private void updatePricingUI(int position) {

        switch (position) {
            case PRICE_AUTO:
                pricing = PRICE_AUTO;

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));

                mBtnPriceAuto.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnPriceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;

            case PRICE_ONE:
                pricing = PRICE_ONE;

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));

                mBtnPriceAuto.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceOne.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnPriceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;
            case PRICE_TWO:
                pricing = PRICE_TWO;

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));

                mBtnPriceAuto.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnPriceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;
            case PRICE_THREE:
                pricing = PRICE_THREE;

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));

                mBtnPriceAuto.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceThree.setBackground(getResources().getDrawable(R.drawable.segmented_fg));
                mBtnPriceFour.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                break;

            case PRICE_FOUR:
                pricing = PRICE_FOUR;

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));

                mBtnPriceAuto.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceOne.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceTwo.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceThree.setBackground(getResources().getDrawable(R.drawable.segmented_bg));
                mBtnPriceFour.setBackground(getResources().getDrawable(R.drawable.segmented_fg));

                break;
        }
    }

    private void registerListeners() {
        mTxtPriceAuto.setOnClickListener(this);
        mTxtPriceOne.setOnClickListener(this);
        mTxtpriceTwo.setOnClickListener(this);
        mTxtPriceThree.setOnClickListener(this);
        mTxtPriceFour.setOnClickListener(this);

        mBtnPriceAuto.setOnClickListener(this);
        mBtnPriceOne.setOnClickListener(this);
        mBtnPriceTwo.setOnClickListener(this);
        mBtnPriceThree.setOnClickListener(this);
        mBtnPriceFour.setOnClickListener(this);

        mImgRatingThree.setOnClickListener(this);
        mImgRatingFour.setOnClickListener(this);
        mImgRatingFive.setOnClickListener(this);

        mTxtDistanceOne.setOnClickListener(this);
        mTxtDistanceTwo.setOnClickListener(this);
        mTxtDistanceThree.setOnClickListener(this);
        mTxtDistanceFour.setOnClickListener(this);
        mTxtDistanceFive.setOnClickListener(this);

        mBtnDistanceOne.setOnClickListener(this);
        mBtnDistanceTwo.setOnClickListener(this);
        mBtnDistanceThree.setOnClickListener(this);
        mBtnDistanceFour.setOnClickListener(this);
        mBtnDistanceFive.setOnClickListener(this);

        mRdSortByDistance.setOnClickListener(this);
        mRdSortByRatings.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;

        try {
            view = inflater.inflate(R.layout.fragment_nav_pref, container, false);
            ButterKnife.bind(this, view);

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mNavigationView = activity.findViewById(R.id.nav_view);

        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerListeners();

        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @OnClick(R.id.txt_pref_apply)
    public void savePreferences() {
        if (null != userId && !userId.isEmpty()) {

            mUserPreference = new UserPreference(userId, String.valueOf(userRating),
                    String.valueOf(pricing),
                    String.valueOf(distance),
                    isSortByDistance, isSortByRating,
                    mRootUserPreference.getUserCuisinePreferences(),
                    mRootUserPreference.getUserFoodPreferences());

            setDataToMemoryObject();
            postOrPutUserPreferences();

        } else {
            //Non logged in user
            setDataToMemoryObject();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, homeFragment);
            mNavigationView.setCheckedItem(R.id.nav_home);
            isDirty = false;
            isCuisineDirty = false;
            isFoodDirty = false;
            transaction.commit();
        }
    }

    private void postOrPutUserPreferences() {
        if (null != mUserPreference) {
            showProgressDialog();
            if (preferences.getBoolean(getString(R.string.isFirstTimeUser), false)) {
                presenter.savePreferences(mUserPreference);
            } else {
                presenter.updatePreferences(mUserPreference);
            }
        }
    }

    public void setDataToMemoryObject() {
        mRootUserPreference.setRestaurant_rating(String.valueOf(userRating));
        mRootUserPreference.setRestaurant_price(String.valueOf(pricing));
        mRootUserPreference.setRestaurant_distance(String.valueOf(distance));
        mRootUserPreference.setSort_by_distance(isSortByDistance);
        mRootUserPreference.setSort_by_rating(isSortByRating);
    }


    @Override
    public void onResume() {
        super.onResume();
        setApplyColor();
        checkDataFromDb();
    }

    @OnClick(R.id.cardview_cuisine)
    public void showCusinePrefScreen() {
        presenter.presentScreen(CUISINE_PREF);
    }

    @OnClick(R.id.cardview_food)
    public void showFoodPrefScreen() {
        presenter.presentScreen(FOOD_PREF);
    }

    private void checkDataFromDb() {

        if (null != mRootUserPreference) {

            if (null != mRootUserPreference.getUser_Id()) {
                userId = mRootUserPreference.getUser_Id();

                if (null != mRootUserPreference.getRestaurant_price()) {
                    pricing = Integer.parseInt(mRootUserPreference.getRestaurant_price());
                    updatePricingUI(pricing);
                }

                if (null != mRootUserPreference.getRestaurant_rating()) {
                    userRating = Integer.parseInt(mRootUserPreference.getRestaurant_rating());
                    updateRatingUI(userRating);
                }

                if (null != mRootUserPreference.getRestaurant_distance()) {
                    distance = Integer.parseInt(mRootUserPreference.getRestaurant_distance());
                    updateDistanceUI(distance);
                }

                if (mRootUserPreference.isSort_by_distance()) {
                    isSortByDistance = true;
                    mRdSortByDistance.setChecked(true);
                    mRdSortByRatings.setChecked(false);
                } else if (mRootUserPreference.isSort_by_rating()) {
                    isSortByRating = true;
                    mRdSortByRatings.setChecked(true);
                    mRdSortByDistance.setChecked(false);
                }
                if (null != mRootUserPreference.getUserCuisinePreferences()) {
                    cuisinedPrefList = mRootUserPreference.getUserCuisinePreferences();
                }

                if (null != mRootUserPreference.getUserFoodPreferences()) {
                    foodPrefList = mRootUserPreference.getUserFoodPreferences();
                }
            }
            mUserPreference = helper.mapLocalObject(mRootUserPreference);
        }

        if (null != cuisinedPrefList && 0 < cuisinedPrefList.size()) {
            mCuisineCheckBox.setVisibility(View.VISIBLE);
            mCuisineCheckBox.setChecked(true);
        } else {
            mCuisineCheckBox.setVisibility(View.GONE);
            mCuisineCheckBox.setChecked(false);
        }

        if (null != foodPrefList && 0 < foodPrefList.size()) {
            mFoodCheckBox.setVisibility(View.VISIBLE);
            mFoodCheckBox.setChecked(true);
        } else {
            mFoodCheckBox.setVisibility(View.GONE);
            mFoodCheckBox.setChecked(false);
        }

    }

    private void updateRatingUI(int rating) {
        switch (rating) {
            case THREE_STAR:
                userRating = THREE_STAR;
                mImgRatingThree.setImageResource(R.drawable.three_star);
                mImgRatingFour.setImageResource(R.drawable.four_star_bw);
                mImgRatingFive.setImageResource(R.drawable.five_star_bw);
                break;

            case FOUR_STAR:
                userRating = FOUR_STAR;
                mImgRatingThree.setImageResource(R.drawable.three_star_bw);
                mImgRatingFour.setImageResource(R.drawable.four_star);
                mImgRatingFive.setImageResource(R.drawable.five_star_bw);
                break;

            case FIVE_STAR:
                userRating = FIVE_STAR;
                mImgRatingThree.setImageResource(R.drawable.three_star_bw);
                mImgRatingFour.setImageResource(R.drawable.four_star_bw);
                mImgRatingFive.setImageResource(R.drawable.five_star);
                break;
        }

    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof UserPreference) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, homeFragment);
            mNavigationView.setCheckedItem(R.id.nav_home);
            isDirty = false;
            isCuisineDirty = false;
            isFoodDirty = false;
            presenter.saveLocalData(mUserPreference);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.isFirstTimeUser), false);
            editor.apply();
            transaction.commit();
        } else if (value instanceof RootUserPreference) {
            setUserPreferenceObject((RootUserPreference) value);
        }
    }

    public void setUserPreferenceObject(RootUserPreference userPreferenceObject) {
        this.userPreferenceObject = userPreferenceObject;
    }


    @Override
    public void onPause() {
        super.onPause();
        setDataToMemoryObject();
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
                    postOrPutUserPreferences();
                };
                showSnackBar(mParentLayout, setClickListener(click));
            }
        });
    }

    @Override
    public void networkError(Object value) {
        showNetworkErrorDialog((dialog, which) -> {

        });
    }

    @Override
    public void onClick(View v) {
        isDirty = true;
        setApplyColor();
        switch (v.getId()) {
            case R.id.txt_price_auto:
                updatePricingUI(PRICE_AUTO);
                break;
            case R.id.txt_price_one:
                updatePricingUI(PRICE_ONE);
                break;
            case R.id.txt_price_two:
                updatePricingUI(PRICE_TWO);
                break;

            case R.id.txt_price_three:
                updatePricingUI(PRICE_THREE);
                break;

            case R.id.txt_price_four:
                updatePricingUI(PRICE_FOUR);
                break;

            case R.id.rating_three:
                updateRatingUI(THREE_STAR);
                break;

            case R.id.rating_four:
                updateRatingUI(FOUR_STAR);
                break;

            case R.id.rating_five:
                updateRatingUI(FIVE_STAR);
                break;

            case R.id.txt_distance_one:
                updateDistanceUI(DISTANCE_ONE);

                break;
            case R.id.txt_distance_two:
                updateDistanceUI(DISTANCE_TWO);

                break;
            case R.id.txt_distance_three:
                updateDistanceUI(DISTANCE_THREE);
                break;

            case R.id.txt_distance_four:
                updateDistanceUI(DISTANCE_FOUR);

                break;
            case R.id.txt_distance_five:
                updateDistanceUI(DISTANCE_FIVE);
                break;

            case R.id.btn_distance_one:
                updateDistanceUI(DISTANCE_ONE);

                break;
            case R.id.btn_distance_two:
                updateDistanceUI(DISTANCE_TWO);

                break;
            case R.id.btn_distance_three:
                updateDistanceUI(DISTANCE_THREE);
                break;

            case R.id.btn_distance_four:
                updateDistanceUI(DISTANCE_FOUR);

                break;
            case R.id.btn_distance_five:
                updateDistanceUI(DISTANCE_FIVE);
                break;

            case R.id.btn_price_auto:
                updatePricingUI(PRICE_AUTO);
                break;

            case R.id.btn_price_one:
                updatePricingUI(PRICE_ONE);
                break;

            case R.id.rd_price_two:
                updatePricingUI(PRICE_TWO);
                break;

            case R.id.btn_price_three:
                updatePricingUI(PRICE_THREE);
                break;

            case R.id.btn_price_four:
                updatePricingUI(PRICE_FOUR);
                break;

            case R.id.radio_distance:
                isSortByDistance = true;
                isSortByRating = false;
                break;

            case R.id.radio_ratings:
                isSortByDistance = false;
                isSortByRating = true;
                break;
        }

    }

    private void setApplyColor() {
        if (isDirty || isCuisineDirty || isFoodDirty) {
            isDirty = true;
            mTxtApply.setClickable(true);
            mTxtApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        } else {
            mTxtApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_tertiary));
            mTxtApply.setClickable(false);
        }
    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }
}
