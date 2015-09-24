package com.pmovil.soccer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentBase extends Fragment {

    private Tracker tracker;
    private String activityId;
    private String fragmentId;
    private List<String> NameScreen = new ArrayList<String>();
    protected FragmentActivity myActivity;

    public FragmentBase() {
        hideLoading();
    }

    //FBARBIERI
    private static final Field sChildFragmentManagerField;
    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e("Error", e.getMessage());
        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        NameScreen = new ArrayList<String>();
        NameScreen.add(getString(R.string.GoogleAnalyticsBattles));
        NameScreen.add(getString(R.string.GoogleAnalyticsChangeTournament));
        NameScreen.add(getString(R.string.GoogleAnalyticsMatchInTwitter));
        NameScreen.add(getString(R.string.GoogleAnalyticsVideos));
        NameScreen.add(getString(R.string.GoogleAnalyticsNews));
        NameScreen.add(getString(R.string.GoogleAnalyticsTeams));
        NameScreen.add(getString(R.string.GoogleAnalyticsCalendar));
        NameScreen.add(getString(R.string.GoogleAnalyticsNotifications));
        NameScreen.add(getString(R.string.GoogleAnalyticsRealTime));
        NameScreen.add(getString(R.string.GoogleAnalyticsStatistics));
        NameScreen.add(getString(R.string.GoogleAnalyticsBets));
    }

    @Override
    public void onStart() {
        super.onStart();
        this.tracker = GoogleAnalytics.getInstance(getActivity()).getTracker(
                getString(R.string.ga_trackingId));
        this.fragmentId = getClass().getSimpleName();
        this.activityId = getActivity().getClass().getSimpleName();

        if (fragmentId.equalsIgnoreCase("BattlesFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsBattles);
        } else if (fragmentId.equalsIgnoreCase("ChangeTournamentFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsChangeTournament);
        } else if (fragmentId.equalsIgnoreCase("MatchTwitterFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsMatchInTwitter);
        } else if (fragmentId.equalsIgnoreCase("VideosFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsVideos);
        } else if (fragmentId.equalsIgnoreCase("NewsFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsNews);
        } else if (fragmentId.equalsIgnoreCase("TeamFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsTeams);
        } else if (fragmentId.equalsIgnoreCase("FixtureFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsCalendar);
        } else if (fragmentId.equalsIgnoreCase("SettingFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsNotifications);
        } else if (fragmentId.equalsIgnoreCase("MinToMinFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsRealTime);
        } else if (fragmentId.equalsIgnoreCase("StatisticsFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsStatistics);
        } else if (fragmentId.equalsIgnoreCase("BetFragment")) {
            fragmentId = getString(R.string.GoogleAnalyticsBets);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NameScreen.contains(fragmentId)) {
            EasyTracker easyTracker = EasyTracker.getInstance(getActivity());
            Map<String, String> map = MapBuilder.createAppView()
                    .set(Fields.SCREEN_NAME, this.fragmentId).build();
            easyTracker.send(map);
        }
    }

    protected void setTouchModeAboveofSlidingMenu(int touchMode) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof MainActivity) {
            MainActivity parent = (MainActivity) getActivity();
            SlidingMenu sm = parent.getSlidingMenu();
            if (sm != null)
                sm.setTouchModeAbove(touchMode);
        }
    }

    public void showLoading() {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof MainActivity) {
            MainActivity parent = (MainActivity) getActivity();
            parent.showLoading();
        }
    }

    public void hideLoading() {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof MainActivity) {
            MainActivity parent = (MainActivity) getActivity();
            parent.hideLoading();
        }
    }

    protected void setBlockSlidingMenu(boolean blocked) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof MainActivity) {
            MainActivity parent = (MainActivity) getActivity();
            parent.setBlockedSlidingMenu(blocked);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.i("FragmentBase", "DestroyView");
        View view = getView();
        if (view != null) {

            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Log.i("FragmentBase", "Destroy");
        View view = getView();
        if (view != null) {

            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    //FBARBIERI
    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myActivity = (FragmentActivity) activity;
    }

}
