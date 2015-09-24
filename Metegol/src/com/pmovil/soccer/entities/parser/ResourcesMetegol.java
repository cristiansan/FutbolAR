package com.pmovil.soccer.entities.parser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Data;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.FootballGame;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.MinToMinData;
import com.pmovil.soccer.entities.News;
import com.pmovil.soccer.entities.PlayerTweet;
import com.pmovil.soccer.entities.Positions;
import com.pmovil.soccer.entities.Scorers;
import com.pmovil.soccer.entities.SplashDynamic;
import com.pmovil.soccer.entities.Statistic;
import com.pmovil.soccer.entities.TimeLine;
import com.pmovil.soccer.entities.TopMentionsTwitter;
import com.pmovil.soccer.entities.TopTweet;
import com.pmovil.soccer.entities.TotalesTweets;
import com.pmovil.soccer.entities.Video;
import com.pmovil.soccer.net.CacheManager;
import com.pmovil.soccer.net.CacheManager.CacheType;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import twitter4j.auth.RequestToken;

public class ResourcesMetegol {

    public static final String VALUE_REPLACE_IMG_URL = "##RES##";
    public static final String VALUE_SCALE_INSIDE = "xxRf";
    public static final String VALUE_TYPE = "##type##";
    public static final String VALUE_PLAYER_NAME = "##playerName##";
    public static final String VALUE_IN_NAME = "##inName##";
    public static final String VALUE_TEAM = "##teamName##";
    public static final String VALUE_OFF_NAME = "##offName##";
    public static final String VALUE_KNOWN_IN_NAME = "##knownInName##";
    public static final String VALUE_KNOWN_OFF_NAME = "##knownOffName##";
    public static final String VALUE_RECEIVED_NAME = "##receivedName##";
    public static final String VALUE_ASSISTANCE_BY = "##assistanceBy##";
    public static final String VALUE_CATCHED_BY = "##catchedBy##";
    public static final String LANGUAGE_PORTUGUESE = "pt_BR";
    public static final String LANGUAGE_SPANISH = "es_ES";
    public static final String LANGUAGE_ENGLISH = "en_US";
    public static final String CHAMPIONCHIP_VALUE_ID = "championshipId";
    public static final String CURRENT_VALUE_ID = "matchId";
    public static final String GAME_SELECTED_ID = "gameID";
    public static final String TITLE_GAME_SELECTED_ID = "titlegameID";
    public static final String REDIRECTION_TEAM_ID = "redirectionTeamID";
    private static final String PREFRERENCE_GLOBAL_NAME = "preferenceGlobal";
    private static com.pmovil.soccer.entities.parser.ResourcesMetegol INSTANCE;
    private static RequestToken requestToken;
    private final com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol;
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final Editor editor;
    private int idChampionshipSelected = -1;
    private List<Championship> championships = null;
    private Fixture fixture = null;
    private String titleGame = new String();
    private int gameSelectedId = -1;
    private Positions positions = null;
    private Scorers scorers = null;
    private Game gameSelected = null;
    private MinToMinData minToMinData = null;
    private List<News> newsList = null;
    private List<Video> videoList = null;
    private FootballGame footballGame = null;
    private List<Statistic> statisticList = null;
    private List<Statistic> statisticSummaryList = null;
    private String currentDate = new String();
    private List<TimeLine> timeLines;
    private List<TopMentionsTwitter> topMentionsTwitters;
    private TotalesTweets totalesTweets = null;
    private List<TopTweet> topTweets;
    private List<PlayerTweet> playerTweet = null;
    private Set<Integer> idTeamsExistingOfNotification;
    private Set<Integer> idTeamsToAddNotification;
    private Set<Integer> idTeamsToRemoveNotification;

    private SplashDynamic splashDynamic;
    private String nameFragmenteToOpen;

    // private boolean redirectionTeam = false;

    private ResourcesMetegol(Context context) {
        connWsFutebol = new com.pmovil.soccer.net.ConnectionsWSMetegol(context);
        sharedPreferences = context.getSharedPreferences(
                PREFRERENCE_GLOBAL_NAME, 0);
        editor = sharedPreferences.edit();
        this.context = context;
        initImageLoader();
        // redirectionTeam = false;
        // changeLenguageApp(LANGUAGE_PORTUGUESE);
    }

    public static com.pmovil.soccer.entities.parser.ResourcesMetegol getInstance(Context context) {
        synchronized (com.pmovil.soccer.entities.parser.ResourcesMetegol.class) {
            if (INSTANCE == null) {
                synchronized (com.pmovil.soccer.entities.parser.ResourcesMetegol.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new com.pmovil.soccer.entities.parser.ResourcesMetegol(context);
                    }
                }
            }
        }
        return INSTANCE;
    }

    public static RequestToken getRequestToken() {
        return requestToken;
    }

    public static void setRequestToken(RequestToken requestToken) {
        com.pmovil.soccer.entities.parser.ResourcesMetegol.requestToken = requestToken;
    }

    public void changeLenguageApp(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources()
                .updateConfiguration(config, null);
    }

    public Set<Integer> getIdTeamsByNotification() {
        return idTeamsExistingOfNotification;
    }

    public void addIdTeamExistingOfNotification(Integer id) {
        if (idTeamsExistingOfNotification == null) {
            idTeamsExistingOfNotification = new HashSet<Integer>();
        }
        if (!idTeamsExistingOfNotification.contains(id)) {
            idTeamsExistingOfNotification.add(id);
        }

    }

    public void resetListIdNotification() {
        if (idTeamsExistingOfNotification != null) {
            idTeamsExistingOfNotification.clear();
        }
        if (idTeamsToAddNotification != null) {
            idTeamsToAddNotification.clear();
        }
        if (idTeamsToRemoveNotification != null) {
            idTeamsToRemoveNotification.clear();
        }
        idTeamsExistingOfNotification = new HashSet<Integer>();
        idTeamsToAddNotification = new HashSet<Integer>();
        idTeamsToRemoveNotification = new HashSet<Integer>();
    }

    private void createListIdTeamsNotification() {
        if (idTeamsExistingOfNotification == null) {
            idTeamsExistingOfNotification = new HashSet<Integer>();
        }
        if (idTeamsToAddNotification == null) {
            idTeamsToAddNotification = new HashSet<Integer>();
        }
        if (idTeamsToRemoveNotification == null) {
            idTeamsToRemoveNotification = new HashSet<Integer>();
        }
    }

    public void addIdTeamByNotification(Integer id) {
        createListIdTeamsNotification();
        if (idTeamsToRemoveNotification.contains(id)) {
            idTeamsToRemoveNotification.remove(id);
            idTeamsExistingOfNotification.add(id);
        } else if (!idTeamsExistingOfNotification.contains(id)
                && !idTeamsToAddNotification.contains(id)) {
            idTeamsToAddNotification.add(id);
        }

    }

    public void removeIdTeamByNotification(Integer id) {
        createListIdTeamsNotification();
        if (idTeamsExistingOfNotification.contains(id)) {
            idTeamsExistingOfNotification.remove(id);
            idTeamsToRemoveNotification.add(id);
        } else if (idTeamsToAddNotification.contains(id)) {
            idTeamsToAddNotification.remove(id);
        }
    }

    public Championship getChampionshipByIdSelected() {
        if (championships == null) {

            championships = JsonParsers.ParserChampionships(CacheManager
                    .getInstance(context).getCacheJSON(
                            CacheType.CACHE_CHAMPIONSHIP));
            if (championships == null)
                return null;

        }

        int id = getIdChampionshipSelected();
        Data data = new Data();
        data.setId(id);
        if (championships.contains(data))
            return championships.get(championships.indexOf(data));
        else if (!championships.isEmpty())
            return championships.get(0);
        else
            return null;
    }

    public void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public void clearGameSimple() {
        if (footballGame != null)
            footballGame.clear();
        footballGame = null;
        if (gameSelected != null)
            gameSelected.clear();
        gameSelected = null;
        if (minToMinData != null)
            minToMinData.clear();
        if (statisticList != null)
            statisticList.clear();
        if (timeLines != null) {
            timeLines.clear();
        }
        timeLines = null;
        if (topMentionsTwitters != null) {
            topMentionsTwitters.clear();
        }
        topMentionsTwitters = null;
        totalesTweets = null;
        if (topTweets != null) {
            topTweets.clear();
        }
        topTweets = null;
        if (playerTweet != null) {
            playerTweet.clear();
        }
        playerTweet = null;
        statisticList = null;
        minToMinData = null;
        gameSelected = null;
        fixture = null;
    }

    public void clearGame() {
        if (footballGame != null)
            footballGame.clear();
        footballGame = null;
        if (gameSelected != null)
            gameSelected.clear();
        gameSelected = null;
        if (minToMinData != null)
            minToMinData.clear();
        if (statisticList != null)
            statisticList.clear();
        if (timeLines != null) {
            timeLines.clear();
        }
        timeLines = null;
        if (topMentionsTwitters != null) {
            topMentionsTwitters.clear();
        }
        topMentionsTwitters = null;
        totalesTweets = null;
        if (topTweets != null) {
            topTweets.clear();
        }
        topTweets = null;
        if (playerTweet != null) {
            playerTweet.clear();
        }
        playerTweet = null;
        statisticList = null;
        minToMinData = null;
        fixture = null;
        gameSelectedId = -1;
        editor.putInt(GAME_SELECTED_ID, -1);
        editor.commit();

    }

    public void clearResources() {
        clearGame();
        // if (championships != null)
        // championship s.clear();
        if (newsList != null)
            newsList.clear();
        if (videoList != null)
            videoList.clear();
        positions = null;
        scorers = null;

    }

    public List<Championship> getChampionships() {
        if (championships == null) {
            championships = JsonParsers.ParserChampionships(CacheManager
                    .getInstance(context).getCacheJSON(
                            CacheType.CACHE_CHAMPIONSHIP));
            if (championships == null)
                return null;

        }

        return championships;
    }

    public void setChampionships(List<Championship> championships) {
        this.championships = championships;
    }

    public Fixture getFixture() {
        if (fixture == null) {
            fixture = JsonParsers.ParserFixture(CacheManager.getInstance(
                    context).getCacheJSON(CacheType.CACHE_FIXTURE));
            if (fixture == null)
                return null;

        }
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public int getIdChampionshipSelected() {
        if (idChampionshipSelected == -1)
            idChampionshipSelected = sharedPreferences.getInt(
                    CHAMPIONCHIP_VALUE_ID, -1);
        return idChampionshipSelected;
    }

    public void setIdChampionshipSelected(int idChampionshipSelected) {
        editor.putInt(CHAMPIONCHIP_VALUE_ID, idChampionshipSelected);
        editor.commit();
        this.idChampionshipSelected = idChampionshipSelected;
    }

    public com.pmovil.soccer.net.ConnectionsWSMetegol getConnWsFutebol() {
        return connWsFutebol;
    }

    public Positions getPositions() {
        return positions;
    }

    public void setPositions(Positions positions) {
        this.positions = positions;
    }

    public Scorers getScorers() {
        return scorers;
    }

    public void setScorers(Scorers scorers) {
        this.scorers = scorers;
    }

    public Game getGameSelected() {
        if (gameSelected == null) {
            if (getGameSelectedId() == -1)
                return null;
            try {
                Fixture fixture = getFixture();
                List<Game> game = fixture.getDates().getMatchs();
                Data data = new Data();
                data.setId(getGameSelectedId());
                if (game.contains(data))
                    gameSelected = game.get(game.indexOf(data));
            } catch (Exception e) {
            }
        }
        return gameSelected;
    }

    public void setGameSelected(Game gameSelected) {
        this.gameSelected = gameSelected;
    }

    public MinToMinData getMinToMinData() {
        return minToMinData;
    }

    public void setMinToMinData(MinToMinData minToMinData) {
        this.minToMinData = minToMinData;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public FootballGame getFootballGame() {
        return footballGame;
    }

    public void setFootballGame(FootballGame footballGame) {
        this.footballGame = footballGame;
    }

    public List<Statistic> getStatisticList() {
        return statisticList;
    }

    public void setStatisticList(List<Statistic> statisticList) {
        this.statisticList = statisticList;
    }

    public List<Statistic> getStatisticSummaryList() {
        return statisticSummaryList;
    }

    public void setStatisticSummaryList(List<Statistic> statisticSummaryList) {
        this.statisticSummaryList = statisticSummaryList;
    }

    public String getCurrentDate() {
        if (currentDate == null)
            currentDate = sharedPreferences.getString(CURRENT_VALUE_ID, "");
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        editor.putString(CURRENT_VALUE_ID, currentDate);
        editor.commit();
    }

    public int getGameSelectedId() {
        if (gameSelectedId == -1)
            gameSelectedId = sharedPreferences.getInt(GAME_SELECTED_ID, -1);
        return gameSelectedId;
    }

    public void setGameSelectedId(int gameSelectedId) {
        editor.putInt(GAME_SELECTED_ID, gameSelectedId);
        editor.commit();
        this.gameSelectedId = gameSelectedId;
    }

    public String getTitleGame() {
        if (titleGame == null)
            titleGame = sharedPreferences.getString(TITLE_GAME_SELECTED_ID,
                    null);
        return titleGame;
    }

    public void setTitleGame(String titleGame) {
        editor.putString(TITLE_GAME_SELECTED_ID, titleGame);
        editor.commit();
        this.titleGame = titleGame;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public Editor getEditor() {
        return editor;
    }

    public Set<Integer> getIdTeamsToAddNotification() {
        return idTeamsToAddNotification;
    }

    public Set<Integer> getIdTeamsToRemoveNotification() {
        return idTeamsToRemoveNotification;
    }

    // public boolean isRedirectionTeam() {
    // return redirectionTeam;
    // }
    //
    // public void setRedirectionTeam(boolean redirectionTeam) {
    // this.redirectionTeam = redirectionTeam;
    // }

    public boolean isRedirectionTeam() {
        return sharedPreferences.getBoolean(REDIRECTION_TEAM_ID, false);
    }

    public void setRedirectionTeam(boolean redirectionTeam) {
        editor.putBoolean(REDIRECTION_TEAM_ID, redirectionTeam);
        editor.commit();
    }

    public List<TimeLine> getTimeLines() {
        return timeLines;
    }

    public void setTimeLines(List<TimeLine> timeLines) {
        this.timeLines = timeLines;
    }

    public List<TopMentionsTwitter> getTopMentionsTwitters() {
        return topMentionsTwitters;
    }

    public void setTopMentionsTwitters(
            List<TopMentionsTwitter> topMentionsTwitters) {
        this.topMentionsTwitters = topMentionsTwitters;
    }

    public List<TopTweet> getTopTweets() {
        return topTweets;
    }

    public void setTopTweets(List<TopTweet> topTweets) {
        this.topTweets = topTweets;
    }

    public TotalesTweets getTotalesTweets() {
        return totalesTweets;
    }

    public void setTotalesTweets(TotalesTweets totalesTweets) {
        this.totalesTweets = totalesTweets;
    }

    public List<PlayerTweet> getPlayerTweet() {
        return playerTweet;
    }

    public void setPlayerTweet(List<PlayerTweet> playerTweet) {
        this.playerTweet = playerTweet;
    }

    public SplashDynamic getSplashDynamic() {
        return splashDynamic;
    }

    public void setSplashDynamic(SplashDynamic splashDynamic) {
        this.splashDynamic = splashDynamic;
    }

    public String getNameFragmenteToOpen() {
        return nameFragmenteToOpen;
    }

    public void setNameFragmenteToOpen(String nameFragmenteToOpen) {
        this.nameFragmenteToOpen = nameFragmenteToOpen;
    }

}
