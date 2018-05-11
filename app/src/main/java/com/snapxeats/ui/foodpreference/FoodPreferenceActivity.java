package com.snapxeats.ui.foodpreference;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.snapxeats.common.utilities.NetworkUtility;
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

    @BindView(R.id.parent_layout)
    protected ConstraintLayout mParentLayout;

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
    private List<FoodPref> mRootFoodPrefList;
    private List<FoodPref> selectedFoodPrefList;
    private List<UserFoodPreferences> mUserFoodPrefList;
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
        mUserFoodPrefList = new ArrayList<>();
        mRootFoodPrefList = new ArrayList<>();
        preferences = utility.getSharedPreferences();
        userId = preferences.getString(getString(R.string.user_id), "");

        showProgressDialog();
        presenter.getFoodPrefList();

        ViewTreeObserver viewTreeObserver = mRecyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {

            selectedFoodPrefList = helper.getSelectedFoodList(mRootFoodPrefList);

            if (selectedFoodPrefList.size() > 0) {
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
            for (int index = 0; index < mRootFoodPrefList.size(); index++) {
                mRootFoodPrefList.get(index).set_food_favourite(false);
                mRootFoodPrefList.get(index).set_food_like(false);
                selectedFoodPrefList.clear();
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
        if (null != value) {
            dismissProgressDialog();
            RootFoodPref rootFoodPref = (RootFoodPref) value;

            mRootFoodPrefList = rootFoodPref.getFoodTypeList();
            Collections.sort(mRootFoodPrefList);
            setUpRecyclerView();
        }
    }

    private void setUpRecyclerView() {
        getFoodPrefDataDb();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,
                2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (null != mRootFoodPrefList) {
            mFoodPrefAdapter = new FoodPrefAdapter(this, mRootFoodPrefList,
                    new OnDoubleTapListenr() {
                        @Override
                        public void onSingleTap(int position, boolean isLike) {
                            isDirty = true;
                            mRootFoodPrefList.get(position).set_food_like(isLike);
                            mRootFoodPrefList.get(position).setSelected(true);
                            mFoodPrefAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onDoubleTap(int position, boolean isSuperLike) {
                            isDirty = true;
                            mRootFoodPrefList.get(position).set_food_favourite(isSuperLike);
                            mRootFoodPrefList.get(position).setSelected(true);
                            mFoodPrefAdapter.notifyItemChanged(position);
                        }
                    });

            mRecyclerView.setAdapter(mFoodPrefAdapter);
            mFoodPrefAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(this) && null != mRootFoodPrefList) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    presenter.getFoodPrefList();
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                presenter.getFoodPrefList();
            }
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
        mUserFoodPrefList = mRootUserPreference.getUserFoodPreferences();
        mRootFoodPrefList = helper.getFoodPrefData(mRootFoodPrefList, mUserFoodPrefList);
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
        mUserFoodPrefList = helper.getSelectedUserFoodPreferencesList(mRootFoodPrefList);
        mRootUserPreference.setUserFoodPreferences(mUserFoodPrefList);

        if (null != userId && !userId.isEmpty()) {
            presenter.saveFoodPrefList(mRootFoodPrefList);
        }
        finish();
    }
}
