package com.snapxeats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Prajakta Patil on 28/12/17.
 */


public abstract class BaseActivity extends DaggerAppCompatActivity {

    /**
     * The singleton Navigator object of the application
     */
    @Inject
    Router router;

    @Inject
    SnapXDialog mSnapXDialog;

    protected AlertDialog.Builder mDenyDialog;
    protected AlertDialog.Builder mNetworkErrorDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        router.setActivity(this);
    }

    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return (dialogInterface, i) -> button.action();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        router.setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        router.setActivity(this);
    }

    public void showProgressDialog() {
            mSnapXDialog.createProgressDialog(this);
    }

    public void dismissProgressDialog() {
            mSnapXDialog.dismissProgressSialog();
    }

    public void showDenyDialog(DialogInterface.OnClickListener positiveClick, DialogInterface.OnClickListener negativeClick) {
        mDenyDialog = new AlertDialog.Builder(this);
        mDenyDialog.setTitle(getString(R.string.location_permission_denied))
                .setMessage(getString(R.string.permission_denied_msg));
        mDenyDialog.setNegativeButton(getString(R.string.im_sure), negativeClick);
        mDenyDialog.setPositiveButton(getString(R.string.retry), positiveClick);
        mDenyDialog.show();
    }

    public void showNetworkErrorDialog(DialogInterface.OnClickListener click) {
        mNetworkErrorDialog = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.CustomAlertDialog));
        mNetworkErrorDialog.setMessage(getString(R.string.network_error));
        mNetworkErrorDialog.setPositiveButton(getString(R.string.ok), click);
        mNetworkErrorDialog.show();
    }
}
