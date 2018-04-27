package com.snapxeats.ui.home.fragment.snapnshare;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import com.snapxeats.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Snehal Tembare on 26/4/18.
 */

public class RemindMeLaterService extends IntentService {


    public RemindMeLaterService() {
        super("RemindMeLaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String restaurantId = intent.getStringExtra(getString(R.string.intent_restaurant_id));

        Intent notifyIntent = new Intent(this, SnapNotificationReceiver.class);
        notifyIntent.putExtra(getString(R.string.intent_restaurant_id), restaurantId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 6, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int notificationId = intent.getIntExtra(getString(R.string.notification_id), 0);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.cancel(notificationId);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                TimeUnit.MINUTES.toMillis(10), pendingIntent);
    }
}
