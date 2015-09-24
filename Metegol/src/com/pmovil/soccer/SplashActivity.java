package com.pmovil.soccer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.CacheManager;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import error.manager.CustomExceptionHandler;

public class SplashActivity extends Activity implements OnClickListener {

    public static final String REDIRECTION_TEAM = "redirectionTeam";
    public static final String ID_TEAM = "idTeam";
    public static final String ID_CHAMPIONSHIP = "idChampionship";
    private static Runnable launchDownloadTask = null;
    private final String TAG = "SplashActivity";
    private ImageView ivLogo = null;
    private ProgressBar ivPreloader = null;
    private View view = null;
    private static Team teamStored = null;
    private static String language = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private TextView tvWelcome = null;
    private TextView tvDescription = null;
    private SharedPreferences sharedPreferences = null;
    private WebView wvSplash;
    private View viewTransparent;
    private String splashDynamic;
    private ConnectionHandlerResponseBody splashDynamicHandler = new ConnectionHandlerResponseBody() {

        @Override
        public void onSuccess(String response) {
            splashDynamic = response;
            new ConnectionSplashDynamic().execute();

        }

        @Override
        public void onError(String response) {

        }
    };
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsMetegol = null;
    private ConnectionHandlerResponseBody responseChampionship = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                resourcesMetegol.setChampionships(JsonParsers
                        .ParserChampionships(new JSONObject(response)));
                CacheManager.getInstance(getApplicationContext())
                        .setResponseChampionship(getApplicationContext(),
                                response);
                // ivPreloader.setVisibility(View.GONE);
                sharedPreferences = resourcesMetegol.getSharedPreferences();
                List<Championship> championship = resourcesMetegol
                        .getChampionships();

                if (championship != null && championship.size() == 1) {

                    Editor editor = sharedPreferences.edit();
                    editor.putInt(com.pmovil.soccer.entities.parser.ResourcesMetegol.CHAMPIONCHIP_VALUE_ID,
                            championship.get(0).getId());
                    editor.commit();
                    resourcesMetegol.setIdChampionshipSelected(championship
                            .get(0).getId());

                    connWsMetegol = resourcesMetegol.getConnWsFutebol();
                    connWsMetegol.getSplashDynamic(splashDynamicHandler);

                    return;

                }

                int championshipID = sharedPreferences.getInt(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.CHAMPIONCHIP_VALUE_ID, -1);
                if (championshipID == -1 && view != null) {
                    view.setVisibility(View.VISIBLE);

                    loadOptionsToSelectionChampionship();

                } else {

                    resourcesMetegol
                            .setIdChampionshipSelected(championshipID);

                    // viewTransparent.setVisibility(View.VISIBLE);
                    // ivPreloader.setVisibility(View.VISIBLE);
                    connWsMetegol = resourcesMetegol.getConnWsFutebol();
                    connWsMetegol.getSplashDynamic(splashDynamicHandler);

                    // startApp();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                alertDialog();
            }

        }

        public void onError(String response) {
            alertDialog();
        }
    };
    private TextView messageTextView = null;
    private View containerMesssage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // errorManager();

        setContentView(R.layout.activity_splash);
        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(getApplicationContext());
        sharedPreferences = resourcesMetegol.getSharedPreferences();
        onNewIntent(getIntent());
        view = findViewById(R.id.container_select_championship);
        ivLogo = (ImageView) findViewById(R.id.iv_logo_splash);
        wvSplash = (WebView) findViewById(R.id.wv_splash);
        viewTransparent = findViewById(R.id.view_background);
        containerMesssage = findViewById(R.id.container_no_connection);
        messageTextView = (TextView) findViewById(R.id.tv_noconnection);
        ivPreloader = (ProgressBar) findViewById(R.id.progress_preloader);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome_select_championship);
        tvDescription = (TextView) findViewById(R.id.tv_select_championship);
        tvWelcome.setText(R.string.Welcome);
        tvDescription.setText(R.string.TournamentSelectionMessageKey);
        launchDownloadTask = new Runnable() {

            public void run() {
                ivLogo.setVisibility(View.GONE);
                ivPreloader.setVisibility(View.VISIBLE);
                com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = resourcesMetegol
                        .getConnWsFutebol();
                connWsFutebol.getChampionshipsPost(responseChampionship);
            }
        };

        MyHandler handlerThread = new MyHandler();
        handlerThread.postDelayed(launchDownloadTask, 2000);

        // ConnectionsWSMetegol connWsFutebol = resourcesMetegol
        // .getConnWsFutebol();
        // connWsFutebol.getChampionshipsPost(responseChampionship);

        wvSplash.setWebViewClient(new WebViewClient(


        ) {

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                String des = view.getTitle();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                wvSplash.setVisibility(View.INVISIBLE);
                noConnection();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {

                    String nameFragment = url;


                    if (nameFragment.contains("market")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startApp();
                        startActivity(intent);

                    }else if (nameFragment.contains("exitapp")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    } else if (nameFragment.contains("menuProde")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    } else if (nameFragment.contains("menuNoticias")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    } else if (nameFragment.contains("menuVideos")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    } else if (nameFragment.contains("menuBatallas")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    } else if (nameFragment.contains("menuFixture")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    } else if (nameFragment.contains("menuCampeonatos")) {
                        resourcesMetegol.setNameFragmenteToOpen(url);
                        startApp();
                    }
//					startApp();
                }
                return super.shouldOverrideUrlLoading(view, url);



            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url != null) {
                    resourcesMetegol.setNameFragmenteToOpen(url);
                    // startApp();
                }

            }
        });

//        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("unused")
    private void errorManager() {
        String outFileName = null;
        File dir = null;
        try {
            outFileName = Environment.getExternalStorageDirectory()
                    .getCanonicalPath() + "/Doc";
            dir = new File(outFileName);

            if (!dir.exists()) {
                boolean create = dir.mkdirs();
                //Log.i("Doc", "Create " + create);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (outFileName != null && dir != null && dir.exists())
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                    outFileName));

    }

    ;

    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG, "Destroy");
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) findViewById(android.R.id.content);
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }

    }

    private void loadOptionsToSelectionChampionship() {
        LinearLayout containerOption = (LinearLayout) view
                .findViewById(R.id.container_button_select_championship);
        LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        for (int indexChampionship = 0; indexChampionship < resourcesMetegol
                .getChampionships().size(); indexChampionship++) {
            Championship championship = resourcesMetegol.getChampionships()
                    .get(indexChampionship);
            View view = mInflater.inflate(R.layout.button, null);
            View viewButton = view.findViewById(R.id.iv_backgorud_button);
            viewButton.setTag(championship.getId());
            viewButton.setOnClickListener(SplashActivity.this);
            TextView nameButton = (TextView) view.findViewById(R.id.tv_button);
            nameButton.setText(championship.getValue());
            containerOption.addView(view);

        }
    }

    private void startApp() {

        sharedPreferences = SplashActivity.this.getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences.getString("language", "");

        if(!language.equalsIgnoreCase("")){
            com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getApplicationContext()).changeLenguageApp(language);
        }

        sharedPreferences = SplashActivity.this.getSharedPreferences("Team", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("JsonTeamSelected", "");
        teamStored = gson.fromJson(json, Team.class);

        Intent intent;
        if (teamStored == null) {
            intent = new Intent(getApplicationContext(), SelectTeamActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        Editor editor = sharedPreferences.edit();
        if (extras != null) {
            if (extras.containsKey(REDIRECTION_TEAM)) {
                resourcesMetegol.setRedirectionTeam(extras.getBoolean(
                        REDIRECTION_TEAM, false));
            }
            if (extras.containsKey(ID_TEAM)) {
                int id_game=getIntent().getExtras().getInt(ID_TEAM, 0);
                resourcesMetegol.setGameSelectedId(id_game);
                editor.putInt(com.pmovil.soccer.entities.parser.ResourcesMetegol.GAME_SELECTED_ID ,id_game);
                editor.commit();
            }
            if (extras.containsKey(ID_CHAMPIONSHIP)) {
                int id_champ=getIntent().getExtras().getInt(ID_CHAMPIONSHIP, 0);
                resourcesMetegol.setIdChampionshipSelected(id_champ);
                editor.putInt(com.pmovil.soccer.entities.parser.ResourcesMetegol.CHAMPIONCHIP_VALUE_ID,id_champ);
                editor.commit();
            }
        }
        if (resourcesMetegol.isRedirectionTeam()) {
            resourcesMetegol.clearGameSimple();
        } else {
            resourcesMetegol.clearGame();
        }
        if (resourcesMetegol.getGameSelectedId() == 0
                || resourcesMetegol.getIdChampionshipSelected() == 0) {
            resourcesMetegol.setRedirectionTeam(false);
            resourcesMetegol.clearGame();

        }
    }

	/*
     * Instances of static inner classes do not hold an implicit reference to
	 * their outer class.
	 */

    private void alertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SplashActivity.this)
                .setMessage(R.string.NoConnectionInternetKey).setNeutralButton(
                        R.string.OKKey, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        }
                );
        try {
            alert.show();
        } catch (Exception e) {

        }
    }

    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof Integer) {
            //Log.i(TAG, "" + v.getTag());
            Editor editor = sharedPreferences.edit();
            editor.putInt(com.pmovil.soccer.entities.parser.ResourcesMetegol.CHAMPIONCHIP_VALUE_ID,
                    (Integer) v.getTag());
            editor.commit();
            resourcesMetegol
                    .setIdChampionshipSelected((Integer) v.getTag());

            startApp();

        }
    }

    public void noConnection() {
        if (containerMesssage != null) {
            if (messageTextView != null)
                messageTextView.setText(R.string.NoConnectionInternetKey);
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

    private static class MyHandler extends Handler {
    }

    private class ConnectionSplashDynamic extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                resourcesMetegol.setSplashDynamic(JsonParsers
                        .parserSplashDynamic(new JSONObject(splashDynamic)));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (resourcesMetegol.getSplashDynamic().getComeUrl() == 1) {
                ivPreloader.setVisibility(View.INVISIBLE);
                viewTransparent.setVisibility(View.INVISIBLE);
                wvSplash.setVisibility(View.VISIBLE);
                if (resourcesMetegol.getSplashDynamic().getUrl() != null
                        && !resourcesMetegol.getSplashDynamic().getUrl()
                        .equalsIgnoreCase("")) {

                    if(resourcesMetegol.getSplashDynamic().getUrl().contains("market")){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(resourcesMetegol.getSplashDynamic()
                                .getUrl()));
                        startApp();
                        startActivity(intent);

                    }else {
                        wvSplash.loadUrl(resourcesMetegol.getSplashDynamic()
                                .getUrl());

                    }

                }

            } else {
                startApp();
            }

        }
    }
}
