package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

import com.example.synerzip.snapxeats.BaseActivity;
import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.constants.SnapXToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceActivity extends BaseActivity implements PreferenceContract.PreferenceView {
    public interface PreferenceConstant {
        int ACCESS_FINE_LOCATION = 1;
    }

    @BindView(R.id.txt_place_name)
    protected TextView mTxtPlaceName;

    protected ProgressDialog mDialog;
    protected AlertDialog.Builder mDenyDialog;
    protected AlertDialog.Builder mNetworkErrorDialog;

    @Inject
    PreferenceContract.PreferencePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.takeView(this);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * Update user location
     *
     * @param placeName
     */
    @Override
    public void updatePlaceName(String placeName) {
        mTxtPlaceName.setText(placeName);
    }

    @Override
    public void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage(getString(R.string.please_wait));
            mDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showDenyDialog() {
        mDenyDialog=new AlertDialog.Builder(this);
        mDenyDialog.setTitle(getString(R.string.location_permission_denied))
        .setMessage(getString(R.string.permission_denied_msg));

        mDenyDialog.setNegativeButton(getString(R.string.im_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.presentLocationScreen();
            }
        });

        mDenyDialog.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.showPermissionDialog();
            }
        });
        mDenyDialog.show();
    }

    @Override
    public void showNetworkErrorDialog() {
        mNetworkErrorDialog=new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.CustomAlertDialog));
        mNetworkErrorDialog .setMessage(getString(R.string.network_error));

        mNetworkErrorDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               finish();
            }
        });
        mNetworkErrorDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
            presenter.getUserLocation();
    }

    @OnClick(R.id.txt_place_name)
    public void presentLocationScreen() {
        presenter.presentLocationScreen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PreferenceConstant.ACCESS_FINE_LOCATION) {
            for (int index = 0, len = permissions.length; index < len; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    SnapXToast.info("Permissions denied");

                   /** TODO- Functionality yet to complete

                   presenter.showDenyDialog();*/
                } else {
                    SnapXToast.info("Permissions granted");
                }
            }
        }
    }
}
