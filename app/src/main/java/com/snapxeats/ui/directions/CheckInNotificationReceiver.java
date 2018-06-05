package com.snapxeats.ui.directions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.snapxeats.R;

/**
 * Created by Prajakta Patil on 3/6/18.
 */
public class CheckInNotificationReceiver extends BroadcastReceiver {

    public CheckInNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, CheckInNotificationService.class);
        //restId for CheckInRequest
        String restaurantId = intent.getStringExtra(context.getString(R.string.intent_restaurant_id));
        intentService.putExtra(context.getString(R.string.intent_restaurant_id), restaurantId);
        context.startService(intentService);
    }
}
