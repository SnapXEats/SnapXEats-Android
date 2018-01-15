package com.snapxeats.common.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;

import com.snapxeats.R;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 11/1/18.
 */

public class SnapXDialog {
    private android.app.ProgressDialog mDialog;
    private AlertDialog.Builder mNetworkErrorDialog;

    @Inject
    public SnapXDialog() {

    }

    public void createProgressDialog(Context context){
        mDialog = new android.app.ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.please_wait));
        mDialog.setProgressStyle(R.style.CustomProgressDialog);
        mDialog.show();
    }

   public void dismissProgressSialog() {
        mDialog.dismiss();
    }

    public void createNetworkErrorDialog(Context context) {
        AlertDialog.Builder mNetworkErrorDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialog));
        mNetworkErrorDialog.setMessage(context.getString(R.string.network_error));

        mNetworkErrorDialog.setPositiveButton(context.getString(R.string.ok),
                (dialogInterface, i) -> ((Activity)context).finish());

        mNetworkErrorDialog.show();
    }
}
