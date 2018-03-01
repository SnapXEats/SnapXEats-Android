package com.snapxeats.ui.cuisinepreference;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.NetworkCheckReceiver;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.UserCuisinePreferences;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.SCREENNAMES.CUISINE;


/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefActivity extends BaseActivity implements CuisinePrefContract.CuisinePrefView,
        AppContract.SnapXResults {

    @BindView(R.id.recyclerview_cuisine)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Inject
    CuisinePrefContract.CuisinePrefPresenter prefPresenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;
    private CuisinePrefAdapter mCuisinePrefAdapter;
    private NetworkCheckReceiver networkCheckReceiver;
    private List<Cuisines> rootCuisineList;
    private List<UserCuisinePreferences> cuisinedPrefList;
    public static boolean isCuisineSelecetionChange;

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.cuisine_preference));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        networkCheckReceiver = new NetworkCheckReceiver();
        cuisinedPrefList = new ArrayList<>();

        showProgressDialog();
        prefPresenter.getCuisinePrefList();
    }

    private void getCuisinePrefData() {
        cuisinedPrefList = prefPresenter.getCuisineListFromDb();

        rootCuisineList = new CuisinePrefDbHelper().getCuisinePrefData(cuisinedPrefList, rootCuisineList);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.txt_reset)
    public void resetFoodPref() {
        for (int index = 0; index < rootCuisineList.size(); index++) {
            rootCuisineList.get(index).set_cuisine_favourite(false);
            rootCuisineList.get(index).set_cuisine_like(false);
            prefPresenter.saveCuisineList(rootCuisineList);
            prefPresenter.resetCuisineList();
            isCuisineSelecetionChange = false;
            mCuisinePrefAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void success(Object value) {
        if (value != null) {
            if (value instanceof RootCuisine) {
                dismissProgressDialog();
                RootCuisine rootCuisine = (RootCuisine) value;
                rootCuisineList = rootCuisine.getCuisineList();
                setUpRecyclerView();
            }
        }
    }

    private void setUpRecyclerView() {
        getCuisinePrefData();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(CuisinePrefActivity.this, 2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(layoutManager);
        if (null != rootCuisineList) {
            mCuisinePrefAdapter = new CuisinePrefAdapter(this,
                    rootCuisineList, new OnDoubleTapListenr() {
                Cuisines cuisines;

                @Override
                public void onSingleTap(int position, boolean isLike) {
                    cuisines = rootCuisineList.get(position);
                    cuisines.set_cuisine_like(isLike);
                    isCuisineSelecetionChange = true;
                    mCuisinePrefAdapter.notifyDataSetChanged();
                }

                @Override
                public void onDoubleTap(int position, boolean isSuperLike) {
                    cuisines = rootCuisineList.get(position);
                    cuisines.set_cuisine_favourite(isSuperLike);
                    isCuisineSelecetionChange = true;
                    mCuisinePrefAdapter.notifyDataSetChanged();
                }
            });
            mRecyclerView.setAdapter(mCuisinePrefAdapter);
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
        Intent intent = new Intent();
        intent.putExtra("screen", CUISINE);
//        intent.setAction("android.net.conn.CONNECTIVITY_CHANGE");
//        intent.setAction("android.net.wifi.WIFI_STATE_CHANGED");
        sendBroadcast(intent);

        showNetworkErrorDialog((dialog, which) -> {
        });


    }

    @Override
    public void networkError(Object value) {
        dismissProgressDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isCuisineSelecetionChange) {
                showSavePrefDialog();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isCuisineSelecetionChange) {
            showSavePrefDialog();
        } else {
            finish();
        }
    }

    public void showSavePrefDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.want_to_save_pref));
        builder.setPositiveButton(getString(R.string.save), (dialog, which) -> {

            saveCuisinePrefInDbAndFinish();
        });

        builder.setNegativeButton(getString(R.string.discard), (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void saveCuisinePrefInDbAndFinish() {
        prefPresenter.saveCuisineList(rootCuisineList);
        isCuisineSelecetionChange = false;
        finish();
    }
}
