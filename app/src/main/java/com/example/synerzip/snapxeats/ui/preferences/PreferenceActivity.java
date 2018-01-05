package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.synerzip.snapxeats.BaseActivity;
import com.example.synerzip.snapxeats.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceActivity extends BaseActivity implements PreferenceContract.PreferenceView {
    public interface PreferenceConstant {
        public static final int ACCESS_FINE_LOCATION = 1;
    }

    @BindView(R.id.txt_place_name)
    protected TextView mTxtPlaceName;

    protected ProgressDialog mDialog;

    @Inject
    PreferenceContract.PreferencePresenter presenter;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updatePlaceName(String place) {
        mTxtPlaceName.setText(place);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.takeView(this);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
        presenter.showPermissionDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PreferenceConstant.ACCESS_FINE_LOCATION) {
            presenter.getUserLocation();
        }

    }
}
