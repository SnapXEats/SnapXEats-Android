package com.snapxeats.ui.foodpreference;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.common.model.preference.RootFoodPref;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 13/2/17.
 */

public class FoodPreferenceActivity extends BaseActivity implements
        FoodPreferenceContract.FoodPreferenceView,
        AppContract.SnapXResults {

    @BindView(R.id.recyclerview_food)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.txt_reset)
    protected TextView mTxtReset;

    @BindView(R.id.btn_food_pref_save)
    protected TextView mTxtSave;

    @Inject
    FoodPreferenceContract.FoodPreferencePresenter presenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    FoodPrefDbHelper helper;

    @Inject
    RootUserPreference mRootUserPreference;

    private FoodPrefAdapter mFoodPrefAdapter;
    private List<FoodPref> rootFoodPrefList;
    private List<UserFoodPreferences> userFoodPreferencesList;
    private String userId;
    private SharedPreferences preferences;

    public static boolean isDirty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_preference);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        presenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.food_preference));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userFoodPreferencesList = new ArrayList<>();
        rootFoodPrefList = new ArrayList<>();
        preferences = utility.getSharedPreferences();
        userId = preferences.getString(getString(R.string.user_id), "");

        showProgressDialog();
        presenter.getFoodPrefList();

        ViewTreeObserver viewTreeObserver = mRecyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {

            List<FoodPref> selectedCuisineList = helper.getSelectedFoodList(rootFoodPrefList);

            if (selectedCuisineList.size() > 0) {
                mTxtReset.setClickable(true);
                mTxtReset.setTextColor(ContextCompat.getColor(FoodPreferenceActivity.this,
                        R.color.text_color_primary));
            } else {
                mTxtReset.setClickable(false);
                mTxtReset.setTextColor(ContextCompat.getColor(FoodPreferenceActivity.this,
                        R.color.text_color_tertiary));
            }

            if (isDirty) {
                mTxtSave.setClickable(true);
                mTxtSave.setTextColor(ContextCompat.getColor(FoodPreferenceActivity.this,
                        R.color.colorPrimary));
            } else {
                mTxtSave.setClickable(false);
                mTxtSave.setTextColor(ContextCompat.getColor(FoodPreferenceActivity.this,
                        R.color.text_color_tertiary));
            }
        });
    }

    @OnClick(R.id.btn_food_pref_save)
    public void saveFoodPref() {
        saveFoodPrefInDbAndFinish();
    }


    @OnClick(R.id.txt_reset)
    public void resetFoodPref() {

        AppContract.DialogListenerAction positiveClick = () -> {
            for (int index = 0; index < rootFoodPrefList.size(); index++) {
                rootFoodPrefList.get(index).set_food_favourite(false);
                rootFoodPrefList.get(index).set_food_like(false);
                userFoodPreferencesList.clear();
                isDirty = true;
                mFoodPrefAdapter.notifyDataSetChanged();
            }
        };

        AppContract.DialogListenerAction negativeClick = () -> {
        };

        showResetDialog(setListener(negativeClick), setListener(positiveClick));
    }

    @Override
    public void success(Object value) {
        if (value != null) {
            dismissProgressDialog();
            RootFoodPref rootFoodPref = (RootFoodPref) value;

            rootFoodPrefList = rootFoodPref.getFoodTypeList();
            Collections.sort(rootFoodPrefList);
            setUpRecyclerView();
        }
    }

    private void setUpRecyclerView() {
        getFoodPrefDataDb();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(FoodPreferenceActivity.this,
                2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(layoutManager);

        mFoodPrefAdapter = new FoodPrefAdapter(this, rootFoodPrefList,
                new OnDoubleTapListenr() {
                    @Override
                    public void onSingleTap(int position, boolean isLike) {
                        isDirty = true;
                        rootFoodPrefList.get(position).set_food_like(isLike);
                        mFoodPrefAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDoubleTap(int position, boolean isSuperLike) {
                        isDirty = true;
                        rootFoodPrefList.get(position).set_food_favourite(isSuperLike);
                        mFoodPrefAdapter.notifyDataSetChanged();
                    }
                });

        mRecyclerView.setAdapter(mFoodPrefAdapter);
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
        });
    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (isDirty) {
            showSavePrefDialog();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isDirty) {
                showSavePrefDialog();
            } else {
                finish();
            }
        }
        return true;
    }


    private void getFoodPrefDataDb() {
        userFoodPreferencesList = mRootUserPreference.getUserFoodPreferences();
        rootFoodPrefList = helper.getFoodPrefData(rootFoodPrefList, userFoodPreferencesList);
    }

    public void showSavePrefDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.preference_save_message));
        builder.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            saveFoodPrefInDbAndFinish();
        });

        builder.setNegativeButton(getString(R.string.discard), (dialog, which) -> {
            dialog.dismiss();
            isDirty = false;
            finish();
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void saveFoodPrefInDbAndFinish() {
        NavPrefFragment.isFoodDirty = true;
        isDirty = false;
        userFoodPreferencesList = helper.getSelectedUserFoodPreferencesList(rootFoodPrefList);
        mRootUserPreference.setUserFoodPreferences(userFoodPreferencesList);

        if (userId != null && !userId.isEmpty()) {
            presenter.saveFoodPrefList(rootFoodPrefList);
        }
        finish();
    }
}
