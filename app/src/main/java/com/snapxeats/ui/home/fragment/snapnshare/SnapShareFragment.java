package com.snapxeats.ui.home.fragment.snapnshare;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.restaurantInfo.RestaurantInfo;
import com.snapxeats.common.model.restaurantInfo.RestaurantSpeciality;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.snapxeats.common.constants.UIConstants.PHOTO_NOTIFICATION_REQUEST_CODE;
import static com.snapxeats.common.constants.UIConstants.PHOTO_NOTIFICATION_TIME;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SnapShareFragment extends BaseFragment implements SnapShareContract.SnapShareView,
        AppContract.SnapXResults {

    private Toolbar mToolbar;
    protected NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Activity activity;
    private boolean isFromNotification;

    @BindView(R.id.txt_rest_name)
    protected TextView mTxtRestName;

    @BindView(R.id.layout_rest_specialties)
    protected LinearLayout mLayoutRestSpecialties;

    @BindView(R.id.view_pager)
    protected ViewPager mViewPager;

    @BindView(R.id.layout_dots)
    protected LinearLayout mSliderDotsPanel;

    @BindView(R.id.layout_parent)
    protected LinearLayout mParentLayout;

    @Inject
    SnapShareContract.SnapSharePresenter mPresenter;

    private RootRestaurantInfo mRootRestaurantInfo;
    private String restaurantId;

    @Inject
    AppUtility utility;

    @Inject
    DbHelper dbHelper;

    @Inject
    public SnapShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_snap_share, container, false);
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

        Toolbar mToolbar = view.findViewById(R.id.toolbar);

        mNavigationView = activity.findViewById(R.id.nav_view);
        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));
        MenuItem smartPhotoMenu = mNavigationView.getMenu().findItem(R.id.nav_smart_photos);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (ZERO != dbHelper.getDraftPhotoDao().loadAll().size()) {
                    smartPhotoMenu.setEnabled(true);
                } else {
                    smartPhotoMenu.setEnabled(false);
                }
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void initView() {
        mPresenter.addView(this);
        dbHelper.setContext(getActivity());
        if (null != getArguments()) {
            restaurantId = getArguments().getString(getString(R.string.intent_restaurant_id));
            isFromNotification = getArguments().getBoolean(getString(R.string.notification));
        }
        showProgressDialog();
        mPresenter.getRestaurantInfo(restaurantId);

        if (null != getActivity() && isAdded()) {
            utility.setImagesCorousal(mViewPager);
        }
    }

    @OnClick(R.id.img_snap)
    public void snapImage() {
        startCameraActivity();
    }

    private void startCameraActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(getString(R.string.restaurant_info_object), mRootRestaurantInfo);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof RootRestaurantInfo) {
            mRootRestaurantInfo = (RootRestaurantInfo) value;
            if (null != getActivity() && isAdded()) {
                setView(mRootRestaurantInfo.getRestaurantDetails());
            }
        }
    }

    private void setView(RestaurantInfo restaurantDetails) {
        mTxtRestName.setText(restaurantDetails.getRestaurant_name());
        utility.setViewPager(restaurantDetails.getRestaurant_pics(), mViewPager, mSliderDotsPanel);
        setMenusView(restaurantDetails.getRestaurant_speciality());
        if (!isFromNotification) {
            showPhotoReminderDialog();
        }
    }

    /**
     * Dialog for Take photo as a Reminder
     */
    private void showPhotoReminderDialog() {
        Dialog mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.share_photo_dialog);
        Window window = mDialog.getWindow();
        if (null != window) {
            window.setLayout(UIConstants.CHECKIN_DIALOG_WIDTH, UIConstants.CHECKIN_DIALOG_HEIGHT);
            window.setBackgroundDrawable(getActivity().getDrawable(R.drawable.checkin_background));
        }
        mDialog.show();

        Button mBtnTakePhoto = mDialog.findViewById(R.id.btn_take_photo);
        TextView mTxtRemindMeLater = mDialog.findViewById(R.id.txt_remind_me_later);

        mBtnTakePhoto.setOnClickListener(v -> {
            startCameraActivity();
            mDialog.dismiss();
        });

        mTxtRemindMeLater.setOnClickListener(v -> {
            mDialog.dismiss();
            startTimerForNotification();
        });
    }

    private void setMenusView(List<RestaurantSpeciality> restaurant_speciality) {
        for (int index = ZERO; index < restaurant_speciality.size(); index++) {
            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            View view = mInflater.inflate(R.layout.layout_rest_specialties,
                    mLayoutRestSpecialties, false);
            ImageView imageView = view.findViewById(R.id.img_restaurant_specialties);

            Glide.with(getActivity())
                    .load(restaurant_speciality.get(index).getDish_image_url())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .dontTransform())
                    .thumbnail(THUMBNAIL)
                    .into(imageView);
            mLayoutRestSpecialties.addView(view);
        }
    }

    @Override
    public void error(Object value) {
    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();

        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity()) && null != mRootRestaurantInfo) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    mPresenter.getRestaurantInfo(restaurantId);
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                mPresenter.getRestaurantInfo(restaurantId);
            }
        });
    }

    @Override
    public void networkError(Object value) {
    }

    public void startTimerForNotification() {
        Intent intent = new Intent(getActivity(), SnapNotificationReceiver.class);
        intent.putExtra(getString(R.string.intent_restaurant_id), restaurantId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), PHOTO_NOTIFICATION_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                TimeUnit.MINUTES.toMillis(PHOTO_NOTIFICATION_TIME), pendingIntent);

    }
}
