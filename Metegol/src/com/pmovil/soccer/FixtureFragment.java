package com.pmovil.soccer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.components.CustomViewPager;
import com.google.android.gms.ads.InterstitialAd;
import com.pmovil.soccer.FixtureListView.FixtureListListener;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.Team;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class FixtureFragment extends FragmentBase implements
        FixtureListListener {

    private static final String TAG = "FixtureFragment";
    public ProgressBar ivPreloader;
    private View view;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol;
    private TextView tvChampionshipFixture;
    private List<Date> dates = null;
    private TextView tvDateLater = null;
    private TextView tvDateCurrent = null;
    private TextView tvDateEarlier = null;
    private int indexCurrentDates = 0;
    private CustomViewPager pager;
    private MyPagerAdapter pagerAdapter;
    private FixtureListView listInstance;
    private static Team teamStored = null;
    private SharedPreferences sharedPreferences = null;
    private InterstitialAd interstitial;
    private boolean showInterstitial;
    private static String adId = "ca-app-pub-7993377819754702/8233369076";
    //private Timer timer = new Timer();

    public FixtureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        showInterstitial = getArguments().getBoolean("showInterstitial");

        if(showInterstitial==true){
           createInterstitial();
        }*/

        hideLoading();

        if (container != null)
            container.removeAllViews();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());

        //Verifica si el campeonato ta finalizo, si es asi le deja seleccionar otro.
        if(resourcesMetegol.getChampionshipByIdSelected().getFinished() == 1){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.championship_finished));
            builder.setMessage(getResources().getString(R.string.select_another_championship));
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Fragment fgChangeTournament = new ChangeTournamentFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(android.R.id.content,fgChangeTournament);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fixture_fragment, container, false);

        tvChampionshipFixture = (TextView) view
                .findViewById(R.id.tv_championship_fixture);
        ivPreloader = (ProgressBar) view.findViewById(R.id.progress_preloader);
        tvDateCurrent = (TextView) view
                .findViewById(R.id.tv_date_current_fixture);
        tvDateEarlier = (TextView) view
                .findViewById(R.id.tv_date_earlier_fixture);
        tvDateLater = (TextView) view.findViewById(R.id.tv_date_later_fixture);

        /*
        if(showInterstitial==true){
            displayInterstitial();
        }*/


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity) {
            ((MainActivity) act).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);
        }
        Championship championshipByIdSelected = resourcesMetegol
                .getChampionshipByIdSelected();
        if (championshipByIdSelected == null)
            return;
        tvChampionshipFixture.setText(resourcesMetegol
                .getChampionshipByIdSelected().getValue().toUpperCase());
        dates = resourcesMetegol.getChampionshipByIdSelected().getDates();

        if (dates == null) {
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noData();
            return;
        }
        initViewPagerSimple();
        setPositionDateOfPager();
        prepareDatesNames();
        pager.setCurrentItem(indexCurrentDates);
    }

    private void initViewPagerSimple() {
        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());
        pager = (CustomViewPager) view.findViewById(R.id.viewpager_fixture);
        pager.setOffscreenPageLimit(3);
        pagerAdapter = new MyPagerAdapter(getActivity());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            public void onPageSelected(int indexPage) {

                for (RelativeLayout pages : pagerAdapter.views) {
                    ((FixtureListView) pages).setInFocus(false);
                }
                indexCurrentDates = indexPage;
                prepareDatesNames();
                FixtureListView page = ((FixtureListView) pagerAdapter.views
                        .get(indexPage));

                page.checkDate();
                page.initAutoRefreshProcess();

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onDestroyView() {

        if (pagerAdapter != null && pagerAdapter.views != null) {
            for (RelativeLayout pages : pagerAdapter.views) {
                ((FixtureListView) pages).setInFocus(false);
            }
        }
        super.onDestroyView();
        // listInstance.stopHanble();
        //Log.i(TAG, "DestroyView");
        if (view != null) {

            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }

        //timer.cancel();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initList();

    }

    public void setPositionDateOfPager() {
        Championship championshipByIdSelected = resourcesMetegol
                .getChampionshipByIdSelected();
        if (championshipByIdSelected == null)
            return;

        List<Date> dates = championshipByIdSelected.getDates();
        if (dates == null) {
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noData();
            return;
        }
        indexCurrentDates = championshipByIdSelected.getCurrentDateIndex();
    }

    private void blockedDatesMove(boolean blocked) {
        if (tvDateEarlier != null)
            tvDateEarlier.setEnabled(!blocked);
        if (tvDateLater != null)
            tvDateLater.setEnabled(!blocked);
    }

    private void prepareDatesNames() {
        if (dates == null)
            return;
        if (indexCurrentDates > 0) {
            tvDateLater.setVisibility(View.VISIBLE);
            tvDateLater.setText(dates.get(indexCurrentDates - 1).getName()
                    .toUpperCase());
        } else {
            tvDateLater.setVisibility(View.INVISIBLE);
        }
        if ((indexCurrentDates + 1) < dates.size()) {
            tvDateEarlier.setVisibility(View.VISIBLE);
            tvDateEarlier.setText(dates.get(indexCurrentDates + 1).getName()
                    .toUpperCase());
        } else {
            tvDateEarlier.setVisibility(View.INVISIBLE);
        }
        resourcesMetegol.setCurrentDate(dates.get(indexCurrentDates)
                .getName());
        tvDateCurrent.setText(dates.get(indexCurrentDates).getName()
                .toUpperCase());

    }

    private void showLoadingFixture() {
        ivPreloader.setVisibility(View.VISIBLE);
    }

    private void hideLoadingFixture() {
        ivPreloader.setVisibility(View.GONE);
    }

    public void onDataLoaded() {
        pager.setPagingEnabled(true);

        hideLoadingFixture();
        blockedDatesMove(false);
        setBlockSlidingMenu(false);
    }

    public void onDataFailed() {
        pager.setPagingEnabled(true);
        setBlockSlidingMenu(false);
        blockedDatesMove(false);
        hideLoadingFixture();
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).noConnection();
    }

    public void onDataStartToDownload() {
        pager.setPagingEnabled(false);
        blockedDatesMove(true);
        showLoadingFixture();
        setBlockSlidingMenu(true);

    }

    private class MyPagerAdapter extends PagerAdapter {

        public ArrayList<RelativeLayout> views;

        public MyPagerAdapter(Context context) {
            views = new ArrayList<RelativeLayout>();
            for (int indexDates = 0; indexDates < dates.size(); indexDates++) {
                listInstance = new FixtureListView(getActivity(), indexDates,
                        FixtureFragment.this);
                views.add(listInstance);
            }
        }

        @Override
        public void destroyItem(View view, int arg1, Object object) {
            ((ViewPager) view).removeView((RelativeLayout) object);
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View view, int position) {
            View myView = views.get(position);
            ((ViewPager) view).addView(myView);
            return myView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }

    /*
    public void createInterstitial(){

        interstitial = new InterstitialAd(this.getActivity().getApplicationContext());
        interstitial.setAdUnitId(adId);
        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
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
    }*/

}
