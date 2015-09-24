package com.pmovil.soccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.components.AdapterGeneric;
import com.components.Item;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.entities.Statistic;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StatisticsFragment extends FragmentBase {

    private static final String TAG = "StaticsFragment";
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private View view = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol;
    private ListView lvStatistics = null;
    private TextView tvGoalAway = null;
    private TextView tvGoalHome = null;
    private ImageView ivEmblemAway = null;
    private TextView tvStatisticsStatusTeamAway = null;
    private TextView tvStatisticsStatusTeamHome = null;
    private ImageView ivEmblemHome = null;
    private List<Item> itemList = null;
    private View loadingAway = null;
    private View loadingHome = null;
    private JSONObject responseJSONStats = null;
    private AdView bannerAdView;

    private ConnectionHandlerResponseBody statisticsSummaryHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, "Summary Successfull " + response);

            try {
                resourcesMetegol.setStatisticSummaryList(
                        JsonParsers.ParserStatisticSummary(
                                new JSONObject(response), responseJSONStats, getActivity()
                        )
                );
                responseJSONStats = null;

                loadListAdapterAndViewsData();
            } catch (Exception e) {
                e.printStackTrace();

            }
            hideLoading();
            setBlockSlidingMenu(false);
        }

        public void onError(String response) {
            //Log.i(TAG, "Error summary" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            //loadStatus();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private ConnectionHandlerResponseBody statisticsHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, "Statistics Successfull " + response);

            try {

                responseJSONStats = new JSONObject(response);
                resourcesMetegol.setStatisticList(
                        JsonParsers.ParserStatistic(
                                responseJSONStats, getActivity()
                        )
                );

                connWsFutebol.getStatsSummaryGamePost(
                        statisticsSummaryHandler,
                        resourcesMetegol.getIdChampionshipSelected(),
                        resourcesMetegol.getGameSelectedId()
                );

            } catch (Exception e) {
                e.printStackTrace();
                hideLoading();
                setBlockSlidingMenu(false);
            }

        }

        public void onError(String response) {
            //Log.i(TAG, "error Statistics" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            //loadStatus();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private TextView tvDateTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideLoading();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                    .getInstance(getActivity());
        if (connWsFutebol == null)
            connWsFutebol = resourcesMetegol.getConnWsFutebol();
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.statistics_fragment, container, false);
        /*tvDateTitle = (TextView) view.findViewById(R.id.tv_date_title_fragment);
        tvGoalAway = (TextView) view.findViewById(R.id.tv_goals_away);
        tvGoalHome = (TextView) view.findViewById(R.id.tv_goals_home);
        ivEmblemAway = (ImageView) view.findViewById(R.id.iv_emblem_away);
        ivEmblemHome = (ImageView) view.findViewById(R.id.iv_emblem_home);
        tvStatisticsStatusTeamAway = (TextView) view
                .findViewById(R.id.tv_statistics_status_team_away_fragment);
        loadingAway = view.findViewById(R.id.progressBarEmblemAway);
        loadingHome = view.findViewById(R.id.progressBarEmblemHome);
        tvStatisticsStatusTeamHome = (TextView) view
                .findViewById(R.id.tv_statistics_status_team_home_fragment);*/
        lvStatistics = (ListView) view.findViewById(R.id.lv_statistics);

        //ADMOB
        bannerAdView = (AdView) view.findViewById(R.id.bannerView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("942EC2AA2A735DFBD71A9EEF70692CE7")
                .build();
        // Start loading the ad in the background.
        bannerAdView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkDataAndLoadAdapter();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            //((MainActivity) getActivity()).refreshMenu();
        }

    }

    private void checkDataAndLoadAdapter() {

        // if (resourcesMetegol.getStatisticList() == null) {
        connWsFutebol.getStatsGamePost(statisticsHandler,
                resourcesMetegol.getIdChampionshipSelected(),
                resourcesMetegol.getGameSelectedId());
        showLoading();
        setBlockSlidingMenu(true);
        // } else {
        // loadListAdapterAndViewsData();
        // }
    }

    /*
    private void loadStatus() {

        if (resourcesMetegol.getFootballGame() != null) {

            tvDateTitle.setText(resourcesMetegol.getFootballGame()
                    .getDate().toUpperCase());
            tvDateTitle.setVisibility(View.VISIBLE);
            List<Team> teams = resourcesMetegol.getFootballGame()
                    .getTeams();
            if (teams != null) {
                for (Team team : teams) {
                    int width = ivEmblemHome.getLayoutParams().width;
                    int height = ivEmblemHome.getLayoutParams().height;
                    String urlEmblem = null;
                    if (team.getHomeOrAway().equalsIgnoreCase("local")) {

                        tvStatisticsStatusTeamHome.setText(team
                                .getAbbreviation().toUpperCase());

                        if (team.getScore() > -1) {
                            tvGoalHome.setText("" + team.getScore());
                            tvGoalHome.setVisibility(View.VISIBLE);
                        } else {
                            tvGoalHome.setVisibility(View.INVISIBLE);

                        }
                        urlEmblem = team.getEmblem().replace(
                                ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                                "" + width + "x" + height);
                        imageLoader.displayImage(urlEmblem, ivEmblemHome,
                                options, new SimpleImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(
                                            String imageUri, View view) {
                                        super.onLoadingStarted(imageUri, view);
                                        loadingHome.setVisibility(View.VISIBLE);
                                        ((ImageView) view)
                                                .setImageDrawable(null);
                                    }

                                    @Override
                                    public void onLoadingCancelled(
                                            String imageUri, View view) {
                                        super.onLoadingCancelled(imageUri, view);
                                        loadingHome
                                                .setVisibility(View.INVISIBLE);

                                    }

                                    @Override
                                    public void onLoadingFailed(
                                            String imageUri, View view,
                                            FailReason failReason) {
                                        super.onLoadingFailed(imageUri, view,
                                                failReason);
                                        loadingHome
                                                .setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onLoadingComplete(
                                            String imageUri, View view,
                                            Bitmap loadedImage) {
                                        loadingHome
                                                .setVisibility(View.INVISIBLE);

                                        if (loadedImage != null) {
                                            ImageView imageView = (ImageView) view;
                                            boolean firstDisplay = !displayedImages
                                                    .contains(imageUri);
                                            if (firstDisplay) {
                                                FadeInBitmapDisplayer.animate(
                                                        imageView, 500);
                                                displayedImages.add(imageUri);
                                            }
                                        }
                                    }
                                }
                        );
                    } else {

                        tvStatisticsStatusTeamAway.setText(team
                                .getAbbreviation().toUpperCase());
                        if (team.getScore() > -1) {
                            tvGoalAway.setText("" + team.getScore());
                            tvGoalAway.setVisibility(View.VISIBLE);
                        } else {
                            tvGoalAway.setVisibility(View.INVISIBLE);

                        }
                        urlEmblem = team.getEmblem().replace(
                                ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                                "" + width + "x" + height);
                        imageLoader.displayImage(urlEmblem, ivEmblemAway,
                                options, new SimpleImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(
                                            String imageUri, View view) {
                                        super.onLoadingStarted(imageUri, view);
                                        loadingAway.setVisibility(View.VISIBLE);
                                        ((ImageView) view)
                                                .setImageDrawable(null);
                                    }

                                    @Override
                                    public void onLoadingCancelled(
                                            String imageUri, View view) {
                                        super.onLoadingCancelled(imageUri, view);
                                        loadingAway
                                                .setVisibility(View.INVISIBLE);

                                    }

                                    @Override
                                    public void onLoadingFailed(
                                            String imageUri, View view,
                                            FailReason failReason) {
                                        super.onLoadingFailed(imageUri, view,
                                                failReason);
                                        loadingAway
                                                .setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onLoadingComplete(
                                            String imageUri, View view,
                                            Bitmap loadedImage) {
                                        loadingAway
                                                .setVisibility(View.INVISIBLE);

                                        if (loadedImage != null) {
                                            ImageView imageView = (ImageView) view;
                                            boolean firstDisplay = !displayedImages
                                                    .contains(imageUri);
                                            if (firstDisplay) {
                                                FadeInBitmapDisplayer.animate(
                                                        imageView, 500);
                                                displayedImages.add(imageUri);
                                            }
                                        }
                                    }
                                }
                        );
                    }
                }
            }
        } else {
            tvDateTitle.setVisibility(View.INVISIBLE);

        }
    }*/

    private void loadListAdapterAndViewsData() {
        if (lvStatistics == null) {
            //Log.e(TAG, "lvStatistics == null");

            return;
        }
        lvStatistics.setDividerHeight(0);
        if (itemList == null)
            itemList = new ArrayList<Item>();
        itemList.clear();

        //loadStatus();

        List<Statistic> statisticList = new ArrayList<Statistic>();
        //statisticList.addAll(resourcesMetegol.getStatisticSummaryList());
        statisticList.addAll(resourcesMetegol.getStatisticList());

        if (statisticList == null) {
            //Log.e(TAG, "statisticList == null");
            return;
        }
        for (int indexStatitics = 0; indexStatitics < statisticList.size(); indexStatitics++) {
            Statistic statistic = statisticList.get(indexStatitics);
            statistic.setPos(indexStatitics);
            Item item = new ItemStatistics();
            item.setData(statistic);
            itemList.add(item);

        }

        //To display banner
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View footer = inflater.inflate(R.layout.listview_footer, null);
        lvStatistics.addFooterView(footer);

        if (lvStatistics.getAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) lvStatistics.getAdapter();
            adapter.setItemsList(itemList);
            adapter.notifyDataSetChanged();
        } else
            lvStatistics.setAdapter(new AdapterGeneric(getActivity(), itemList));

        //TODO
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
    }
}
