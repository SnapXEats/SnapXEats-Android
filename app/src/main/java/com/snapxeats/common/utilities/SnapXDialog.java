package com.snapxeats.common.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Activity context;
    protected AlertDialog.Builder mDenyDialog;

    @Inject
    public SnapXDialog() {
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void createProgressDialog() {
        mDialog = new android.app.ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.please_wait));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void dismissProgressSialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void showNetworkErrorDialog(DialogInterface.OnClickListener click) {
        mNetworkErrorDialog = new AlertDialog.Builder(context);
        mNetworkErrorDialog.setMessage(context.getString(R.string.network_error));

        mNetworkErrorDialog.setPositiveButton(context.getString(R.string.ok), click);
        mNetworkErrorDialog.show();
    }

    public void showDenyDialog(DialogInterface.OnClickListener positiveClick,
                               DialogInterface.OnClickListener negativeClick) {
        mDenyDialog = new AlertDialog.Builder(context);
        mDenyDialog.setTitle(context.getString(R.string.location_permission_denied))
                .setMessage(context.getString(R.string.permission_denied_msg));

        mDenyDialog.setNegativeButton(context.getString(R.string.im_sure), negativeClick);

        mDenyDialog.setPositiveButton(context.getString(R.string.retry), positiveClick);
        mDenyDialog.show();
    }

    public void showGpsPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.location_service_not_active));
        builder.setMessage(context.getString(R.string.enable_gps));
        builder.setPositiveButton(context.getString(R.string.ok), (dialogInterface, i) -> {
            // Show location settings when the user acknowledges the alert dialog
            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
