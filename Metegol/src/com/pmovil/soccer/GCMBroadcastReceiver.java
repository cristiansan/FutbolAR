package com.pmovil.soccer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class GCMBroadcastReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;
    static final String TAG = "GCMDemo";
    private static boolean DEBUG = false;
    private NotificationManager mNotificationManager;
    private Context ctx;
    private int championshipId = 0;
    private int matchId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        final String registration = intent.getStringExtra("registration_id");
        if (intent.getStringExtra("error") != null) {
            //Log.i(TAG, "ERROR");
            return;
            // Registration failed, should try again later.
        } else if (intent.getStringExtra("unregistered") != null) {
            //Log.i(TAG, "UNREGISTERED");
            return;
            // unregistration done, new messages from the authorized sender will
            // be rejected
        } else if (registration != null) {
            //Log.i(TAG, registration);
            GoogleCloudMessagingUtilities.setRegistrationId(context,
                    registration);
            ServiceRegister.registerInWebServiceDevice(registration, context);
            return;
            // Send the registration ID to the 3rd party site that is sending
            // the messages.
            // This should be done in a separate thread.
            // When done, remember that all registration is done.
        }

        if (DEBUG) {
            try {
                FileUtils.writeStringToFile(
                        new File(Environment.getExternalStorageDirectory(),
                                "Notification.txt"), intent.getExtras()
                                .getString("title")
                                + " "
                                + intent.getExtras().getString("message")
                );
                championshipId = Integer.parseInt(intent.getExtras().getString(
                        "title"));
                matchId = Integer.parseInt(intent.getExtras().getString(
                        "message"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // sendNotification(context, intent.getExtras().getString("alert"));
            sendNotification(context, "TEST");

        } else {

            try {
                championshipId = Integer.parseInt(intent.getExtras().getString(
                        "idc"));
                matchId = Integer.parseInt(intent.getExtras().getString("idm"));

            } catch (Exception e) {

            }

            sendNotification(context, intent.getExtras().getString("alert"));

        }

        setResultCode(Activity.RESULT_OK);
    }

    // Put the GCM message into a notification and post it.
    private void sendNotification(Context context, String msg) {
        mNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(ctx, SplashActivity.class);
        // Para abrir en
        intent.putExtra(SplashActivity.REDIRECTION_TEAM, true);
        intent.putExtra(SplashActivity.ID_CHAMPIONSHIP, championshipId);
        intent.putExtra(SplashActivity.ID_TEAM, matchId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                ctx).setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))

                        // .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        int defaultNotification = -1;
        if (GoogleCloudMessagingUtilities.isSoundNotification(ctx)) {
            defaultNotification = Notification.DEFAULT_SOUND;

        }
        if (GoogleCloudMessagingUtilities.isVibrationNotification(ctx)) {
            if (defaultNotification == -1) {
                defaultNotification = Notification.DEFAULT_VIBRATE;

            } else {
                defaultNotification = defaultNotification
                        | Notification.DEFAULT_VIBRATE;
            }
        }

        if (defaultNotification != -1) {
            mBuilder.setDefaults(defaultNotification);
        }
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify((int) System.currentTimeMillis()
                % Integer.MAX_VALUE, mBuilder.build());
    }

}
