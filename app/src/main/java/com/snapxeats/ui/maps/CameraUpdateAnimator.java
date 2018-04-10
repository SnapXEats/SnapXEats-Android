package com.snapxeats.ui.maps;

import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta Patil on 9/4/18.
 */
public class CameraUpdateAnimator implements GoogleMap.OnCameraIdleListener {
    private final GoogleMap mMap;
    private final GoogleMap.OnCameraIdleListener mOnCameraIdleListener;
    private List<Animation> cameraUpdates = new ArrayList<>();
    private boolean mIsRotateGestureEnabled;
    private boolean mIsScrollGestureEnabled;
    private boolean mIsTiltGestureEnabled;
    private boolean mIsZoomGestureEnabled;

    public CameraUpdateAnimator(GoogleMap map,
                                GoogleMap.OnCameraIdleListener onCameraIdleListener) {
        mMap = map;
        mIsRotateGestureEnabled = mMap.getUiSettings().isRotateGesturesEnabled();
        mIsScrollGestureEnabled = mMap.getUiSettings().isScrollGesturesEnabled();
        mIsTiltGestureEnabled = mMap.getUiSettings().isTiltGesturesEnabled();
        mIsZoomGestureEnabled = mMap.getUiSettings().isZoomGesturesEnabled();

        mOnCameraIdleListener = onCameraIdleListener;
    }

    public void add(CameraUpdate cameraUpdate, boolean animate, long delay) {
        if (cameraUpdate != null) {
            cameraUpdates.add(new Animation(cameraUpdate, animate, delay));
        }
    }

    public void clear() {
        cameraUpdates.clear();
    }

    public void execute() {
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);

        mMap.setOnCameraIdleListener(this);
        executeNext();
    }

    private void executeNext() {
        if (!cameraUpdates.isEmpty()) {
            final Animation animation = cameraUpdates.remove(0);

            Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (animation.mAnimate) {
                        mMap.animateCamera(animation.mCameraUpdate);
                    } else {
                        mMap.moveCamera(animation.mCameraUpdate);
                    }
                }
            }, animation.mDelay);
        } else {
            mMap.setOnCameraIdleListener(mOnCameraIdleListener);
            mMap.getUiSettings().setRotateGesturesEnabled(mIsRotateGestureEnabled);
            mMap.getUiSettings().setScrollGesturesEnabled(mIsScrollGestureEnabled);
            mMap.getUiSettings().setTiltGesturesEnabled(mIsTiltGestureEnabled);
            mMap.getUiSettings().setZoomGesturesEnabled(mIsZoomGestureEnabled);
        }
    }

    @Override
    public void onCameraIdle() {
        executeNext();
    }

    private class Animation {
        private final CameraUpdate mCameraUpdate;
        private final boolean mAnimate;
        private final long mDelay;

        public Animation(CameraUpdate cameraUpdate, boolean animate, long delay) {
            mCameraUpdate = cameraUpdate;
            mAnimate = animate;
            mDelay = delay;
        }
    }
}