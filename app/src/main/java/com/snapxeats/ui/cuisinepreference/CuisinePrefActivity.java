package com.snapxeats.ui.cuisinepreference;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.NetworkCheckReceiver;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.FoodPref;
import com.snapxeats.common.model.Location;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.UserCuisinePreferences;
import com.snapxeats.common.model.UserFoodPreferences;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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

    private Realm mRealm;

    private CuisinePrefAdapter mCuisinePrefAdapter;
    private NetworkCheckReceiver networkCheckReceiver;
    private List<Cuisines> rootCuisineList;

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

        mRealm = Realm.getDefaultInstance();
        networkCheckReceiver = new NetworkCheckReceiver();

        showProgressDialog();
        prefPresenter.getCuisinePrefList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        getActivity().registerReceiver(networkCheckReceiver,
                intentFilter);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(networkCheckReceiver);
    }

    @Override
    public void success(Object value) {
        if (value != null) {
            dismissProgressDialog();
            RootCuisine rootCuisine = (RootCuisine) value;
            rootCuisineList = rootCuisine.getCuisineList();
            SnapXToast.debug("CuisinePrefActivity: Size" + rootCuisineList.size());
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
                        mCuisinePrefAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDoubleTap(int position, boolean isSuperLike) {
                        cuisines = rootCuisineList.get(position);
                        cuisines.set_cuisine_favourite(isSuperLike);
                        mCuisinePrefAdapter.notifyDataSetChanged();
                    }
                });

                mRecyclerView.setAdapter(mCuisinePrefAdapter);
            }

        }
    }

    @OnClick(R.id.btn_cuisine_pref_save)
    public void saveCusinePref() {
        Cuisines cuisines = new Cuisines();
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        Intent intent = new Intent();
        intent.putExtra("screen", "CuisinePrefActivity");
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
            if (isCuisinesSelected()) {
                showSavePrefDialog();
            } else {
                finish();
            }
        }
        return true;
    }

    private boolean isCuisinesSelected() {
        for (int index = 0; index < rootCuisineList.size(); index++) {
            if (rootCuisineList.get(index).is_cuisine_like()
                    || rootCuisineList.get(index).is_cuisine_favourite()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (isCuisinesSelected()) {
            showSavePrefDialog();
        } else {
            finish();
        }
    }

    public void showSavePrefDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.want_to_save_pref));
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mRealm.beginTransaction();
                SnapxData snapxData = mRealm.createObject(SnapxData.class);

                RealmList<UserCuisinePreferences> list = new RealmList<>();
//                list.addAll(rootCuisineList);

                UserCuisinePreferences preferences;

                for (Cuisines cuisines : rootCuisineList) {
                    preferences = new UserCuisinePreferences(cuisines.getCuisine_info_id(),
                            cuisines.is_cuisine_like(), cuisines.is_cuisine_favourite());
                    list.add(preferences);
                }

                snapxData.setSelectedCuisinesList(list);
                mRealm.commitTransaction();

               /* for (Cuisines cuisines : mCuisinePrefAdapter.selectedCuisineList) {
                    SnapXToast.debug("****List" + cuisines.toString());
                }*/

                RealmResults<SnapxData> results = mRealm.where(SnapxData.class).findAll();
                results.load();
                List<UserCuisinePreferences> listFromRealm = results.get(0).getSelectedCuisinesList();

                for (Cuisines cuisines : rootCuisineList) {
                    SnapXToast.debug("****From db List" + cuisines.toString());
                }
            }
        });

        builder.setNegativeButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}
