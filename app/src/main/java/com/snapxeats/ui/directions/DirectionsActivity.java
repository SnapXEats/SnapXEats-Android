package com.snapxeats.ui.directions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.model.restaurantDetails.RootRestaurantDetails;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
public class DirectionsActivity extends BaseActivity
        implements DirectionsContract.DirectionsView, OnMapReadyCallback {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    DirectionsContract.DirectionsPresenter mPresenter;

    @BindView(R.id.txtDirRestName)
    protected TextView mTxtRestName;

    @BindView(R.id.txtDirRestAddr)
    protected TextView mTxtRestAddr;

    @BindView(R.id.txtDirRating)
    protected TextView mTxtRating;

    @BindView(R.id.txt_price_maps)
    protected TextView mTxtPrice;

    @BindView(R.id.txt_distance_maps)
    protected TextView mTxtRestDist;

    private RootRestaurantDetails mDetails;

    private GoogleMap mMap;

    private List<LatLng> list;

    @BindView(R.id.spinner_directions)
    protected Spinner mDirSpinner;

    @BindView(R.id.txt_dir_rest_open)
    protected TextView mTxtDirRestOpen;

    @BindView(R.id.txt_dir_rest_close)
    protected TextView mTxtDirRestClose;

    @BindView(R.id.toolbar_directions)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * Set up map view
     **/
    private void setMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_directions);
        mapFragment.getMapAsync(DirectionsActivity.this);
    }

    /**
     * Initialize components
     **/
    @Override
    public void initView() {
        mPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.directions));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setMapView();
        setUpViews();
    }

    /**
     * Set values for components
     **/
    private void setUpViews() {
        mDetails = getIntent().getExtras().getParcelable(getString(R.string.intent_rest_details));
        if (null != mDetails && null != mDetails.getRestaurantDetails()) {
            mTxtRestName.setText(String.valueOf(mDetails.getRestaurantDetails().getRestaurant_name()));
            mTxtRestAddr.setText(mDetails.getRestaurantDetails().getRestaurant_address());
            mTxtRating.setText(mDetails.getRestaurantDetails().getRestaurant_rating());
            distInMiles(Double.valueOf(UIConstants.LATITUDE),
                    Double.valueOf(UIConstants.LONGITUDE),
                    Double.valueOf(mDetails.getRestaurantDetails().getLocation_lat()),
                    Double.valueOf(mDetails.getRestaurantDetails().getLocation_long()));
            setRestTimings();
            setRestPrice();
        }
    }

    private void setRestPrice() {
        String price = mDetails.getRestaurantDetails().getRestaurant_price();
        switch (price) {
            case "1":
                mTxtPrice.setText(getString(R.string.price_one));
                break;
            case "2":
                mTxtPrice.setText(getString(R.string.price_two));
                break;
            case "3":
                mTxtPrice.setText(getString(R.string.price_three));
                break;
            case "4":
                mTxtPrice.setText(getString(R.string.price_four));
                break;
            default:
                mTxtPrice.setText(getString(R.string.price_one));
        }
    }

    private void setRestTimings() {
        List<String> listTimings = new ArrayList<>();
        String isOpenNow = mDetails.getRestaurantDetails().getIsOpenNow();
        if (null != mDetails && 0 != mDetails.getRestaurantDetails().getRestaurant_timings().size()) {
            for (int row = 0; row < mDetails.getRestaurantDetails().getRestaurant_timings().size(); row++) {
                listTimings.add(mDetails.getRestaurantDetails().getRestaurant_timings().get(row).getDay_of_week() +
                        "         " +
                        mDetails.getRestaurantDetails().getRestaurant_timings().get(row).getRestaurant_open_close_time());
            }
            Comparator<String> dateComparator = (s1, s2) -> {
                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format_month));
                    Date d1 = format.parse(s1);
                    Date d2 = format.parse(s2);
                    if (d1.equals(d2)) {
                        return s1.substring(s1.indexOf(" ") + 1).compareTo(s2.substring(s2.indexOf(" ") + 1));
                    } else {
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(d1);
                        cal2.setTime(d2);
                        return cal1.get(Calendar.DAY_OF_WEEK) - cal2.get(Calendar.DAY_OF_WEEK);
                    }
                } catch (ParseException pe) {
                    throw new RuntimeException(pe);
                }
            };
            Collections.sort(listTimings, dateComparator);
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listTimings);
            mDirSpinner.setAdapter(adapter);
        } else if (isOpenNow.equalsIgnoreCase("true")) {
            mDirSpinner.setVisibility(View.GONE);
            mTxtDirRestOpen.setVisibility(View.VISIBLE);
        } else {
            mDirSpinner.setVisibility(View.GONE);
            mTxtDirRestClose.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Set route on google map
     **/
    public void setGoogleRoute() {
        RootGoogleDir mGoogleDir = getIntent().getExtras().getParcelable(getString(R.string.intent_google_dir));
        //TODO latlng are hardcoded
        LatLng src = new LatLng(Double.parseDouble(UIConstants.LATITUDE), Double.parseDouble(UIConstants.LONGITUDE));

        mMap.addMarker(new MarkerOptions().position(src)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_pin)));
        LatLng destination = new LatLng(Double.parseDouble(mDetails.getRestaurantDetails().getLocation_lat()),
                Double.parseDouble(mDetails.getRestaurantDetails().getLocation_long()));
        mMap.addMarker(new MarkerOptions().position(destination)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_dest)));
        if (null != mGoogleDir) {
            String encodedString = mGoogleDir.getRoutes().get(0).getOverview_polyline().getPoints();
            list = decodePoly(encodedString);

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(ContextCompat.getColor(DirectionsActivity.this, R.color.colorBlack));
            polyOptions.addAll(list);

            mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(UIConstants.ROUTE_WIDTH)
                    .color(Color.rgb(UIConstants.ROUTE_COLOR, UIConstants.ROUTE_COLOR, UIConstants.ROUTE_COLOR))
                    .geodesic(true));
            zoomRoute(mMap, list);
        }
        setAnimation(mMap, list, BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_src));
    }

    /* Fetch latlng list of points for setting route */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    /* Set animation for google moving marker */
    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final BitmapDescriptor bitmap) {
        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(bitmap)
                .position(directionPoint.get(0))
                .flat(true));
        animateMarker(myMap, marker, directionPoint, false);
    }

    /* Set up google moving marker */
    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int point = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / UIConstants.DURATION_MARKER);
                if (point < directionPoint.size())
                    marker.setPosition(directionPoint.get(point));
                point++;
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setGoogleRoute();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    /* Zoom over google direction route to fit screen */
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
        if (null != googleMap || null != lstLatLngRoute || !lstLatLngRoute.isEmpty()) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng latLngPoint : lstLatLngRoute)
                boundsBuilder.include(latLngPoint);
            int routePadding = 100;
            LatLngBounds latLngBounds = boundsBuilder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
        }
    }

    //calculate distance in miles
    private void distInMiles(double srcLlat, double srcLng, double destLat, double destLng) {
        double theta = srcLng - destLng;
        double dist = Math.sin(deg2rad(srcLlat))
                * Math.sin(deg2rad(destLat))
                + Math.cos(deg2rad(srcLlat))
                * Math.cos(deg2rad(destLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        NumberFormat distance = new DecimalFormat(UIConstants.DIST_FORMAT);
        mTxtRestDist.setText(distance.format(dist) + " " + getString(R.string.mi));
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void success(Object value) {

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
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
