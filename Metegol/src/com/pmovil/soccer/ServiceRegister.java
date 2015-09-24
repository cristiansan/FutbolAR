package com.pmovil.soccer;

import android.content.Context;
import android.os.AsyncTask;

import com.pmovil.soccer.net.ManagerConnection.ConnectionResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class ServiceRegister {

    private static String TAG = "ServiceRegister";
    private static boolean DEBUG = false;

    private static void registerGCMClient(String registrationId, String senderId) {
        String url = "http://gcm4public.appspot.com/registergcmclient?senderId="
                + senderId + "&registrationId=" + registrationId;
        //Log.d(TAG, url);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        try {
            httpclient.execute(httpget);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerInWebServiceDevice(final String deviceToken,
                                                  final Context context) {
        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                try {

                    if (DEBUG) {
                        registerGCMClient(deviceToken,
                                GoogleCloudMessagingUtilities.SENDER_ID);
                    } else {
                        com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                                .getInstance(context).getConnWsFutebol();
                        final ConnectionResponse response = connectionsWSMetegol
                                .getRegisterDevice(deviceToken);
                        try {
                            //Log.i(TAG, new String(response.getBodyResponse(),"UTF-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    //Log.i(TAG, "Error :" + ex.getMessage());
                }
                return null;
            }

        }.execute(null, null, null);

    }

}
