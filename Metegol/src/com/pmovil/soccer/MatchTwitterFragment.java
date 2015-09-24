package com.pmovil.soccer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.components.AdapterGeneric;
import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.entities.FootballGame;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.TimeLine;
import com.pmovil.soccer.entities.TopMentionsTwitter;
import com.pmovil.soccer.entities.TopTweet;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.pmovil.soccer.net.ManagerConnection.ConnectionResponse;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MatchTwitterFragment extends FragmentBase {

    public static final int MATCH = 0;
    public static final int TIME_LINE = 1;
    public static final int STATISTICS = 2;
    private static final String TAG = "MatchTwitterFragment";
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    // private View containerTimeLineHeader = null;
    // private View containerMatchHeader = null;
    // private View containerStatisticsHeader = null;
    private List<Item> itemsList = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol ResourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsMetegol = null;
    private TextView tvMatch = null;
    private TextView tvTimeLine = null;
    private TextView tvStatistics = null;
    private View submenuLineMatch = null;
    private View submenuLineTimeLine = null;
    private View submenuLineStatistics = null;
    private Drawable submenuLineTimeLineDrawable = null;
    private Drawable submenuLineMatchDrawable = null;
    private Drawable submenuLineStatisticsDrawable = null;
    private View view = null;
    private ListView lvMatch = null;
    private ListView lvTimeLine = null;
    private ListView lvStatistics = null;
    private ViewFlow viewFlow;
    private int page = 0;
    private SeekBar seekBar;
    private View ivLeft;
    private View ivRight;
    private TextView tvDateTitle;
    private TextView tvTeamName;
    private ImageView ivEmblemAway;
    private ImageView ivEmblemHome;
    private TextView tvGoalAway;
    private TextView tvGoalHome;
    private TextView tvTime = null;
    private View loadingHome = null;
    private View loadingAway = null;
    private int currentTime = 0;
    private ConnectionHandlerResponseBody matchHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                viewFlow.getSelectedView().setVisibility(View.VISIBLE);
                ResourcesMetegol.getInstance(getActivity()).setTopTweets(
                        JsonParsers.ParserTopTweets(new JSONObject(response)));
                loadDataListAdapter();
                //Log.i(TAG, "Position Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
                viewFlow.getSelectedView().setVisibility(View.INVISIBLE);
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
            if (act != null && act instanceof MainActivity) {
                ((MainActivity) act).noConnection();
                viewFlow.getSelectedView().setVisibility(View.INVISIBLE);
            }
        }
    };
    private ConnectionHandlerResponseBody timeLineHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                viewFlow.getSelectedView().setVisibility(View.VISIBLE);
                ResourcesMetegol.getInstance(getActivity()).setTimeLines(
                        JsonParsers.ParserTimeLine(new JSONArray(response)));
                loadDataListAdapter();
                //Log.i(TAG, "Time Line Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
                viewFlow.getSelectedView().setVisibility(View.INVISIBLE);
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
            if (act != null && act instanceof MainActivity) {
                ((MainActivity) act).noConnection();

                viewFlow.getSelectedView().setVisibility(View.INVISIBLE);
            }
        }
    };

    public MatchTwitterFragment() {
        itemsList = new ArrayList<Item>();
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);

        switch (page) {
            default:

            case MATCH:
                viewFlow.setSelection(MATCH);
                break;
            case TIME_LINE:
                viewFlow.setSelection(TIME_LINE);
                break;
            case STATISTICS:
                viewFlow.setSelection(STATISTICS);
                break;

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        connWsMetegol = ResourcesMetegol.getConnWsFutebol();
        if (submenuLineMatchDrawable == null)
            submenuLineMatchDrawable = getResources().getDrawable(
                    R.drawable.selected_bar_position);// sub_menu_left);
        if (submenuLineTimeLineDrawable == null)
            submenuLineTimeLineDrawable = getResources().getDrawable(
                    R.drawable.selected_bar_huelguistas);
        if (submenuLineStatisticsDrawable == null)
            submenuLineStatisticsDrawable = getResources().getDrawable(
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

        initStatus();

        initSubmenu();
        checkDataAndLoadAdapter();

    }

    private void checkDataAndLoadAdapter() {

        switch (page) {
            case MATCH:
            default:
                if (ResourcesMetegol.getTopTweets() == null) {
                    connWsMetegol.getTopTwitterPost(matchHandler, ""
                            + ResourcesMetegol.getGameSelectedId(), "" + ResourcesMetegol.getIdChampionshipSelected());
                    showLoading();
                    setBlockSlidingMenu(true);
                } else
                    loadDataListAdapter();
                break;
            case TIME_LINE:
                if (ResourcesMetegol.getTimeLines() == null) {
                    connWsMetegol.getTimelineTwitterPost(timeLineHandler, ""
                            + ResourcesMetegol.getGameSelectedId());

                    showLoading();
                    setBlockSlidingMenu(true);
                } else
                    loadDataListAdapter();
                break;
            case STATISTICS:

                if (ResourcesMetegol.getTopMentionsTwitters() == null) {
                    new ConnectionStatistics().execute();
                } else
                    loadDataListAdapter();

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideLoading();

        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        }
        if (ResourcesMetegol == null) {
            ResourcesMetegol = ResourcesMetegol.getInstance(getActivity());
        }
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.match_tweeter_fragment, container,
                false);
        lvMatch = new ListView(getActivity());
        lvMatch.setCacheColorHint(Color.TRANSPARENT);

        lvTimeLine = new ListView(getActivity());
        lvTimeLine.setCacheColorHint(Color.TRANSPARENT);
        lvStatistics = new ListView(getActivity());
        lvStatistics.setCacheColorHint(Color.TRANSPARENT);
        lvStatistics.setDividerHeight(0);

        tvMatch = (TextView) view.findViewById(R.id.tv_match);
        tvTimeLine = (TextView) view.findViewById(R.id.tv_time_line);
        tvStatistics = (TextView) view.findViewById(R.id.tv_statistics);

        tvMatch.setText(getString(R.string.Match).toUpperCase());
        tvTimeLine.setText(getString(R.string.Timeline).toUpperCase());
        tvStatistics.setText(getString(R.string.Stats).toUpperCase());

        submenuLineMatch = view.findViewById(R.id.iv_submenu_line_match);
        submenuLineTimeLine = view.findViewById(R.id.iv_submenu_line_time_line);
        submenuLineStatistics = view
                .findViewById(R.id.iv_submenu_line_statistics);
        // containerMatchHeader =
        // view.findViewById(R.id.container_match_header);
        // containerTimeLineHeader = view
        // .findViewById(R.id.container_time_line_header);
        // containerStatisticsHeader = view
        // .findViewById(R.id.container_statistics_header);
        loadingAway = view.findViewById(R.id.progressBarEmblemAway);
        loadingHome = view.findViewById(R.id.progressBarEmblemHome);

        viewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        viewFlow.setAdapter(new Adapter());
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvDateTitle = (TextView) view.findViewById(R.id.tv_date_title_fragment);
        ivRight = view.findViewById(R.id.team_view_right);
        ivLeft = view.findViewById(R.id.team_view_left);
        ivEmblemAway = (ImageView) view.findViewById(R.id.iv_emblem_team_away);
        ivEmblemHome = (ImageView) view.findViewById(R.id.iv_emblem_team_home);
        tvGoalAway = (TextView) view.findViewById(R.id.tv_goal_team_away);
        tvGoalHome = (TextView) view.findViewById(R.id.tv_goal_team_home);
        tvTeamName = (TextView) view.findViewById(R.id.tv_team_name);
        seekBar = (SeekBar) view.findViewById(R.id.seek_change_team);

        return view;
    }

    @SuppressWarnings("deprecation")
    private void initSubmenu() {
        switch (page) {
            default:
            case MATCH:
                submenuLineMatch.setVisibility(View.VISIBLE);
                submenuLineStatistics.setVisibility(View.INVISIBLE);
                submenuLineTimeLine.setVisibility(View.INVISIBLE);
                submenuLineMatch.setBackgroundDrawable(submenuLineMatchDrawable);
                tvMatch.setTextColor(getResources().getColor(R.color.blue_selected));
                tvTimeLine.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvStatistics.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                // containerMatchHeader.setVisibility(View.VISIBLE);
                // containerTimeLineHeader.setVisibility(View.GONE);
                // containerStatisticsHeader.setVisibility(View.GONE);

                break;
            case TIME_LINE:
                submenuLineMatch.setVisibility(View.INVISIBLE);
                submenuLineStatistics.setVisibility(View.INVISIBLE);
                submenuLineTimeLine.setVisibility(View.VISIBLE);
                submenuLineMatch.setBackgroundDrawable(submenuLineTimeLineDrawable);
                tvMatch.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvStatistics.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvTimeLine.setTextColor(getResources().getColor(
                        R.color.blue_selected));
                // containerStatisticsHeader.setVisibility(View.GONE);
                //
                // containerMatchHeader.setVisibility(View.GONE);
                // containerTimeLineHeader.setVisibility(View.VISIBLE);
                break;
            case STATISTICS:
                submenuLineMatch.setVisibility(View.INVISIBLE);
                submenuLineStatistics.setVisibility(View.VISIBLE);
                submenuLineTimeLine.setVisibility(View.INVISIBLE);
                submenuLineMatch
                        .setBackgroundDrawable(submenuLineStatisticsDrawable);
                tvMatch.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvStatistics.setTextColor(getResources().getColor(
                        R.color.blue_selected));
                tvTimeLine.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                // containerMatchHeader.setVisibility(View.GONE);
                // containerTimeLineHeader.setVisibility(View.GONE);
                // containerStatisticsHeader.setVisibility(View.VISIBLE);

                break;

        }
    }

    private void initStatus() {

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
                currentTime = 0;
                if (seekBar.getProgress() < 50) {
                    seekBar.setProgress(3);
                } else {
                    seekBar.setProgress(97);
                    currentTime = 1;
                }

                if (ResourcesMetegol.getFootballGame().getTeams()
                        .get(currentTime).getNameShort() != null
                        && !ResourcesMetegol.getFootballGame().getTeams()
                        .get(currentTime).getNameShort()
                        .equalsIgnoreCase("")) {

                    tvTeamName.setText(ResourcesMetegol.getFootballGame()
                            .getTeams().get(currentTime).getNameShort()
                            .toUpperCase());

                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress >= 97) {
                    seekBar.setProgress(97);
                } else if (progress <= 3) {
                    seekBar.setProgress(3);

                }

            }
        });

        FootballGame game = ResourcesMetegol.getFootballGame();

        if (game != null) {

            tvTeamName.setText(game.getTeams().get(0).getNameShort()
                    .toUpperCase());
            String time = game.getTime();
            if (time.length() >= 8) {
                time = time.substring(0, 5);
            }
            tvTime.setText(time);

        }

        if (game != null) {
            tvDateTitle.setVisibility(View.VISIBLE);
            tvDateTitle.setText(game.getDate().toUpperCase());
        } else {
            tvDateTitle.setVisibility(View.INVISIBLE);

        }

        if (ResourcesMetegol.getFootballGame() != null) {
            List<Team> teams = ResourcesMetegol.getFootballGame().getTeams();
            if (teams != null) {
                for (Team team : teams) {
                    int width = ivEmblemHome.getLayoutParams().width;
                    int height = ivEmblemHome.getLayoutParams().height;
                    String urlEmblem = null;
                    if (team.getHomeOrAway().equalsIgnoreCase("local")) {
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
        }

        ivLeft.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (currentTime == 0) {
                    currentTime = 1;
                    seekBar.setProgress(97);
                } else {
                    currentTime = 0;
                    seekBar.setProgress(3);
                }
                if (ResourcesMetegol.getFootballGame().getTeams()
                        .get(currentTime).getNameShort() != null
                        && !ResourcesMetegol.getFootballGame().getTeams()
                        .get(currentTime).getNameShort()
                        .equalsIgnoreCase("")) {

                    tvTeamName.setText(ResourcesMetegol.getFootballGame()
                            .getTeams().get(currentTime).getNameShort()
                            .toUpperCase());

                }
            }
        });
        ivRight.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (currentTime == 0) {
                    currentTime = 1;
                    seekBar.setProgress(97);
                } else {
                    currentTime = 0;
                    seekBar.setProgress(3);
                }
                tvTeamName.setText(ResourcesMetegol.getFootballGame()
                        .getTeams().get(currentTime).getNameShort()
                        .toUpperCase());
            }
        });

        tvTeamName.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (currentTime == 0) {
                    currentTime = 1;
                    seekBar.setProgress(97);
                } else {
                    currentTime = 0;
                    seekBar.setProgress(3);
                }
                tvTeamName.setText(ResourcesMetegol.getFootballGame()
                        .getTeams().get(currentTime).getNameShort()
                        .toUpperCase());

            }
        });

    }

    private void generateItemMatchs() {
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();
        List<TopTweet> topTweets = ResourcesMetegol.getTopTweets();
        if (topTweets == null)
            return;
        TopTweet topTweetsArray[];
        int indexTopTweets = 0;
        String mentionsMax = "";
        if (!topTweets.isEmpty()) {
            mentionsMax = topTweets.get(0).getMentions();
        }
        while (indexTopTweets < topTweets.size()) {
            topTweets.get(indexTopTweets).setMentionsMax(mentionsMax);
            topTweetsArray = new TopTweet[2];
            topTweetsArray[0] = topTweets.get(indexTopTweets);
            if (topTweets.size() > ++indexTopTweets) {
                topTweets.get(indexTopTweets).setMentionsMax(mentionsMax);
                topTweetsArray[1] = topTweets.get(indexTopTweets);
            }
            Item item = new ItemMatchRowTwitter();
            item.setData(topTweetsArray);
            itemsList.add(item);
            indexTopTweets++;
        }
    }

    private void generateItemTimeLine() {
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();
        List<TimeLine> timeLines = ResourcesMetegol.getTimeLines();
        if (timeLines == null)
            return;
        for (int indexTeams = 0; indexTeams < timeLines.size(); indexTeams++) {
            TimeLine timeLine = timeLines.get(indexTeams);
            Item item = new ItemTimeLineTwitter();
            item.setData(timeLine);
            itemsList.add(item);

        }
    }

    private void loadDataListAdapter() {
        ListView listView = null;
        switch (page) {
            case MATCH:
                generateItemMatchs();
                listView = lvMatch;

                break;
            case TIME_LINE:
                generateItemTimeLine();
                listView = lvTimeLine;

                break;
            case STATISTICS:
                generateItemStatistics();
                listView = lvStatistics;

                break;

            default:
                generateItemMatchs();
                listView = lvMatch;

                break;
        }

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

    private void generateItemStatistics() {
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();
        List<TopMentionsTwitter> topMentionsTweeter = ResourcesMetegol
                .getTopMentionsTwitters();
        if (topMentionsTweeter == null)
            return;

        Item totals = new ItemStatisticsTotalsTweeter();
        totals.setData(ResourcesMetegol.getTotalesTweets());
        itemsList.add(totals);

        itemsList.add(new ItemStatisticsHeaderTweeter());
        String mentionMax = new String();
        if (!topMentionsTweeter.isEmpty()) {
            mentionMax = topMentionsTweeter.get(0).getMentions();
        }
        for (int indexTopMentionsTweeter = 0; indexTopMentionsTweeter < topMentionsTweeter
                .size(); indexTopMentionsTweeter++) {
            TopMentionsTwitter topMentionsTwitter = topMentionsTweeter
                    .get(indexTopMentionsTweeter);
            topMentionsTwitter.setMentionsMax(mentionMax);
            Item item = new ItemStatisticsTweeter();
            item.setData(topMentionsTwitter);
            itemsList.add(item);

        }

        // Item totals = new ItemStatisticsTotalsTweeter();
        // totals.setData(ResourcesMetegol.getTotalesTweets());
        // itemsList.add(totals);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        if (viewFlow != null) {
            switch (page) {
                case MATCH:
                    viewFlow.setSelection(MATCH);
                    break;
                case TIME_LINE:
                    viewFlow.setSelection(TIME_LINE);
                    break;
                case STATISTICS:
                    viewFlow.setSelection(STATISTICS);
                    break;

                default:
                    viewFlow.setSelection(MATCH);

                    break;
            }
            initSubmenu();

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
    }

    private class ConnectionStatistics extends AsyncTask<Void, Void, Void> {

        String topMentions = "";
        String totalTweeter = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
            setBlockSlidingMenu(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConnectionResponse connectionResponse = connWsMetegol
                    .getTopMentionsTwitter(
                            "" + ResourcesMetegol.getIdChampionshipSelected(),
                            "" + ResourcesMetegol.getGameSelectedId());
            if (connectionResponse.getBodyResponse() != null) {

                try {
                    topMentions = new String(
                            connectionResponse.getBodyResponse(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                connectionResponse = connWsMetegol.getTotalsTwitter(""
                        + ResourcesMetegol.getGameSelectedId());
                try {
                    totalTweeter = new String(
                            connectionResponse.getBodyResponse(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            setBlockSlidingMenu(false);
            hideLoading();
            if (topMentions.equalsIgnoreCase("")
                    || totalTweeter.equalsIgnoreCase("")) {
                Activity act = getActivity();
                if (act != null && act instanceof MainActivity) {
                    ((MainActivity) act).noConnection();
                    viewFlow.getSelectedView().setVisibility(View.INVISIBLE);
                }
                return;
            }

            if (ResourcesMetegol != null) {
                try {
                    viewFlow.getSelectedView().setVisibility(View.VISIBLE);
                    ResourcesMetegol
                            .setTopMentionsTwitters(JsonParsers
                                    .ParserTopMentionsTwitter(new JSONArray(
                                            topMentions)));
                    ResourcesMetegol.setTotalesTweets(JsonParsers
                            .ParserTotalesTweets(new JSONObject(totalTweeter)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            loadDataListAdapter();
        }

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
                case MATCH:
                    return lvMatch;
                case TIME_LINE:
                    return lvTimeLine;
                case STATISTICS:
                    return lvStatistics;
            }
        }

    }


}
