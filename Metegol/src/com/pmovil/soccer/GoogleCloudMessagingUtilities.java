/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pmovil.soccer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class GoogleCloudMessagingUtilities {

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
    public static final String SERVER_URL = "http://backends.infinixsoft.com/ford/api/android_token";

    /**
     * Google API project id registered to use GCM.
     */
//	public static final String SENDER_ID = "322069183094"; // Test

//	 public static final String SENDER_ID = "845822663886"; // MEtegol

    public static final String SENDER_ID = "282638156249"; //futbolAr
    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION = "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";
    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_SOUND_NOTIFICATION = "sound";
    public static final String PROPERTY_VIBRATION_NOTIFICATION = "vibration";
    public static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
    /**
     * Default lifespan (7 days) of a reservation until it is considered
     * expired.
     */
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by the
     * UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    /**
     * Gets the current registration id for application on GCM service.
     * <p/>
     * If result is empty, the registration has failed.
     *
     * @return registration id, or empty string if the registration is not
     * complete.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(
                GoogleCloudMessagingUtilities.PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            //Log.v(TAG, "Registration not found.");
            return "";
        }
        // check if app was updated; if so, it must clear registration id to
        // avoid a race condition if GCM sends a message
        int registeredVersion = prefs.getInt(
                GoogleCloudMessagingUtilities.PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion
                || isRegistrationExpired(context)) {
            //Log.v(TAG, "App version changed or registration expired.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Checks if the registration has expired.
     * <p/>
     * <p/>
     * To avoid the scenario where the device sends the registration to the
     * server but the server loses it, the app developer may choose to
     * re-register after REGISTRATION_EXPIRY_TIME_MS.
     *
     * @return true if the registration has expired.
     */
    private static boolean isRegistrationExpired(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        // checks if the information is not stale
        long expirationTime = prefs
                .getLong(
                        GoogleCloudMessagingUtilities.PROPERTY_ON_SERVER_EXPIRATION_TIME,
                        -1);
        return System.currentTimeMillis() > expirationTime;
    }

    /**
     * Stores the registration id, app versionCode, and expiration time in the
     * application's {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration id
     */
    public static void setRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        //Log.v(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(GoogleCloudMessagingUtilities.PROPERTY_REG_ID, regId);
        editor.putInt(GoogleCloudMessagingUtilities.PROPERTY_APP_VERSION,
                appVersion);
        long expirationTime = System.currentTimeMillis()
                + GoogleCloudMessagingUtilities.REGISTRATION_EXPIRY_TIME_MS;

        //Log.v(TAG, "Setting registration expiry time to " + new Timestamp(expirationTime));
        editor.putLong(
                GoogleCloudMessagingUtilities.PROPERTY_ON_SERVER_EXPIRATION_TIME,
                expirationTime);
        editor.commit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(
                GoogleCloudMessagingUtilities.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    public static boolean isVibrationNotification(Context context) {
        return getGCMPreferences(context).getBoolean(
                PROPERTY_VIBRATION_NOTIFICATION, false);
    }

    public static boolean isSoundNotification(Context context) {
        return getGCMPreferences(context).getBoolean(
                PROPERTY_SOUND_NOTIFICATION, false);
    }

    public static void setVibrationNotification(Context context, boolean value) {
        SharedPreferences preferences = getGCMPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean(PROPERTY_VIBRATION_NOTIFICATION, value);
        editor.commit();
    }

    public static void setSoundNotification(Context context, boolean value) {
        SharedPreferences preferences = getGCMPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean(PROPERTY_SOUND_NOTIFICATION, value);
        editor.commit();
    }

}
