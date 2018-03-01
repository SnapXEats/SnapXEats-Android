package com.snapxeats.ui.home.fragment.navpreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.FoodPref;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.UserCuisinePreferences;
import com.snapxeats.common.model.UserCuisinePreferencesDao;
import com.snapxeats.common.model.UserFoodPreferences;
import com.snapxeats.common.model.UserPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.ui.foodpreference.FoodPreferenceActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ceryle.segmentedbutton.SegmentedButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import static com.snapxeats.common.Router.Screen.CUISINE_PREF;
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

/**
 * Created by Snehal Tembare on 17/2/18.
 */


public class NavPrefFragment extends BaseFragment implements View.OnClickListener {

    private static final int FOOD_PREF_SCREEN = 1;

    enum RATINGS {
        THREE_STAR,
        FOUR_START,
        FIVE_STAR
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

    private OnFragmentInteractionListener mListener;
    private DrawerLayout mDrawerLayout;
    private Activity activity;
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

    @Inject
    NavPrefContract.NavPrefPresenter presenter;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    //View for Rating
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

    //Pricing

    @BindView(R.id.sbg_pricing)
    protected SegmentedButtonGroup mSbgPricing;

    @BindView(R.id.seg_btn_price_auto)
    protected SegmentedButton mSbPriceAuto;

    @BindView(R.id.seg_btn_price_one)
    protected SegmentedButton mSbPriceOne;

    @BindView(R.id.seg_btn_price_two)
    protected SegmentedButton mSbPriceTwo;

    @BindView(R.id.seg_btn_price_three)
    protected SegmentedButton mSbPriceThree;

    @BindView(R.id.seg_btn_price_four)
    protected SegmentedButton mSbPriceFour;

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
    @BindView(R.id.sbg_distance)
    protected SegmentedButtonGroup mSbgDistance;

    @BindView(R.id.seg_distance_one)
    protected SegmentedButton mSbDistanceOne;

    @BindView(R.id.seg_distance_two)
    protected SegmentedButton mSbDistanceTwo;

    @BindView(R.id.seg_distance_three)
    protected SegmentedButton mSbDistanceThree;

    @BindView(R.id.seg_distance_four)
    protected SegmentedButton mSbDistanceFour;

    @BindView(R.id.seg_distance_five)
    protected SegmentedButton mSbDistanceFive;

    @BindView(R.id.txt_distance_one)
    protected TextView mTxtDistanceOne;

    @BindView(R.id.txt_distance_two)
    protected TextView mTxtDistanceTwo;

    @BindView(R.id.txt_distance_three)
    protected TextView mTxtDistanceThree;

    @BindView(R.id.txt_distance_four)
    protected TextView mTxtSegDistanceFour;

    @BindView(R.id.txt_distance_five)
    protected TextView mTxtDistanceFive;

    @BindView(R.id.card_rating_three)
    protected CardView mLayoutRatingThree;

    @BindView(R.id.card_rating_four)
    protected CardView mLayoutRatingFour;

    @BindView(R.id.card_rating_five)
    protected CardView mLayoutRatingFive;

    @BindView(R.id.radio_group_sort_by)
    protected RadioGroup mRadioGrpSortBy;

    @BindView(R.id.check_food)
    protected CheckBox mFoodCheckBox;

    @BindView(R.id.check_cuisines)
    protected CheckBox mCuisineCheckBox;
    private ArrayList<FoodPref> rootFoodPrefList;

    private List<UserCuisinePreferences> cuisinedPrefList;

    private DaoSession daoSession;
    private SnapxDataDao snapxDataDao;
    private UserCuisinePreferencesDao cuisinePreferencesDao;

    @Inject
    public NavPrefFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    private void initView() {
        utility.setContext(activity);
        snapXDialog.setContext(activity);
        preferences = utility.getSharedPreferences();

        daoSession = ((SnapXApplication) getActivity().getApplication()).getDaoSession();
    }

    private void updateDistanceUI(int position) {
        switch (position) {
            case 0:
                distance = DISTANCE_ONE;
                mSbgDistance.setPosition(position);

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                break;

            case DISTANCE_ONE:
                distance = DISTANCE_TWO;
                mSbgDistance.setPosition(position);

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                break;
            case DISTANCE_TWO:
                distance = DISTANCE_THREE;
                mSbgDistance.setPosition(position);

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                break;
            case DISTANCE_THREE:
                distance = DISTANCE_FOUR;
                mSbgDistance.setPosition(position);

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                break;
            case DISTANCE_FOUR:
                distance = DISTANCE_FIVE;
                mSbgDistance.setPosition(position);

                mTxtDistanceOne.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.pref_txt_selected_color));
                break;
        }

    }

    private void updatePricingUI(int position) {

        switch (position) {
            case PRICE_AUTO:
                pricing = PRICE_AUTO;
                mSbgPricing.clearFocus();
                mSbgPricing.setPosition(0);

                mSbPriceOne.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceTwo.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceThree.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceFour.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;

            case PRICE_ONE:
                pricing = PRICE_ONE;
                mSbgPricing.clearFocus();

                mSbgPricing.setPosition(1);

                mSbPriceAuto.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceTwo.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceThree.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceFour.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case PRICE_TWO:
                pricing = PRICE_TWO;
                mSbgPricing.clearFocus();

                mSbgPricing.setPosition(2);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case PRICE_THREE:
                pricing = PRICE_THREE;
                mSbgPricing.clearFocus();

                mSbgPricing.setPosition(3);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;

            case PRICE_FOUR:
                pricing = PRICE_FOUR;
                mSbgPricing.setPosition(4);

                mSbPriceAuto.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceOne.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceTwo.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));
                mSbPriceThree.setBackgroundColor(ContextCompat.getColor(activity, R.color.pref_slider_background));

                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                break;
        }
    }

    private void registerListeners() {
        mTxtPriceAuto.setOnClickListener(this);
        mTxtPriceOne.setOnClickListener(this);
        mTxtpriceTwo.setOnClickListener(this);
        mTxtPriceThree.setOnClickListener(this);
        mTxtPriceFour.setOnClickListener(this);

        mLayoutRatingThree.setOnClickListener(this);
        mLayoutRatingFour.setOnClickListener(this);
        mLayoutRatingFive.setOnClickListener(this);

        mTxtDistanceOne.setOnClickListener(this);
        mTxtDistanceTwo.setOnClickListener(this);
        mTxtDistanceThree.setOnClickListener(this);
        mTxtSegDistanceFour.setOnClickListener(this);
        mTxtDistanceFive.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
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

        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        mSbgPricing.setOnClickedButtonListener(position -> {
            updatePricingUI(position);
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @OnClick(R.id.btn_pref_apply)
    public void savePreferences() {
        UserFoodPreferences preferences;
        List<UserFoodPreferences> list = new ArrayList<>();
        for (FoodPref foodPref : rootFoodPrefList) {
            preferences = new UserFoodPreferences(null,foodPref.getFood_type_info_id(),
                    foodPref.is_food_like(), foodPref.is_food_favourite());
            list.add(preferences);
        }
           }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkDataFromDb();
    }

    @OnClick(R.id.cardview_cuisine)
    public void showCusinePrefScreen() {
        if (NetworkUtility.isNetworkAvailable(activity)) {
            presenter.presentScreen(CUISINE_PREF);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    @OnClick(R.id.cardview_food)
    public void showFoodPrefScreen() {
        if (NetworkUtility.isNetworkAvailable(activity)) {
            getActivity().startActivityFromFragment(this,
                    new Intent(getActivity(), FoodPreferenceActivity.class),
                    FOOD_PREF_SCREEN);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FOOD_PREF_SCREEN:
                checkDataFromDb();
                break;
        }
    }

    private void checkDataFromDb() {

        cuisinePreferencesDao = daoSession.getUserCuisinePreferencesDao();

        cuisinedPrefList = cuisinePreferencesDao.loadAll();

        if (null != cuisinedPrefList && 0 < cuisinedPrefList.size()) {
            mCuisineCheckBox.setVisibility(View.VISIBLE);
            mCuisineCheckBox.setChecked(true);
        } else {
            mCuisineCheckBox.setVisibility(View.GONE);
            mCuisineCheckBox.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_price_auto:
                /*TODO-Need to ask
                pricing = 1;*/
                mSbgPricing.setPosition(0);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_price_one:
                pricing = 1;
                mSbgPricing.setPosition(1);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_price_two:
                pricing = 2;
                mSbgPricing.setPosition(2);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_price_three:

                pricing = 3;
                mSbgPricing.setPosition(3);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_price_four:
                pricing = 4;
                mSbgPricing.setPosition(4);
                mTxtPriceAuto.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtpriceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtPriceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                break;

            case R.id.card_rating_three:
                rating = 3;
                mViewRatingThree.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mTxtRatingThree.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mRatingThree.setRating(3);

                mViewRatingFour.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
                mTxtRatingFour.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                mRatingFour.setRating(0);

                mViewRatingFive.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
                mTxtRatingFive.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                mRatingFive.setRating(0);
                break;

            case R.id.card_rating_four:
                rating = 4;
                mViewRatingFour.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mTxtRatingFour.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mRatingFour.setRating(4);

                mViewRatingThree.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
                mTxtRatingThree.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                mRatingThree.setRating(0);

                mViewRatingFive.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
                mTxtRatingFive.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                mRatingFive.setRating(0);
                break;

            case R.id.card_rating_five:
                rating = 5;
                mViewRatingFive.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mTxtRatingFive.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                mRatingFive.setRating(5);

                mViewRatingFour.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
                mTxtRatingFour.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                mRatingFour.setRating(0);

                mViewRatingThree.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
                mTxtRatingThree.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                mRatingThree.setRating(0);
                break;


            case R.id.txt_distance_one:
                distance = DISTANCE_ONE;
                mSbgDistance.setPosition(0);
                mTxtDistanceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_distance_two:
                distance = DISTANCE_TWO;
                mSbgDistance.setPosition(1);
                mTxtDistanceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_distance_three:
                distance = DISTANCE_THREE;
                mSbgDistance.setPosition(2);
                mTxtDistanceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;

            case R.id.txt_distance_four:
                distance = DISTANCE_FOUR;
                mSbgDistance.setPosition(3);
                mTxtDistanceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                break;
            case R.id.txt_distance_five:
                distance = DISTANCE_FIVE;
                mSbgDistance.setPosition(4);
                mTxtDistanceOne.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceTwo.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceThree.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtSegDistanceFour.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_color));
                mTxtDistanceFive.setTextColor(ContextCompat.getColor(activity, R.color.pref_txt_selected_color));
                break;


        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
