package com.snapxeats.ui.home.fragment.smartphotos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.ui.home.fragment.smartphotos.draft.DraftFragment;
import com.snapxeats.ui.home.fragment.smartphotos.smart.SmartFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.android.segmented.SegmentedGroup;

import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SmartPhotoFragment extends BaseFragment {

    @BindView(R.id.btn_group)
    protected SegmentedGroup mBtnGroup;

    @BindView(R.id.btn_smart_photos)
    protected RadioButton mBtnSmartPhotos;

    @BindView(R.id.btn_draft)
    protected RadioButton mBtnDraft;

    @BindView(R.id.layout_main_contents)
    protected LinearLayout mLayoutMainContents;

    protected NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Activity activity;

    @Inject
    SmartFragment smartFragment;

    @Inject
    DraftFragment draftFragment;

    @Inject
    DbHelper dbHelper;

    private Fragment selectedFragment;

    private FragmentTransaction fragmentTransaction;

    private CircularImageView imgUser;
    private TextView txtUserName;
    private TextView txtNotLoggedIn;
    private LinearLayout mLayoutUserData;

    @Inject
    AppUtility appUtility;

    @Inject
    public SmartPhotoFragment() {
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
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_smart_photo, container, false);
            ButterKnife.bind(this, view);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar mToolbar = view.findViewById(R.id.toolbar);

        mNavigationView = activity.findViewById(R.id.nav_view);
        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                setUserInfo();
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initView();
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = appUtility.getSharedPreferences();
        String serverUserId = preferences.getString(getString(R.string.user_id), "");
        return !serverUserId.isEmpty();
    }

    public void initNavHeaderViews() {
        View mNavHeader = mNavigationView.getHeaderView(ZERO);
        imgUser = mNavHeader.findViewById(R.id.img_user);
        txtUserName = mNavHeader.findViewById(R.id.txt_user_name);
        txtNotLoggedIn = mNavHeader.findViewById(R.id.txt_nav_not_logged_in);
        TextView txtRewards = mNavHeader.findViewById(R.id.txt_nav_rewards);
        mLayoutUserData = mNavHeader.findViewById(R.id.layout_user_data);
    }

    public void setUserInfo() {
        initNavHeaderViews();
        if (dbHelper.getSnapxDataDao().loadAll().size() > ZERO) {
            txtNotLoggedIn.setVisibility(View.GONE);
            mLayoutUserData.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(dbHelper.getSnapxDataDao().loadAll().get(ZERO).getImageUrl())
                    .placeholder(R.drawable.user_image).into(imgUser);
            txtUserName.setText(dbHelper.getSnapxDataDao().loadAll().get(ZERO).getUserName());
            Menu menu = mNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.nav_logout);
            menuItem.setTitle(getString(R.string.log_out));

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

    private void initView() {
        dbHelper.setContext(getActivity());
        changeUI();
        mBtnGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.btn_smart_photos:
                    selectedFragment = smartFragment;
                    break;
                case R.id.btn_draft:
                    selectedFragment = draftFragment;
                    break;
            }
            if (null != selectedFragment) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.layout_main_contents, selectedFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void changeUI() {
        if (null != dbHelper.getDraftPhotoDao().loadAll()
                && ZERO != dbHelper.getDraftPhotoDao().loadAll().size()) {
            mBtnDraft.setEnabled(true);
            mBtnDraft.setChecked(true);

            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_main_contents, draftFragment);
            fragmentTransaction.commit();
        } else {
            mBtnDraft.setEnabled(false);
            mBtnDraft.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_tertiary));
        }

        if (null != dbHelper.getSmartPhotoDao().loadAll()
                && ZERO != dbHelper.getSmartPhotoDao().loadAll().size()) {
            mBtnSmartPhotos.setEnabled(true);
            mBtnSmartPhotos.setChecked(true);

            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_main_contents, smartFragment);
            fragmentTransaction.commit();
        } else {
            mBtnSmartPhotos.setEnabled(false);
            mBtnSmartPhotos.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_tertiary));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
