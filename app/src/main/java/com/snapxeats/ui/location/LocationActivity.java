package com.snapxeats.ui.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.Location;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationActivity extends BaseActivity implements LocationContract.LocationView,
        AppContract.SnapXResults {

    private static final String TAG = "LocationActivity";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LocationAdapter mAdapter;
    private List<String> predictionList;
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;

    @Inject
    LocationContract.LocationPresenter locationPresenter;

    @BindView(R.id.layout_current_location)
    protected LinearLayout mCurrentLocationLayout;

    @BindView(R.id.search_location)
    protected AutoCompleteTextView mAutoCompleteTextView;

    @BindView(R.id.img_delete_input)
    protected ImageView mImgeClearText;

    @Inject
    public AppUtility utility;

    @BindView(R.id.listview)
    protected ListView mListView;

    @Inject
    SnapXDialog snapXDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public void initView() {
        locationPresenter.addView(this);
        utility.setmContext(this);
        snapXDialog.setContext(this);
        predictionList = new ArrayList<>();

        mAdapter = new LocationAdapter(LocationActivity.this,
                R.layout.item_prediction_layout);
        mListView.setAdapter(mAdapter);
        mListView.setTextFilterEnabled(true);

        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("beforeTextChanged", "LocationActivity");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", "LocationActivity");

                setClearIcon(s);

                if (s.length() > 2) {
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChanged", "LocationActivity");
                if (s.length() == 0)
                    if (mAdapter.resultList != null && mAdapter != null) {
                        mAdapter.resultList.clear();
                        mAdapter.notifyDataSetChanged();
                        mListView.setVisibility(View.GONE);
                    }
            }
        });

        mListView.setOnItemClickListener((parent, view, position, id) -> {
            String address = (String) parent.getItemAtPosition(position);
            Log.i("Address" + address, "Id" + parent.getItemAtPosition(position));
           /*TODO-Place details

           List<Prediction> predictionList = placeAPI.getPredictionList();
            if (predictionList != null) {
                locationPresenter.getPlaceDetails(predictionList.get(position).getPlace_id());
            }*/
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void checkGpsPermission() {
        if (!NetworkHelper.isGpsEnabled(this)) {
            snapXDialog.showGpsPermissionDialog();
        }
    }

    @OnClick(R.id.img_delete_input)
    public void clearText() {
        mAutoCompleteTextView.setText("");
        if (mAdapter != null) {
            mAdapter.resultList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromInputMethod(getCurrentFocus().getApplicationWindowToken(), 0);
        }
    }

    /**
     * Set icon to clear text
     */
    private void setClearIcon(CharSequence sequence) {
        if (sequence.length() > 0) {
            mImgeClearText.setVisibility(View.VISIBLE);
        } else {
            mImgeClearText.setVisibility(View.GONE);
        }
    }

    /**
     * Detect current location
     */
    @OnClick(R.id.layout_current_location)
    public void detectLocation() {
        checkPermissions();
    }

    private void checkPermissions() {
        //Check device level location permission
        if (NetworkHelper.isGpsEnabled(this)) {
            if (NetworkHelper.checkPermission(this)) {
                NetworkHelper.requestPermission(this);
            } else if (NetworkUtility.isNetworkAvailable(this)) {
                android.location.Location location = utility.getLocation();
                if (location != null) {
                    SnapXToast.showToast(this,
                            "LocationActivity:Detected location" + utility.getPlaceName(location));
                }
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                });
            }
        } else {
            checkGpsPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_FINE_LOCATION:
                handleLocationRequest(permissions, grantResults);
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleLocationRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SnapXToast.debug("Permissions granted");
            checkPermissions();
            //To add data in preferences
        } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
            SnapXToast.debug("Permissions denied check never ask again");
            snapXDialog.showChangePermissionDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermissions();
    }

    @OnClick(R.id.img_close)
    public void close() {
        finish();
    }

    @Override
    public void success() {

    }

    @Override
    public void error() {

    }

    @Override
    public void noNetwork() {

    }

    @Override
    public void networkError() {

    }

    @Override
    public void getPredictionList(List<String> predictionList) {
        this.predictionList = predictionList;
    }

    @Override
    public void setLatLng(double lat, double lng) {

    }


}
