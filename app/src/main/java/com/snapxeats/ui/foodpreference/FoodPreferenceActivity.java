package com.snapxeats.ui.foodpreference;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.FoodPref;
import com.snapxeats.common.model.RootFoodPref;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    @Inject
    FoodPreferenceContract.FoodPreferencePresenter presenter;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    private FoodPrefAdapter mFoodPrefAdapter;
    private List<FoodPref> rootFoodPrefList;
    public static boolean isFoodPrefSelected;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

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
        gson = new Gson();
        preferences = utility.getSharedPreferences();
        editor = preferences.edit();

        String json = preferences.getString("rootFoodPrefList", "");


        Type type = new TypeToken<ArrayList<FoodPref>>() {
        }.getType();
        ArrayList<FoodPref> listFromDb = gson.fromJson(json, type);

        if (listFromDb != null) {
            rootFoodPrefList = listFromDb;
            setUpRecyclerView();
        } else {
            showProgressDialog();
            presenter.getFoodPrefList();
        }
    }

    @OnClick(R.id.btn_food_pref_save)
    public void saveFoodPref() {
        String json = gson.toJson(rootFoodPrefList);
        editor.putString("rootFoodPrefList", json);
        editor.apply();
        finish();
    }


    @OnClick(R.id.txt_reset)
    public void resetFoodPref() {
        for (int index = 0; index < rootFoodPrefList.size(); index++) {
            rootFoodPrefList.get(index).set_food_favourite(false);
            rootFoodPrefList.get(index).set_food_like(false);
            mFoodPrefAdapter.notifyDataSetChanged();
        }
        editor.putString("rootFoodPrefList", "");
        editor.apply();
    }

    @Override
    public void success(Object value) {
        if (value != null) {
            dismissProgressDialog();
            RootFoodPref rootFoodPref = (RootFoodPref) value;

            rootFoodPrefList = rootFoodPref.getFoodTypeList();
            SnapXToast.debug("CuisinePrefActivity: Size" + rootFoodPrefList.size());

            setUpRecyclerView();

        }
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(FoodPreferenceActivity.this,
                2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(layoutManager);

        mFoodPrefAdapter = new FoodPrefAdapter(this, rootFoodPrefList,
                new OnDoubleTapListenr() {
                    @Override
                    public void onSingleTap(int position, boolean isLike) {
                        rootFoodPrefList.get(position).set_food_like(isLike);
                        isFoodPrefSelected = true;
                        mFoodPrefAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDoubleTap(int position, boolean isSuperLike) {
                        rootFoodPrefList.get(position).set_food_favourite(isSuperLike);
                        isFoodPrefSelected = true;
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
        if (isFoodPrefSelected) {
            showSavePrefDialog();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isFoodPrefSelected) {
                showSavePrefDialog();
            } else {
                finish();
            }
        }
        return true;
    }

    public void showSavePrefDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.want_to_save_pref));
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String json = gson.toJson(rootFoodPrefList);
                editor.putString("rootFoodPrefList", json);
                editor.apply();
                finish();

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
