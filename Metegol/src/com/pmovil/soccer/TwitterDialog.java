package com.pmovil.soccer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.components.ShareData;


public class TwitterDialog extends Dialog {

    private String mUrl;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private boolean progressDialogRunning = false;
    private Context context;


    public TwitterDialog(Context context, String url) {
        super(context, R.style.CustomDialogTheme);
        this.context = context;
        mUrl = url;
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_popup);
        mSpinner = new ProgressDialog(context);
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage(context.getString(R.string.LoadingKey));
        mWebView = (WebView) findViewById(R.id.webTwitter);
        setUpWebView();

    }

    private void setUpWebView() {

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
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
            TwitterDialog.this.dismiss();
        }
    }

    public TwitterListener getTwitterListener() {
        return twitterListener;
    }

    public void setTwitterListener(TwitterListener twitterListener) {
        this.twitterListener = twitterListener;
    }

    private class TwitterWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(ShareData.TWITTER_CALLBACK_URL)) {
                TwitterDialog.this.dismiss();
                twitterListener.finishLogin(url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            TwitterDialog.this.dismiss();
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
    */
}
