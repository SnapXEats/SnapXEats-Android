package com.snapxeats.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.pkmmte.view.CircularImageView;
import com.snapxeats.BaseActivity;
import com.snapxeats.DownloadTask;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.checkin.CheckInResponse;
import com.snapxeats.common.model.checkin.CheckInRestaurants;
import com.snapxeats.common.model.checkin.RestaurantInfo;
import com.snapxeats.common.model.location.Location;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.smartphotos.SmartPhotoResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.NoNetworkResults;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.LocationHelper;
import com.snapxeats.ui.foodstack.FoodStackDbHelper;
import com.snapxeats.ui.home.fragment.checkin.CheckInDbHelper;
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
import com.snapxeats.ui.login.InstagramDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.Router.Screen.LOGIN;
import static com.snapxeats.common.constants.UIConstants.ACCESS_FINE_LOCATION;
import static com.snapxeats.common.constants.UIConstants.CHANGE_LOCATION_PERMISSIONS;
import static com.snapxeats.common.constants.UIConstants.DIALOG_Y_POSITION;
import static com.snapxeats.common.constants.UIConstants.LOGOUT_DIALOG_HEIGHT;
import static com.snapxeats.common.constants.UIConstants.NOTIFICATION_ID;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.PREF_DEFAULT_STRING;
import static com.snapxeats.common.constants.UIConstants.STORAGE_REQUEST_PERMISSION;
import static com.snapxeats.common.constants.UIConstants.STRING_SPACE;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isCuisineDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isDirty;
import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isFoodDirty;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeContract.HomeView,
        AppContract.SnapXResults,
        View.OnClickListener,
        MediaPlayer.OnCompletionListener,
        InstagramDialog.InstagramDialogListener,
        DownloadTask.OnDownloadCompleted {

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

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
    private Menu menu;
    @Inject
    HomeFragment homeFragment;

    @Inject
    NavPrefFragment navPrefFragment;

    @Inject
    WishlistFragment wishlistFragment;

    @Inject
    FoodJourneyFragment foodJourneyFragment;

    @Inject
    SmartPhotoFragment smartPhotoFragment;

    @Inject
    SnapShareFragment snapShareFragment;

    @Inject
    HomeContract.HomePresenter mPresenter;

    @Inject
    AppUtility utility;

    LocationHelper locationHelper;

    @Inject
    HomeDbHelper homeDbHelper;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    RootUserPreference mRootUserPreference;

    com.snapxeats.common.model.location.Location mCurrentLocation;
    private double lattitude;
    private double longitude;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    @Inject
    DbHelper dbHelper;

    private SelectedBundle selectedBundle;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private SnapXUserRequest snapXUserRequest;
    private CallbackManager mCallbackManager;
    private String restaurantId;
    private List<SnapXData> mSnapxData;
    private SharedPreferences preferences;
    private String userId;
    private UserPreference mUserPreference;
    private Dialog mCheckInDialog;
    private Dialog mRewardDialog;
    private List<RestaurantInfo> mRestaurantList;
    private CheckInAdapter mAdapter;
    private CheckInRequest checkInRequest;
    private String rewards;

    //Smart photos
    private Dialog mSmartPhotoDialog;
    private SmartPhotoResponse mSmartPhoto;
    private ImageView mImg;
    private ImageView mImgClose;
    private ImageView mImgInfo;
    private ImageView mImgAudioReview;
    private ImageView mImgTextReview;
    private ImageView mImgPlayAudio;
    private ImageView mImgDownload;
    private ProgressDialog mProgressbar;

    private TextView mTxtSmartPhotoRestName;
    private TextView mTxtTimeOfAudio;
    private TextView mTxtSmartRestAddress;
    private TextView mTxtRestReviewContents;

    private LinearLayout mLayoutControls;
    private LinearLayout mLayoutDescription;
    private LinearLayout mLayoutInfo;
    private LinearLayout mLayoutReview;
    private LinearLayout mLayoutAudio;
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
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private boolean isSnapNShareEnabled = false;

    @Inject
    CheckInDbHelper checkInDbHelper;

    private MenuItem snapNShareMenu, foodJourneyMenu, checkInMenu;

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
    private Location currentLocation;

    private GeofencingClient mGeofencingClient;

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

        snapXDialog.setContext(this);
        mCallbackManager = CallbackManager.Factory.create();
        checkInDbHelper.setContext(this);
        date = new Date();
        simpleDateFormat = new SimpleDateFormat(getString(R.string.format_checkedIn));
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        userId = preferences.getString(getString(R.string.user_id), PREF_DEFAULT_STRING);
        mRootUserPreference = mPresenter.getUserPreferenceFromDb();
        mSnapxData = mPresenter.getUserDataFromDb();
        setAppLink();
        utility.setUserInfo(mNavigationView);

        if (isDataLoaded()) {
            if (mSnapxData.get(ZERO).getIsFirstTimeUser()) {
                transaction.replace(R.id.frame_layout, navPrefFragment);
            } else {
                transaction.replace(R.id.frame_layout, homeFragment);
            }
        } else
            transaction.replace(R.id.frame_layout, homeFragment);

        mNavigationView.setCheckedItem(R.id.nav_home);

        //Notification for take photo
        setNotificationForPhoto();
        //Notification for check in
        setNotificationCheckIn();

        transaction.commit();
        changeItems();
        utility.enableReceiver(new ComponentName(this, SnapNotificationReceiver.class));

        if (dbHelper.getCheckInDataDao().loadAll().size() > ZERO &&
                dbHelper.getCheckInDataDao().loadAll().get(ZERO).getIsCheckedIn()) {
            isSnapNShareEnabled = utility.getCheckedInTimeDiff();
        }
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //Update wishlist count
                setWishlistCount();

                //Update user info
                utility.setUserInfo(mNavigationView);

                //Update CheckIn and Snap-N-Share menus
                if (dbHelper.getCheckInDataDao().loadAll().size() > ZERO &&
                        dbHelper.getCheckInDataDao().loadAll().get(ZERO).getIsCheckedIn()) {
                    utility.getCheckedInTimeDiff();
                }

                //Update Smart Photos menu
                MenuItem smartPhotoMenu = mNavigationView.getMenu().findItem(R.id.nav_smart_photos);
                if (dbHelper.isSmartPhotoAvailable() || dbHelper.isDraftPhotoAvailable()) {
                    smartPhotoMenu.setEnabled(true);
                } else {
                    smartPhotoMenu.setEnabled(false);
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setNotificationCheckIn() {
        Intent intent = getIntent();
        boolean isCheckInNotification = intent.getBooleanExtra(getString(R.string.checkin_notification), false);
        String checkInrestId = intent.getStringExtra(getString(R.string.intent_restaurant_id));

        if (isCheckInNotification && null != checkInrestId) {
            if (utility.isLoggedIn()) {
                showProgressDialog();
                for (RestaurantInfo restaurantInfo : mRestaurantList) {
                    if (restaurantInfo.isSelected()) {
                        checkInRequest = new CheckInRequest();
                        checkInRequest.setRestaurant_info_id(checkInrestId);
                        checkInRequest.setReward_type(getString(R.string.reward_type_check_in));
                    }
                }
                mPresenter.checkIn(checkInRequest);
            }
        }
    }

    /**
     * Check id SnapXData is loaded or not
     */
    public boolean isDataLoaded() {
        return null != mSnapxData && ZERO < mSnapxData.size();
    }

    private void setNotificationForPhoto() {
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
                utility.cancelReminder(new Intent(this, SnapNotificationReceiver.class));
                bundle.putBoolean(getString(R.string.notification), true);
            }
            bundle.putString(getString(R.string.intent_restaurant_id), restaurantId);
            snapShareFragment.setArguments(bundle);
            transaction.replace(R.id.frame_layout, snapShareFragment);
            mNavigationView.setCheckedItem(R.id.nav_snap);
        } else if (isSetPref) {
            transaction.replace(R.id.frame_layout, navPrefFragment);
        }
    }

    // ATTENTION: This was auto-generated to handle app links.
    private void setAppLink() {
        Intent appLinkIntent = getIntent();
        if (null != appLinkIntent && null != appLinkIntent.getData()) {
            dishId = appLinkIntent.getData().getSchemeSpecificPart().split("id=")[ONE];
            showProgressDialog();
            mPresenter.getSmartPhotoInfo(dishId);
        }
    }

    public void initNavMenuItems() {
        menu = mNavigationView.getMenu();
        snapNShareMenu = menu.findItem(R.id.nav_snap);
        foodJourneyMenu = menu.findItem(R.id.nav_food_journey);
        checkInMenu = menu.findItem(R.id.nav_check_in);
    }

    private void changeItems() {
        initNavMenuItems();
        snapNShareMenu.setEnabled(false);
        if (!utility.isLoggedIn()) {
            foodJourneyMenu.setEnabled(false);
        }
        if (dbHelper.getCheckInDataDao().loadAll().size() > ZERO &&
                dbHelper.getCheckInDataDao().loadAll().get(ZERO).getIsCheckedIn()) {
            checkInMenu.setTitle(getString(R.string.checkout));
            snapNShareMenu.setEnabled(true);
        }
    }

    private void setWishlistCount() {
        LinearLayout linearLayout = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                .getActionView().findViewById(R.id.layout_wishlist_count);

        if (isDataLoaded() && utility.isLoggedIn()) {
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

    @Override
    protected void onResume() {
        super.onResume();
        setWishlistCount();
        utility.setUserInfo(mNavigationView);
        if (dbHelper.getCheckInDataDao().loadAll().size() > ZERO &&
                dbHelper.getCheckInDataDao().loadAll().get(ZERO).getIsCheckedIn()) {
            isSnapNShareEnabled = utility.getCheckedInTimeDiff();
        }
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
                case R.id.nav_snap:
                    selectedFragment = snapShareFragment;
                    Bundle bundle = new Bundle();
                    if (isSnapNShareEnabled) {
                        if (snapShareFragment.isVisible()) {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            bundle.putString(getString(R.string.intent_restaurant_id),
                                    dbHelper.getCheckInDataDao().loadAll().get(ZERO).getRestId());
                            bundle.putBoolean(getString(R.string.notification), true);
                            snapShareFragment.setArguments(bundle);
                            transaction.replace(R.id.frame_layout, snapShareFragment);
                        }
                    }
                    break;
                case R.id.nav_smart_photos:
                    selectedFragment = smartPhotoFragment;
                    break;

                case R.id.nav_check_in:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    if (isSnapNShareEnabled) {
                        updateCheckout();
                    } else {
                        showCheckInDialog();
                    }
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

    private void updateCheckout() {
        checkInDbHelper.clearCheckInData();
        isSnapNShareEnabled = false;
        snapNShareMenu.setEnabled(false);
        checkInMenu.setTitle(getString(R.string.check_in));
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment);
        transaction.commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
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
                        transaction = fragmentManager.beginTransaction();
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

    private void saveCheckInDataToDb() {
        String checkdInTime = simpleDateFormat.format(date);
        String restId = mRestaurantList.get(ZERO).getRestaurant_info_id();
        if (utility.isLoggedIn()) {
            checkInDbHelper.saveCheckInDataInDb(restId, userId, checkdInTime, true);
        } else {
            checkInDbHelper.saveCheckInDataInDb(restId, null, checkdInTime, true);
        }
    }

    public void checkIn() {
        isSnapNShareEnabled = true;
        snapNShareMenu.setEnabled(true);
        checkInMenu.setTitle(getString(R.string.checkout));
        saveCheckInDataToDb();

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
            setUserPrefData();
        } else if (value instanceof Boolean) {
            if ((boolean) value) {
                mPresenter.presentScreen(LOGIN);
            }
        } else if (value instanceof CheckInRestaurants) {
            mRestaurantList = ((CheckInRestaurants) value).getRestaurants_info();
            setDataForCheckIn();
        } else if (value instanceof CheckInResponse) {
            rewards = ((CheckInResponse) value).getReward_point() + STRING_SPACE + getString(R.string.reward_points);
            showCheckInResponseDialog();
            saveCheckInDataToDb();
            homeDbHelper.updateRewardPoint(((CheckInResponse) value).getReward_point());

        } else if (value instanceof SmartPhotoResponse) {
            mSmartPhoto = (SmartPhotoResponse) value;
            mAminitiesList = mSmartPhoto.getRestaurant_aminities();
            showSmartPhotoDialog();
        } else if (value instanceof SnapXUserResponse) {
            SnapXUserResponse snapXUserResponse = (SnapXUserResponse) value;
            String mToken = snapXUserResponse.getUserInfo().getToken();
            selectedBundle.onBundleSelect(mToken);
        }
    }

    /**
     * set CheckInResponse data values
     **/
    private void showCheckInResponseDialog() {
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
    }

    /**
     * set CheckInRestaurants data values
     **/
    private void setDataForCheckIn() {
        mCheckInDialog.show();
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
    }

    /**
     * set UserPreference data values
     **/
    private void setUserPrefData() {
        transaction = fragmentManager.beginTransaction();
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
    }

    /**
     * Show check-In dialog
     */
    private void showCheckInDialog() {
        initCheckInViews();
        viewTreeObsCheckIn();

        //Setting context to utility to use location functions
        utility.setContext(this);
        getCurrentLocation();
    }

    private void viewTreeObsCheckIn() {
        ViewTreeObserver viewTreeObserver = mRecyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            for (RestaurantInfo restaurantInfo : mRestaurantList) {
                if (restaurantInfo.isSelected()) {
                    mBtnCheckIn.setBackground(getDrawable(R.drawable.custom_button_selected));
                    mBtnCheckIn.setEnabled(true);
                }
            }
        });
    }

    private void initCheckInViews() {
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
                if (null != mMediaPlayer) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                mSmartPhotoDialog.dismiss();
                break;

            case R.id.img_info:
                isInfoTap = !isInfoTap;
                if (isInfoTap) {
                    isAudioViewTap = false;
                    isReviewTap = false;

                    utility.resetMediaPlayer(mMediaPlayer);
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info_selected));
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);
                    mLayoutDownloadSuccess.setVisibility(View.GONE);

                    mListAminities.setAdapter(new AminityAdapter(this, mAminitiesList));

                    mTxtSmartPhotoRestName.setText(mSmartPhoto.getRestaurant_name());
                    mTxtSmartRestAddress.setText(mSmartPhoto.getRestaurant_address());
                } else {
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutDescription.setVisibility(View.GONE);
                }
                break;

            case R.id.img_text_review:
                isReviewTap = !isReviewTap;
                if (isReviewTap) {
                    isAudioViewTap = false;
                    isInfoTap = false;

                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review_selected));
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutReview.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutDownloadSuccess.setVisibility(View.GONE);

                    mTxtRestReviewContents.setText(mSmartPhoto.getText_review());
                    utility.resetMediaPlayer(mMediaPlayer);

                } else {
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));
                    mLayoutReview.setVisibility(View.GONE);
                    mLayoutDescription.setVisibility(View.GONE);
                }
                break;

            case R.id.img_audio:
                isAudioViewTap = !isAudioViewTap;
                if (isAudioViewTap) {
                    isReviewTap = false;
                    isInfoTap = false;

                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker_selected));
                    mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));
                    mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);
                    mLayoutDownloadSuccess.setVisibility(View.GONE);

                    if (null == mMediaPlayer) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mSmartPhoto.getAudio_review_url()));
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setOnCompletionListener(this);
                        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
                    }

                } else {
                    mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));
                    mLayoutAudio.setVisibility(View.GONE);
                    mLayoutDescription.setVisibility(View.GONE);
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
                    mLayoutDownloadSuccess.setVisibility(View.GONE);

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
                mImgInfo.setImageDrawable(getDrawable(R.drawable.ic_info));
                mImgTextReview.setImageDrawable(getDrawable(R.drawable.ic_text_review));
                mImgAudioReview.setImageDrawable(getDrawable(R.drawable.ic_audio_speaker));

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
        switch (requestCode) {
            case ACCESS_FINE_LOCATION:
                handleLocationRequest(permissions, grantResults);
                break;

            case STORAGE_REQUEST_PERMISSION:
                handleStoragePermissions(permissions, grantResults);
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleStoragePermissions(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[ZERO] == PackageManager.PERMISSION_GRANTED) {
            startDownloadingTask();
        } else if (!shouldShowRequestPermissionRationale(permissions[ZERO])) {
            snapXDialog.showChangePermissionDialog(STORAGE_REQUEST_PERMISSION);
        } else {
            SnapXToast.showToast(this, getString(R.string.enable_storage_permissions));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleLocationRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[ZERO] == PackageManager.PERMISSION_GRANTED && utility.checkPermissions()) {
            getCurrentLocation();

        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            snapXDialog.showChangePermissionDialog(CHANGE_LOCATION_PERMISSIONS);
        } else {
            SnapXToast.showToast(this, getString(R.string.enable_location_permission));
        }
    }

    private void startDownloadingTask() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (NetworkUtility.isNetworkAvailable(this)) {
                mImgDownload.setImageResource(R.drawable.ic_download_select);
                mLayoutDescription.setVisibility(View.GONE);
                mLayoutAudio.setVisibility(View.GONE);
                mLayoutInfo.setVisibility(View.GONE);
                mLayoutReview.setVisibility(View.GONE);

                new DownloadTask(HomeActivity.this, this, mSmartPhoto)
                        .execute(mSmartPhoto.getDish_image_url(), mSmartPhoto.getAudio_review_url());
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                });
            }
        }
    }

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
                        .asBitmap()
                        .placeholder(R.drawable.ic_pref_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
                            mPresenter.getNearByRestaurantToCheckIn(lattitude, longitude);
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
                        mPresenter.getNearByRestaurantToCheckIn(lattitude, longitude);
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
        if (null != mMediaPlayer)
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

    /**
     * Dialog to show smart photo information
     */
    private void showSmartPhotoDialog() {
        mSmartPhotoDialog = new Dialog(this);
        mSmartPhotoDialog.setOnCancelListener(dialog -> utility.resetMediaPlayer(mMediaPlayer));
        mSmartPhotoDialog.setContentView(R.layout.draft_dialog_layout);
        Window window = mSmartPhotoDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = DIALOG_Y_POSITION;

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        window.setAttributes(params);
        initViewsForSmartPhotoDialog();

        //Check duplicate entry for dish to download
        if (homeDbHelper.isDuplicateSmartPhoto(mSmartPhoto.getRestaurant_dish_id())) {
            mImgDownload.setVisibility(View.GONE);
        } else {
            mImgDownload.setVisibility(View.VISIBLE);
        }

        mImgPlayAudio = mSmartPhotoDialog.findViewById(R.id.img_play_audio);
        mListAminities = mSmartPhotoDialog.findViewById(R.id.list_aminities);

        if (null == mSmartPhoto.getText_review() || mSmartPhoto.getText_review().isEmpty())
            mImgTextReview.setVisibility(View.GONE);

        if (null == mSmartPhoto.getAudio_review_url() || mSmartPhoto.getAudio_review_url().isEmpty())
            mImgAudioReview.setVisibility(View.GONE);

        Glide.with(this)
                .load(mSmartPhoto.getDish_image_url())
                .asBitmap()
                .placeholder(R.drawable.ic_rest_info_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(THUMBNAIL)
                .into(mImg);

        //Register listeners
        registerListeners();
        mSmartPhotoDialog.show();
    }

    private void registerListeners() {
        mImg.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
        mImgInfo.setOnClickListener(this);
        mImgTextReview.setOnClickListener(this);
        mImgAudioReview.setOnClickListener(this);
        mImgDownload.setOnClickListener(this);
        mImgPlayAudio.setOnClickListener(this);
    }

    private void initViewsForSmartPhotoDialog() {
        mLayoutDescription = mSmartPhotoDialog.findViewById(R.id.layout_description);
        mLayoutControls = mSmartPhotoDialog.findViewById(R.id.layout_controls);
        mLayoutInfo = mSmartPhotoDialog.findViewById(R.id.layout_info);
        mLayoutReview = mSmartPhotoDialog.findViewById(R.id.layout_review);
        mLayoutAudio = mSmartPhotoDialog.findViewById(R.id.layout_audio);
        mLayoutDownloadSuccess = mSmartPhotoDialog.findViewById(R.id.layout_download_success);

        mTxtSmartPhotoRestName = mSmartPhotoDialog.findViewById(R.id.txt_rest_name);
        mTxtSmartRestAddress = mSmartPhotoDialog.findViewById(R.id.txt_rest_address);
        mTxtRestReviewContents = mSmartPhotoDialog.findViewById(R.id.txt_review_contents);
        mTxtTimeOfAudio = mSmartPhotoDialog.findViewById(R.id.timer_play);

        mImg = mSmartPhotoDialog.findViewById(R.id.image_view);
        mImgClose = mSmartPhotoDialog.findViewById(R.id.img_close);
        mImgInfo = mSmartPhotoDialog.findViewById(R.id.img_info);
        mImgTextReview = mSmartPhotoDialog.findViewById(R.id.img_text_review);
        mImgAudioReview = mSmartPhotoDialog.findViewById(R.id.img_audio);
        mImgDownload = mSmartPhotoDialog.findViewById(R.id.img_download);

        mProgressbar = new ProgressDialog(this);
        mProgressbar.setMessage(getString(R.string.downloading));
        mProgressbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressbar.setCancelable(false);
        mProgressbar.setMax(UIConstants.PERCENTAGE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgPlayAudio.setImageDrawable(getDrawable(R.drawable.ic_play_review));
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(mSmartPhoto.getAudio_review_url()));
        mMediaPlayer.setOnCompletionListener(this);
        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case STORAGE_REQUEST_PERMISSION:
                startDownloadingTask();
                break;

            case CHANGE_LOCATION_PERMISSIONS:
                getCurrentLocation();
                break;
        }
        //login facebook callback
        if (resultCode == RESULT_OK) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onReturnValue(String token) {
        showProgressDialog();
        mPresenter.getInstaInfo(token);
    }

    public void setInstaValue(SelectedBundle selectedBundle) {
        this.selectedBundle = selectedBundle;
    }

    public void loginWithFacebook() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        snapXUserRequest = new SnapXUserRequest(AccessToken.getCurrentAccessToken().getToken(),
                                getString(R.string.platform_facebook), AccessToken.getCurrentAccessToken().getUserId());
                        showProgressDialog();
                        mPresenter.getUserdata(snapXUserRequest);
                    }

                    @Override
                    public void onCancel() {
                        mPresenter.response(SnapXResult.NETWORKERROR, null);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        mPresenter.response(SnapXResult.ERROR, null);
                    }
                });
    }

    @Override
    public void isDownloadComplete(boolean isComplete, SmartPhotoResponse smartPhotoResponse) {
        if (isComplete) {
            mProgressbar.dismiss();
            mImgDownload.setVisibility(View.GONE);
            mLayoutDescription.setVisibility(View.VISIBLE);
            mLayoutDownloadSuccess.setVisibility(View.VISIBLE);
            mSmartPhoto.setDish_image_url(smartPhotoResponse.getDish_image_url());
            mSmartPhoto.setAudio_review_url(smartPhotoResponse.getAudio_review_url());
            homeDbHelper.saveSmartPhotoDataInDb(mSmartPhoto);
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    public void getCurrentLocation() {

        android.location.Location location;

        if (utility.checkPermissions()) {
            showProgressDialog();
            location = utility.getLocation();
            if (null != location) {
                dismissProgressDialog();
                mCurrentLocation = new Location(location.getLatitude(),
                        location.getLongitude(), utility.getPlaceName(location));

                lattitude = mCurrentLocation.getLat();
                longitude = mCurrentLocation.getLng();
                showProgressDialog();
                mPresenter.getNearByRestaurantToCheckIn(lattitude, longitude);
            }
        }
    }

    public interface SelectedBundle {
        void onBundleSelect(String bundle);
    }
}
