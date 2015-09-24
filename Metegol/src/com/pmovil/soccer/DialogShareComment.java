package com.pmovil.soccer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.components.ShareData;
import com.components.TextViewPlus;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.pmovil.soccer.entities.Incidence;

public class DialogShareComment extends Dialog {
    private ProgressDialog pDialog;

    private int contentView;
    private Context context;
    private Activity activity = null;
    private static SharedPreferences mSharedPreferences;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Incidence incidence = null;
    MinToMinFragment mintomin = new MinToMinFragment();

    public DialogShareComment(Context context, int theme, int contentView) {
        super(context, theme);
        this.contentView = contentView;
        this.context = context;
        initDialoag();
    }

    private void initDialoag() {
        setContentView(contentView);
        mSharedPreferences = context.getSharedPreferences(ShareData.SHARE_NAME,
                0);

        ImageView btnFacebook = (ImageView) findViewById(R.id.iv_btn_facebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                loginFacebook(activity);
            }
        });

        TextViewPlus btnCancel = (TextViewPlus) findViewById(R.id.tv_cancel_comment);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                getActivity().finish();

            }
        });
    }

    public boolean loginFacebook (final Activity activity) {
        Session session = Session.getActiveSession();
        if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
            SharedPreferences sharedPreferences = activity
                    .getSharedPreferences("appData", Context.MODE_PRIVATE);
            session = new Session.Builder(activity.getApplicationContext())
                    .setApplicationId(
                            sharedPreferences.getString("appID",
                                    activity.getString(R.string.app_id))
                    )
                    .build();

            Session.setActiveSession(session);
        }

        if (!session.isOpened()) {

            StatusCallback callback = new StatusCallback() {

                public void call(Session session, SessionState state,
                                 Exception exception) {

                    if (state == SessionState.OPENED) {
                        if (getDataUserFacebook()) {

                            mintomin.goComment(com.pmovil.soccer.net.ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK);
                            getActivity().finish();
                        }
                    }

                }
            };

            if (!session.isOpened() && !session.isClosed()) {
                session.openForRead(new Session.OpenRequest(activity)
                        .setCallback(callback));
            } else {
                try {
                    Session.openActiveSession(activity, true, callback);
                } catch (Exception e) {

                }

            }
            return false;
        } else {
            return getDataUserFacebook();

        }
    }

    private boolean getDataUserFacebook() {

        final Session session = Session.getActiveSession();
        final SharedPreferences mSharedPreferences = context
                .getSharedPreferences(ShareData.SHARE_NAME, 0);
        if (mSharedPreferences.getString(ShareData.USER_ID_FACEBOOK, "")
                .equalsIgnoreCase("")) {

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            // If the session is open, make an API call to get user data
            // and define a new callback to handle the response

            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            if (pDialog != null)
                                pDialog.dismiss();
                            // If the response is successful
                            if (session == Session.getActiveSession()) {
                                if (user != null) {
                                    String user_ID = user.getId();// user id
                                    String profileName = user.getName();// user's
                                    Editor e = mSharedPreferences.edit();
                                    // "http://graph.facebook.com/"+userID+"/picture?type=small"

                                    e.putString(
                                            ShareData.PROFILE_IMAGE_URL_FACEBOOK,
                                            "http://graph.facebook.com/"
                                                    + user_ID
                                                    + "/picture?type=small"
                                    );
                                    e.putString(ShareData.USER_ID_FACEBOOK,
                                            user_ID);
                                    e.putString(ShareData.USER_NAME_FACEBOOK,
                                            profileName);
                                    // e.putString(ShareData.PROFILE_IMAGE_URL_TWITTER,
                                    // urlImage.toString());
                                    e.commit(); // save changes

                                    mintomin.goComment(com.pmovil.soccer.net.ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK);
                                    getActivity().finish();
                                }
                            }
                        }
                    }
            );
            Request.executeBatchAsync(request);
            return false;
        }

        return true;

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setIncidence(Incidence incidence) {
        this.incidence = incidence;
    }

}
