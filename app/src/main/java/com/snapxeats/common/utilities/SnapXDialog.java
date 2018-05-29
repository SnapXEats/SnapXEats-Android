package com.snapxeats.common.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.snapxeats.R;
import javax.inject.Inject;
import javax.inject.Singleton;
import static com.snapxeats.common.constants.UIConstants.CHANGE_PERMISSIONS;
import static com.snapxeats.common.constants.UIConstants.DEVICE_LOCATION;

/**
 * Created by Snehal Tembare on 11/1/18.
 */

@Singleton
public class SnapXDialog {
    private ProgressDialog mDialog;
    private AlertDialog.Builder mNetworkErrorDialog;
    private Activity context;
    protected AlertDialog.Builder mDenyDialog;
    private Snackbar mSnackbar;
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
    public void showResetDialog(DialogInterface.OnClickListener negativeClick,
                                DialogInterface.OnClickListener positiveClick) {
        mDenyDialog = new AlertDialog.Builder(context);
        mDenyDialog.setMessage(context.getString(R.string.preference_reset_message));

        mDenyDialog.setNegativeButton(context.getString(R.string.cancel), negativeClick);
        mDenyDialog.setPositiveButton(context.getString(R.string.ok), positiveClick);
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

    /**
     * Show dialog when user denies permissions permanently
     */
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
                            context.startActivityForResult(intent, CHANGE_PERMISSIONS);
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Show Snackbar for network error
     */
    public Snackbar showSnackBar(View view, View.OnClickListener positiveClick) {
        mSnackbar = Snackbar.make(view, context.getString(R.string.check_network), Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction(context.getString(R.string.retry), positiveClick);
        mSnackbar.show();
        return mSnackbar;
    }

}
