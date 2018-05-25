package com.snapxeats.ui.home;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pkmmte.view.CircularImageView;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.checkin.CheckInResponse;
import com.snapxeats.common.model.checkin.CheckInRestaurants;
import com.snapxeats.common.model.checkin.RestaurantInfo;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.smartphotos.SmartPhotoResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.NoNetworkResults;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.foodstack.FoodStackDbHelper;
import com.snapxeats.ui.home.fragment.checkin.CheckInFragment;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyFragment;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import com.snapxeats.ui.home.fragment.smartphotos.AminityAdapter;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoFragment;
import com.snapxeats.ui.home.fragment.snapnshare.CheckInAdapter;
import com.snapxeats.ui.home.fragment.snapnshare.SnapNotificationReceiver;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareFragment;
import com.snapxeats.ui.home.fragment.wishlist.WishlistDbHelper;
import com.snapxeats.ui.home.fragment.wishlist.WishlistFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.Router.Screen.LOGIN;
import static com.snapxeats.common.constants.UIConstants.LAT;
import static com.snapxeats.common.constants.UIConstants.LNG;
import static com.snapxeats.common.constants.UIConstants.LOGOUT_DIALOG_HEIGHT;
import static com.snapxeats.common.constants.UIConstants.NOTIFICATION_ID;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isCuisineDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isFoodDirty;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeContract.HomeView,
        AppContract.SnapXResults, View.OnClickListener, MediaPlayer.OnCompletionListener {

    @Inject
    HomeFragment homeFragment;

    @Inject
    NavPrefFragment navPrefFragment;

    @Inject
    WishlistFragment wishlistFragment;

    @Inject
    CheckInFragment checkInFragment;

    @Inject
    FoodJourneyFragment foodJourneyFragment;

    @Inject
    SmartPhotoFragment smartPhotoFragment;

    @Inject
    SnapShareFragment snapShareFragment;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    private FragmentTransaction transaction;

    private FragmentManager fragmentManager;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.parent_layout)
    protected View mParentLayout;

    //CheckIndialog
    protected LinearLayout mSingleRestLayout;
    protected LinearLayout mNoRestLayout;
    protected LinearLayout mBtnLayout;
    protected LinearLayout mMultipleRestLayout;
    protected RecyclerView mRecyclerView;
    protected CircularImageView mImgRestaurant;
    protected TextView mTxtRestName;
    protected TextView mTxtCancel;
    protected Button mBtnCheckIn;
    private String restaurantId;

    @Inject
    HomeContract.HomePresenter mPresenter;

    private List<SnapXData> mSnapxData;

    @Inject
    AppUtility mAppUtility;

    private SharedPreferences preferences;
    private String userId;

    @Inject
    AppUtility utility;

    @Inject
    HomeDbHelper homeDbHelper;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    RootUserPreference mRootUserPreference;

    private CircularImageView imgUser;
    private View mNavHeader;
    private TextView txtUserName;
    private TextView txtNotLoggedIn;
    private TextView txtRewards;
    private LinearLayout mLayoutUserData;
    private UserPreference mUserPreference;
    private Dialog mCheckInDialog;
    private Dialog mRewardDialog;
    private List<RestaurantInfo> mRestaurantList;
    private CheckInAdapter mAdapter;
    private CheckInRequest checkInRequest;

    //Smart photos
    private Dialog mDialog;
    private SmartPhotoResponse mSmartPhoto;
    private ImageView mImg;
    private ImageView mImgInfo;
    private ImageView mImgAudioReview;
    private ImageView mImgTextReview;
    private ImageView mImgPlayAudio;
    private ImageView mImgDownload;

    private TextView mTxtSmartPhotoRestName;
    private TextView mTxtTimeOfAudio;
    private TextView mTxtSmartRestAddress;
    private TextView mTxtRestReviewContents;

    private LinearLayout mLayoutControls;
    private LinearLayout mLayoutDescription;
    private LinearLayout mLayoutInfo;
    private LinearLayout mLayoutReview;
    private LinearLayout mLayoutAudio;
    private LinearLayout mLayoutDownload;
    private LinearLayout mLayoutDownloadSuccess;

    private ListView mListAminities;
    private List<String> mAminitiesList;
    private boolean isImageTap;
    private boolean isInfoTap;
    private boolean isReviewTap;
    private boolean isAudioViewTap;
    private boolean isAudioPlayTap;
    private String dishId;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    @Inject
    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        mPresenter.addView(this);
        utility.setContext(this);
        wishlistDbHelper.setContext(this);
        preferences = utility.getSharedPreferences();
        mNavigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        foodStackDbHelper.setContext(this);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        if (appLinkIntent != null && appLinkIntent.getData() != null) {
            dishId = appLinkIntent.getData().getSchemeSpecificPart().split("id=")[ONE];
            showProgressDialog();
            mPresenter.getSmartPhotoInfo(dishId);
        }

        userId = preferences.getString(getString(R.string.user_id), "");
        mRootUserPreference = mPresenter.getUserPreferenceFromDb();
        mSnapxData = mPresenter.getUserDataFromDb();

        setUserInfo();

        if (null != mSnapxData && mSnapxData.size() > ZERO) {
            if (mSnapxData.get(ZERO).getIsFirstTimeUser()) {
                transaction.replace(R.id.frame_layout, navPrefFragment);
            } else {
                transaction.replace(R.id.frame_layout, homeFragment);
            }
        } else
            transaction.replace(R.id.frame_layout, homeFragment);

        mNavigationView.setCheckedItem(R.id.nav_home);

        //Notification for take photo
        Intent intent = getIntent();
        boolean isFromNotification = intent.getBooleanExtra(getString(R.string.notification), false);
        boolean isShareAnother = intent.getBooleanExtra(getString(R.string.share_another), false);
        boolean isSetPref = intent.getBooleanExtra(getString(R.string.set_preferences), false);

        String restaurantId = intent.getStringExtra(getString(R.string.intent_restaurant_id));

        if (isFromNotification || isShareAnother) {
            Bundle bundle = new Bundle();
            if (isFromNotification) {
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.cancel(NOTIFICATION_ID);
                cancelReminder();
                bundle.putBoolean(getString(R.string.notification), isFromNotification);
            }
            bundle.putString(getString(R.string.intent_restaurant_id), restaurantId);
            snapShareFragment.setArguments(bundle);
            transaction.replace(R.id.frame_layout, snapShareFragment);
            mNavigationView.setCheckedItem(R.id.nav_snap);
        } else if (isSetPref) {
            transaction.replace(R.id.frame_layout, navPrefFragment);
        }

        transaction.commit();
        changeItems();
        enableReceiver();
    }

    private void changeItems() {
        Menu menu = mNavigationView.getMenu();
        //Disable smart photos option
        MenuItem smartPhotoMenu = menu.findItem(R.id.nav_smart_photos);
        MenuItem snapNShareMenu = menu.findItem(R.id.nav_snap);
        MenuItem foodJourneyMenu = menu.findItem(R.id.nav_food_journey);

        if (ZERO != dbHelper.getDraftPhotoDao().loadAll().size()) {
            smartPhotoMenu.setEnabled(true);
        } else {
            smartPhotoMenu.setEnabled(false);
        }
        snapNShareMenu.setEnabled(false);
        if (!utility.isLoggedIn()) {
            foodJourneyMenu.setEnabled(false);
        }
    }

    private void setWishlistCount() {
        LinearLayout linearLayout = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                .getActionView().findViewById(R.id.layout_wishlist_count);

        if (null != mSnapxData && ZERO < mSnapxData.size() && isLoggedIn()) {
            linearLayout.setVisibility(View.VISIBLE);
            TextView view = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                    .getActionView().findViewById(R.id.txt_count_wishlist);

            view.setText(getString(R.string.zero));

            dbHelper.setContext(this);
            if (ZERO != homeDbHelper.getWishlistCount()) {
                view.setText(String.valueOf(homeDbHelper.getWishlistCount()));
            } else {
                view.setText(getString(R.string.zero));
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = mAppUtility.getSharedPreferences();
        String serverUserId = preferences.getString(getString(R.string.user_id), "");
        return !serverUserId.isEmpty();
    }

    public void initNavHeaderViews() {
        mNavHeader = mNavigationView.getHeaderView(ZERO);
        imgUser = mNavHeader.findViewById(R.id.img_user);
        txtUserName = mNavHeader.findViewById(R.id.txt_user_name);
        txtNotLoggedIn = mNavHeader.findViewById(R.id.txt_nav_not_logged_in);
        txtRewards = mNavHeader.findViewById(R.id.txt_nav_rewards);
        mLayoutUserData = mNavHeader.findViewById(R.id.layout_user_data);
    }

    public void setUserInfo() {
        initNavHeaderViews();
        if (isLoggedIn() && null != mSnapxData && mSnapxData.size() > ZERO) {

            Picasso.with(this).load(mSnapxData.get(ZERO).getImageUrl())
                    .placeholder(R.drawable.user_image).into(imgUser);
            txtUserName.setText(mSnapxData.get(ZERO).getUserName());

        } else {
            mLayoutUserData.setVisibility(View.GONE);
            txtNotLoggedIn.setVisibility(View.VISIBLE);
            if (!isLoggedIn()) {
                Menu menu = mNavigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.nav_logout);
                menuItem.setTitle(getString(R.string.log_in));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWishlistCount();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (!isDirty) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = homeFragment;
                    break;
                case R.id.nav_wishlist:
                    if (ZERO != homeDbHelper.getWishlistCount()) {
                        selectedFragment = wishlistFragment;
                    }
                    if (!utility.isLoggedIn()) {
                        showWishlistDialogNonLoggedInUser();
                    }
                    break;
                case R.id.nav_preferences:
                    selectedFragment = navPrefFragment;
                    break;
                case R.id.nav_food_journey:
                    selectedFragment = foodJourneyFragment;
                    break;

                case R.id.nav_smart_photos:
                    selectedFragment = smartPhotoFragment;
                    break;

                case R.id.nav_check_in:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    showCheckInDialog();
                    break;

                case R.id.nav_logout:
                    if (utility.isLoggedIn()) {
                        showLogoutDialog();
                    } else {
                        mPresenter.presentScreen(LOGIN);
                    }
                    break;
            }

            if (null != selectedFragment) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }

        } else {
            dialogPreferences();
        }
        return true;
    }

    private void dialogPreferences() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(getString(R.string.preference_save_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.apply), (dialog, which) -> {

                    if (null != userId && !userId.isEmpty()) {

                        mUserPreference = homeDbHelper.mapLocalObject(mRootUserPreference);
                        postOrPutUserPreferences();
                    } else {
                        //For non logged in user
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_layout, homeFragment);
                        mNavigationView.setCheckedItem(R.id.nav_home);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        isDirty = false;
                        isCuisineDirty = false;
                        isFoodDirty = false;
                        transaction.commit();
                    }
                });
        alertDialog.show();
    }

    private void showWishlistDialogNonLoggedInUser() {
        Dialog mWishlistDialog = new Dialog(this);
        mWishlistDialog.setContentView(R.layout.layout_wishlist_dialog);
        Window window = mWishlistDialog.getWindow();
        if (null != window) {
            window.setLayout(UIConstants.CHECKIN_DIALOG_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        Button btnOk = mWishlistDialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> mWishlistDialog.dismiss());
        mWishlistDialog.show();
    }

    public void checkIn() {
        if (utility.isLoggedIn()) {
            showProgressDialog();
            for (RestaurantInfo restaurantInfo : mRestaurantList) {
                if (restaurantInfo.isSelected()) {
                    checkInRequest = new CheckInRequest();
                    checkInRequest.setRestaurant_info_id(restaurantInfo.getRestaurant_info_id());
                    checkInRequest.setReward_type(getString(R.string.reward_type_check_in));
                }
            }
            mPresenter.checkIn(checkInRequest);
        } else {
            mCheckInDialog.dismiss();
            transaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.intent_restaurant_id), restaurantId);
            snapShareFragment.setArguments(bundle);
            transaction.replace(R.id.frame_layout, snapShareFragment);
            transaction.commit();
        }
    }

    public void dismissCheckInDialog() {
        mCheckInDialog.dismiss();
    }

    /**
     * Show logout dialog
     */
    private void showLogoutDialog() {
        Dialog logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.layout_logout_dialog);
        Button btnCancel = logoutDialog.findViewById(R.id.btn_cancel);
        Button btnOk = logoutDialog.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(v -> logoutDialog.dismiss());
        btnOk.setOnClickListener(v -> {
            //Clear local db
            showProgressDialog();
            logoutDialog.dismiss();
            mPresenter.sendUserGestures(wishlistDbHelper.getFoodGestures());
        });
        Window window = logoutDialog.getWindow();
        if (null != window) {
            window.setLayout(UIConstants.CHECKIN_DIALOG_WIDTH, LOGOUT_DIALOG_HEIGHT);
        }
        logoutDialog.setCanceledOnTouchOutside(false);
        logoutDialog.show();
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof UserPreference) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, homeFragment);
            mNavigationView.setCheckedItem(R.id.nav_home);
            mDrawerLayout.closeDrawer(GravityCompat.START);
            isDirty = false;
            isCuisineDirty = false;
            isFoodDirty = false;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.isFirstTimeUser), false);
            editor.apply();
            transaction.commit();
        } else if (value instanceof Boolean) {
            if ((boolean) value) {
                mPresenter.presentScreen(LOGIN);
            }
        } else if (value instanceof CheckInRestaurants) {
            mCheckInDialog.show();
            mRestaurantList = ((CheckInRestaurants) value).getRestaurants_info();
            if (ONE == mRestaurantList.size()) {
                mNoRestLayout.setVisibility(View.GONE);
                mSingleRestLayout.setVisibility(View.VISIBLE);
                mMultipleRestLayout.setVisibility(View.GONE);
                mBtnLayout.setVisibility(View.VISIBLE);
                setSingleCheckInView(mRestaurantList.get(ZERO));
            } else if (ZERO < mRestaurantList.size()) {
                mNoRestLayout.setVisibility(View.GONE);
                mSingleRestLayout.setVisibility(View.GONE);
                mMultipleRestLayout.setVisibility(View.VISIBLE);
                mBtnLayout.setVisibility(View.VISIBLE);
                setupRecyclerView();
            } else {
                mNoRestLayout.setVisibility(View.VISIBLE);
                mBtnLayout.setVisibility(View.GONE);
                mSingleRestLayout.setVisibility(View.GONE);
                mMultipleRestLayout.setVisibility(View.GONE);
            }
        } else if (value instanceof CheckInResponse) {
            mRewardDialog = new Dialog(this);
            mRewardDialog.setContentView(R.layout.layout_reward_message);
            Window window = mRewardDialog.getWindow();
            if (null != window) {
                window.setLayout(UIConstants.REWARD_DIALOG_WIDTH, UIConstants.REWARD_DIALOG_HEIGHT);
                window.setBackgroundDrawable(getDrawable(R.drawable.reward_background));
            }
            dismissCheckInDialog();
            mRewardDialog.show();
            TextView mTxtRewards = mRewardDialog.findViewById(R.id.txt_reward_points);
            String rewards = ((CheckInResponse) value).getReward_point() + " " + getString(R.string.reward_points);
            mTxtRewards.setText(rewards);

            mRewardDialog.findViewById(R.id.btn_continue).setOnClickListener(v -> {
                String restaurantId = null;
                for (RestaurantInfo restaurantInfo : mRestaurantList) {
                    if (restaurantInfo.isSelected()) {
                        restaurantId = restaurantInfo.getRestaurant_info_id();
                    }
                }
                mRewardDialog.dismiss();
                transaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.intent_restaurant_id), restaurantId);
                snapShareFragment.setArguments(bundle);
                transaction.replace(R.id.frame_layout, snapShareFragment);
                transaction.commit();
            });
        }else if (value instanceof SmartPhotoResponse){
            mSmartPhoto = (SmartPhotoResponse) value;
            mAminitiesList = mSmartPhoto.getRestaurant_aminities();
            showSmartPhotoDialog();
        }
    }


    /**
     * Show check-In dialog
     */
    private void showCheckInDialog() {
        mCheckInDialog = new Dialog(this);
        mCheckInDialog.setContentView(R.layout.manual_checkin_dialog);
        mCheckInDialog.setCancelable(true);
        mCheckInDialog.onBackPressed();
        Window window = mCheckInDialog.getWindow();
        if (null != window) {
            window.setLayout(UIConstants.CHECKIN_DIALOG_WIDTH, UIConstants.CHECKIN_DIALOG_HEIGHT);
            window.setBackgroundDrawable(getDrawable(R.drawable.checkin_background));
        }

        mSingleRestLayout = mCheckInDialog.findViewById(R.id.single_restaurant_layout);
        mNoRestLayout = mCheckInDialog.findViewById(R.id.layout_no_restaurants);
        mBtnLayout = mCheckInDialog.findViewById(R.id.buttons_layout);
        mMultipleRestLayout = mCheckInDialog.findViewById(R.id.multiple_restaurants_layout);
        mRecyclerView = mCheckInDialog.findViewById(R.id.recyclerview);

        mImgRestaurant = mCheckInDialog.findViewById(R.id.img_restaurant);
        mTxtRestName = mCheckInDialog.findViewById(R.id.txt_rest_name);
        mTxtCancel = mCheckInDialog.findViewById(R.id.txt_cancel);
        mBtnCheckIn = mCheckInDialog.findViewById(R.id.btn_check_in);

        mCheckInDialog.findViewById(R.id.btn_check_in).setOnClickListener(this);
        mCheckInDialog.findViewById(R.id.txt_cancel).setOnClickListener(this);
        mCheckInDialog.findViewById(R.id.btn_okay).setOnClickListener(this);

        ViewTreeObserver viewTreeObserver = mRecyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            for (RestaurantInfo restaurantInfo : mRestaurantList) {
                if (restaurantInfo.isSelected()) {
                    mBtnCheckIn.setBackground(getDrawable(R.drawable.custom_button_selected));
                    mBtnCheckIn.setEnabled(true);
                }
            }
        });

        showProgressDialog();
        mPresenter.getNearByRestaurantToCheckIn(LAT, LNG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_in:
                checkIn();
                break;
            case R.id.txt_cancel:
                dismissCheckInDialog();
                break;
            case R.id.btn_okay:
                dismissCheckInDialog();
                break;

            case R.id.image_view:
                isImageTap = !isImageTap;
                if (isImageTap) {
                    mLayoutControls.setVisibility(View.VISIBLE);
                } else {
                    mLayoutControls.setVisibility(View.GONE);
                    mLayoutDescription.setVisibility(View.GONE);
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                break;

            case R.id.img_close:
                utility.resetMediaPlayer(mMediaPlayer);
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                mDialog.dismiss();
                break;

            case R.id.img_info:
                isInfoTap = !isInfoTap;
                if (isInfoTap) {
                    utility.resetMediaPlayer(mMediaPlayer);
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info_selected));
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);
                    mListAminities.setAdapter(new AminityAdapter(this, mAminitiesList));

                    mTxtSmartPhotoRestName.setText(mSmartPhoto.getRestaurant_name());
                    mTxtSmartRestAddress.setText(mSmartPhoto.getRestaurant_address());
                } else {
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));
                    mLayoutInfo.setVisibility(View.GONE);
                }
                break;

            case R.id.img_text_review:
                isReviewTap = !isReviewTap;
                if (isReviewTap) {
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review_selected));
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutReview.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mTxtRestReviewContents.setText(mSmartPhoto.getText_review());
                    utility.resetMediaPlayer(mMediaPlayer);

                } else {
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));
                    mLayoutReview.setVisibility(View.GONE);
                }
                break;

            case R.id.img_audio:
                isAudioViewTap = !isAudioViewTap;
                if (isAudioViewTap) {
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker_selected));
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);

                    if (null == mMediaPlayer) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mSmartPhoto.getAudio_review_url()));
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setOnCompletionListener(this);
                        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
                    }

                } else {
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));
                    mLayoutAudio.setVisibility(View.GONE);
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                break;

            case R.id.img_play_audio:
                isAudioPlayTap = !isAudioPlayTap;

                if (isAudioPlayTap) {
                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mImgPlayAudio.setImageDrawable(getDrawable(R.drawable.ic_audio_pause));
                    mLayoutAudio.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);

                    if (null != mMediaPlayer) {
                        mMediaPlayer.start();
                        mUpdateTimeTask.run();
                    }
                } else {
                    mImgPlayAudio.setImageDrawable(getDrawable(R.drawable.ic_play_review));
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }
                break;
            case R.id.img_download:
                mImgDownload.setImageResource(R.drawable.ic_download_select);
                mLayoutDescription.setVisibility(View.VISIBLE);
                mLayoutDownload.setVisibility(View.VISIBLE);
                mLayoutAudio.setVisibility(View.GONE);
                mLayoutInfo.setVisibility(View.GONE);
                mLayoutReview.setVisibility(View.GONE);
                break;

        }
    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (null != mMediaPlayer) {
                long currentDuration = mMediaPlayer.getCurrentPosition();

                // Displaying time completed playing
                mTxtTimeOfAudio.setText("" + utility.milliSecondsToTimer(currentDuration));

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void setupRecyclerView() {
        if (null != mRestaurantList) {
            mAdapter = new CheckInAdapter(this, mRestaurantList, (position, isSelected) -> {
                for (RestaurantInfo restaurantInfo : mRestaurantList) {
                    restaurantInfo.setSelected(false);
                }
                restaurantId = mRestaurantList.get(position).getRestaurant_info_id();
                mRestaurantList.get(position).setSelected(isSelected);
                mAdapter.notifyDataSetChanged();
            });

            LinearLayoutManager manager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    /**
     * Set view if only one restaurant is available
     */
    private void setSingleCheckInView(RestaurantInfo restaurantInfo) {
        if (null != restaurantInfo) {
            if (null != restaurantInfo.getRestaurant_logo() && !restaurantInfo.getRestaurant_logo().isEmpty()) {

                Glide.with(this)
                        .load(restaurantInfo.getRestaurant_logo())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .dontTransform())
                        .thumbnail(THUMBNAIL)
                        .into(mImgRestaurant);
            }
            if (null != restaurantInfo.getRestaurant_name() && !restaurantInfo.getRestaurant_name().isEmpty()) {
                mTxtRestName.setText(restaurantInfo.getRestaurant_name());
            }
            Window window = mCheckInDialog.getWindow();
            if (null != window) {
                window.setLayout(UIConstants.CHECKIN_SINGLE_ITEM_DIALOG_WIDTH, UIConstants.CHECKIN_SINGLE_ITEM_DIALOG_HEIGHT);
                window.setBackgroundDrawable(getDrawable(R.drawable.checkin_background));
            }
            restaurantId = restaurantInfo.getRestaurant_info_id();
            restaurantInfo.setSelected(true);
            mBtnCheckIn.setBackground(getDrawable(R.drawable.custom_button_selected));
            mBtnCheckIn.setEnabled(true);
        }
    }

    @Override
    public void error(Object value) {
    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            NoNetworkResults api = (NoNetworkResults) value;

            if (!NetworkUtility.isNetworkAvailable(this)) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();

                    switch (api) {
                        case CHECKIN_RESTAURANTS:
                            mPresenter.getNearByRestaurantToCheckIn(LAT, LNG);
                            break;
                        case CHECKIN:
                            mPresenter.checkIn(checkInRequest);
                            break;
                        case USER_PREFERENCES:
                            postOrPutUserPreferences();
                            break;
                        case FOODSTACK_GESTURES:
                            mPresenter.sendUserGestures(wishlistDbHelper.getFoodGestures());
                            break;
                        case SMART_PHOTO:
                            mPresenter.getSmartPhotoInfo(dishId);
                            break;
                    }
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();

                switch (api) {
                    case CHECKIN_RESTAURANTS:
                        mPresenter.getNearByRestaurantToCheckIn(LAT, LNG);
                        break;
                    case CHECKIN:
                        mPresenter.checkIn(checkInRequest);
                        break;
                    case USER_PREFERENCES:
                        postOrPutUserPreferences();
                        break;
                    case FOODSTACK_GESTURES:
                        mPresenter.sendUserGestures(wishlistDbHelper.getFoodGestures());
                        break;
                    case SMART_PHOTO:
                        mPresenter.getSmartPhotoInfo(dishId);
                        break;
                }
            }
        });
    }

    private void postOrPutUserPreferences() {
        if (null != mUserPreference) {
            showProgressDialog();
            if (preferences.getBoolean(getString(R.string.isFirstTimeUser), false)) {
                mPresenter.savePreferences(mUserPreference);
            } else {
                mPresenter.updatePreferences(mUserPreference);
            }
        }
    }

    @Override
    public void networkError(Object value) {
        showNetworkErrorDialog((dialog, which) -> {

        });
    }

    /**
     * on back pressed action
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        utility.resetMediaPlayer(mMediaPlayer);

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        isDirty = false;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRootUserPreference.resetRootUserPreference();
    }

    private void enableReceiver() {

        ComponentName receiver = new ComponentName(this, SnapNotificationReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void cancelReminder() {

        ComponentName componentName = new ComponentName(this, SnapNotificationReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent cancelIntent = new Intent(this, SnapNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                ZERO, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (null != am)
            am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    /**
     * Dialog to show smart photo information
     */
    private void showSmartPhotoDialog() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.draft_dialog_layout);
        Window window = mDialog.getWindow();
        if (null != window) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.START);
        }

        mLayoutDescription = mDialog.findViewById(R.id.layout_description);
        mLayoutControls = mDialog.findViewById(R.id.layout_controls);
        mLayoutInfo = mDialog.findViewById(R.id.layout_info);
        mLayoutReview = mDialog.findViewById(R.id.layout_review);
        mLayoutAudio = mDialog.findViewById(R.id.layout_audio);
        mLayoutDownload = mDialog.findViewById(R.id.layout_download);
        mLayoutDownloadSuccess = mDialog.findViewById(R.id.layout_download_success);

        mTxtSmartPhotoRestName = mDialog.findViewById(R.id.txt_rest_name);
        mTxtSmartRestAddress = mDialog.findViewById(R.id.txt_rest_address);
        mTxtRestReviewContents = mDialog.findViewById(R.id.txt_review_contents);
        mTxtTimeOfAudio = mDialog.findViewById(R.id.timer_play);

        ImageView img = mDialog.findViewById(R.id.image_view);
        ImageView imgClose = mDialog.findViewById(R.id.img_close);
        mImgInfo = mDialog.findViewById(R.id.img_info);
        mImgTextReview = mDialog.findViewById(R.id.img_text_review);
        mImgAudioReview = mDialog.findViewById(R.id.img_audio);
        mImgDownload = mDialog.findViewById(R.id.img_download);
        /**
         * TODO-Functionality yet to complete
        */
//        mImgDownload.setVisibility(View.VISIBLE);

        mImgPlayAudio = mDialog.findViewById(R.id.img_play_audio);
        mListAminities = mDialog.findViewById(R.id.list_aminities);

        if (null == mSmartPhoto.getText_review() || mSmartPhoto.getText_review().isEmpty())
            mImgTextReview.setVisibility(View.GONE);

        if (null == mSmartPhoto.getAudio_review_url() || mSmartPhoto.getAudio_review_url().isEmpty())
            mImgAudioReview.setVisibility(View.GONE);

        Glide.with(this)
                .load(mSmartPhoto.getDish_image_url())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                        .dontAnimate()
                        .dontTransform())
                .thumbnail(THUMBNAIL)
                .into(img);

        //Register listeners
        img.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        mImgInfo.setOnClickListener(this);
        mImgTextReview.setOnClickListener(this);
        mImgAudioReview.setOnClickListener(this);
        mImgDownload.setOnClickListener(this);
        mImgPlayAudio.setOnClickListener(this);

        mDialog.show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgPlayAudio.setImageDrawable(getDrawable(R.drawable.ic_play_review));
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(mSmartPhoto.getAudio_review_url()));
        mMediaPlayer.setOnCompletionListener(this);
        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.id_draft_fragment);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
