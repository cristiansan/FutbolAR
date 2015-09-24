package com.pmovil.soccer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.SlidingMenu;

public class BetFragment extends FragmentBase {

    private static final String TAG = "BetFragment";
    private View view;
    private WebView webView;
    private RelativeLayout progressBarLay;
    private ProgressBar pb;
    private ImageView ivClose;
    private boolean error = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        error = false;

        hideLoading();
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.bet_fragment, container, false);
        webView = (WebView) view.findViewById(R.id.webview);
        progressBarLay = (RelativeLayout) view
                .findViewById(R.id.progressbarlay);
        pb = (ProgressBar) view.findViewById(R.id.progressbar);
        ivClose = (ImageView) view.findViewById(R.id.iv_betfragment_close);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity act = getActivity();
        if (act != null && act instanceof MainActivity) {
            ((MainActivity) act).prepareBet();
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient(

        ) {

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                String des = view.getTitle();
//				if (des.trim().equalsIgnoreCase("404 not found")) {
//					webView.setVisibility(View.GONE);
//					error = true;
//					Activity act = getActivity();
//					if (act instanceof MainActivity) {
//						((MainActivity) act).noConnection();
//					}
//				} else {
                super.onPageFinished(view, url);

//				}
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                // super.onReceivedError(view, errorCode, description,
                // failingUrl);
                webView.setVisibility(View.INVISIBLE);
                error = true;
                Activity act = getActivity();
                if (act instanceof MainActivity) {
                    ((MainActivity) act).noConnection();
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.contains("exitapp")) {
                    if (getActivity() != null
                            && getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).toggle();
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url != null && url.contains("exitapp")) {
                    if (getActivity() != null
                            && getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).toggle();
                    return;
                }
                super.onPageStarted(view, url, favicon);

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                Activity act = getActivity();
                if (act != null) {
                    act.setProgress(progress * 1000);
                    pb.setProgress(progress * 1000);
                    progressBarLay.setVisibility(View.VISIBLE);
                    if (progress >= 95) {
                        progressBarLay.setVisibility(View.GONE);
                        if (error) {
                            webView.setVisibility(View.GONE);

                        } else
                            webView.setVisibility(View.VISIBLE);
                        ivClose.setVisibility(View.VISIBLE);

                    }
                }
                // pb.setVisibility(View.GONE);
            }
        });
        webView.setVisibility(View.INVISIBLE);
        webView.setBackgroundColor(0x00000000);
//		webView.loadUrl("http://prode.br.pmovil.net/login");
        webView.loadUrl(getString(R.string.url_bet));

        ivClose.setVisibility(View.INVISIBLE);
        ivClose.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (getActivity() != null
                        && getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).toggle();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        error = false;
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity) {
            ((MainActivity) act).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ////Log.i(TAG, "DestroyView");
        error = false;

        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }
}
