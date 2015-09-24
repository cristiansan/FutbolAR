package com.pmovil.soccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.components.AdapterGeneric;
import com.components.Item;
import com.components.ShareData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pmovil.soccer.entities.Comment;
import com.pmovil.soccer.entities.Incidence;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends Activity {

    public static final String MATCH_ID_VALUE = "matchID";
    public static final String INCIDENCE_VALUE = "incidence";
    public static final String SOCIAL_NETWORK_VALUE = "socialNetWork";
    private static final String TAG = "CommentActivity";
    public RelativeLayout containerBanner;
    ConnectionHandlerResponseBody responseAddCommentHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            ////Log.i(TAG, response);
            // hideLoading();
            if (response.contains("OK")) {
                // Toast.makeText(getApplicationContext(),
                // "Comment Success. Update Comments..", Toast.LENGTH_LONG)
                // .show();
                connectAndLoadComment();
            }

        }

        public void onError(String response) {
            ////Log.i(TAG, response);
            hideLoading();
            noConnection();
            // Toast.makeText(getBaseContext(), "Update Error",
            // Toast.LENGTH_LONG)
            // .show();
        }
    };
    private Incidence incidence = null;
    private ListView lvComment = null;
    private EditText editComment = null;
    private View btnSend = null;
    private View containerPreloader = null;
    private ProgressBar ivPreloader = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private int matchId = 0;
    private int minToMinId = 0;
    private String socialNetwork = null;
    private List<Item> itemList = null;
    private List<Comment> commentList = null;
    ConnectionHandlerResponseBody responseGetCommentHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                commentList = JsonParsers.ParserComments(new JSONObject(
                        response));
                if (commentList == null) {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            }

            loadListAdapter();
            hideLoading();
            ////Log.i(TAG, response);
        }

        public void onError(String response) {
            hideLoading();
            finish();
            noConnection();
            ////Log.i(TAG, response);

        }
    };
    private LinearLayout relativeLayout = null;
    private View containerNoConnection = null;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        if (intent != null) {
            matchId = intent.getIntExtra(MATCH_ID_VALUE, 0);
            incidence = (Incidence) intent
                    .getSerializableExtra(INCIDENCE_VALUE);
            if (incidence != null)
                minToMinId = incidence.getId();
            socialNetwork = intent.getStringExtra(SOCIAL_NETWORK_VALUE);
        }
        View view = findViewById(R.id.iv_menu_icon_actionbar);
        if (view != null)
            view.setVisibility(View.INVISIBLE);

        containerBanner = (RelativeLayout) findViewById(R.id.container_banner);

        //ADMOB
        bannerAdView = (AdView) findViewById(R.id.bannerView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // Start loading the ad in the background.
        bannerAdView.loadAd(adRequest);


        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(getApplicationContext());
        containerNoConnection = findViewById(R.id.container_no_connection);
        lvComment = (ListView) findViewById(R.id.lv_comment);
        btnSend = findViewById(R.id.container_btn_send);
        editComment = (EditText) findViewById(R.id.edit_comment);
        containerPreloader = findViewById(R.id.container_preloader);
        ivPreloader = (ProgressBar) findViewById(R.id.progress_preloader);
        relativeLayout = (LinearLayout) findViewById(R.id.container_min_to_min_title_fragment);
        ItemMinByMin item = new ItemMinByMin();
        item.setComment(false);
        item.setData(incidence);
        item.setLeft(true);
        item.setWithoutShareAndComment(true);
        relativeLayout.addView(item.getView(null, getBaseContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bannerAdView != null) {
            bannerAdView.resume();
        }
        connectAndLoadComment();
        btnSend.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (editComment.getText().toString().length() > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);

                    String msg = editComment.getText().toString();
                    editComment.setText("");
                    showLoading();
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            ShareData.SHARE_NAME, 0);
                    String userId = "";
                    String userName = "";
                    String userPhoto = "";
                    if (socialNetwork
                            .equalsIgnoreCase(com.pmovil.soccer.net.ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK)) {
                        userId = sharedPreferences.getString(
                                ShareData.USER_ID_FACEBOOK, "");
                        userName = sharedPreferences.getString(
                                ShareData.USER_NAME_FACEBOOK, "");
                        userPhoto = sharedPreferences.getString(
                                ShareData.PROFILE_IMAGE_URL_FACEBOOK, "");
                    } else {
                        userId = ""
                                + sharedPreferences.getLong(
                                ShareData.USER_ID_TWITTER, 0);
                        userName = sharedPreferences.getString(
                                ShareData.USER_NAME_TWITTER, "");
                        userPhoto = sharedPreferences.getString(
                                ShareData.PROFILE_IMAGE_URL_TWITTER, "");

                    }
                    com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                            .getConnWsFutebol();
                    connectionsWSMetegol.addCommentsFromMatchAndMinByMinPost(
                            responseAddCommentHandler, matchId, minToMinId,
                            userId, userName, userPhoto, socialNetwork, msg);
                }

            }
        });
    }

    @Override
    public void onPause() {
        if (bannerAdView != null) {
            bannerAdView.pause();
        }
        super.onPause();
    }

    public void showLoading() {
        if (findViewById(R.id.lv_comment) != null)
            findViewById(R.id.lv_comment).setVisibility(View.GONE);
        if (containerPreloader != null) {
            containerPreloader.setVisibility(View.VISIBLE);
        }

    }

    public void hideLoading() {
        if (findViewById(R.id.lv_comment) != null)
            findViewById(R.id.lv_comment).setVisibility(View.VISIBLE);
        if (containerPreloader != null) {
            containerPreloader.setVisibility(View.GONE);
        }
    }

    private void connectAndLoadComment() {
        showLoading();
        com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                .getConnWsFutebol();
        connectionsWSMetegol.getCommentsFromMatchAndMintoMinPost(
                responseGetCommentHandler, matchId, minToMinId, 20);
    }

    private void loadListAdapter() {
        if (lvComment == null)
            return;
        lvComment.setDividerHeight(8);
        if (itemList == null)
            itemList = new ArrayList<Item>();
        itemList.clear();
        if (commentList == null)
            return;
        for (int indexCommentList = 0; indexCommentList < commentList.size(); indexCommentList++) {
            Item item = new ItemComment();
            item.setData(commentList.get(indexCommentList));
            itemList.add(item);

        }
        if (lvComment.getAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) lvComment.getAdapter();
            adapter.setItemsList(itemList);
            adapter.notifyDataSetChanged();
        } else
            lvComment.setAdapter(new AdapterGeneric(getApplicationContext(),
                    itemList));
    }

    public void noConnection() {
        if (containerNoConnection != null) {
            containerNoConnection.setVisibility(View.VISIBLE);
            Handler handler = new Handler(getMainLooper());
            handler.postDelayed(new Runnable() {

                public void run() {
                    if (containerNoConnection != null) {
                        Animation animation = AnimationUtils.loadAnimation(
                                getApplicationContext(),
                                android.R.anim.fade_out);
                        animation.setAnimationListener(new AnimationListener() {

                            public void onAnimationStart(Animation animation) {

                            }

                            public void onAnimationRepeat(Animation animation) {

                            }

                            public void onAnimationEnd(Animation animation) {
                                containerNoConnection.setVisibility(View.GONE);
                            }
                        });
                        containerNoConnection.startAnimation(animation);
                    }
                }
            }, 2000);
        }
    }
}
