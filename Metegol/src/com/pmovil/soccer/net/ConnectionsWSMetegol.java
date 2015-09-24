package com.pmovil.soccer.net;

import android.content.Context;

import com.pmovil.soccer.R;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.pmovil.soccer.net.ManagerConnection.ConnectionResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class ConnectionsWSMetegol {

    public static final String URL_BASE_POST = "http://fut-df.srv.pmovil.net/";
    //public static final String URL_BASE_POST = "http://fut-df.srv.homolog.pmovil.net/";

    public static final String URLCALLBACK_VALUE = "http://end";
    public static final String COUNTRY_VALUE = "AR";
    public static final String MSISDN_VALUE = "msisdn";
    public static final String SESSION_ID_VALUE = "sessionID";
    public static final String CELCO_VALUE = "celco";
    public static final String DATE_VALUE = "date";
    public static final String SOCIAL_NETWORK_FACEBOOK = "FACEBOOK";
    public static final String SOCIAL_NETWORK_TWITTER = "TWITTER";
    //private static final String URL_BASE = "http://fut-df.srv.corp.pmovil.net:8084/";

    // public static final String URL_BASE_POST =
    // "http://fut-df.srv.corp.pmovil.net:8084/"; // Testing

    // private static final String URL_BASE_SUBSCRIPTION_SERVICE =
    // "http://mpp.srv.corp.pmovil.net:8084/";// Testing
    private static final String ID_PAIS = "AR";
    private static final String PLATAFORM_VALUE = "ANDROID";
    private static final String APP_NAME_VALUE = "METEGOL";
    private static final String APP_ID_VALUE = "METEGOL_AR";
    private static final String CERT_VALUE = "PRODUCTION";
    private static final String ALERT_VALUE = "default";
    private static final int ini = 0; // Start of News or Video
    private static final int qtd = 10;// Amount of News or Video
    private static final String URL_BASE_SUBSCRIPTION_SERVICE = "http://mpp.srv.pmovil.net/";// Production
    //private static final String URL_BASE_SUBSCRIPTION_SERVICE = "http://fut-df.srv.homolog.pmovil.net/";
    private static final String START_BUYING_ACTION = "startBuying";
    private static final String CHECK_BUYING_PROCESS_ACTION = "checkBuyingProcess";
    private static final String CHECK_COBRANDING_SUBSCRIPTIONS = "checkCobrandingSubscriptions";

    // notification action
    private static final String COBRANDING_VALUE = "METEGOL_AR";
    private static final String UPCOMING_ACTION = "traeProximos";
    private static final String REGISTER_DEVICE_ACTION = "registerDevice";

    // normal action
    private static final String SET_CONFIG_PUSH_ACTION = "setConfigPush";
    private static final String PAGE_KEY = "pag";
    private static final String CHAMPIONSHIP_ACTION = "traeCampeonatos";
    private static final String POSITION_ACTION = "traePosiciones";
    private static final String FIXTURE_ACTION = "traeFixture";
    private static final String SCORERS_ACTION = "traeGoleadores";
    private static final String FOOTBALL_GAME_ACTION = "traeFichaPartido";
    private static final String FOOTBALL_GAME_MIN_TO_MIN_ACTION = "traeFichaPartido2";
    private static final String PLAYER_DATA_ACTION = "traeFichaJugador";
    private static final String NEWS_ACTION = "traeNoticias";
    private static final String VIDEO_ACTION = "traeVideos";
    private static final String COMMENTS_FROM_MATCH_ACTION = "getCommentsFromMatch";
    private static final String ADD_COMMENTS_FROM_MATCH_ACTION = "addCommentToMatch";
    private static final String STATS_ACTION = "traeStatsPartido";
    private static final String STATS_SUMMARY_ACTION = "traeResumoPartido2";
    private static final String PLAYER_TWITTER_ACTION = "traeJugadorTwitter";
    private static final String TOP_TWEETS_ACTION = "traeTopTweets";
    private static final String TOP_MENTIONS_TWEETS_ACTION = "traeTopMentionsTwitter";
    private static final String TOTALES_TWITTER_ACTION = "traeTotalesTwitter";
    private static final String TIMELINE_TWITTER_ACTION = "traeTimelineTwitter";

    // http://fut-df.srv.pmovil.net/?action=traeJugadorTwitter&matchId=1&playerId=1
    // http://fut-df.srv.pmovil.net/?action=traeTopTweets&matchId=1
    // http://fut-df.srv.pmovil.net/?action=traeTotalesTwitter&matchId=1
    // http://fut-df.srv.pmovil.net/?action=traeTopMentionsTwitter&tournamentId=17&matchId=7
    // http://fut-df.srv.pmovil.net/?action=traeTimelineTwitter&matchId=1

    // parameter key for call api
    private static final String SPLASH_DYNAMIC_ACTION = "inicia";
    private static final String FILTER_INCIDENCE = "APP_GENIO_TELA_INCIDENCIAS";
    private static final String ACTION_PARAMETER_KEY = "action";
    private static final String ID_IDIOMA_EXIBICAO_PARAMETER_KEY = "id_idioma_exibicao";
    private static final String ID_PAIS_PARAMETER_KEY = "id_pais";
    private static final String ALERT_PARAMETER_KEY = "alert";
    private static final String APP_ID_PARAMETER_KEY = "app_id";
    private static final String ID_JUGADOR_PARAMETER_KEY = "id_jugador";
    private static final String APP_VERSION_PARAMETER_KEY = "app_version";
    private static final String CERT_PARAMETER_KEY = "cert";
    private static final String DEVICE_HW_PARAMETER_KEY = "device_hw";
    private static final String DEVICE_TOKEN_PARAMETER_KEY = "devicetoken";
    private static final String PLATAFORM_PARAMETER_KEY = "platform";
    private static final String STATUS = "status";
    private static final String TEAM_ID_PARAMETER_KEY = "team_id";
    private static final String MATCH_ID_TWITTER_PARAMETER_KEY = "matchId";
    private static final String PLAYER_ID_TWITTER_PARAMETER_KEY = "playerId";
    private static final String TOURNAMENT_ID_TWITTER_PARAMETER_KEY = "tournamentId";
    private static final String TOURNAMENT_ID_TWITTER_PARAMETER_KEY_NEW = "tournamentid";
    private static final String ID_CAMPEONATO_PARAMETER_KEY = "id_campeonato";
    private static final String ID_PARTIDO_PARAMETER_KEY = "id_partido";
    private static final String ID_FECHA_PARAMETER_KEY = "id_fecha";
    private static final String INI_PARAMETER_KEY = "ini";
    private static final String QTD_PARAMETER_KEY = "qtd";
    private static final String COUNTRY_PARAMETER_KEY = "country";
    private static final String COBRADING_PARAMETER_KEY = "cobranding";
    private static final String URL_CALLBACK_PARAMETER_KEY = "urlCallback";
    private static final String MSISDN_PARAMETER_KEY = "msisdn";
    private static final String CELCO_PARAMETER_KEY = "celco";
    private static final String SESSION_ID_PARAMETER_KEY = "sessionId";
    private static final String MATCH_ID_PARAMETER_KEY = "match_id";
    private static final String MIN_BY_MIN_EVENT_ID_PARAMETER_KEY = "minute_by_minute_event_id";
    private static final String PAGE_SIZE_PARAMETER_KEY = "page_size";
    private static final String LAST_COMMENT_ID_PARAMETER_KEY = "last_comment_id";
    private static final String WANTS_OLDER_MESSAGE_PARAMETER_KEY = "wants_older_messages";
    private static final String USER_ID_PARAMETER_KEY = "user_id";
    private static final String USER_NAME_PARAMETER_KEY = "user_name";
    private static final String USER_PHOTO_URL_PARAMETER_KEY = "user_photo_url";
    private static final String USER_SOCIAL_NETWORK_PARAMETER_KEY = "user_social_network";
    private static final String COMMENT_PARAMETER_KEY = "comment";
    private static final String FILTRO_PARAMETER_KEY = "filtro";
    private static final String INTERFACE_ACESSO_PARAMETER_KEY = "interface_acesso";
    private static final String VERSAO_INTERFACE_API_PARAMETER_KEY = "versao_interface_api";
    private static final String TAG = "ConnectionsWSMetegol";
    private static final String URL_BASE_BATTLE = "http://batallas.br.pmovil.net?";
    private static String version = new String();
    private ManagerConnection managerConnection;
    private Context context;
    private String macAdress;

    public ConnectionsWSMetegol(Context context) {
        managerConnection = ManagerConnection.getInstance();
        managerConnection.setAcceptGzip(true);
        this.context = context;
        macAdress = ManagerConnection.getMacAdress(context);
        version = context.getString(R.string.versionBuild);
    }

    public static String getUrlbattles(Context context) {
        // "http://batallas.br.pmovil.net?id_idioma_exibicao=es&app_version=METEGOL - v1.1 - build 213&id_pais=AR&interface_acesso=ANDROID&app_id=METEGOL_AR&versao_interface_api=0.0.0");
        if (version.equalsIgnoreCase("")) {
            version = context.getString(R.string.versionBuild);
        }
        return URL_BASE_BATTLE + ID_IDIOMA_EXIBICAO_PARAMETER_KEY + "="
                + getIdLanguage() + "&" + APP_VERSION_PARAMETER_KEY + "="
                + version + "&" + ID_PAIS_PARAMETER_KEY + "=" + ID_PAIS + "&"
                + INTERFACE_ACESSO_PARAMETER_KEY + "=" + PLATAFORM_VALUE + "&"
                + APP_ID_PARAMETER_KEY + "=" + APP_ID_VALUE + "&"
                + VERSAO_INTERFACE_API_PARAMETER_KEY + "=0.0.0";
    }

    private static String getIdLanguage() {
        String locale = Locale.getDefault().toString();
        String idIdioma = "en";
        if (locale.startsWith("en")) {
            idIdioma = "en";
        } else if (locale.startsWith("pt")) {
            idIdioma = "pt";

        } else if (locale.startsWith("es")) {
            idIdioma = "es";

        }
        return idIdioma;
    }

    // http://fut-df.srv.pmovil.net/?action=traeStatsPartido&id_pais=BR&id_campeonato=1939&id_partido=171519
    private List<NameValuePair> parametersForStatsGame(int idChampionship, int idGame) {
        return prepareParameters(STATS_ACTION, idChampionship, idGame, -1);
    }

    // http://fut-df.srv.pmovil.net/?action=traeResumoPartido2&id_pais=BR&id_campeonato=1939&id_partido=171519
    private List<NameValuePair> parametersForSummaryGame(int idChampionship, int idGame) {
        return prepareParameters(STATS_SUMMARY_ACTION, idChampionship, idGame, -1);
    }

    private List<NameValuePair> parametersForScorers(int idChampionship) {
        // return prepareParameters(SCORERS_ACTION, idChampionship, -1, -1);

        return prepareParameters(SCORERS_ACTION, idChampionship, -1, -1, -1,
                -1, -1, -1, -1, -1, null, null, null, null, null, null, false,
                -1, macAdress);
    }

    private List<NameValuePair> parametersForFootballGame(int idChampionship,
                                                          int idGame) {
        return prepareParameters(FOOTBALL_GAME_ACTION, idChampionship, idGame,
                -1);
    }

    private List<NameValuePair> parametersForFootballGameMinToMin(
            int idChampionship, int idGame) {
        return prepareParameters(FOOTBALL_GAME_MIN_TO_MIN_ACTION,
                idChampionship, idGame, true);
    }

    private List<NameValuePair> prepareParameters(
            String footballGameMinToMinAction, int idChampionship, int idGame,
            boolean filter) {
        return prepareParameters(footballGameMinToMinAction, idChampionship,
                idGame, -1, -1, -1, -1, -1, -1, -1, null, null, null, null,
                null, null, filter);
    }

    public List<NameValuePair> parametersForFixture(int idChampionship,
                                                    int idDate) {
        // return prepareParameters(FIXTURE_ACTION, idChampionship, -1, idDate);

        return prepareParameters(FIXTURE_ACTION, idChampionship, -1, idDate,
                -1, -1, -1, -1, -1, -1, null, null, null, null, null, null,
                false, -1, macAdress);
    }

    private List<NameValuePair> parametersForPositions(int idChampionship) {
        // return prepareParameters(POSITION_ACTION, idChampionship, -1, -1);

        return prepareParameters(POSITION_ACTION, idChampionship, -1, -1, -1,
                -1, -1, -1, -1, -1, null, null, null, null, null, null, false,
                -1, macAdress);
    }

    private List<NameValuePair> parametersForChampionship() {
        return prepareParameters(CHAMPIONSHIP_ACTION, -1, -1, -1);
    }

    private List<NameValuePair> parametersForVideo(int idChampionship, int count) {
        return prepareParameters(VIDEO_ACTION, idChampionship, -1, -1, ini,
                count);
    }

    // http://fut-df.srv.pmovil.net/?action=traeVideos&id_campeonato=1861&qtd=10&ini=0
    private List<NameValuePair> parametersForVideo(int idChampionship) {
        return prepareParameters(VIDEO_ACTION, idChampionship, -1, -1, ini, qtd);
    }

    private List<NameValuePair> parametersForNews(int idChampionship, int start) {
        return prepareParameters(NEWS_ACTION, idChampionship, -1, -1, start,
                qtd);

    }

    // http://fut-df.srv.pmovil.net/?action=traeNoticias&id_campeonato=1861&ini=0&qtd=10
    private List<NameValuePair> parametersForNews(int idChampionship) {
        return prepareParameters(NEWS_ACTION, idChampionship, -1, -1, ini, qtd);
    }

    private List<NameValuePair> parametersGetComment(int matchId,
                                                     int minute_by_minute_event_id, int page_size, int last_comment_id,
                                                     String wants_older_messages) {
        return prepareParameters(COMMENTS_FROM_MATCH_ACTION, -1, -1, -1, -1,
                -1, matchId, minute_by_minute_event_id, page_size,
                last_comment_id, wants_older_messages, null, null, null, null,
                null, false);
    }

    private List<NameValuePair> parametersAddComment(int matchId,
                                                     int minute_by_minute_event_id, String user_id, String user_name,
                                                     String user_photo_url, String user_social_network, String comment) {
        return prepareParameters(ADD_COMMENTS_FROM_MATCH_ACTION, -1, -1, -1,
                -1, -1, matchId, minute_by_minute_event_id, -1, -1, null,
                user_id, user_name, user_photo_url, user_social_network,
                comment, false);
    }

    private List<NameValuePair> prepareParameters(String action,
                                                  int idChampionship, int idGame, int idDate) {
        return prepareParameters(action, idChampionship, idGame, idDate, -1, -1);
    }

    private List<NameValuePair> prepareParameters(String action,
                                                  int idChampionship, int idGame, int idDate, int ini, int qtd) {
        return prepareParameters(action, idChampionship, idGame, idDate, ini,
                qtd, -1, -1, -1, -1, null, null, null, null, null, null, false);
    }

    private List<NameValuePair> prepareParametersCheckBuyingProcess(
            String sesssionId) {

        return prepareParametersSubscription(null, CHECK_BUYING_PROCESS_ACTION,
                sesssionId, null, null, null, null);
    }

    private List<NameValuePair> prepareParametersCheckCobradingSubscription(
            String cobranding, String country, String celco, String msisdn) {

        return prepareParametersSubscription(country,
                CHECK_COBRANDING_SUBSCRIPTIONS, null, cobranding, msisdn, null,
                celco);

    }

    private List<NameValuePair> prepareParametersStarBuying(String cobranding,
                                                            String country, String urlCallback, String msisdn) {

        return prepareParametersSubscription(country, START_BUYING_ACTION,
                null, cobranding, msisdn, urlCallback, null);

    }

    private List<NameValuePair> prepareParametersSubscription(String country,
                                                              String action, String sesssionId, String cobranding, String msisdn,
                                                              String urlCallback, String celco) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(ACTION_PARAMETER_KEY, action));
        if (country != null)
            parameters.add(new BasicNameValuePair(COUNTRY_PARAMETER_KEY,
                    country));
        if (cobranding != null)
            parameters.add(new BasicNameValuePair(COBRADING_PARAMETER_KEY,
                    cobranding));
        if (urlCallback != null)
            parameters.add(new BasicNameValuePair(URL_CALLBACK_PARAMETER_KEY,
                    urlCallback));
        if (msisdn != null)
            parameters
                    .add(new BasicNameValuePair(MSISDN_PARAMETER_KEY, msisdn));
        if (celco != null)
            parameters.add(new BasicNameValuePair(CELCO_PARAMETER_KEY, celco));
        if (sesssionId != null)
            parameters.add(new BasicNameValuePair(SESSION_ID_PARAMETER_KEY,
                    sesssionId));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));
        return parameters;
    }

    public ConnectionResponse getRegisterDevice(String deviceToken) {
        managerConnection
                .setNameValuePairs(parametersRegisterDevice(deviceToken));
        return managerConnection.post(URL_BASE_POST);
    }

    public ConnectionResponse setConfigPush(String deviceToken, String status,
                                            String teamId) {
        managerConnection.setNameValuePairs(parametersSetConfigPush(
                deviceToken, status, teamId));
        return managerConnection.post(URL_BASE_POST);
    }

    private List<NameValuePair> parametersRegisterDevice(String deviceToken) {
        return prepareParametersNotification(REGISTER_DEVICE_ACTION,
                deviceToken, ALERT_VALUE, APP_ID_VALUE, version, CERT_VALUE,
                PLATAFORM_VALUE, null, null);
    }

    private List<NameValuePair> parametersSetConfigPush(String deviceToken,
                                                        String status, String teamId) {
        return prepareParametersNotification(SET_CONFIG_PUSH_ACTION,
                deviceToken, null, null, null, null, null, status, teamId);
    }

    private List<NameValuePair> prepareParametersNotification(String action,
                                                              String deviceToken, String alert, String appId, String appVersion,
                                                              String cert, String plataform, String status, String teamId) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        // Constant Values

        parameters.add(new BasicNameValuePair(ID_IDIOMA_EXIBICAO_PARAMETER_KEY,
                getIdLanguage()));
        parameters.add(new BasicNameValuePair(ID_PAIS_PARAMETER_KEY, ID_PAIS));
        parameters.add(new BasicNameValuePair(DEVICE_HW_PARAMETER_KEY,
                macAdress));
        parameters.add(new BasicNameValuePair(INTERFACE_ACESSO_PARAMETER_KEY,
                PLATAFORM_VALUE));
        parameters.add(new BasicNameValuePair(
                VERSAO_INTERFACE_API_PARAMETER_KEY, "0.0.0"));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));

        // Others Values

        if (action != null)
            parameters
                    .add(new BasicNameValuePair(ACTION_PARAMETER_KEY, action));

        if (status != null)
            parameters.add(new BasicNameValuePair(STATUS, status));
        if (teamId != null)
            parameters
                    .add(new BasicNameValuePair(TEAM_ID_PARAMETER_KEY, teamId));

        if (alert != null)
            parameters.add(new BasicNameValuePair(ALERT_PARAMETER_KEY, alert));

        if (cert != null)
            parameters.add(new BasicNameValuePair(CERT_PARAMETER_KEY, cert));
        if (deviceToken != null)
            parameters.add(new BasicNameValuePair(DEVICE_TOKEN_PARAMETER_KEY,
                    deviceToken));
        if (plataform != null)
            parameters.add(new BasicNameValuePair(PLATAFORM_PARAMETER_KEY,
                    plataform));
        return parameters;
    }

    public void getUpcomingPost(ConnectionHandlerResponseBody response,
                                int idChampionship, int idDate, int page) {
        managerConnection.setNameValuePairs(parametersForUpcoming(
                idChampionship, idDate, page));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public List<NameValuePair> parametersForUpcoming(int idChampionship,
                                                     int idDate, int page) {
        // return prepareParameters(FIXTURE_ACTION, idChampionship, -1, idDate);

        return prepareParametersUpcoming(UPCOMING_ACTION, idChampionship,
                idDate, page);
    }

    private List<NameValuePair> prepareParametersUpcoming(String action,
                                                          int idChampionship, int idDate, int page_size) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(ACTION_PARAMETER_KEY, action));

        String id_idioma = getIdLanguage();

        parameters.add(new BasicNameValuePair(ID_IDIOMA_EXIBICAO_PARAMETER_KEY,
                id_idioma));
        parameters.add(new BasicNameValuePair(ID_PAIS_PARAMETER_KEY, ID_PAIS));
        if (idChampionship != -1)
            parameters.add(new BasicNameValuePair(ID_CAMPEONATO_PARAMETER_KEY,
                    "" + idChampionship));
        if (idDate != -1)
            parameters.add(new BasicNameValuePair(ID_FECHA_PARAMETER_KEY, ""
                    + idDate));
        if (page_size != -1)
            parameters.add(new BasicNameValuePair(PAGE_KEY, "" + page_size));
        parameters.add(new BasicNameValuePair(DEVICE_HW_PARAMETER_KEY,
                macAdress));
        parameters.add(new BasicNameValuePair(INTERFACE_ACESSO_PARAMETER_KEY,
                PLATAFORM_VALUE));
        parameters.add(new BasicNameValuePair(
                VERSAO_INTERFACE_API_PARAMETER_KEY, "0.0.0"));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));
        return parameters;
    }

    // http://fut-df.srv.pmovil.net/?action=traeFichaJugador&id_pais=BR&id_campeonato=1939&id_partido=171595&id_jugador=9274
    private List<NameValuePair> parametersForPlayer(int idChampionship,
                                                    int idGame, int idPlayer) {
        return prepareParameters(PLAYER_DATA_ACTION, idChampionship, idGame,
                -1, -1, -1, -1, -1, -1, -1, null, null, null, null, null, null,
                false, idPlayer, null);
    }

    private List<NameValuePair> prepareParameters(String action,
                                                  int idChampionship, int idGame, int idDate, int ini, int qtd,
                                                  int matchId, int minute_by_minute_event_id, int page_size,
                                                  int last_comment_id, String wants_older_messages, String user_id,
                                                  String user_name, String user_photo_url,
                                                  String user_social_network, String comment, boolean filter) {
        return prepareParameters(action, idChampionship, idGame, idDate, ini,
                qtd, matchId, minute_by_minute_event_id, page_size,
                last_comment_id, wants_older_messages, user_id, user_name,
                user_photo_url, user_social_network, comment, filter, -1, null);
    }

    private List<NameValuePair> prepareParameters(String action,
                                                  int idChampionship, int idGame, int idDate, int ini, int qtd,
                                                  int matchId, int minute_by_minute_event_id, int page_size,
                                                  int last_comment_id, String wants_older_messages, String user_id,
                                                  String user_name, String user_photo_url,
                                                  String user_social_network, String comment, boolean filter,
                                                  int idPlayer, String deviceHW) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(ACTION_PARAMETER_KEY, action));

        String id_idioma = getIdLanguage();

        parameters.add(new BasicNameValuePair(ID_IDIOMA_EXIBICAO_PARAMETER_KEY,
                id_idioma));
        parameters.add(new BasicNameValuePair(ID_PAIS_PARAMETER_KEY, ID_PAIS));
        if (idChampionship != -1)
            parameters.add(new BasicNameValuePair(ID_CAMPEONATO_PARAMETER_KEY,
                    "" + idChampionship));
        if (idGame != -1)
            parameters.add(new BasicNameValuePair(ID_PARTIDO_PARAMETER_KEY, ""
                    + idGame));
        if (idPlayer != -1)
            parameters.add(new BasicNameValuePair(ID_JUGADOR_PARAMETER_KEY, ""
                    + idPlayer));
        if (idDate != -1)
            parameters.add(new BasicNameValuePair(ID_FECHA_PARAMETER_KEY, ""
                    + idDate));
        if (ini != -1)
            parameters.add(new BasicNameValuePair(INI_PARAMETER_KEY, "" + ini));
        if (qtd != -1)
            parameters.add(new BasicNameValuePair(QTD_PARAMETER_KEY, "" + qtd));

        if (deviceHW != null)
            parameters.add(new BasicNameValuePair(DEVICE_HW_PARAMETER_KEY,
                    deviceHW));

        // //////// GET COMMENTS
        if (matchId != -1)
            parameters.add(new BasicNameValuePair(
                    MATCH_ID_TWITTER_PARAMETER_KEY, "" + matchId));
        if (minute_by_minute_event_id != -1)
            parameters.add(new BasicNameValuePair(
                    MIN_BY_MIN_EVENT_ID_PARAMETER_KEY, ""
                    + minute_by_minute_event_id
            ));
        if (page_size != -1)
            parameters.add(new BasicNameValuePair(PAGE_SIZE_PARAMETER_KEY, ""
                    + page_size));
        if (last_comment_id != -1)
            parameters.add(new BasicNameValuePair(
                    LAST_COMMENT_ID_PARAMETER_KEY, "" + last_comment_id));
        if (wants_older_messages != null)
            parameters.add(new BasicNameValuePair(
                    WANTS_OLDER_MESSAGE_PARAMETER_KEY, ""
                    + wants_older_messages
            ));
        // //////// ADD COMMENTS

        if (user_id != null)
            parameters.add(new BasicNameValuePair(USER_ID_PARAMETER_KEY, ""
                    + user_id));
        if (user_name != null)
            parameters.add(new BasicNameValuePair(USER_NAME_PARAMETER_KEY, ""
                    + user_name));
        if (user_photo_url != null)
            parameters.add(new BasicNameValuePair(USER_PHOTO_URL_PARAMETER_KEY,
                    "" + user_photo_url));
        if (user_social_network != null)
            parameters
                    .add(new BasicNameValuePair(
                            USER_SOCIAL_NETWORK_PARAMETER_KEY, ""
                            + user_social_network
                    ));
        if (comment != null) {
            parameters.add(new BasicNameValuePair(COMMENT_PARAMETER_KEY,
                    comment));
        }
        // FILTER INCIDENCE
        if (filter) {
            parameters.add(new BasicNameValuePair(FILTRO_PARAMETER_KEY,
                    FILTER_INCIDENCE));
        }
        parameters.add(new BasicNameValuePair(INTERFACE_ACESSO_PARAMETER_KEY,
                PLATAFORM_VALUE));
        parameters.add(new BasicNameValuePair(
                VERSAO_INTERFACE_API_PARAMETER_KEY, "0.0.0"));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));
        return parameters;
    }

    public void getTimelineTwitterPost(ConnectionHandlerResponseBody response,
                                       String matchId) {
        managerConnection
                .setNameValuePairs(prepareParametersTimeLineTwitter(matchId));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getTopTwitterPost(ConnectionHandlerResponseBody response,
                                  String matchId, String tournamentId) {
        managerConnection
                .setNameValuePairs(prepareParametersTopTwitter(matchId, tournamentId));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public ConnectionResponse getTotalsTwitter(String matchId) {
        managerConnection
                .setNameValuePairs(prepareParametersTotalsTwitter(matchId));
        return managerConnection.post(URL_BASE_POST);
    }

    public ConnectionResponse getTopMentionsTwitter(String tournamentId,
                                                    String matchId) {
        managerConnection
                .setNameValuePairs(prepareParametersTopMentionsTwitter(
                        tournamentId, matchId));
        return managerConnection.post(URL_BASE_POST);
    }

    public void getSplashDynamic(ConnectionHandlerResponseBody response) {
        managerConnection
                .setNameValuePairs(prepareParametersSplashDynamic());
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getPlayerTwitterPost(ConnectionHandlerResponseBody response,
                                     String playerID, String matchId) {
        managerConnection.setNameValuePairs(prepareParametersPlayerTwitter(
                playerID, matchId));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    private List<NameValuePair> prepareParametersTotalsTwitter(String matchId) {
        return prepareParametersTwitter(TOTALES_TWITTER_ACTION, null, null,
                matchId);
    }

    private List<NameValuePair> prepareParametersTopMentionsTwitter(
            String tournamentId, String matchId) {
        return prepareParametersTwitter(TOP_MENTIONS_TWEETS_ACTION,
                tournamentId, null, matchId);
    }

    private List<NameValuePair> prepareParametersTopTwitter(String matchId, String tournamentId) {
        return prepareParametersTwitterForTwitter(TOP_TWEETS_ACTION, tournamentId, null, matchId);
    }

    private List<NameValuePair> prepareParametersPlayerTwitter(String playerID,
                                                               String matchId) {
        return prepareParametersTwitter(PLAYER_TWITTER_ACTION, null, playerID,
                matchId);
    }

    private List<NameValuePair> prepareParametersTimeLineTwitter(String matchId) {
        return prepareParametersTwitter(TIMELINE_TWITTER_ACTION, null, null,
                matchId);
    }

    private List<NameValuePair> prepareParametersTwitter(String action,
                                                         String tournamentId, String playerID, String matchId) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        // Constant Values

        parameters.add(new BasicNameValuePair(ID_IDIOMA_EXIBICAO_PARAMETER_KEY,
                getIdLanguage()));
        parameters.add(new BasicNameValuePair(ID_PAIS_PARAMETER_KEY, ID_PAIS));
        parameters.add(new BasicNameValuePair(DEVICE_HW_PARAMETER_KEY,
                macAdress));
        parameters.add(new BasicNameValuePair(INTERFACE_ACESSO_PARAMETER_KEY,
                PLATAFORM_VALUE));
        parameters.add(new BasicNameValuePair(
                VERSAO_INTERFACE_API_PARAMETER_KEY, "0.0.0"));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));

        // Others Values

        if (action != null)
            parameters
                    .add(new BasicNameValuePair(ACTION_PARAMETER_KEY, action));
        if (matchId != null)
            parameters.add(new BasicNameValuePair(
                    MATCH_ID_TWITTER_PARAMETER_KEY, matchId));

        if (playerID != null)
            parameters.add(new BasicNameValuePair(
                    PLAYER_ID_TWITTER_PARAMETER_KEY, playerID));
        if (tournamentId != null)
            parameters.add(new BasicNameValuePair(
                    TOURNAMENT_ID_TWITTER_PARAMETER_KEY, tournamentId));
        return parameters;
    }

    private List<NameValuePair> prepareParametersTwitterForTwitter(String action,
                                                                   String tournamentId, String playerID, String matchId) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        // Constant Values

        parameters.add(new BasicNameValuePair(ID_IDIOMA_EXIBICAO_PARAMETER_KEY,
                getIdLanguage()));
        parameters.add(new BasicNameValuePair(ID_PAIS_PARAMETER_KEY, ID_PAIS));
        parameters.add(new BasicNameValuePair(DEVICE_HW_PARAMETER_KEY,
                macAdress));
        parameters.add(new BasicNameValuePair(INTERFACE_ACESSO_PARAMETER_KEY,
                PLATAFORM_VALUE));
        parameters.add(new BasicNameValuePair(
                VERSAO_INTERFACE_API_PARAMETER_KEY, "0.0.0"));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));

        // Others Values

        if (action != null)
            parameters
                    .add(new BasicNameValuePair(ACTION_PARAMETER_KEY, action));
        if (matchId != null)
            parameters.add(new BasicNameValuePair(
                    MATCH_ID_TWITTER_PARAMETER_KEY, matchId));

        if (playerID != null)
            parameters.add(new BasicNameValuePair(
                    PLAYER_ID_TWITTER_PARAMETER_KEY, playerID));
        if (tournamentId != null)
            parameters.add(new BasicNameValuePair(
                    TOURNAMENT_ID_TWITTER_PARAMETER_KEY_NEW, tournamentId));

//		
//		if (country != null)
        parameters.add(new BasicNameValuePair(
                COUNTRY_PARAMETER_KEY, COUNTRY_VALUE.toLowerCase()));

        return parameters;
    }

    private List<NameValuePair> preparePametersSplashDynamic() {

        return prepareParametersSplashDynamic();
    }

    private List<NameValuePair> prepareParametersSplashDynamic() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        parameters.add(new BasicNameValuePair(ID_IDIOMA_EXIBICAO_PARAMETER_KEY,
                getIdLanguage()));
        parameters.add(new BasicNameValuePair(ID_PAIS_PARAMETER_KEY, ID_PAIS));
        parameters.add(new BasicNameValuePair(DEVICE_HW_PARAMETER_KEY,
                macAdress));
        parameters.add(new BasicNameValuePair(INTERFACE_ACESSO_PARAMETER_KEY,
                PLATAFORM_VALUE));
        parameters.add(new BasicNameValuePair(
                VERSAO_INTERFACE_API_PARAMETER_KEY, "0.0.0"));
        parameters.add(new BasicNameValuePair(APP_ID_PARAMETER_KEY,
                APP_ID_VALUE));

        parameters.add(new BasicNameValuePair(APP_VERSION_PARAMETER_KEY,
                version));
        parameters.add(new BasicNameValuePair(ACTION_PARAMETER_KEY, SPLASH_DYNAMIC_ACTION));

        parameters.add(new BasicNameValuePair(PLATAFORM_PARAMETER_KEY, PLATAFORM_VALUE));

        return parameters;
    }

    public void postCheckCobradingSubscription(
            ConnectionHandlerResponseBody response, String msisdn, String celco) {
        managerConnection
                .setNameValuePairs(prepareParametersCheckCobradingSubscription(
                        COBRANDING_VALUE, COUNTRY_VALUE, celco, msisdn));
        managerConnection.postAsyn(URL_BASE_SUBSCRIPTION_SERVICE, response);
    }

    public void postCheckBuyingProcess(ConnectionHandlerResponseBody response,
                                       String sessionId) {
        managerConnection
                .setNameValuePairs(prepareParametersCheckBuyingProcess(sessionId));
        managerConnection.postAsyn(URL_BASE_SUBSCRIPTION_SERVICE, response);
    }

    public void postStartBuying(ConnectionHandlerResponseBody response) {
        managerConnection.setNameValuePairs(prepareParametersStarBuying(
                COBRANDING_VALUE, COUNTRY_VALUE, URLCALLBACK_VALUE, null));
        managerConnection.postAsyn(URL_BASE_SUBSCRIPTION_SERVICE, response);
    }

    public void getCommentsFromMatchAndMintoMinPost(
            ConnectionHandlerResponseBody response, int matchId,
            int minute_by_minute_event_id, int page_size) {
        managerConnection.setNameValuePairs(parametersGetComment(matchId,
                minute_by_minute_event_id, page_size, -1, null));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void addCommentsFromMatchAndMinByMinPost(
            ConnectionHandlerResponseBody response, int matchId,
            int minute_by_minute_event_id, String user_id, String user_name,
            String user_photo_url, String user_social_network, String comment) {
        managerConnection.setNameValuePairs(parametersAddComment(matchId,
                minute_by_minute_event_id, user_id, user_name, user_photo_url,
                user_social_network, comment));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getChampionshipsPost(ConnectionHandlerResponseBody response) {
        managerConnection.setNameValuePairs(parametersForChampionship());
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getPlayerDataPost(ConnectionHandlerResponseBody response,
                                  int idChampionship, int idGame, int idPlayer) {
        managerConnection.setNameValuePairs(parametersForPlayer(idChampionship,
                idGame, idPlayer));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getNewsPost(ConnectionHandlerResponseBody response,
                            int idChampionship, int start) {
        managerConnection.setNameValuePairs(parametersForNews(idChampionship,
                start));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getNewsPost(ConnectionHandlerResponseBody response,
                            int idChampionship) {
        managerConnection.setNameValuePairs(parametersForNews(idChampionship));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getVideoPost(ConnectionHandlerResponseBody response,
                             int idChampionship, int count) {
        managerConnection.setNameValuePairs(parametersForVideo(idChampionship,
                count));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getVideoPost(ConnectionHandlerResponseBody response,
                             int idChampionship) {
        managerConnection.setNameValuePairs(parametersForVideo(idChampionship));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getFixturePost(ConnectionHandlerResponseBody response,
                               int idChampionship, int idDate) {
        managerConnection.setNameValuePairs(parametersForFixture(
                idChampionship, idDate));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public ConnectionResponse getFixturePostSync(int idChampionship, int idDate) {
        managerConnection.setNameValuePairs(parametersForFixture(
                idChampionship, idDate));
        return managerConnection.post(URL_BASE_POST);
    }

    public void getPositionPost(ConnectionHandlerResponseBody response,
                                int idChampionship) {
        managerConnection
                .setNameValuePairs(parametersForPositions(idChampionship));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getScoresPost(ConnectionHandlerResponseBody response,
                              int idChampionship) {
        managerConnection
                .setNameValuePairs(parametersForScorers(idChampionship));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getStatsGamePost(ConnectionHandlerResponseBody response,
                                 int idChampionship, int idGame) {
        managerConnection.setNameValuePairs(parametersForStatsGame(idChampionship, idGame));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getStatsSummaryGamePost(ConnectionHandlerResponseBody response, int idChampionship, int idGame) {
        managerConnection.setNameValuePairs(parametersForSummaryGame(idChampionship, idGame));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getFootballGameMinToMinPost(
            ConnectionHandlerResponseBody response, int idChampionship,
            int idGame) {
        managerConnection.setNameValuePairs(parametersForFootballGameMinToMin(
                idChampionship, idGame));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

    public void getFootballGamePost(ConnectionHandlerResponseBody response,
                                    int idChampionship, int idGame) {
        managerConnection.setNameValuePairs(parametersForFootballGame(
                idChampionship, idGame));
        managerConnection.postAsyn(URL_BASE_POST, response);
    }

}
