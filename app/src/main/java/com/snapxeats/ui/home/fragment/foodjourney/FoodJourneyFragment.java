package com.snapxeats.ui.home.fragment.foodjourney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.foodJourney.RootFoodJourney;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.HomeActivity;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.ZERO;

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
    FoodJourneyContract.FoodJourneyPresenter mPresenter;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.list_current_food_journey)
    protected LinearLayout mListFoodJourney;

    @BindView(R.id.list_older_food_journey)
    protected LinearLayout mListOlderFoodJourney;

    private RootFoodJourney mRootFoodJourney;

    @BindView(R.id.layout_parent)
    protected LinearLayout mParentLayout;

    @BindView(R.id.txt_this_week)
    protected TextView mTxtCurrentJourney;

    @BindView(R.id.txt_older_journey)
    protected TextView mTxtOlderJourney;

    @Inject
    DbHelper dbHelper;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        Objects.requireNonNull(((BaseActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        mPresenter.addView(this);
        showProgressDialog();
        mPresenter.getFoodJourney();
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
            if (ZERO == mRootFoodJourney.getUserCurrentWeekHistory().size()
                    && ZERO == mRootFoodJourney.getUserPastHistory().size()) {
                showFoodJourneyUnavailableDialog();
            } else {
                setCurrentJourney();
                setOlderJourney();
            }
        }
    }

    private void showFoodJourneyUnavailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.foodjourney_error));
        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(getString(R.string.share_another), false);
            startActivity(intent);
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void setOlderJourney() {
        if (null != mRootFoodJourney.getUserPastHistory() && ZERO != mRootFoodJourney.getUserPastHistory().size()) {
            mTxtOlderJourney.setVisibility(View.VISIBLE);
            OlderFoodJourneyAdapter olderFoodJourneyAdapter =
                    new OlderFoodJourneyAdapter(getActivity(), mRootFoodJourney.getUserPastHistory());
            for (int row = ZERO; row < olderFoodJourneyAdapter.getCount(); row++) {
                View view = olderFoodJourneyAdapter.getView(row, null, mListOlderFoodJourney);
                mListOlderFoodJourney.addView(view);
            }
        } else {
            mTxtOlderJourney.setVisibility(View.GONE);
        }
    }

    public void setCurrentJourney() {
        if (null != mRootFoodJourney.getUserCurrentWeekHistory() && ZERO != mRootFoodJourney.getUserCurrentWeekHistory().size()) {
            mTxtCurrentJourney.setVisibility(View.VISIBLE);
            CurrentFoodJourneyAdapter currentFoodJourneyAdapter =
                    new CurrentFoodJourneyAdapter(getActivity(), mRootFoodJourney.getUserCurrentWeekHistory());
            for (int row = ZERO; row < currentFoodJourneyAdapter.getCount(); row++) {
                View view = currentFoodJourneyAdapter.getView(row, null, mListFoodJourney);
                mListFoodJourney.addView(view);
            }
        } else {
            mTxtCurrentJourney.setVisibility(View.GONE);
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
                    mPresenter.getFoodJourney();
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                mPresenter.getFoodJourney();
            }
        });
    }

    @Override
    public void networkError(Object value) {
    }
}
