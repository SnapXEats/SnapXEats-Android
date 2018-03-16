package com.snapxeats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSnapXDialog.setContext(this);
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
        mSnapXDialog.createProgressDialog();
    }

    public void dismissProgressDialog() {
        mSnapXDialog.dismissProgressSialog();
    }

    public void showResetDialog(DialogInterface.OnClickListener negativeClick ,
                                DialogInterface.OnClickListener positiveClick) {
        mSnapXDialog.showResetDialog(negativeClick, positiveClick);
    }

    public void showNetworkErrorDialog(DialogInterface.OnClickListener click) {
        mSnapXDialog.showNetworkErrorDialog(click);
    }
}
