package com.snapxeats.ui.location;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.location.Location;
import com.snapxeats.common.model.location.Prediction;
import com.snapxeats.common.model.location.Result;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.LocationHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.constants.UIConstants.ACCESS_FINE_LOCATION;
import static com.snapxeats.common.constants.UIConstants.ACTION_LOCATION_GET;
import static com.snapxeats.common.constants.UIConstants.CHANGE_LOCATION_PERMISSIONS;
import static com.snapxeats.common.constants.UIConstants.DEVICE_LOCATION;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationActivity extends BaseActivity implements
        LocationContract.LocationView,
        AppContract.SnapXResults,
        OnTaskCompleted {

    private LocationAdapter mAdapter;

    private Location selectedLocation;

    @Inject
    LocationContract.LocationPresenter locationPresenter;

    @BindView(R.id.layout_current_location)
    protected LinearLayout mCurrentLocationLayout;

    @BindView(R.id.location_layout_parent)
    protected LinearLayout mParentLayout;

    @BindView(R.id.search_location)
    protected AutoCompleteTextView mAutoCompleteTextView;

    @BindView(R.id.img_delete_input)
    protected ImageView mImgeClearText;

    @Inject
    public AppUtility utility;
    LocationHelper locationHelper;

    @BindView(R.id.listview)
    protected ListView mListView;

    @BindView(R.id.img_loader)
    protected ImageView mImgLoader;

    @Inject
    SnapXDialog snapXDialog;

    private List<String> resultList;
    private List<Prediction> predictionList;
    private String placeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        utility.setContext(this);
        snapXDialog.setContext(this);
        resultList = new ArrayList<>();
        predictionList = new ArrayList<>();
        locationHelper = new LocationHelper(this);
        mAutoCompleteTextView.setSingleLine();
        mListView.setTextFilterEnabled(true);

        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setClearIcon(s);

                if (s.length() > 2) {
                    if (NetworkUtility.isNetworkAvailable(LocationActivity.this)) {
                        mListView.setVisibility(View.VISIBLE);
                        mAutoCompleteTextView.setTextColor(ContextCompat.getColor(LocationActivity.this
                                , R.color.text_color_primary));

                        mImgLoader.setVisibility(View.VISIBLE);
                        new GetPredictionTask(LocationActivity.this).execute(s.toString());
                    } else {
                        showNetworkErrorDialog((dialog, which) -> {

                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == ZERO)
                    if (null != resultList) {
                        resetViews();
                    }
            }
        });

        mListView.setOnItemClickListener((parent, view, position, id) -> {
            String address = (String) parent.getItemAtPosition(position);

            utility.hideKeyboard();
            if (null != predictionList && predictionList.size() != ZERO) {
                placeId = predictionList.get(position).getPlace_id();
                locationPresenter.getPlaceDetails(placeId);
            }
        });
    }

    private void resetViews() {
        resultList.clear();
        predictionList.clear();
        mListView.setVisibility(View.GONE);
        mImgLoader.setVisibility(View.INVISIBLE);
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.img_delete_input)
    public void clearText() {
        mAutoCompleteTextView.setText("");
        resetViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        utility.hideKeyboard();
    }

    /**
     * Set icon to clear text
     */
    private void setClearIcon(CharSequence sequence) {
        if (sequence.length() > ZERO) {
            mImgeClearText.setVisibility(View.VISIBLE);
        } else {
            mImgeClearText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Detect current location
     */
    @OnClick(R.id.layout_current_location)
    public void detectLocation() {
        if (utility.checkPermissions()) {
            getData();
        }
    }

    public void getData() {
        if (NetworkUtility.isNetworkAvailable(this)) {
            showProgressDialog();
            android.location.Location location = utility.getLocation();
            if (null != location) {
                dismissProgressDialog();
                selectedLocation = new Location(location.getLatitude(),
                        location.getLongitude(), utility.getPlaceName(location));
                putData(selectedLocation);
            }
        } else {
            showNetworkErrorDialog((dialog, which) -> {
                if (!NetworkUtility.isNetworkAvailable(this)) {
                    AppContract.DialogListenerAction click = () ->
                            getData();
                    showSnackBar(mParentLayout, setClickListener(click));
                }else {
                    getData();
                }
            });
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
        if (grantResults[ZERO] == PackageManager.PERMISSION_GRANTED) {
            getData();
        } else if (!shouldShowRequestPermissionRationale(permissions[ZERO])) {
            snapXDialog.showChangePermissionDialog(CHANGE_LOCATION_PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DEVICE_LOCATION:
                if (utility.checkPermissions()) {
                    getData();
                }
                break;

            case CHANGE_LOCATION_PERMISSIONS:
                if (utility.checkPermissions()) {
                    getData();
                }
                break;
        }
    }

    @OnClick(R.id.img_close)
    public void close() {
        finish();
    }

    @Override
    public void success(Object value) {
        Result location = (Result) value;
        String placeName;

        if (null != location.getName()) {
            placeName = location.getName();
        } else {
            placeName = location.getVicinity();
        }

        selectedLocation = new Location(location.getGeometry().getLocation().getLat(),
                location.getGeometry().getLocation().getLng(),
                placeName);

        //Send selected location to home fragment
        putData(selectedLocation);
    }

    public void putData(Location location) {

        if (null != location) {
            Intent intent = new Intent();
            intent.setAction(ACTION_LOCATION_GET);
            intent.putExtra(getString(R.string.selected_location), location);
            this.sendBroadcast(intent);
            this.finish();
        }
    }

    @Override
    public void error(Object value) {
        SnapXToast.showToast(this, getString(R.string.something_went_wrong));
    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    locationPresenter.getPlaceDetails(placeId);
                };
                utility.hideKeyboard();
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                utility.hideKeyboard();
                showProgressDialog();
                locationPresenter.getPlaceDetails(placeId);
            }
        });
    }

    @Override
    public void networkError(Object value) {

    }

    @Override
    public void onTaskCompleted(List<Prediction> predictionList) {
        resultList.clear();
        this.predictionList = predictionList;
        if (null != predictionList
                && predictionList.size() != ZERO) {
            mImgLoader.setVisibility(View.INVISIBLE);
            for (int index = ZERO; index < predictionList.size(); index++) {
                resultList.add(predictionList.get(index).getDescription());
            }
        } else {
            resetViews();
        }
        mAdapter = new LocationAdapter(LocationActivity.this,
                R.layout.item_prediction_layout, resultList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
