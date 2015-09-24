package com.pmovil.soccer.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.FragmentBase;
import com.pmovil.soccer.Interfaces.SlidingMenuListener;
import com.pmovil.soccer.MainActivity;
import com.pmovil.soccer.MinToMinFragment;
import com.pmovil.soccer.R;
import com.pmovil.soccer.StatisticsFragment;
import com.pmovil.soccer.TeamFragment;
import com.pmovil.soccer.entities.FootBallGameFull;
import com.pmovil.soccer.entities.FootballGame;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class GameFragment extends FragmentBase {

    private ViewPager viewpager_game = null;
    private static final String TAG = "GamesFragment";
    //private List<Item> itemList = null;
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private View view = null;
    private SeekBar seekBar;
    private TextView tvTeamName;
    private ImageView ivEmblemAway;
    private ImageView ivEmblemHome;
    private View ivLeft;
    private View ivRight;
    private TextView tvGoalAway;
    private TextView tvGoalHome;
    private TextView tvStadium = null;
    private TextView tvTime = null;
    private View loadingHome = null;
    private View loadingAway = null;
    private int currentTime = 0;
    private TextView tvDateTitle;
    private SlidingMenuListener slidingMenuCallback;
    private int touchMode;
    private TeamFragment teamFrag = null;
    private StatisticsFragment statFrag = null;
    private MinToMinFragment mintominFrag = null;
    private ResumenFragment resFrag = null;
    private static InterstitialAd interstitial;
    private static String adId = "ca-app-pub-7993377819754702/8233369076";
    private Timer timer = new Timer();
    public RelativeLayout containerBanner;

    private ManagerConnection.ConnectionHandlerResponseBody handlerFootBallGame = new ManagerConnection.ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, response);
            try {
                FootBallGameFull footballGame = JsonParsers
                        .ParserFootBallGameFull(new JSONObject(response));

                resourcesMetegol.setFootballGame(footballGame
                        .getFootballGame());

                tvStadium.setText(String.format("%s %s", getString(R.string.Stadium), resourcesMetegol.getGameSelected().getNameStadium()));
                //tvStadium.setText(String.format("%s %s", getString(R.string.Stadium), resourcesMetegol.getFootballGame().getStadium().getValue()));

                initViewItems();

                /*
                Activity act = getActivity();
                if (act != null && act instanceof MainActivity)
                    ((MainActivity) act).refreshMenu();*/

                //Log.i(TAG, "Fixture Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
                //Log.i(TAG, "Fixture Error");

            }
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());

        if (view != null)
            return view;

        view = inflater.inflate(R.layout.game_fragment, container, false);

        ivEmblemAway = (ImageView) view.findViewById(R.id.iv_emblem_team_away);
        ivEmblemHome = (ImageView) view.findViewById(R.id.iv_emblem_team_home);
        tvGoalAway = (TextView) view.findViewById(R.id.tv_goal_team_away);
        tvGoalHome = (TextView) view.findViewById(R.id.tv_goal_team_home);
        tvDateTitle = (TextView) view.findViewById(R.id.tv_date_title_fragment);
        ivRight = view.findViewById(R.id.team_view_right);
        tvStadium = (TextView) view.findViewById(R.id.tv_stadium);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        ivLeft = view.findViewById(R.id.team_view_left);
        loadingAway = view.findViewById(R.id.progressBarEmblemAway);
        loadingHome = view.findViewById(R.id.progressBarEmblemHome);
        seekBar = (SeekBar) view.findViewById(R.id.seek_change_team);
        tvTeamName = (TextView) view.findViewById(R.id.tv_team_name);
        viewpager_game = (ViewPager) view.findViewById(R.id.vp_game);
        viewpager_game.setAdapter(new SampleFragmentPagerAdapter());

        // Give the PagerSlidingTabStrip the ViewPager
        final PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewpager_game);

        //tabsStrip.setTypeface(Typeface.createFromAsset(this.getActivity().getAssets(), getString(R.string.font_name)), Typeface.NORMAL);

        viewpager_game.setOffscreenPageLimit(4);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                if(position==0)

                    touchMode = SlidingMenu.TOUCHMODE_FULLSCREEN;
                else
                    touchMode = SlidingMenu.TOUCHMODE_MARGIN;

                slidingMenuCallback.enableSlidingMenu(true, touchMode);

            }



        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {


                if (seekBar.getProgress() < 50) {
                    seekBar.setProgress(3);
                    currentTime = 0;
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);

                } else {

                    seekBar.setProgress(97);
                    currentTime = 1;
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);
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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity) {
            ((MainActivity) act).showIconMinByMin();
        }
        initViewItems();
        checkIfFootballGameExist();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshMenu();
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

        timer.cancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        slidingMenuCallback = (SlidingMenuListener) activity;
    }

    private void checkIfFootballGameExist() {

        // if (resourcesMetegol.getFootballGame() == null) {
        showLoading();
        com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                .getConnWsFutebol();
        connectionsWSMetegol.getFootballGamePost(handlerFootBallGame,
                resourcesMetegol.getIdChampionshipSelected(),
                resourcesMetegol.getGameSelectedId());

        // } else {
        // soccerField
        // .createPlayersInFieldWithPlayersArray(resourcesMetegol
        // .getFootballGame().getTeams().get(0).getPlayers());
        // }
    }

    private void initViewItems() {

        FootballGame game = resourcesMetegol.getFootballGame();

        if (game != null) {
            String time = new String();
//			game.getStateEvent().setId(6);
            if (game.getStateEvent() != null
                    && (game.getStateEvent().getId() == 1 || game
                    .getStateEvent().getId() == 6)) {
                time = game.getTimeOfGame();
            } else if (game.getStateEvent().getId() != 2){
                time = game.getTime();
                if (time.length() >= 8) {
                    time = time.substring(0, 5);
                }
            }else{
                time = game.getTime() + " " + game.getStateEvent().getValue().toUpperCase();
            }
            tvTime.setText(time);

        }

        if (game != null) {
            tvDateTitle.setVisibility(View.VISIBLE);
            tvDateTitle.setText(game.getDate().toUpperCase());
        } else {
            tvDateTitle.setVisibility(View.INVISIBLE);

        }

        seekBar.setProgress(3);

        if (resourcesMetegol.getFootballGame() != null) {
            List<Team> teams = resourcesMetegol.getFootballGame()
                    .getTeams();
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
                                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
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
                                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
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

                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                }
            }
        }

        ivLeft.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (currentTime == 0) {
                    currentTime = 1;
                    seekBar.setProgress(97);
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);

                } else {
                    currentTime = 0;
                    seekBar.setProgress(3);
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);
                }
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (currentTime == 0) {
                    currentTime = 1;
                    seekBar.setProgress(97);
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);
                } else {
                    currentTime = 0;
                    seekBar.setProgress(3);
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);
                }
            }
        });

        tvTeamName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (currentTime == 0) {
                    currentTime = 1;
                    seekBar.setProgress(97);
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);
                } else {
                    currentTime = 0;
                    seekBar.setProgress(3);
                    tvTeamName.setText(teamFrag.getTeamName(currentTime));
                    teamFrag.loadSoccerField(currentTime);
                }
            }
        });

    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[] { getResources().getString(R.string.tab_team),getResources().getString(R.string.tab_summary),getResources().getString(R.string.tab_statictics), getResources().getString(R.string.tab_live)};

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
                    if (teamFrag == null)
                        teamFrag = new TeamFragment();
                    return teamFrag;

                case 1:
                    if (resFrag == null)
                        resFrag = new ResumenFragment();
                    return resFrag;

                case 2:
                    if (statFrag == null)
                        statFrag = new StatisticsFragment();
                    return statFrag;

                case 3:
                    if (mintominFrag == null)
                      mintominFrag = new MinToMinFragment();
                    return mintominFrag;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

}
