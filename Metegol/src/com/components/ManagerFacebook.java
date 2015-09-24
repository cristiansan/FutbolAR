package com.components;

import android.app.Activity;
import android.os.Bundle;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class ManagerFacebook {
    private Activity mActivity;

    public void updateStatus(final String name, final String review,
                             final String caption, final String description, final String link,
                             final String urlPicture, final Activity activity,
                             final ListenerShareFacebook listenerShareFacebook) {
        mActivity = activity;

        // start Facebook Login
        Session.openActiveSession(activity, true, new Session.StatusCallback() {
            // callback when session changes state
            public void call(final Session session, SessionState state,
                             Exception exception) {
                if (session.isOpened()) {
                    publishFeedDialog(name, review, caption, description, link,
                            urlPicture, listenerShareFacebook);
                }

            }
        });
    }

    private void publishFeedDialog(String name, String review, String caption,
                                   String description, String link, String urlPicture,
                                   final ListenerShareFacebook listenerShareFacebook) {
        Bundle params = new Bundle();
        params.putString("name", name);
        params.putString("message", review);
        params.putString("caption", caption);
        params.putString("description", description);
        params.putString("link", link);
        params.putString("picture", urlPicture);


        Session session = Session.getActiveSession();

        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mActivity,
                session, params)).setOnCompleteListener(
                new OnCompleteListener() {
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                listenerShareFacebook.onShareFacebookSuccess();
                            } else {
                                // User clicked the Cancel button
                                listenerShareFacebook
                                        .onShareFacebookFailure("Publish cancelled");
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            listenerShareFacebook
                                    .onShareFacebookFailure("Publish cancelled");
                        } else {
                            // Generic, ex: network error
                            listenerShareFacebook
                                    .onShareFacebookFailure("Error posting story");
                        }
                    }

                }
        ).build();
        feedDialog.show();
    }
}