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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.restaurantDetails.RestaurantDetails;
import com.snapxeats.common.model.restaurantDetails.RestaurantPics;
import com.snapxeats.common.model.restaurantDetails.RestaurantSpeciality;
import com.snapxeats.common.model.restaurantDetails.RootRestaurantDetails;
import com.snapxeats.dagger.AppContract;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.snapxeats.common.constants.UIConstants.MARGIN;
import static com.snapxeats.common.constants.UIConstants.PHOTO_NOTIFICATION_REQUEST_CODE;
import static com.snapxeats.common.constants.UIConstants.PHOTO_NOTIFICATION_TIME;
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
    private int dotsCount;
    private ImageView[] dots;
    private boolean isFromNotification;

    @BindView(R.id.txt_rest_name)
    protected TextView mTxtRestName;

    @BindView(R.id.layout_rest_specialties)
    protected LinearLayout mLayoutRestSpecialties;

    @BindView(R.id.view_pager)
    protected ViewPager mViewPager;

    @BindView(R.id.layout_dots)
    protected LinearLayout mSliderDotsPanel;

    @Inject
    SnapShareContract.SnapSharePresenter mPresenter;
    private RootRestaurantDetails mRootRestaurantDetails;
    private String restaurantId;

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
        mToolbar = view.findViewById(R.id.toolbar);

        mNavigationView = activity.findViewById(R.id.nav_view);
        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        initView();
    }

    public void initView() {
        mPresenter.addView(this);
        if (null != getArguments()) {
            restaurantId = getArguments().getString(getString(R.string.intent_restaurant_id));
            isFromNotification = getArguments().getBoolean(getString(R.string.notification));
        }

        showProgressDialog();
        mPresenter.getRestaurantInfo(restaurantId);
        if (null != getActivity() && isAdded()) {

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    for (int index = ZERO; index < dotsCount; index++) {
                        dots[index].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    @OnClick(R.id.img_snap)
    public void snapImage() {
        startActivity(new Intent(getActivity(), CameraActivity.class));
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
        if (value instanceof RootRestaurantDetails) {
            mRootRestaurantDetails = (RootRestaurantDetails) value;
            if (null != getActivity() && isAdded()) {
                setView(mRootRestaurantDetails.getRestaurantDetails());
            }
        }
    }

    private void setView(RestaurantDetails restaurantDetails) {
        mTxtRestName.setText(restaurantDetails.getRestaurant_name());

        setViewPager(restaurantDetails.getRestaurant_pics());
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

            Intent intent = new Intent(getActivity(), CameraActivity.class);
            intent.putExtra(getString(R.string.review_rest_id), restaurantId);
            intent.putExtra(getString(R.string.review_rest_name), mRootRestaurantDetails.getRestaurantDetails().getRestaurant_name());
            startActivity(intent);
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

            Picasso.with(getActivity()).load(restaurant_speciality.get(index).getDish_image_url())
                    .placeholder(R.drawable.ic_cuisine_placeholder).into(imageView);
            mLayoutRestSpecialties.addView(view);
        }
    }

    private void setViewPager(List<RestaurantPics> restaurant_pics) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), restaurant_pics);
        mViewPager.setAdapter(viewPagerAdapter);
        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int index = ZERO; index < dotsCount; index++) {
            dots[index] = new ImageView(getActivity());
            dots[index].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(MARGIN, ZERO, MARGIN, ZERO);
            mSliderDotsPanel.addView(dots[index], params);
        }
        dots[ZERO].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }

    @Override
    public void error(Object value) {
    }

    @Override
    public void noNetwork(Object value) {
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
                TimeUnit.SECONDS.toMillis(PHOTO_NOTIFICATION_TIME), pendingIntent);

    }
}
