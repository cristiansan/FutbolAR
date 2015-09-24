package com.pmovil.soccer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.components.AdapterGeneric;
import com.components.Item;
import com.components.TextViewPlus;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.Positions;
import com.pmovil.soccer.entities.Scorers;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class PositionsScorersAverageFragment extends FragmentBase {

    public static final int POSITION = 0;
    public static final int SCORERS = 1;
    public static final int AVERAGE = 2;
    private static final String TAG = "PositionsScorersFragment";
    private List<Item> itemsList = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = null;
    private TextViewPlus tvPosition = null;
    private TextViewPlus tvScorers = null;
    private TextViewPlus tvAverage = null;
    private View containerScorersHeader = null;
    private View containerPositionsHeader = null;
    private View containerAverageHeader = null;
    private View submenuLinePosition = null;
    private View submenuLineScore = null;
    private View submenuLineAverage = null;
    private Drawable submenuLineScorersDrawable = null;
    private Drawable submenuLinePositionDrawable = null;
    private Drawable submenuLineAverageDrawable = null;
    private View view = null;
    private ListView lvPosition = null;
    private ListView lvScorers = null;
    private ListView lvAverage = null;
    private ViewFlow viewFlow;
    private int page = 0;
    private InterstitialAd interstitial;
    public RelativeLayout containerBanner;
    private static String adId = "ca-app-pub-7993377819754702/8233369076";
    private Timer timer;

    private TextView tvNombreActual;
    private TextView tvNombreAnterior1;
    private TextView tvNombreAnterior2;

    private Tracker tracker;
    private ConnectionHandlerResponseBody positionHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).setPositions(
                        JsonParsers.ParserPosition(new JSONObject(response)));
                loadDataListAdapter();
                //Log.i(TAG, "Position Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
            hideLoading();
            setBlockSlidingMenu(false);
        }

        public void onError(String response) {
            //Log.i(TAG, "error Fixture" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private ConnectionHandlerResponseBody scorersHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).setScorers(
                        JsonParsers.ParserScorers(new JSONObject(response)));
                loadDataListAdapter();
                //Log.i(TAG, "Scorers Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
            hideLoading();
            setBlockSlidingMenu(false);
        }

        public void onError(String response) {
            //Log.i(TAG, "error Fixture" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private boolean isX;

    public PositionsScorersAverageFragment() {

        itemsList = new ArrayList<Item>();
    }

    @Override
    public void onResume() {
        super.onResume();

        String nameOfScreenSelected;

        Activity act = getActivity();
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);

        switch (page) {
            default:

            case POSITION:
                viewFlow.setSelection(POSITION);
                break;
            case SCORERS:
                viewFlow.setSelection(SCORERS);
                break;
            case AVERAGE:
                viewFlow.setSelection(AVERAGE);
                break;

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());
        connWsFutebol = resourcesMetegol.getConnWsFutebol();
        if (submenuLinePositionDrawable == null)
            submenuLinePositionDrawable = getResources().getDrawable(
                    R.drawable.selected_bar_position);// sub_menu_left);
        if (submenuLineScorersDrawable == null)
            submenuLineScorersDrawable = getResources().getDrawable(
                    R.drawable.selected_bar_huelguistas);
        if (submenuLineAverageDrawable == null)
            submenuLineAverageDrawable = getResources().getDrawable(
                    R.drawable.selected_bar_average);
        viewFlow.setOnViewSwitchListener(new ViewSwitchListener() {

            public void onSwitched(View view, int pos) {

                if (page <= 2) {

                    page = pos;
                }
                initSubmenu();
                checkDataAndLoadAdapter();

            }
        });

        // setClickMenu();
        initSubmenu();
        checkDataAndLoadAdapter();

    }

    private void checkDataAndLoadAdapter() {

        switch (page) {
            case POSITION:
            case AVERAGE:
            default:

                if (resourcesMetegol.getPositions() == null) {
                    connWsFutebol.getPositionPost(positionHandler,
                            resourcesMetegol.getIdChampionshipSelected());
                    showLoading();
                    setBlockSlidingMenu(true);
                } else
                    loadDataListAdapter();
                break;
            case SCORERS:
                if (resourcesMetegol.getScorers() == null) {
                    connWsFutebol.getScoresPost(scorersHandler,
                            resourcesMetegol.getIdChampionshipSelected());
                    showLoading();
                    setBlockSlidingMenu(true);
                } else
                    loadDataListAdapter();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createInterstitial();

        hideLoading();
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.positions_scorers_average_fragment,
                container, false);
        lvPosition = new ListView(getActivity());
        lvPosition.setCacheColorHint(Color.TRANSPARENT);

        lvScorers = new ListView(getActivity());
        lvScorers.setCacheColorHint(Color.TRANSPARENT);
        lvAverage = new ListView(getActivity());
        lvAverage.setCacheColorHint(Color.TRANSPARENT);
        tvPosition = (TextViewPlus) view.findViewById(R.id.tv_positions);
        tvScorers = (TextViewPlus) view.findViewById(R.id.tv_scorers);
        tvAverage = (TextViewPlus) view.findViewById(R.id.tv_average);
        submenuLinePosition = view.findViewById(R.id.iv_submenu_line_position);
        submenuLineScore = view.findViewById(R.id.iv_submenu_line_score);
        submenuLineAverage = view.findViewById(R.id.iv_submenu_line_average);
        containerPositionsHeader = view
                .findViewById(R.id.container_position_header);
        containerScorersHeader = view
                .findViewById(R.id.container_scorers_header);
        containerAverageHeader = view
                .findViewById(R.id.container_average_header);

        tvNombreActual = (TextView) view.findViewById(R.id.tv_tableColumn_3);
        tvNombreAnterior1 = (TextView) view.findViewById(R.id.tv_tableColumn_2);
        tvNombreAnterior2 = (TextView) view.findViewById(R.id.tv_tableColumn_1);

        viewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        viewFlow.setAdapter(new Adapter());

//        displayInterstitial();

        return view;
    }

    @SuppressWarnings("deprecation")
    private void initSubmenu() {
        switch (page) {
            default:
            case POSITION:
                submenuLinePosition.setVisibility(View.VISIBLE);
                submenuLineAverage.setVisibility(View.INVISIBLE);
                submenuLineScore.setVisibility(View.INVISIBLE);
                submenuLinePosition
                        .setBackgroundDrawable(submenuLinePositionDrawable);
                tvPosition.setTextColor(getResources().getColor(
                        R.color.blue_selected));
                tvScorers.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvAverage.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                containerPositionsHeader.setVisibility(View.VISIBLE);
                containerScorersHeader.setVisibility(View.GONE);
                containerAverageHeader.setVisibility(View.GONE);

                tvPosition.setCustomFont(getActivity(), getString(R.string.font_name_bold));
                tvScorers.setCustomFont(getActivity(), getString(R.string.font_name));
                tvAverage.setCustomFont(getActivity(), getString(R.string.font_name));

                break;
            case SCORERS:
                submenuLinePosition.setVisibility(View.INVISIBLE);
                submenuLineAverage.setVisibility(View.INVISIBLE);
                submenuLineScore.setVisibility(View.VISIBLE);
                submenuLinePosition
                        .setBackgroundDrawable(submenuLineScorersDrawable);
                tvPosition.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvAverage.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvScorers.setTextColor(getResources().getColor(
                        R.color.blue_selected));
                containerAverageHeader.setVisibility(View.GONE);

                containerPositionsHeader.setVisibility(View.GONE);
                containerScorersHeader.setVisibility(View.VISIBLE);

                tvPosition.setCustomFont(getActivity(), getString(R.string.font_name));
                tvScorers.setCustomFont(getActivity(), getString(R.string.font_name_bold));
                tvAverage.setCustomFont(getActivity(), getString(R.string.font_name));
                break;
            case AVERAGE:
                submenuLinePosition.setVisibility(View.INVISIBLE);
                submenuLineAverage.setVisibility(View.VISIBLE);
                submenuLineScore.setVisibility(View.INVISIBLE);
                submenuLinePosition
                        .setBackgroundDrawable(submenuLineAverageDrawable);
                tvPosition.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvAverage.setTextColor(getResources().getColor(
                        R.color.blue_selected));
                tvScorers.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                containerPositionsHeader.setVisibility(View.GONE);
                containerScorersHeader.setVisibility(View.GONE);
                containerAverageHeader.setVisibility(View.VISIBLE);

                tvPosition.setCustomFont(getActivity(), getString(R.string.font_name));
                tvScorers.setCustomFont(getActivity(), getString(R.string.font_name));
                tvAverage.setCustomFont(getActivity(), getString(R.string.font_name_bold));

                break;

        }
    }

    private void generateItemPositions() {
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();
        Positions positions = resourcesMetegol.getPositions();
        if (positions == null)
            return;
        for (int indexTeams = 0; positions.getTeams() != null
                && indexTeams < positions.getTeams().size(); indexTeams++) {
            Team team = positions.getTeams().get(indexTeams);
            team.setPosition(indexTeams + 1);
            Item item = new ItemPosition();
            item.setData(team);
            itemsList.add(item);

        }
    }

    // private void generateItemAverage() {
    // if (itemsList == null)
    // itemsList = new ArrayList<Item>();
    // itemsList.clear();
    // Positions positions = resourcesMetegol.getPositions();
    // if (positions == null)
    // return;
    // for (int indexTeams = 0; positions.getTeams() != null
    // && indexTeams < positions.getTeams().size(); indexTeams++) {
    // Team team = positions.getTeams().get(indexTeams);
    // team.setPosition(indexTeams + 1);
    // ItemAverage item = new ItemAverage();
    // item.setTotalPosition(positions.getTeams().size());
    // item.setData(team);
    // itemsList.add(item);
    //
    // }
    //
    // Collections.sort(itemsList, new Comparator<Item>() {
    //
    // public int compare(Item lhs, Item rhs) {
    //
    // Team team1 = (Team) lhs.getData();
    // Team team2 = (Team) rhs.getData();
    // return
    // Double.compare(team2.getAverageRelegation(),team1.getAverageRelegation()
    // );
    //
    // }
    // });
    // }

    private void generateItemScorers() {
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();
        Scorers scorers = resourcesMetegol.getScorers();
        if (scorers == null)
            return;
        if (scorers.getPlayers() == null)
            return;
        for (int indexTeams = 0; indexTeams < scorers.getPlayers().size(); indexTeams++) {
            Player player = scorers.getPlayers().get(indexTeams);
            player.setPosition(indexTeams);
            Item item = new ItemScorers();
            item.setData(player);
            itemsList.add(item);

        }
    }

    private void loadDataListAdapter() {
        String nameOfScreenSelected = null;
        this.tracker = GoogleAnalytics.getInstance(getActivity()).getTracker(
                getString(R.string.ga_trackingId));
        ListView listView = null;
        switch (page) {
            case POSITION:
                generateItemPositions();
                listView = lvPosition;
                nameOfScreenSelected = getString(R.string.GoogleAnalyticsPositions);
                break;
            case SCORERS:
                generateItemScorers();
                listView = lvScorers;
                nameOfScreenSelected = getString(R.string.GoogleAnalyticsStrikers);
                break;
            case AVERAGE:
                generateItemAverage();
                listView = lvAverage;
                nameOfScreenSelected = getString(R.string.GoogleAnalyticsAverage);
                break;

            default:
                generateItemPositions();
                listView = lvPosition;

                break;
        }

        Map<String, String> map = MapBuilder.createAppView()
                .set(Fields.SCREEN_NAME, nameOfScreenSelected).build();
        this.tracker.send(map);

        if (itemsList.isEmpty()) {
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noData();
        }

        if (listView.getAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) listView.getAdapter();
            adapter.setItemsList(itemsList);
            adapter.notifyDataSetChanged();
            listView.refreshDrawableState();
            //Log.i(TAG, "Update");
        } else
            listView.setAdapter(new AdapterGeneric(getActivity(), itemsList));
    }

    private void generateItemAverage() {
        Positions positions = resourcesMetegol.getPositions();
        if (positions == null || positions.getTeams() == null) {
            return;
        }
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();

        TreeSet<Double> set = new TreeSet<Double>(new SortByAverage());

        for (int indexTeams = 0; positions.getTeams() != null
                && indexTeams < positions.getTeams().size(); indexTeams++) {

            set.add(positions.getTeams().get(indexTeams).getAverageRelegation());
        }

        tvNombreActual.setText(positions.getTeams().get(0).getNombreActual());
        tvNombreAnterior1.setText(positions.getTeams().get(0).getNombreAnterior1());
        tvNombreAnterior2.setText(positions.getTeams().get(0).getNombreAnterior2());

        Double minPoints = (double) 0;
        if (set.size() - 3 >= 0) {
            minPoints = (Double) set.toArray()[2];
        }

        ItemAverage.setMinAverage(minPoints);
        List<Team> list = null;
        if (positions.getTeams() != null) {
            list = new ArrayList<Team>(positions.getTeams());
            Collections.sort(list, new Comparator<Team>() {

                public int compare(Team lhs, Team rhs) {

                    return Double.compare(rhs.getAverageRelegation(),
                            lhs.getAverageRelegation());

                }
            });
        }

        for (int indexTeams = 0; list != null && indexTeams < list.size(); indexTeams++) {
            Team team = list.get(indexTeams);
            team.setPosition(indexTeams + 1);
            ItemAverage item = new ItemAverage();
            item.setTotalPosition(positions.getTeams().size());
            item.setData(team);
            itemsList.add(item);

        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        if (viewFlow != null) {
            switch (page) {
                case POSITION:
                    viewFlow.setSelection(POSITION);
                    break;
                case SCORERS:
                    viewFlow.setSelection(SCORERS);
                    break;
                case AVERAGE:
                    viewFlow.setSelection(AVERAGE);
                    break;

                default:
                    viewFlow.setSelection(POSITION);

                    break;
            }
            initSubmenu();

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.i(TAG, "DestroyView");
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
        if (timer != null)
            timer.cancel();
    }

    private class Adapter extends BaseAdapter {

        public int getCount() {
            return 3;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                default:
                case POSITION:
                    return lvPosition;
                case SCORERS:
                    return lvScorers;
                case AVERAGE:
                    return lvAverage;
            }
        }

    }

    public class SortByAverage implements Comparator<Double> {
        public int compare(Double n1, Double n2) {
            return n1.compareTo(n2);
        }
    }

    public void createInterstitial() {

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
        timer = new Timer();
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

}
