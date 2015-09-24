package com.pmovil.soccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NewsWebActivity extends Activity {

    public static final String URL = "url";
    public RelativeLayout containerBanner;
    private WebView webView;
    private String url = "";
    private RelativeLayout progressBarLay;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        webView = (WebView) findViewById(R.id.webview);
        url = getIntent().getStringExtra(URL);
        progressBarLay = (RelativeLayout) findViewById(R.id.progressbarlay);
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

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setInitialScale(100);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(url);
        // webView.loadUrl("http://www.youtube.com/watch?v=u4iwe52n4l0&feature=youtube_gdata");

        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar);
        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                activity.setProgress(progress * 1000);
                pb.setProgress(progress * 1000);
                progressBarLay.setVisibility(View.VISIBLE);
                if (progress >= 90) {
                    progressBarLay.setVisibility(View.GONE);
                }
                // pb.setVisibility(View.GONE);
            }
        });
        webView.setBackgroundColor(0x00000000);

    }

}
