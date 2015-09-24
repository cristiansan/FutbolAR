package com.pmovil.soccer;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.components.ShareData;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.pmovil.soccer.Interfaces.SlidingMenuListener;
import com.pmovil.soccer.MenuFragment.Options;
import com.pmovil.soccer.entities.Team;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends SlidingFragmentActivity implements SlidingMenuListener {

    private static final String TAG = "MainActivity";
    private static final String TAG_CONTENT = "content";
    public MenuFragment menuFragment = null;
    public Session.StatusCallback statusCallback = new SessionStatusCallback();
    public RelativeLayout containerBanner;
    private Fragment content;
    private ImageView ivBtnMenuSliding = null;
    private ImageView ivBtnMinByMinOrTeam = null;
    private View tabBar = null;
    private View containerView = null;
    private SlidingMenu slidingMenu;
    private View containerPreloader = null;
    private boolean fragmentShouldBeChanged = false;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private View containerMesssage = null;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    private TextView messageTextView = null;
    private String regid;
    private AdView bannerAdView;
    private String nameFragment = null;
    private static Team teamStored = null;
    private SharedPreferences sharedPreferences = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the Above View

        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getBaseContext());

        menuFragment = new MenuFragment();
        menuFragment.setMainActivity(this);
        if (savedInstanceState != null)
            content = getSupportFragmentManager().getFragment(
                    savedInstanceState, TAG_CONTENT);
        if (content == null) {
            if (resourcesMetegol.getSplashDynamic() != null
                    && resourcesMetegol.getSplashDynamic().getComeUrl() == 1) {
                if (resourcesMetegol.getNameFragmenteToOpen() != null
                        && !resourcesMetegol.getNameFragmenteToOpen()
                        .equalsIgnoreCase("")) {

                    nameFragment = resourcesMetegol
                            .getNameFragmenteToOpen();
                    if (nameFragment.contains("exitapp")) {

                        FixtureFragment fragFixture = new FixtureFragment();
                        Bundle args = new Bundle();
                        args.putBoolean("showInterstitial", false);
                        fragFixture.setArguments(args);
                        content = fragFixture;

                        menuFragment.appendFragment(Options.FIXTURE.ordinal(),
                                content);
                    } else if (nameFragment.contains("menuProde")) {
                        FixtureFragment fragFixture = new FixtureFragment();
                        Bundle args = new Bundle();
                        args.putBoolean("showInterstitial", false);
                        fragFixture.setArguments(args);
                        content = fragFixture;
                        menuFragment.appendFragment(Options.FIXTURE.ordinal(),
                                content);

                    /*FBARBIERI
                    else if (nameFragment.contains("menuNoticias")) {
                        content = new NewsFragment();
                        menuFragment.appendFragment(Options.NEWS.ordinal(),
                                content);
                    } else if (nameFragment.contains("menuVideos")) {
                        content = new VideosFragment();
                        menuFragment.appendFragment(Options.VIDEOS.ordinal(),
                                content);*/

                    }else if (nameFragment.contains("menuMedia")) {
                        content = new NewsFragment();
                        menuFragment.appendFragment(Options.MEDIA.ordinal(),
                                content);

                    } /* FBARBIERI
                        else if (nameFragment.contains("menuBatallas")) {
                        content = new BattlesFragment();
                        menuFragment.appendFragment(Options.BATTLES.ordinal(),
                                content);
                    } */
                        else if (nameFragment.contains("menuFixture")) {
                        FixtureFragment fragFixture = new FixtureFragment();
                        Bundle args = new Bundle();
                        args.putBoolean("showInterstitial", false);
                        fragFixture.setArguments(args);
                        content = fragFixture;
                        menuFragment.appendFragment(Options.FIXTURE.ordinal(),
                                content);
                    } else if (nameFragment.contains("menuCampeonatos")) {
                        content = new ChangeTournamentFragment();
                        menuFragment.appendFragment(
                                Options.CHANGE_TOURNAMENT.ordinal(), content);
                    }

                } else {
                    FixtureFragment fragFixture = new FixtureFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("showInterstitial", false);
                    fragFixture.setArguments(args);
                    content = fragFixture;
                    menuFragment.appendFragment(Options.FIXTURE.ordinal(),
                            content);
                }
            } else {
                FixtureFragment fragFixture = new FixtureFragment();
                Bundle args = new Bundle();
                args.putBoolean("showInterstitial", false);
                fragFixture.setArguments(args);
                content = fragFixture;
                menuFragment.appendFragment(Options.FIXTURE.ordinal(), content);
            }

        }
        if (resourcesMetegol.isRedirectionTeam()) {
            content = new TeamFragment();
            resourcesMetegol.setRedirectionTeam(false);
        }
        setContentView(R.layout.activity_content_frame);

        containerBanner = (RelativeLayout) findViewById(R.id.container_banner);

        //ADMOB
        bannerAdView = (AdView) findViewById(R.id.bannerView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("942EC2AA2A735DFBD71A9EEF70692CE7")
                .build();
        // Start loading the ad in the background.
        bannerAdView.loadAd(adRequest);

        if (nameFragment != null && nameFragment.contains("menuBatallas")) {
            containerBanner.setVisibility(View.GONE);
        }

        tabBar = findViewById(R.id.topbar);
        containerView = findViewById(R.id.content_frame);
        containerPreloader = findViewById(R.id.container_preloader);
        containerMesssage = findViewById(R.id.container_no_connection);
        messageTextView = (TextView) findViewById(R.id.tv_noconnection);
        // Get the background, which has been compiled to an AnimationDrawable
        // object.

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, content).commit();
        setBehindContentView(R.layout.menu_frame);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, menuFragment).commit();
        slidingMenu = getSlidingMenu();
        slidingMenu.setBackgroundColor(getResources().getColor(
                android.R.color.black));
        ivBtnMenuSliding = (ImageView) findViewById(R.id.iv_menu_icon_actionbar);
        prepareActionBar();
        prepareSlidingMenu();

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        SharedPreferences sharedPreferences = getSharedPreferences("appData",
                MODE_PRIVATE);
        // Session session = Session.getActiveSession();

        Session session = new Session.Builder(this).setApplicationId(
                sharedPreferences
                        .getString("appID", getString(R.string.app_id))
        )
                .build();
        Session.setActiveSession(session);
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback,
                        savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this)
                        .setCallback(statusCallback));
            }
        }

        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
        if (checkPlayServices()) {
            // gcm = GoogleCloudMessaging.getInstance(this);
            regid = GoogleCloudMessagingUtilities
                    .getRegistrationId(getApplicationContext());

            if (regid.trim().equalsIgnoreCase("")) {

                Intent registrationIntent = new Intent(
                        "com.google.android.c2dm.intent.REGISTER");
                registrationIntent.putExtra("app", PendingIntent.getBroadcast(
                        MainActivity.this, 0, new Intent(), 0)); // boilerplate
                registrationIntent.putExtra("sender",
                        GoogleCloudMessagingUtilities.SENDER_ID);
//                startService(registrationIntent);

            } else {
                ServiceRegister.registerInWebServiceDevice(regid,
                        getApplicationContext());
            }
        } else {
            //Log.i(TAG, "No valid Google Play Services APK found.");
        }


        //PUSHBOOT
        /*
        ArrayList<String> extraPushIDs = new ArrayList<String>();
        extraPushIDs.add(GoogleCloudMessagingUtilities.SENDER_ID);
        Intent i = new Intent(getApplicationContext(), com.pmovil.pushboot.RegisterPush.class);
        // IF your app needs to receive push notifications, uncomment the line below filling in your project number
        i.putStringArrayListExtra(com.pmovil.pushboot.Config.EXTRA_SENDER_ID, extraPushIDs);
        startService(i);*/
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (slidingMenu != null) {
            slidingMenu.toggle(true);
        }
        return true;
    }

    private void prepareActionBar() {
        ivBtnMenuSliding.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                toggle();

            }
        });
    }

    public void refreshMenu() {
        if (menuFragment != null)
            menuFragment.loadData();
    }

    private void prepareSlidingMenu() {
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow_icon);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setEnabled(false);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setOnClosedListener(new OnClosedListener() {

            public void onClosed() {
                if (fragmentShouldBeChanged) {
                    fragmentShouldBeChanged = false;
                    changeFragment();
                }

            }
        });
    }

    public void hideIconMinByMinAndTeam() {
        if (ivBtnMinByMinOrTeam != null)
            ivBtnMinByMinOrTeam.setVisibility(View.INVISIBLE);
    }

    public void showIconMinByMin() {
        if (ivBtnMinByMinOrTeam != null) {
            ivBtnMinByMinOrTeam.setVisibility(View.VISIBLE);
            ivBtnMinByMinOrTeam.setImageResource(R.drawable.time_icon);
            ivBtnMinByMinOrTeam.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    //refreshMenu();
                    menuFragment.changeItemFromButton(Options.MIN_TO_MIN);
                }
            });
        }

    }

    public void showIconTeam() {
        if (ivBtnMinByMinOrTeam != null) {
            ivBtnMinByMinOrTeam.setVisibility(View.VISIBLE);
            ivBtnMinByMinOrTeam.setImageResource(R.drawable.cancha_icon);
            ivBtnMinByMinOrTeam.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    //refreshMenu();
                    menuFragment.changeItemFromButton(Options.TEAM);
                }
            });

        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil
                        .getErrorDialog(
                                resultCode,
                                this,
                                GoogleCloudMessagingUtilities.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void changeFragment() {
        hideIconMinByMinAndTeam();
        cancelBet();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.content_frame, content).commit();
        if (android.os.Build.VERSION.SDK_INT <= 8) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        }
    }

    public void setBlockedSlidingMenu(boolean blocked) {
        SlidingMenu sm = getSlidingMenu();
        sm.setEnabled(!blocked);
    }

    public void showLoading() {/**/
        /*
        setBlockedSlidingMenu(true);
        if (findViewByIdR.id.content_frame) != null)
            findViewById(R.id.content_frame).setVisibility(View.GONE);
        if (containerPreloader != null) {
            containerPreloader.setVisibility(View.VISIBLE);
        }
        */
    }

    public void hideLoading() {

        /*
        setBlockedSlidingMenu(false);
        if (findViewById(R.id.content_frame) != null)
            findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
        if (containerPreloader != null) {
            containerPreloader.setVisibility(View.GONE);
        }
        */
    }

    public void switchContent(Fragment fragment) {
        if (content == fragment) {
            hideLoading();
            getSlidingMenu().showContent();
            return;

        }
        goneMessage();

        showLoading();
        content = fragment;
        fragmentShouldBeChanged = true;
        getSlidingMenu().showContent();
    }

    public void cancelBet() {
        if (tabBar != null)
            tabBar.setVisibility(View.VISIBLE);
        if (containerView != null) {
            containerView.setBackgroundColor(getResources().getColor(
                    R.color.gray_metegol));
        }
    }

    public void prepareBet() {
        if (tabBar != null)
            tabBar.setVisibility(View.GONE);
        if (containerView != null) {
            containerView.setBackgroundColor(getResources().getColor(
                    R.color.background));
            ;
        }
    }

    public void prepareBattle() {
        if (tabBar != null)
            tabBar.setVisibility(View.GONE);
    }

    public void switchContentNow(Fragment fragment) {

        if (content == fragment) {
            return;
        }
        goneMessage();
        content = fragment;
        fragmentShouldBeChanged = false;
        changeFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, TAG_CONTENT, content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRedirectOfTwitter();
    }

    public void noConnection() {
        if (containerMesssage != null) {
            if (messageTextView != null)
                messageTextView.setText(R.string.NoConnectionInternetKey);
            containerMesssage.setVisibility(View.VISIBLE);
            goneAnimationContainerMessage();
        }
    }

    public void noVideoData() {
        if (containerMesssage != null) {
            if (messageTextView != null)
                messageTextView.setText(R.string.NoVideosMsgKey);
            containerMesssage.setVisibility(View.VISIBLE);
            goneAnimationContainerMessage();
        }
    }

    public void noNewsData() {
        if (containerMesssage != null) {
            if (messageTextView != null)
                messageTextView.setText(R.string.NoNewsKey);
            containerMesssage.setVisibility(View.VISIBLE);
            goneAnimationContainerMessage();
        }
    }

    public void noDataStatic() {
        if (containerMesssage != null) {
            if (messageTextView != null)
                messageTextView.setText(R.string.NoDataKey);
            containerMesssage.setVisibility(View.VISIBLE);
        }
    }

    public void goneMessage() {
        if (containerMesssage != null) {
            containerMesssage.setVisibility(View.INVISIBLE);
        }
    }

    public void noData() {
        if (containerMesssage != null) {
            if (messageTextView != null)
                messageTextView.setText(R.string.NoDataKey);
            containerMesssage.setVisibility(View.VISIBLE);
            goneAnimationContainerMessage();
        }
    }

    private void goneAnimationContainerMessage() {
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {

            public void run() {
                if (containerMesssage != null) {
                    Animation animation = AnimationUtils.loadAnimation(
                            getApplicationContext(), android.R.anim.fade_out);
                    animation.setAnimationListener(new AnimationListener() {

                        public void onAnimationStart(Animation animation) {

                        }

                        public void onAnimationRepeat(Animation animation) {

                        }

                        public void onAnimationEnd(Animation animation) {
                            containerMesssage.setVisibility(View.GONE);
                        }
                    });
                    containerMesssage.startAnimation(animation);
                }
            }
        }, 2000);

    }

    private void checkRedirectOfTwitter() {
        /**
         * This if conditions is tested once is redirected from twitter page.
         * Parse the uri to get oAuth Verifier
         * */
        new checkTwitterState().execute();
    }

    @Override
    protected void onDestroy() {
        try {
            if (mRegisterTask != null) {
                mRegisterTask.cancel(true);
            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        try {
            super.finish();
            resourcesMetegol.clearResources();
            resourcesMetegol = null;
        } catch (Exception e) {
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    @Override
    public void enableSlidingMenu(boolean enable, int touchMode) {
        this.getSlidingMenu().setTouchModeAbove(touchMode);
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        public void call(Session session, SessionState state,
                         Exception exception) {
            // onFacebookLogin();
        }
    }

    private class checkTwitterState extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Uri uri = getIntent().getData();
            if (uri != null
                    && uri.toString()
                    .startsWith(ShareData.TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(ShareData.URL_TWITTER_OAUTH_VERIFIER);
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(ShareData.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(ShareData.TWITTER_CONSUMER_SECRET);
                Configuration configuration = builder.build();

                TwitterFactory factory = new TwitterFactory(configuration);
                Twitter twitter = factory.getInstance();
                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            com.pmovil.soccer.entities.parser.ResourcesMetegol.getRequestToken(), verifier);

                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();
                    String urlImage = user.getProfileImageURL();

                    // Shared Preferences
                    SharedPreferences mSharedPreferences = getSharedPreferences(
                            ShareData.SHARE_NAME, 0);
                    Editor e = mSharedPreferences.edit();

                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(ShareData.PREF_KEY_OAUTH_TOKEN,
                            accessToken.getToken());
                    e.putString(ShareData.PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(ShareData.PREF_KEY_TWITTER_LOGIN, true);
                    e.putLong(ShareData.USER_ID_TWITTER, userID);
                    e.putString(ShareData.USER_NAME_TWITTER, username);
                    e.putString(ShareData.PROFILE_IMAGE_URL_TWITTER, urlImage);
                    e.commit(); // save changes

                    //Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                    // Displaying in xml ui
                } catch (Exception e) {
                    // Check log for login errors
                    //Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
            return null;
        }
    }

}
