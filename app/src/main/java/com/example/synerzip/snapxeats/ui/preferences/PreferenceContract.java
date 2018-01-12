package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Location;
import android.view.View;

import com.example.synerzip.snapxeats.BasePresenter;
import com.example.synerzip.snapxeats.BaseView;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter>, DialogView {

        Activity getActivity();

        void updatePlaceName(String placeName);

    }

    interface PreferencePresenter extends BasePresenter<PreferenceView> {

        void setLocation(Location location);

        void getLocation(PreferenceContract.PreferenceView preferenceView);

        void updatePlace(String placename);

        void presentScreen();

    }

    interface DialogView {
        void showProgressDialog();

        void dismissProgressDialog();

        void showNetworkErrorDialog();

        void showDenyDialog(DialogInterface.OnClickListener positiveClick,
                            DialogInterface.OnClickListener negativeClick);

    }

    interface PreferenceRouter {
        void presentScreen();

        void setView(PreferenceView view);
    }
}