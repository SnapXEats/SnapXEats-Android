package com.snapxeats.ui.cuisinepreference;

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
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.model.preference.RootCuisine;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment.isCuisineDirty;


/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefActivity extends BaseActivity implements
        CuisinePrefContract.CuisinePrefView,
        AppContract.SnapXResults {

    @BindView(R.id.recyclerview_cuisine)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.txt_reset)
    protected TextView mTxtReset;

    @BindView(R.id.btn_cuisine_pref_save)
    protected TextView mTxtSave;

    @BindView(R.id.parent_layout)
    protected ConstraintLayout mParentLayout;

    @Inject
    CuisinePrefContract.CuisinePrefPresenter prefPresenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    CuisinePrefDbHelper helper;

    @Inject
    AppUtility utility;

    @Inject
    RootUserPreference mRootUserPreference;

    private CuisinePrefAdapter mCuisinePrefAdapter;
    private List<Cuisines> mRootCuisineList;
    private List<UserCuisinePreferences> mUserCuisinesList;
    private List<Cuisines> selectedCuisineList;
    public static boolean isDirty;
    private String userId;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_pref);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        prefPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        helper.setContext(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.cuisine_preference));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserCuisinesList = new ArrayList<>();
        selectedCuisineList = new ArrayList<>();
        mRootCuisineList = new ArrayList<>();
        preferences = utility.getSharedPreferences();
        userId = preferences.getString(getString(R.string.user_id), "");

        showProgressDialog();
        prefPresenter.getCuisinePrefList();

        ViewTreeObserver viewTreeObserver = mRecyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {

            selectedCuisineList = helper.getSelectedCuisineList(mRootCuisineList);

            if (selectedCuisineList.size() > 0) {
                mTxtReset.setClickable(true);
                mTxtReset.setTextColor(ContextCompat.getColor(CuisinePrefActivity.this,
                        R.color.text_color_primary));
            } else {
                mTxtReset.setClickable(false);
                mTxtReset.setTextColor(ContextCompat.getColor(CuisinePrefActivity.this,
                        R.color.text_color_tertiary));
            }

            if (isDirty) {
                mTxtSave.setClickable(true);
                mTxtSave.setTextColor(ContextCompat.getColor(CuisinePrefActivity.this,
                        R.color.colorPrimary));
            } else {
                mTxtSave.setClickable(false);
                mTxtSave.setTextColor(ContextCompat.getColor(CuisinePrefActivity.this,
                        R.color.text_color_tertiary));
            }
        });
    }

    private void getCuisinePrefDataFromDb() {
        //Load cuisine pref data from local DB
        mUserCuisinesList = mRootUserPreference.getUserCuisinePreferences();
        mRootCuisineList = helper.getCuisinePrefData(mUserCuisinesList, mRootCuisineList);
    }


    @OnClick(R.id.txt_reset)
    public void resetCuisinePref() {

        AppContract.DialogListenerAction positiveClick = () -> {
            for (int index = 0; index < mRootCuisineList.size(); index++) {
                mRootCuisineList.get(index).set_cuisine_favourite(false);
                mRootCuisineList.get(index).set_cuisine_like(false);
                selectedCuisineList.clear();
                isDirty = true;
                isCuisineDirty = true;
                mCuisinePrefAdapter.notifyDataSetChanged();
            }
        };

        AppContract.DialogListenerAction negativeClick = () -> {
        };

        showResetDialog(setListener(negativeClick), setListener(positiveClick));
    }

    @Override
    public void success(Object value) {
        if (value != null) {
            if (value instanceof RootCuisine) {
                dismissProgressDialog();
                RootCuisine rootCuisine = (RootCuisine) value;
                mRootCuisineList = rootCuisine.getCuisineList();
                Collections.sort(mRootCuisineList);
                setUpRecyclerView();
            }
        }
    }

    private void setUpRecyclerView() {
        getCuisinePrefDataFromDb();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (null != mRootCuisineList) {
            mCuisinePrefAdapter = new CuisinePrefAdapter(this,
                    mRootCuisineList, new OnDoubleTapListenr() {

                @Override
                public void onSingleTap(int position, boolean isLike) {
                    isDirty = true;
                    isCuisineDirty = true;
                    mRootCuisineList.get(position).set_cuisine_like(isLike);
                    mCuisinePrefAdapter.notifyItemChanged(position);
                }

                @Override
                public void onDoubleTap(int position, boolean isSuperLike) {
                    isDirty = true;
                    isCuisineDirty = true;
                    mRootCuisineList.get(position).set_cuisine_favourite(isSuperLike);
                    mCuisinePrefAdapter.notifyItemChanged(position);
                }
            });
            mRecyclerView.setAdapter(mCuisinePrefAdapter);
            mCuisinePrefAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.btn_cuisine_pref_save)
    public void saveCusinePref() {
        saveCuisinePrefInDbAndFinish();
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();

        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(this) && null != mRootCuisineList) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    prefPresenter.getCuisinePrefList();
                };
                showSnackBar(mParentLayout, setClickListener(click));
            }
        });

    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
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

    @Override
    public void onBackPressed() {
        if (isDirty) {
            showSavePrefDialog();
        } else {
            finish();
        }
    }

    public void showSavePrefDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.preference_save_message));
        builder.setPositiveButton(getString(R.string.save), (dialog, which) -> {

            saveCuisinePrefInDbAndFinish();
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
            isDirty = false;
            finish();
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void saveCuisinePrefInDbAndFinish() {
        isCuisineDirty = true;
        isDirty = false;
        mUserCuisinesList = helper.getSelectedUserCuisinePreferencesList(mRootCuisineList);
        mRootUserPreference.setUserCuisinePreferences(mUserCuisinesList);
        if (userId != null && !userId.isEmpty()) {
            prefPresenter.saveCuisineList(mRootCuisineList);
        }
        finish();
    }
}
