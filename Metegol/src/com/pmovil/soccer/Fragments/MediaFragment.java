package com.pmovil.soccer.Fragments;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pmovil.soccer.FragmentBase;
import com.pmovil.soccer.Interfaces.SlidingMenuListener;
import com.pmovil.soccer.NewsFragment;
import com.pmovil.soccer.R;
import com.pmovil.soccer.VideosFragment;
import com.slidingmenu.lib.SlidingMenu;

import java.util.Timer;
import java.util.TimerTask;

public class MediaFragment extends FragmentBase {

    private View view;
    private ViewPager pager;
    private SlidingMenuListener slidingMenuCallback;
    private int touchMode;
    private NewsFragment newsFrag = null;
    private VideosFragment videosFrag = null;
    private InterstitialAd interstitial;
    private static String adId = "ca-app-pub-7993377819754702/8233369076";
    private Timer timer = new Timer();;
    private boolean isX;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createInterstitial();

        view = inflater.inflate(R.layout.activity_media, container, false);

        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new SampleFragmentPagerAdapter());

        // Give the PagerSlidingTabStrip the ViewPager
        final PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(pager);

        //tabsStrip.setTypeface(Typeface.createFromAsset(this.getActivity().getAssets(), getString(R.string.font_name)), Typeface.NORMAL);

        pager.setOffscreenPageLimit(2);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {

                //tabsStrip.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_name)), Typeface.BOLD);

                if(position==0)
                    touchMode = SlidingMenu.TOUCHMODE_FULLSCREEN;
                else
                    touchMode = SlidingMenu.TOUCHMODE_MARGIN;

                slidingMenuCallback.enableSlidingMenu(true, touchMode);

            }

        });

//        displayInterstitial();

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        slidingMenuCallback = (SlidingMenuListener) activity;
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        //FBARBIERI
        private String tabTitles[] = new String[] { getResources().getString(R.string.tab_news), getResources().getString(R.string.tab_videos)};

        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapter() {
            super(getChildFragmentManager());
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (newsFrag == null)
                        newsFrag = new NewsFragment();

                    return newsFrag;

                case 1:
                    if (videosFrag == null)
                        videosFrag = new VideosFragment();

                    return videosFrag;
            }
            return null;
        }

        //FBARBIERI
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(timer!=null){
            timer.cancel();
        }
    }

    public void createInterstitial(){

        interstitial = new InterstitialAd(this.getActivity().getApplicationContext());
        interstitial.setAdUnitId(adId);
        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("XXX", "<---------");
                interstitial.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // no interstitial available
                interstitial = null;
            }

        });
    }

    public void displayInterstitial() {

        if (interstitial.isLoaded()) {
            timer.cancel();
            interstitial.show();
        } else{
            initTimer();
        }
    }

    private void initTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        displayInterstitial();
                    }
                });
            }
        }, 0, 10000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }

}
