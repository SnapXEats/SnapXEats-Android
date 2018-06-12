package com.snapxeats.ui.directions;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.snapxeats.R;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.ui.home.HomeActivity;

import static com.snapxeats.common.constants.UIConstants.CHECKIN_NOTIFICATION_ID;
import static com.snapxeats.common.constants.UIConstants.CHECKIN_SERVICE;
import static com.snapxeats.common.constants.UIConstants.REQUEST_CODE_CHECKIN_ACTION;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 3/6/18.
 */
public class CheckInNotificationService extends IntentService {

    public CheckInNotificationService() {
        super(CHECKIN_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Log.i(CHECKIN_SERVICE, geofencingEvent.toString());
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            String transitionType = getTransitionString(geofenceTransition);
            checkInNotification(intent, transitionType);
        }
    }

    public void checkInNotification(Intent intent, String locTransitionType) {
        String restaurantId = intent.getStringExtra(getString(R.string.intent_restaurant_id));
        Intent checkInNotifyIntent = new Intent(this, HomeActivity.class);
        checkInNotifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        checkInNotifyIntent.putExtra(getString(R.string.checkin_notification), true);
        checkInNotifyIntent.putExtra(getString(R.string.intent_restaurant_id), restaurantId);

        PendingIntent pendingIntentCheckIn = PendingIntent.getActivity(this, REQUEST_CODE_CHECKIN_ACTION, checkInNotifyIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Action checkIn = new Notification.Action(ZERO, getString(R.string.check_in),
                pendingIntentCheckIn);

        Notification.Builder builder = new Notification.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentTitle(getString(R.string.checkin_reminder_title))
                .setStyle(new Notification.BigTextStyle().bigText(UIConstants.CHECKIN_NOTIFICATION_MESSAGE))
                .setSmallIcon(R.drawable.ic_notification)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntentCheckIn)
                .setContentTitle(locTransitionType)
                .addAction(checkIn)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(CHECKIN_NOTIFICATION_ID, builder.build());
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_enter);
            default:
                return getString(R.string.geofence_transition);
        }
    }
}