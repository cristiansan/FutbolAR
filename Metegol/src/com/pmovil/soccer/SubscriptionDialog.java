package com.pmovil.soccer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SubscriptionDialog extends Dialog {

    private String mUrl;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private boolean progressDialogRunning = false;
    private Context context;
    private SubscriptionsListener subscriptionListener;

    public SubscriptionDialog(Context context, String url) {
        super(context, R.style.CustomDialogTheme);
        this.context = context;
        mUrl = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_popup);
        mSpinner = new ProgressDialog(context);
        mSpinner.setCancelable(false);
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage(context.getString(R.string.LoadingKey));
        mWebView = (WebView) findViewById(R.id.webTwitter);
        setUpWebView();

    }

    @SuppressLint("NewApi")
    private void setUpWebView() {

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        // mWebView.getSettings().setBuiltInZoomControls(true);
        // mWebView.setInitialScale(50);
        mWebView.setWebViewClient(new TwitterWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);

    }

    @Override
    protected void onStop() {
        progressDialogRunning = false;
        super.onStop();
    }

    public void onBackPressed() {
        if (!progressDialogRunning) {
            SubscriptionDialog.this.dismiss();
        }
    }

    public SubscriptionsListener getSubscriptionListener() {
        return subscriptionListener;
    }

    public void setSubscriptionListener(
            SubscriptionsListener subscriptionListener) {
        this.subscriptionListener = subscriptionListener;
    }

    public static interface SubscriptionsListener {

        void finishLogin();

        void errorLogin();

    }

    private class TwitterWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(com.pmovil.soccer.net.ConnectionsWSMetegol.URLCALLBACK_VALUE)) {
                SubscriptionDialog.this.dismiss();
                subscriptionListener.finishLogin();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // if
            // (failingUrl.startsWith(ConnectionsWSMetegol.URLCALLBACK_VALUE)) {
            // subscriptionListener.finishLogin();
            //
            // }
            super.onReceivedError(view, errorCode, description, failingUrl);

            SubscriptionDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
            progressDialogRunning = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialogRunning = false;
            mSpinner.dismiss();
        }

    }

}
