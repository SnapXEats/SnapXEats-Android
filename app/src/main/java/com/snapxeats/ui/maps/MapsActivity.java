package com.snapxeats.ui.maps;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.preference.SnapXPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setMapView();
        mPresenter.getUserPreferences();
        mRootCuisine = getIntent().getExtras().getParcelable(getString(R.string.intent_root_cuisine));
        setScrollview();
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
        mInfiniteAdapter = InfiniteScrollAdapter.wrap(new MapsRestAdapter(this, mRootCuisine));
        mScrollView.addOnItemChangedListener(this);
        if (0 != mRootCuisine.getDishesInfo().size()) {
            mScrollView.setAdapter(new MapsRestAdapter(this, mRootCuisine));
        }
        mScrollView.setItemTransformer(new ScaleTransformer.Builder().setMinScale(UIConstants.SCROLL_MIN_SCALE).build());
    }

    private void drawMapCircle() {
        if (null != mPreferences.getUserPreferences() && null != mPreferences.getUserPreferences().getRestaurant_distance()) {
            getSupportActionBar().setTitle(getString(R.string.within) + " " +
                    mPreferences.getUserPreferences().getRestaurant_distance() + " " + getString(R.string.miles));
        }
        //TODO latlng are hardcoded for now
        LatLng currentLatLon = new LatLng(Double.parseDouble(UIConstants.LATITUDE),
                Double.parseDouble(UIConstants.LONGITUDE));

        mMap.addCircle(new CircleOptions()
                .center(currentLatLon)
                .radius(Integer.parseInt(mPreferences.getUserPreferences().getRestaurant_distance()) * UIConstants.DIST_IN_MILES)
                .strokeWidth(UIConstants.MAP_STROKE)
                .strokeColor(UIConstants.MAP_FILL_COLOR)
                .fillColor(UIConstants.MAP_FILL_COLOR));

        CameraUpdateAnimator animator = new CameraUpdateAnimator(mMap, this);
        animator.add(CameraUpdateFactory.newLatLngZoom(currentLatLon, UIConstants.MAP_ZOOM),
                false, (long) UIConstants.DEFAULT);
        animator.add(CameraUpdateFactory.scrollBy(UIConstants.DEFAULT, UIConstants.MAP_SCOLL_BY),
                false, (long) UIConstants.DEFAULT);
        animator.execute();
    }

    private void placeRestMarkers() {
        if (0 != mRootCuisine.getDishesInfo().size()) {
            for (int row = 0; row < mRootCuisine.getDishesInfo().size(); row++) {
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
        drawMapCircle();

        LatLng latLng = new LatLng(Double.parseDouble(UIConstants.LATITUDE), Double.parseDouble(UIConstants.LONGITUDE));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_current)));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void success(Object value) {
        mPreferences = (SnapXPreference) value;
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
