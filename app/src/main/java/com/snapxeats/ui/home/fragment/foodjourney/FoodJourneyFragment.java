package com.snapxeats.ui.home.fragment.foodjourney;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.model.foodJourney.RootFoodJourney;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prajakta Patil on 14/5/18.
 */

public class FoodJourneyFragment extends BaseFragment implements
        FoodJourneyContract.FoodJourneyView,
        AppContract.SnapXResults {
    protected NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Activity activity;
    @Inject
    AppUtility utility;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    FoodJourneyContract.FoodJourneyPresenter presenter;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.list_current_food_journey)
    protected ListView mListFoodJourney;

    @BindView(R.id.list_older_food_journey)
    protected ListView mListOlderFoodJourney;

    private RootFoodJourney mRootFoodJourney;

    @BindView(R.id.layout_parent)
    protected LinearLayout mParentLayout;

    private ActionBarDrawerToggle toggle;


    @Inject
    public FoodJourneyFragment() {
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
            view = inflater.inflate(R.layout.fragment_food_journey, container, false);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initView() {
        utility.setContext(activity);
        snapXDialog.setContext(activity);
        presenter.addView(this);
        showProgressDialog();
        presenter.getFoodJourney();
    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof RootFoodJourney) {
            mRootFoodJourney = ((RootFoodJourney) value);
            setCurrentJourney();
            setOlderJourney();
        }
    }

    private void setOlderJourney() {
        if (null != mRootFoodJourney) {
            OlderFoodJourneyAdapter olderFoodJourneyAdapter =
                    new OlderFoodJourneyAdapter(getActivity(), mRootFoodJourney.getUserPastHistory());
            mListOlderFoodJourney.setAdapter(olderFoodJourneyAdapter);
        }
    }

    public void setCurrentJourney() {
        if (null != mRootFoodJourney) {
            CurrentFoodJourneyAdapter currentFoodJourneyAdapter =
                    new CurrentFoodJourneyAdapter(getActivity(), mRootFoodJourney.getUserCurrentWeekHistory());
            mListFoodJourney.setAdapter(currentFoodJourneyAdapter);
        }
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity()) && null != mRootFoodJourney) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    presenter.getFoodJourney();
                };
                showSnackBar(mParentLayout, setClickListener(click));
            }
        });
    }

    @Override
    public void networkError(Object value) {

    }
}
