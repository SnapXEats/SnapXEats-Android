package com.snapxeats.ui.maps;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.SnapXPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.GOOGLE_MAP_ZOOM;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.STRING_SPACE;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
public class MapsActivity extends BaseActivity
        implements MapsContract.MapsView, AppContract.SnapXResults, DiscreteScrollView.OnItemChangedListener
        , OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    MapsContract.MapsPresenter mPresenter;

    @BindView(R.id.maps_scrollview)
    protected DiscreteScrollView mScrollView;

    @BindView(R.id.toolbar_maps)
    protected Toolbar mToolbar;

    private GoogleMap mMap;
    private SnapXPreference mPreferences;
    private RootCuisinePhotos mRootCuisine;
    private InfiniteScrollAdapter mInfiniteAdapter;
    private MarkerOptions mMarkerOptions;
    private Marker mMarker;
    private LatLng latLng = null;

    @BindView(R.id.txt_toolbar_dist)
    protected TextView mToolbarTitle;

    @Inject
    RootUserPreference rootUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        mPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        mMarkerOptions = new MarkerOptions();
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setMapView();
        if (utility.isLoggedIn()) {
            mPresenter.getUserPreferences();
        }
        mRootCuisine = Objects.requireNonNull(getIntent().getExtras()).getParcelable(getString(R.string.intent_root_cuisine));
        setScrollview();
    }

    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    /**
     * Set up map view
     **/
    private void setMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_view);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void setScrollview() {
        latLng = utility.setLatLng();
        mInfiniteAdapter = InfiniteScrollAdapter.wrap(new MapsRestAdapter(this, mRootCuisine, latLng));
        mScrollView.addOnItemChangedListener(this);
        if (ZERO != mRootCuisine.getDishesInfo().size()) {
            mScrollView.setAdapter(new MapsRestAdapter(this, mRootCuisine, latLng));
        }
        mScrollView.setItemTransformer(new ScaleTransformer.Builder().setMinScale(UIConstants.SCROLL_MIN_SCALE).build());
    }

    private void drawMapCircle() {
        float radius;
        LatLng currentLatLon = utility.setLatLng();
        if (null != mPreferences && null != mPreferences.getUserPreferences()
                && !mPreferences.getUserPreferences().getRestaurant_distance().equalsIgnoreCase("")) {
            mToolbarTitle.setText(getString(R.string.within) + STRING_SPACE +
                    mPreferences.getUserPreferences().getRestaurant_distance() + STRING_SPACE + getString(R.string.miles));
            radius = Integer.parseInt(mPreferences.getUserPreferences().getRestaurant_distance()) * UIConstants.DIST_IN_MILES;
        } else {
            mToolbarTitle.setText(getString(R.string.within) + STRING_SPACE + rootUserPreference.getRestaurant_distance() + STRING_SPACE + getString(R.string.miles));
            radius = Integer.parseInt(rootUserPreference.getRestaurant_distance()) * UIConstants.DIST_IN_MILES;
        }
        mMap.addCircle(new CircleOptions()
                .center(currentLatLon)
                .radius(radius)
                .strokeWidth(UIConstants.MAP_STROKE)
                .strokeColor(UIConstants.MAP_FILL_COLOR)
                .fillColor(UIConstants.MAP_FILL_COLOR));

        CameraUpdateAnimator animator = new CameraUpdateAnimator(mMap, this);
        if (ONE < Integer.parseInt(rootUserPreference.getRestaurant_distance())) {
            animator.add(CameraUpdateFactory.newLatLngZoom(currentLatLon, GOOGLE_MAP_ZOOM), false, ZERO);
        } else {
            animator.add(CameraUpdateFactory.newLatLngZoom(currentLatLon, UIConstants.MAP_ZOOM), false, ZERO);
        }
        animator.add(CameraUpdateFactory.scrollBy(ZERO, UIConstants.MAP_SCOLL_BY), false, ZERO);
        animator.execute();
        mMap.setOnMarkerClickListener(marker -> true);

    }

    private void placeRestMarkers() {
        if (ZERO != mRootCuisine.getDishesInfo().size()) {
            for (int row = ZERO; row < mRootCuisine.getDishesInfo().size(); row++) {
                Double lat = Double.parseDouble(mRootCuisine.getDishesInfo().get(row).getLocation_lat());
                Double lng = Double.parseDouble(mRootCuisine.getDishesInfo().get(row).getLocation_long());
                LatLng latLng = new LatLng(lat, lng);
                mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_grey));
                mMarkerOptions.position(latLng);
                mMap.addMarker(mMarkerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(UIConstants.MAP_MARKER_ZOOM));
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        placeRestMarkers();
        latLng = utility.setLatLng();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_current)));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        drawMapCircle();
    }

    @Override
    public void success(Object value) {
        if (value instanceof SnapXPreference) {
            mPreferences = (SnapXPreference) value;
        }
        drawMapCircle();
    }

    @Override
    public void error(Object value) {
    }

    @Override
    public void noNetwork(Object value) {
    }

    @Override
    public void networkError(Object value) {
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = mInfiniteAdapter.getRealPosition(adapterPosition);
        Double lat = Double.parseDouble(mRootCuisine.getDishesInfo().get(positionInDataSet).getLocation_lat());
        Double lng = Double.parseDouble(mRootCuisine.getDishesInfo().get(positionInDataSet).getLocation_long());
        LatLng latLng = new LatLng(lat, lng);
        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_selected));
        mMarkerOptions.position(latLng);
        if (null != mMarker) {
            mMarker.remove();
        }
        mMarker = mMap.addMarker(mMarkerOptions);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCameraIdle() {

    }
}
