package com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.snapxeats.ui.cuisinepreference.CuisinePrefInteractor;
import com.snapxeats.ui.home.fragment.home.HomeFgmtContract;
import com.snapxeats.ui.home.fragment.home.HomeFgmtInteractor;
import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 20/2/18.
*/

public class NetworkCheckReceiver extends BroadcastReceiver {


    @Inject
    HomeFgmtContract.HomeFgmtPresenter presenter;

    @Inject
    HomeFgmtInteractor interactor;

    @Inject
    CuisinePrefInteractor cuisinePrefInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Network Detected.", Toast.LENGTH_SHORT).show();
        if (null != intent) {
            SCREENNAMES screen = intent.getExtras().getParcelable("screen");

            if (null != screen) {
                switch (screen) {
                    case CUISINE:
                        cuisinePrefInteractor.getCuisinePref();
                        break;
                }
            }
        }
    }
}
