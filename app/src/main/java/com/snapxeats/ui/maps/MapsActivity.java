package com.snapxeats.ui.maps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity
        implements MapsContract.MapsView, AppContract.SnapXResults {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    MapsContract.MapsPresenter mPresenter;

    @BindView(R.id.toolbar_maps)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        snapXDialog.setContext(this);
        ButterKnife.bind(this);
        utility.setContext(this);
        //set mToolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void success(Object value) {

    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {

    }

    @Override
    public void networkError(Object value) {

    }
}
