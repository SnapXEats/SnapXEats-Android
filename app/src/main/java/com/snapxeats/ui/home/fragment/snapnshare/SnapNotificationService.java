package com.snapxeats.ui.home.fragment.snapnshare;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import com.snapxeats.R;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.ui.home.HomeActivity;
import static com.snapxeats.common.constants.UIConstants.NOTIFICATION_ID;

/**
 * Created by Snehal Tembare on 24/4/18.
 */

public class SnapNotificationService extends IntentService {

    public SnapNotificationService() {
        super("SnapNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        customNotification(intent);
    }

    public void customNotification(Intent intent) {
        //Intent for take photo
        Intent notifyIntent = new Intent(this, HomeActivity.class);
        notifyIntent.putExtra(getString(R.string.notification_id), NOTIFICATION_ID);
        notifyIntent.putExtra(getString(R.string.notification), true);
        notifyIntent.putExtra(getString(R.string.intent_restaurant_id),
                intent.getStringExtra(getString(R.string.intent_restaurant_id)));

        PendingIntent pendingIntentTake = PendingIntent.getActivity(this, 2, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Action takePhoto = new Notification.Action(0, getString(R.string.take_photo),
                pendingIntentTake);

        //Intent for notify me later
        Intent notifyForRemindIntent = new Intent(this, RemindMeLaterService.class);
        notifyForRemindIntent.putExtra(getString(R.string.notification_id), NOTIFICATION_ID);
        notifyForRemindIntent.putExtra(getString(R.string.intent_restaurant_id),
                intent.getStringExtra(getString(R.string.intent_restaurant_id)));

        PendingIntent pendingIntentRemind = PendingIntent.getService(this, 4,
                notifyForRemindIntent, 0);

        Notification.Action remindLaterAction = new Notification.Action(0, getString(R.string.remind_me_later),
                pendingIntentRemind);


        //Notification for Take photo with Remind me later and Take photo action
        Notification.Builder builder = new Notification.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentTitle(getString(R.string.photo_notification_title))
                .setStyle(new Notification.BigTextStyle().bigText(UIConstants.NOTIFICATION_MESSAGE))
                .setSmallIcon(R.drawable.ic_notification)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntentTake)
                .addAction(remindLaterAction)
                .addAction(takePhoto)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
