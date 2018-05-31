package com.snapxeats.ui.home.fragment.smartphotos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
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

    @Inject
    AppUtility appUtility;

    private CircularImageView imgUser;
    private TextView txtUserName;
    private TextView txtNotLoggedIn;
    private LinearLayout mLayoutUserData;

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
                appUtility.setUserInfo(mNavigationView, dbHelper.getSnapxDataDao().loadAll());
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initView();
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
