package com.snapxeats.ui.home.fragment.snapnshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.snapxeats.R;

/**
 * Created by Snehal Tembare on 24/4/18.
 */

public class SnapNotificationReceiver extends BroadcastReceiver {

    public SnapNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, SnapNotificationService.class);
        intentService.putExtra(context.getString(R.string.intent_restaurant_id),
                intent.getStringExtra(context.getString(R.string.intent_restaurant_id)));
        context.startService(intentService);
    }
}
