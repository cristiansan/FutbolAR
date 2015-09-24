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

public class VideoWebActivity extends Activity {

    public static final String URL = "url";
    private WebView webView;
    private String url = "";
    private RelativeLayout progressBarLay;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web);
        View view = findViewById(R.id.iv_menu_icon_actionbar);
        if (view != null)
            view.setVisibility(View.INVISIBLE);
        webView = (WebView) findViewById(R.id.webview);
        url = getIntent().getStringExtra(URL);
        progressBarLay = (RelativeLayout) findViewById(R.id.progressbarlay);
        pb = (ProgressBar) findViewById(R.id.progressbar);

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
        // webView.setWebChromeClient(mWebChromeClient);
        webView.setBackgroundColor(0x00000000);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
        try {
            webView.stopLoading();
            webView.loadUrl("file:///android_asset/nonexistent.html");
            finish();
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webView, (Object[]) null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.onBackPressed();
    }

}
