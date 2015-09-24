package com.pmovil.soccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.components.SoccerFieldComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.entities.FootBallGameFull;
import com.pmovil.soccer.entities.FootballGame;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TeamFragment extends FragmentBase implements OnClickListener {

    private static final String TAG = "TeamFragment";
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    ConnectionHandlerResponseBody handlerFootBallGame = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, response);
            try {
                FootBallGameFull footballGame = JsonParsers
                        .ParserFootBallGameFull(new JSONObject(response));

                resourcesMetegol.setFootballGame(footballGame
                        .getFootballGame());

               //tvStadium.setText(String.format("%s %s", getString(R.string.Stadium), resourcesMetegol.getGameSelected().getNameStadium()));
               //tvStadium.setText(String.format("%s %s", getString(R.string.Stadium), resourcesMetegol.getFootballGame().getStadium().getValue()));

                loadSoccerField(0);

                initViewItems();

                Activity act = getActivity();
                //if (act != null && act instanceof MainActivity)
                   // ((MainActivity) act).refreshMenu();

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
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private View view = null;
    private SoccerFieldComponent soccerField;
    private SeekBar seekBar;
    private TextView tvTeamName;
    private ImageView ivEmblemAway;
    private ImageView ivEmblemHome;
    private View ivLeft;
    private View ivRight;
    private TextView tvGoalAway;
    private TextView tvGoalHome;
    private TextView tvDateTitle;
    private TextView tvStadium = null;
    private TextView tvTime = null;
    private View loadingHome = null;
    private View loadingAway = null;
    private ImageButton suplentesButton = null;
    private int currentTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideLoading();
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());

        if (view != null)
            return view;
        view = inflater.inflate(R.layout.team_fragment, container, false);
        /*ivEmblemAway = (ImageView) view.findViewById(R.id.iv_emblem_team_away);
        ivEmblemHome = (ImageView) view.findViewById(R.id.iv_emblem_team_home);
        tvGoalAway = (TextView) view.findViewById(R.id.tv_goal_team_away);
        tvGoalHome = (TextView) view.findViewById(R.id.tv_goal_team_home);
        tvDateTitle = (TextView) view.findViewById(R.id.tv_date_title_fragment);
        ivRight = view.findViewById(R.id.team_view_right);
        tvStadium = (TextView) view.findViewById(R.id.tv_stadium);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        ivLeft = view.findViewById(R.id.team_view_left);
        loadingAway = view.findViewById(R.id.progressBarEmblemAway);
        loadingHome = view.findViewById(R.id.progressBarEmblemHome);*/
        soccerField = (SoccerFieldComponent) view
                .findViewById(R.id.soccerField);
        soccerField.setFragmentParent(this);
        /*seekBar = (SeekBar) view.findViewById(R.id.seek_change_team);
        tvTeamName = (TextView) view.findViewById(R.id.tv_team_name);*/
        suplentesButton = (ImageButton) view.findViewById(R.id.suplentesBtn);

        suplentesButton.setOnClickListener(this);
        suplentesButton.setTag(0);

//		viewBanner = inflater.inflate(R.layout.activity_content_frame, container, false);
//		containerBanner = (RelativeLayout) viewBanner.findViewById(R.id.container_banner);
//		containerBanner.setVisibility(View.GONE);

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

    private void initViewItems() {


        /*
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
                currentTime = 0;
                if (seekBar.getProgress() < 50) {
                    seekBar.setProgress(3);
                } else {
                    seekBar.setProgress(97);
                    currentTime = 1;
                }

                loadSoccerField();
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
        });*/

        FootballGame game = resourcesMetegol.getFootballGame();

        if (game != null) {
            String time = new String();
//			game.getStateEvent().setId(6);
            if (game.getStateEvent() != null
                    && (game.getStateEvent().getId() == 1 || game
                    .getStateEvent().getId() == 6)) {
                time = game.getTimeOfGame();
            } else {
                time = game.getTime();
                if (time.length() >= 8) {
                    time = time.substring(0, 5);
                }
            }
            //tvTime.setText(time);

        }
        /*
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
                loadSoccerField();
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
                loadSoccerField();
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
                loadSoccerField();
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        suplentesButton.setTag(0);
        suplentesButton.setImageDrawable(getResources().getDrawable(R.drawable.suplentes));
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);

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


    public void loadSoccerField(int currentTime) {

        List<Player> players;
        Team team = resourcesMetegol.getFootballGame().getTeams().get(currentTime);
        if ((Integer) suplentesButton.getTag() == 1) {
            //Show substitutes
            players = team.getPlayerSubstitutes();
        } else {
            //Show main
            players = team.getPlayers();
        }
        soccerField.createPlayersInFieldWithPlayersArray(players);
        this.currentTime = currentTime;

    }

    public String getTeamName (int currentTime){
        Team team = resourcesMetegol.getFootballGame().getTeams().get(currentTime);
        String teamName = team.getNameShort().toUpperCase();
        return teamName;
    }

    public void onClick(View v) {
        if (v == suplentesButton) {
            int tag = (Integer) suplentesButton.getTag();
            if (tag == 0) {
                tag = 1;
                suplentesButton.setImageDrawable(getResources().getDrawable(R.drawable.titulares));
            } else {
                tag = 0;
                suplentesButton.setImageDrawable(getResources().getDrawable(R.drawable.suplentes));
            }
            suplentesButton.setTag(tag);
            loadSoccerField(currentTime);
        }
    }

}
