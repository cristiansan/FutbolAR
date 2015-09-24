package com.pmovil.soccer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.components.ShareData;
import com.components.TextViewPlus;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.pmovil.soccer.entities.Comment;

import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommentDialog extends DialogFragment {

    public static CommentDialog newInstance(int title) {
        CommentDialog frag = new CommentDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    private ProgressDialog pDialog;
    private int matchId ;
    private boolean isComment;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private List<Comment> commentList = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        matchId = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity().getApplicationContext()).getGameSelectedId();

        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view  = inflater.inflate(R.layout.popup_comment_share, null);

        TextViewPlus tvTitle = (TextViewPlus) view.findViewById(R.id.tv_text_comment);
        TextViewPlus tvText = (TextViewPlus) view.findViewById(R.id.tv_title_share);

        ImageView btnFacebook = (ImageView) view.findViewById(R.id.iv_btn_facebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                loginFacebook(getActivity());
            }
        });

        TextViewPlus btnCancel = (TextViewPlus) view.findViewById(R.id.tv_cancel_comment);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });

        if(getArguments().getBoolean("showText")==false){
            tvTitle.setVisibility(View.GONE);
            btnFacebook.setPadding(0,30,0,0);
            tvText.setPadding(0,20,30,0);
        }

        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    private void loginFacebook(final Activity activity) {
        if (checkFacebookLogin(activity)) {

            dismiss();
            //goComment(ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK);

        }
    }

    public boolean checkFacebookLogin(final Activity activity) {
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

            Session.StatusCallback callback = new Session.StatusCallback() {

                public void call(Session session, SessionState state,
                                 Exception exception) {

                    if (state == SessionState.OPENED) {
                        if (getDataUserFacebook()) {
                            //goComment(ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK);
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
        final SharedPreferences mSharedPreferences = getActivity().getApplicationContext()
                .getSharedPreferences(ShareData.SHARE_NAME, 0);
        if (mSharedPreferences.getString(ShareData.USER_ID_FACEBOOK, "")
                .equalsIgnoreCase("")) {

            // If the session is open, make an API call to get user data
            // and define a new callback to handle the response

            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        public void onCompleted(GraphUser user,
                                                Response response) {

                            // If the response is successful
                            if (session == Session.getActiveSession()) {
                                if (user != null) {
                                    String user_ID = user.getId();// user id
                                    String profileName = user.getName();// user's
                                    SharedPreferences.Editor e = mSharedPreferences.edit();
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

                                    e.commit(); // save changes

                                    sendResult(1);
                                    dismiss();

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

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}
