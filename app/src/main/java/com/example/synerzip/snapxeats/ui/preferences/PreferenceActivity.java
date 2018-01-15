package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.example.synerzip.snapxeats.BaseActivity;
import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.constants.SnapXToast;
import com.example.synerzip.snapxeats.common.utilities.SnapXDialog;
import com.example.synerzip.snapxeats.dagger.AppContract;
import com.example.synerzip.snapxeats.network.NetworkHelper;

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

    @Inject
    PreferenceContract.PreferencePresenter presenter;

    AppContract.DialogListenerAction denyAction = () -> presenter.presentScreen();
    AppContract.DialogListenerAction allowAction = () -> NetworkHelper.requestPermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.addView(this);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {

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
    protected void onResume() {
        super.onResume();
        presenter.getLocation(this);
    }

    @OnClick(R.id.txt_place_name)
    public void presentLocationScreen() {
        presenter.presentScreen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PreferenceConstant.ACCESS_FINE_LOCATION) {
            for (int index = 0, len = permissions.length; index < len; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    SnapXToast.info("Permissions denied");
                    /** TODO- Functionality yet to complete

                    showDenyDialog(setListener(denyAction), setListener(allowAction)); */
                } else {
                    SnapXToast.info("Permissions granted");
                }
            }
        }
    }
}
