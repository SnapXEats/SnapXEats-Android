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
import static com.snapxeats.common.constants.UIConstants.REQUEST_CODE_REMIND_ACTION;
import static com.snapxeats.common.constants.UIConstants.REQUEST_CODE_TAKE_PHOTO_ACTION;

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
        String restaurantId = intent.getStringExtra(getString(R.string.intent_restaurant_id));
        //Intent for take photo

        Intent takePhotoNotifyIntent = new Intent(this, HomeActivity.class);
        takePhotoNotifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        takePhotoNotifyIntent.putExtra(getString(R.string.notification), true);
        takePhotoNotifyIntent.putExtra(getString(R.string.intent_restaurant_id), restaurantId );

        PendingIntent pendingIntentTakePhoto = PendingIntent.getActivity(this, REQUEST_CODE_TAKE_PHOTO_ACTION, takePhotoNotifyIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Action takePhoto = new Notification.Action(0, getString(R.string.take_photo),
                pendingIntentTakePhoto);

        //Intent for notify me later
        Intent remindNotifyIntent = new Intent(this, RemindMeLaterService.class);
        remindNotifyIntent.putExtra(getString(R.string.intent_restaurant_id),restaurantId);

        PendingIntent pendingIntentRemind = PendingIntent.getService(this, REQUEST_CODE_REMIND_ACTION,
                remindNotifyIntent, PendingIntent.FLAG_ONE_SHOT);

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
                .setContentIntent(pendingIntentTakePhoto)
                .addAction(remindLaterAction)
                .addAction(takePhoto)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
