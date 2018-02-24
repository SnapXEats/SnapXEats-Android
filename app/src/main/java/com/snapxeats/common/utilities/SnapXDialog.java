package com.snapxeats.common.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.snapxeats.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.snapxeats.ui.home.HomeActivity.PreferenceConstant.ACCESS_FINE_LOCATION;
import static com.snapxeats.ui.home.HomeActivity.PreferenceConstant.DEVICE_LOCATION;

/**
 * Created by Snehal Tembare on 11/1/18.
 */

@Singleton
public class SnapXDialog {
    private ProgressDialog mDialog;
    private AlertDialog.Builder mNetworkErrorDialog;
    private Activity context;
    protected AlertDialog.Builder mDenyDialog;

    @Inject
    public SnapXDialog() {
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    /**
     * Show Progress dialog
     */
    public void createProgressDialog() {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.please_wait));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**
     * Dismiss Progress dialog
     */
    public void dismissProgressSialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * Show Network Error dialog
     */
    public void showNetworkErrorDialog(DialogInterface.OnClickListener click) {
        mNetworkErrorDialog = new AlertDialog.Builder(context);
        mNetworkErrorDialog.setMessage(context.getString(R.string.network_error));

        mNetworkErrorDialog.setPositiveButton(context.getString(R.string.ok), click);
        mNetworkErrorDialog.show();
    }

    /**
     * Show dialog when user deny Location permission very first time
     */
    public void showDenyDialog(DialogInterface.OnClickListener positiveClick,
                               DialogInterface.OnClickListener negativeClick) {
        mDenyDialog = new AlertDialog.Builder(context);
        mDenyDialog.setTitle(context.getString(R.string.location_permission_denied))
                .setMessage(context.getString(R.string.permission_denied_msg));

        mDenyDialog.setNegativeButton(context.getString(R.string.yes), negativeClick);

        mDenyDialog.setPositiveButton(context.getString(R.string.retry), positiveClick);
        mDenyDialog.show();
    }

    /**
     * Show Gps permission not available dialog
     */
    public void showGpsPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.location_service_not_active));
        builder.setMessage(context.getString(R.string.enable_gps));
        builder.setPositiveButton(context.getString(R.string.action_settings), (dialogInterface, i) -> {
            // Show location settings when the user acknowledges the alert dialog
            context.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    DEVICE_LOCATION);
        });

        builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();


    }

    public void showChangePermissionDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.change_permissions));
        alertDialogBuilder
                .setMessage(context.getString(R.string.permission_denied_permantantly))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.go_to_settings),
                        (dialog, id) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivityForResult(intent, ACCESS_FINE_LOCATION);
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
