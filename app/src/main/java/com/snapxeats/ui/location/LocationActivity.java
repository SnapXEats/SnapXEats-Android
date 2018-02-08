package com.snapxeats.ui.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.snapxeats.LocationBaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.Location;
import com.snapxeats.common.model.Prediction;
import com.snapxeats.common.model.Result;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.network.LocationHelper;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION;
import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.CUSTOM_LOCATION;


/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationActivity extends LocationBaseActivity implements LocationContract.LocationView,
        AppContract.SnapXResults {

    private static final String TAG = "LocationActivity";
    private LocationAdapter mAdapter;

    private Location selectedLocation;

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
    LocationHelper locationHelper;

    @BindView(R.id.listview)
    protected ListView mListView;

    @Inject
    SnapXDialog snapXDialog;

    PlaceAPI placeAPI;

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
        buildGoogleAPIClient();
        locationPresenter.addView(this);
        utility.setContext(this);
        snapXDialog.setContext(this);
        placeAPI = new PlaceAPI();
        locationHelper = new LocationHelper(this);
        mAutoCompleteTextView.setSingleLine();

        mAdapter = new LocationAdapter(LocationActivity.this,
                R.layout.item_prediction_layout);
        mListView.setAdapter(mAdapter);
        mListView.setTextFilterEnabled(true);

        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                SnapXToast.debug("beforeTextChanged LocationActivity");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SnapXToast.debug("onTextChanged LocationActivity");

                setClearIcon(s);

                if (s.length() > 2) {
                    if (NetworkUtility.isNetworkAvailable(LocationActivity.this)){
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter.getFilter().filter(s);}else {
                        showNetworkErrorDialog((dialog, which) -> {

                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                SnapXToast.debug("afterTextChanged LocationActivity");
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
            SnapXToast.debug("Address" + address+ "Id" + parent.getItemAtPosition(position));


            List<Prediction> predictionList = mAdapter.predictionList;

            if (predictionList != null) {
                locationPresenter.getPlaceDetails(predictionList.get(position).getPlace_id());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if (checkPermissions()) {
            getData();
        }
    }

    public void getData() {
        android.location.Location location = getLocation();
        if (location != null) {
            selectedLocation = new Location(location.getLatitude(),
                    location.getLongitude(), getPlaceName(location));
            if (selectedLocation != null) {
                putData(selectedLocation);
            }
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
            if (checkPermissions()) {
                getData();
            }
            //To add data in preferences
        } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
            SnapXToast.debug("Permissions denied check never ask again");
            snapXDialog.showChangePermissionDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (checkPermissions()) {
            getData();
        }
    }

    @OnClick(R.id.img_close)
    public void close() {
        finish();
    }

    @Override
    public void success(Object value) {
        Result location = (Result) value;
        SnapXToast.debug("Success Name" + location.getName());

        selectedLocation = new Location(location.getGeometry().getLocation().getLat(),
                location.getGeometry().getLocation().getLng(),
                location.getName());

        //Send selected location to preferences screen
        putData(selectedLocation);
    }

    public void putData(Location location) {

        if (location != null) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.selected_location), location);
            setResult(CUSTOM_LOCATION, intent);
            finish();
        }
    }

    @Override
    public void error() {

    }

    @Override
    public void noNetwork(Object value) {
        showNetworkErrorDialog((dialog, which) -> {

        });
    }

    @Override
    public void networkError() {

    }
}
