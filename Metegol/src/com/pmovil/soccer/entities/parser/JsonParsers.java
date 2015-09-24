package com.pmovil.soccer.entities.parser;

import android.content.Context;

import com.pmovil.soccer.R;
import com.pmovil.soccer.entities.Category;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Comment;
import com.pmovil.soccer.entities.Data;
import com.pmovil.soccer.entities.DataExpand;
import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.FileGame;
import com.pmovil.soccer.entities.FileIncidence;
import com.pmovil.soccer.entities.FilePersonal;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.FootBallGameFull;
import com.pmovil.soccer.entities.FootballGame;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.Goal;
import com.pmovil.soccer.entities.Incidence;
import com.pmovil.soccer.entities.IncidenceFootballGame;
import com.pmovil.soccer.entities.MinToMinData;
import com.pmovil.soccer.entities.News;
import com.pmovil.soccer.entities.Official;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.PlayerTweet;
import com.pmovil.soccer.entities.Positions;
import com.pmovil.soccer.entities.Referee;
import com.pmovil.soccer.entities.Scorers;
import com.pmovil.soccer.entities.SplashDynamic;
import com.pmovil.soccer.entities.Stadium;
import com.pmovil.soccer.entities.Statistic;
import com.pmovil.soccer.entities.Stats;
import com.pmovil.soccer.entities.Status;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.TimeLine;
import com.pmovil.soccer.entities.TopMentionsTwitter;
import com.pmovil.soccer.entities.TopTweet;
import com.pmovil.soccer.entities.TotalTweets;
import com.pmovil.soccer.entities.TotalesTweets;
import com.pmovil.soccer.entities.Value;
import com.pmovil.soccer.entities.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParsers {

    private static final int EventTypeChange = 6;

    public static List<TimeLine> ParserTimeLine(JSONArray data) {
        if (data == null) {
            return null;
        }
        List<TimeLine> timeLines = new ArrayList<TimeLine>();
        for (int indexDataArray = 0; indexDataArray < data.length(); indexDataArray++) {
            JSONObject timeLineObject = data.optJSONObject(indexDataArray);
            if (timeLineObject != null) {
                TimeLine timeLine = new TimeLine();
                timeLine.setId(parseInt(timeLineObject, "id"));
                timeLine.setBody(timeLineObject.optString("body", ""));
                timeLine.setLang(timeLineObject.optString("lang", ""));
                timeLine.setDate(timeLineObject.optString("date", ""));
                timeLine.setSource(timeLineObject.optString("source", ""));
                timeLine.setUserId(timeLineObject.optString("user_id", ""));
                timeLine.setUserUsername(timeLineObject.optString(
                        "user_username", ""));
                timeLine.setUserName(timeLineObject.optString("user_name", ""));
                timeLine.setUserFollowers(timeLineObject.optString(
                        "user_followers", ""));
                timeLine.setUserRanking(timeLineObject.optString(
                        "user_ranking", ""));
                timeLine.setUserPicture(timeLineObject.optString(
                        "user_picture", ""));
                timeLine.setBody(timeLineObject.optString("body", ""));
                timeLines.add(timeLine);
            }
        }
        return timeLines;

    }

    public static List<TopMentionsTwitter> ParserTopMentionsTwitter(
            JSONArray data) {
        if (data == null) {
            return null;
        }

        List<TopMentionsTwitter> topMentionsTwitters = new ArrayList<TopMentionsTwitter>();
        for (int indexDataArray = 0; indexDataArray < data.length(); indexDataArray++) {
            JSONObject topMentionsTwitterObject = data
                    .optJSONObject(indexDataArray);
            if (topMentionsTwitterObject != null) {
                TopMentionsTwitter mentionsTwitter = new TopMentionsTwitter();
                mentionsTwitter.setId(parseInt(topMentionsTwitterObject, "id"));
                mentionsTwitter.setFname(topMentionsTwitterObject.optString(
                        "fname", ""));
                mentionsTwitter.setLname(topMentionsTwitterObject.optString(
                        "lname", ""));
                mentionsTwitter.setHashtag(topMentionsTwitterObject.optString(
                        "hashtag", ""));
                mentionsTwitter.setTwitterUser(topMentionsTwitterObject
                        .optString("twitter_user", ""));
                mentionsTwitter.setImage(topMentionsTwitterObject.optString(
                        "image", ""));
                mentionsTwitter.setMentions(topMentionsTwitterObject.optString(
                        "mentions", ""));
                topMentionsTwitters.add(mentionsTwitter);
            }
        }

        return topMentionsTwitters;
    }

    public static TotalesTweets ParserTotalesTweets(JSONObject data) {
        if (data == null) {
            return null;
        }

        TotalesTweets totalesTweets = new TotalesTweets();
        totalesTweets.setRetweets(ParserTotalTweets(data
                .optJSONObject("retweets")));
        totalesTweets
                .setTweets(ParserTotalTweets(data.optJSONObject("tweets")));

        totalesTweets.setTotal(ParserTotalTweets(data.optJSONObject("total")));

        return totalesTweets;

    }

    private static TotalTweets ParserTotalTweets(JSONObject data) {
        TotalTweets totalTweets = new TotalTweets();

        if (data == null) {
            return totalTweets;
        }
        totalTweets.setGlobal(data.optString("global", ""));
        totalTweets.setLocal(data.optString("local", ""));
        totalTweets.setVisitor(data.optString("visitor", ""));
        totalTweets.setReferee(data.optString("referee", ""));
        return totalTweets;
    }

    public static List<TopTweet> ParserTopTweets(JSONObject data) {
        if (data == null) {
            return null;

        }
        data = data.optJSONObject("data");
        if (data == null) {
            return null;
        }
        data = data.optJSONObject("topEntities");
        if (data == null) {
            return null;
        }
        List<TopTweet> topTweets = new ArrayList<TopTweet>();

        JSONArray globalTweetArray = data.optJSONArray("global");
        for (int indexGlobalTweetArray = 0; indexGlobalTweetArray < globalTweetArray
                .length(); indexGlobalTweetArray++) {
            JSONObject globalTweetObject = globalTweetArray
                    .optJSONObject(indexGlobalTweetArray);
            if (globalTweetArray != null) {
                TopTweet topTweet = new TopTweet();
                topTweet.setHashtag(globalTweetObject.optString("hashtag", ""));
                topTweet.setId(parseInt(globalTweetObject, "id"));
                topTweet.setName(globalTweetObject.optString("name", ""));
                topTweet.setMentions(globalTweetObject
                        .optString("mentions", ""));
                topTweet.setType(globalTweetObject.optString("type", ""));

                topTweet.setUrlImage(globalTweetObject.optString("image_url", ""));
                topTweets.add(topTweet);
            }
        }
        return topTweets;
    }

    public static List<PlayerTweet> ParserPlayerTweets(JSONObject data) {
        if (data == null)
            return null;
        List<PlayerTweet> playerTweets = new ArrayList<PlayerTweet>();
        String playerMention = data.optString("player_mentions", "0");
        JSONArray tweetArray = data.optJSONArray("player_tweets");
        for (int indexTweetArray = 0; tweetArray != null
                && indexTweetArray < tweetArray.length(); indexTweetArray++) {
            JSONObject playerTweetObject = tweetArray
                    .optJSONObject(indexTweetArray);
            if (playerTweetObject != null) {
                PlayerTweet playerTweet = new PlayerTweet();
                playerTweet.setTweetId(playerTweetObject.optString("tweet_id",
                        ""));
                playerTweet.setBody(playerTweetObject.optString("body", ""));
                playerTweet.setUserId(playerTweetObject
                        .optString("user_id", ""));
                playerTweet.setUsername(playerTweetObject.optString("username",
                        ""));
                playerTweet.setPictureUrl(playerTweetObject.optString(
                        "picture_url", ""));
                playerTweet.setPlayerMentions(playerMention);
                playerTweets.add(playerTweet);
            }
        }
        return playerTweets;
    }

    public static List<Statistic> ParserStatistic(JSONObject data, Context context) {

        if (context == null)
            return null;
        JSONObject teamsObject = data.optJSONObject("Teams");
        if (teamsObject == null)
            return null;
        JSONArray teamsArray = teamsObject.optJSONArray("Team");
        if (teamsArray == null)
            return null;
        List<Team> teams = new ArrayList<Team>();
        for (int indexTeamsArray = 0; indexTeamsArray < teamsArray.length(); indexTeamsArray++) {
            teams.add(ParserTeam(teamsArray.optJSONObject(indexTeamsArray)));
        }
        Team home = null;
        Team away = null;
        if (teams.size() >= 2) {
            List<Statistic> statisticsList = new ArrayList<Statistic>();
            if (teams.get(0).getHomeOrAway().equalsIgnoreCase("home")) {
                home = teams.get(0);
                away = teams.get(1);
            } else {
                home = teams.get(1);
                away = teams.get(0);
            }

            // /
            Statistic statistic = new Statistic();
            statistic.setValueTitle(context.getString(R.string.GoalsKey));
            statistic.setValueAway("" + away.getGoals());
            statistic.setValueHome("" + home.getGoals());
            statisticsList.add(statistic);
            // /
            statistic = new Statistic();
            statistic
                    .setValueTitle(context.getString(R.string.GoalAttemptsKey));
            statistic.setValueAway("" + away.getGoalAttempts());
            statistic.setValueHome("" + home.getGoalAttempts());
            statisticsList.add(statistic);
            // /
            statistic = new Statistic();
            statistic.setValueTitle(context.getString(R.string.ShotsOnGoalKey));
            statistic.setValueAway("" + away.getGoalShots());
            statistic.setValueHome("" + home.getGoalShots());
            statisticsList.add(statistic);
            // /
            statistic = new Statistic();
            statistic.setValueTitle(context.getString(R.string.FoulsKey));
            statistic.setValueAway("" + away.getFouls());
            statistic.setValueHome("" + home.getFouls());
            statisticsList.add(statistic);
            // /
            statistic = new Statistic();
            statistic.setValueTitle(context.getString(R.string.RedCardKey));
            statistic.setValueAway("" + away.getRedCards());
            statistic.setValueHome("" + home.getRedCards());
            statisticsList.add(statistic);
            // /

            statistic = new Statistic();

            statistic.setValueTitle(context.getString(R.string.YellowCardsKey));
            statistic.setValueAway("" + away.getYellowCards());
            statistic.setValueHome("" + home.getYellowCards());
            statisticsList.add(statistic);
            // /

            statistic = new Statistic();
            statistic.setValueTitle(context.getString(R.string.OffSideKey));
            statistic.setValueAway("" + away.getOffsides());
            statistic.setValueHome("" + home.getOffsides());
            statisticsList.add(statistic);
            // /

            statistic = new Statistic();
            statistic.setValueTitle(context.getString(R.string.CornerKickKey));
            statistic.setValueAway("" + away.getCornerKicks());
            statistic.setValueHome("" + home.getCornerKicks());
            statisticsList.add(statistic);

            return statisticsList;

        }
        return null;
    }

    public static List<Statistic> ParserStatisticSummary(JSONObject dataSummary, JSONObject dataStats, Context context) {

        boolean isSecondTime = false;

        if (context == null)
            return null;
        JSONObject teamsObject = dataStats.optJSONObject("Teams");
        if (teamsObject == null)
            return null;
        JSONArray teamsArray = teamsObject.optJSONArray("Team");
        if (teamsArray == null)
            return null;
        List<Team> teams = new ArrayList<Team>();
        for (int indexTeamsArray = 0; indexTeamsArray < teamsArray.length(); indexTeamsArray++) {
            teams.add(ParserTeam(teamsArray.optJSONObject(indexTeamsArray)));
        }
        Team home = null;
        Team away = null;
        if (teams.size() >= 2) {
            List<Statistic> statisticsList = new ArrayList<Statistic>();
            if (teams.get(0).getHomeOrAway().equalsIgnoreCase("home")) {
                home = teams.get(0);
                away = teams.get(1);
            } else {
                home = teams.get(1);
                away = teams.get(0);
            }

            JSONObject incidenciasObject = dataSummary.optJSONObject("Incidencias");
            if (incidenciasObject == null)
                return null;
            JSONArray incidenciasArray = incidenciasObject.optJSONArray("Incidencia");
            if (incidenciasArray == null)
                return null;

            boolean shouldShowGoals = false;
            int homeGoalsIndex = 0;
            int awayGoalsIndex = 0;
            List<Statistic> summaryGoals = new ArrayList<Statistic>();

            boolean shouldShowChanges = false;
            int homeChangesIndex = 0;
            int awayChangesIndex = 0;
            List<Statistic> summaryChanges = new ArrayList<Statistic>();

            boolean shouldShowYellowCards = false;
            int homeYellowCardsIndex = 0;
            int awayYellowCardsIndex = 0;
            List<Statistic> summaryYellowCards = new ArrayList<Statistic>();

            boolean shouldShowRedCards = false;
            int homeRedCardsIndex = 0;
            int awayRedCardsIndex = 0;
            List<Statistic> summaryRedCards = new ArrayList<Statistic>();

            List<Statistic> statisticsSummaryList = new ArrayList<Statistic>();

            Statistic summaryFirstTime = new Statistic();
            summaryFirstTime.setIsSummary(true);
            summaryFirstTime.setValueAway("");
            summaryFirstTime.setValueHome("");
            summaryFirstTime.setValueTitle("PRIMER TIEMPO");
            statisticsSummaryList.add(summaryFirstTime);

            for (int indexIncidenciasArray = 0; indexIncidenciasArray < incidenciasArray.length(); indexIncidenciasArray++) {
                JSONObject incidencia = incidenciasArray.optJSONObject(indexIncidenciasArray);

                try {

                    if(Integer.valueOf(incidencia.getString("minutes").toString()) > 45 && !isSecondTime){
                        Statistic summarySecondTime = new Statistic();
                        summarySecondTime.setIsSummary(true);
                        summarySecondTime.setValueAway("");
                        summarySecondTime.setValueHome("");
                        summarySecondTime.setValueTitle("SEGUNDO TIEMPO");
                        statisticsSummaryList.add(summarySecondTime);
                        isSecondTime = true;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    int incidenciaType = incidencia.getInt("typeId");
                    int teamID = incidencia.getInt("teamId");
                    String minutes = incidencia.getString("minutes");
                    boolean isHomeTeam = (teamID == home.getId());
                    Statistic statToRecord = null;
                    int statIndexToUse = -1;
                    int statResourceIndexToUse = -1;
                    List<Statistic> statListToUse = null;
                    String statText;

                    if ((incidenciaType == 9) || (incidenciaType == 10) || (incidenciaType == 11) || (incidenciaType == 12) || (incidenciaType == 13)) {
                        shouldShowGoals = true;
                        statListToUse = summaryGoals;
                        statResourceIndexToUse = R.drawable.icon_pelota_team_2;
                        if (isHomeTeam) {
                            statIndexToUse = homeGoalsIndex;
                            homeGoalsIndex++;
                        } else {
                            statIndexToUse = awayGoalsIndex;
                            awayGoalsIndex++;
                        }
                    } else if ((incidenciaType == 6) || (incidenciaType == 7)) {
                        shouldShowChanges = true;
                        statListToUse = summaryChanges;
                        statResourceIndexToUse = R.drawable.icon_change_team_2;
                        if (isHomeTeam) {
                            statIndexToUse = homeChangesIndex;
                            homeChangesIndex++;
                        } else {
                            statIndexToUse = awayChangesIndex;
                            awayChangesIndex++;
                        }
                    } else if (incidenciaType == 3) {
                        shouldShowYellowCards = true;
                        statListToUse = summaryYellowCards;
                        statResourceIndexToUse = R.drawable.icon_tarjeta_amarilla_team_2;
                        if (isHomeTeam) {
                            statIndexToUse = homeYellowCardsIndex;
                            homeYellowCardsIndex++;
                        } else {
                            statIndexToUse = awayYellowCardsIndex;
                            awayYellowCardsIndex++;
                        }
                    } else if ((incidenciaType == 4) || (incidenciaType == 5)) {
                        shouldShowRedCards = true;
                        statListToUse = summaryRedCards;
                        statResourceIndexToUse = R.drawable.icon_tarjeta_roja_team_2;
                        if (isHomeTeam) {
                            statIndexToUse = homeRedCardsIndex;
                            homeRedCardsIndex++;
                        } else {
                            statIndexToUse = awayRedCardsIndex;
                            awayRedCardsIndex++;
                        }
                    }

                    /*if (statIndexToUse < statListToUse.size()) {
                        statToRecord = statListToUse.get(statIndexToUse);
                    } else {*/
                        statToRecord = new Statistic();
                        statToRecord.setMinutes(minutes);
                        statToRecord.setIsSummary(true);
                        statToRecord.setSummaryImage(statResourceIndexToUse);
                        statToRecord.setValueAway("");
                        statToRecord.setValueHome("");
                        statToRecord.setValueTitle("");
                        //statListToUse.add(statToRecord);

                        statisticsSummaryList.add(statToRecord);

                   // }


                    if ((incidenciaType == 6) || (incidenciaType == 7)) {
                        Team team;
                        if (isHomeTeam) {
                            team = home;
                        } else {
                            team = away;
                        }
                        int playerIdIn = incidencia.getInt("inId");
                        int playerIdOut = incidencia.getInt("offId");
                        Player playerIn = null;
                        Player playerOut = null;
                        List<Player> entirePlayerList = new ArrayList<Player>();
                        entirePlayerList.addAll(team.getPlayers());
                        entirePlayerList.addAll(team.getPlayerSubstitutes());
                        for (int indexPlayersArray = 0; indexPlayersArray < entirePlayerList.size(); indexPlayersArray++) {
                            Player player = entirePlayerList.get(indexPlayersArray);
                            if (player.getId() == playerIdIn)
                                playerIn = player;
                            if (player.getId() == playerIdOut)
                                playerOut = player;
                            if (playerIn != null && playerOut != null)
                                indexPlayersArray = entirePlayerList.size();
                        }

                        if (isHomeTeam) {
                            statText =  context.getString(R.string.SummaryChangeIn) + ": " + playerIn.getNom() + " " + playerIn.getApe() + "\n";
                            statText += context.getString(R.string.SummaryChangeOut) + ": " + playerOut.getNom() + " " + playerOut.getApe();
                        } else {
                            statText = context.getString(R.string.SummaryChangeIn) + ": " + playerIn.getNom() + " " + playerIn.getApe() + "\n";
                            statText += context.getString(R.string.SummaryChangeOut) + ": " + playerOut.getNom() + " " + playerOut.getApe();
                        }

                    } else {
                        if (isHomeTeam) {
                            statText = incidencia.getString("playerName");
                        } else {
                            statText = incidencia.getString("playerName");
                        }
                    }

                    if (isHomeTeam) {
                        statToRecord.setValueHome(statText);
                    } else {
                        statToRecord.setValueAway(statText);
                    }

                } catch (Exception e) {
                }


            }

            /*
            if (shouldShowGoals) statisticsSummaryList.addAll(summaryGoals);
            if (shouldShowChanges) statisticsSummaryList.addAll(summaryChanges);
            if (shouldShowYellowCards) statisticsSummaryList.addAll(summaryYellowCards);
            if (shouldShowRedCards) statisticsSummaryList.addAll(summaryRedCards);*/

            /*
            if (shouldShowGoals || shouldShowChanges || shouldShowYellowCards || shouldShowRedCards) {
                Statistic summaryBlank = new Statistic();
                summaryBlank.setIsSummary(true);
                summaryBlank.setValueAway("");
                summaryBlank.setValueHome("");
                summaryBlank.setValueTitle("PRIMER TIEMPO");
                statisticsSummaryList.add(0,summaryBlank);

            }*/

            return statisticsSummaryList;
        }

        return null;
    }

    public static List<Comment> ParserComments(JSONObject data) {
        if (data == null)
            return null;
        String status = data.optString("status", "");
        if (status.equalsIgnoreCase("") || status.equalsIgnoreCase("FAILED")) {
            return null;
        }
        List<Comment> commentsList = new ArrayList<Comment>();

        JSONArray commentArray = data.optJSONArray("comments");
        if (commentArray == null)
            return commentsList;
        for (int indexCommentArray = 0; indexCommentArray < commentArray
                .length(); indexCommentArray++) {
            JSONObject jsonComment = commentArray
                    .optJSONObject(indexCommentArray);
            Comment comment = new Comment();
            comment.setComment(jsonComment.optString("comment", ""));
            comment.setId(parseInt(jsonComment, "comment_id"));
            comment.setMinByMinId(parseInt(jsonComment,
                    "minute_by_minute_event_id"));
            comment.setUser_photo_url(jsonComment.optString("user_photo_url",
                    ""));
            comment.setComment_date_unix_timestamp(jsonComment.optString(
                    "comment_date_unix_timestamp", ""));
            comment.setUser_social_network(jsonComment.optString(
                    "user_social_network", ""));
            comment.setUserName(jsonComment.optString("user_name", ""));
            comment.setComment_date(jsonComment.optString("comment_date", ""));
            commentsList.add(comment);
        }
        return commentsList;
    }

    public static List<Video> ParserVideos(JSONArray data) {
        if (data == null)
            return null;
        List<Video> videos = new ArrayList<Video>();

        for (int indexArrayVideo = 0; indexArrayVideo < data.length(); indexArrayVideo++) {
            videos.add(ParserVideo(data.optJSONObject(indexArrayVideo)));
        }
        return videos;

    }

    public static List<News> ParserNews(JSONObject data) {
        if (data == null)
            return null;

        List<News> newsList = new ArrayList<News>();
        JSONArray arrayNews = data.optJSONArray("noticias");
        if (arrayNews == null)
            return newsList;
        for (int indexArrayNews = 0; indexArrayNews < arrayNews.length(); indexArrayNews++) {
            newsList.add(ParserSimpleNews(arrayNews
                    .optJSONObject(indexArrayNews)));
        }
        return newsList;

    }

    private static News ParserSimpleNews(JSONObject data) {
        if (data == null)
            return null;
        News news = new News();

        news.setId(parseInt(data, "id"));
        news.setTitle(data.optString("titulo", ""));
        news.setLink(data.optString("link", ""));
        news.setOrder(parseInt(data, "orden"));
        news.setImage(data.optString("imagen", ""));
        news.setDescription(data.optString("descripcion", ""));
        return news;
    }

    private static Video ParserVideo(JSONObject data) {
        if (data == null)
            return null;

        Video video = new Video();
        video.setId(parseInt(data, "id"));
        video.setTitle(data.optString("titulo", ""));
        video.setDate(data.optString("fecha", ""));
        video.setThumbnail(data.optString("thumbnail", ""));
        video.setDesc(data.optString("descripcion", ""));
        video.setUrl(data.optString("url", ""));
        if (video.getUrl().equalsIgnoreCase("")) {
            video.setUrl(data.optString("video_url", ""));
        }
        return video;
    }

    public static MinToMinData ParserMinToMin(JSONObject data) {
        if (data == null)
            return null;
        MinToMinData minToMinData = new MinToMinData();
        minToMinData.setStatus(ParserStatus(data.optJSONObject("Status")));
        JSONObject jsonObject = data.optJSONObject("Teams");
        if (jsonObject != null) {
            JSONArray jsonTeamArray = jsonObject.optJSONArray("Team");
            if (jsonTeamArray != null) {

                List<Team> teams = new ArrayList<Team>();
                for (int indexTeamArray = 0; indexTeamArray < jsonTeamArray
                        .length(); indexTeamArray++) {
                    teams.add(ParserTeam(jsonTeamArray
                            .optJSONObject(indexTeamArray)));
                }
                minToMinData.setTeams(teams);
            }

        }
        jsonObject = data.optJSONObject("Incidencias");
        if (jsonObject != null) {
            JSONArray jsonIncidenceArray = jsonObject
                    .optJSONArray("Incidencia");
            if (jsonIncidenceArray != null) {

                List<Incidence> teams = new ArrayList<Incidence>();
                for (int indexTeamArray = 0; indexTeamArray < jsonIncidenceArray
                        .length(); indexTeamArray++) {
                    teams.add(ParserIncidence(jsonIncidenceArray
                            .optJSONObject(indexTeamArray)));
                }
                minToMinData.setIncidences(teams);
            }

        }

        jsonObject = data.optJSONObject("IncidenciasBorradas");
        if (jsonObject != null) {
            JSONObject json = jsonObject.optJSONObject("IncidenciaBorrada");
            List<Data> incidenceDelete = null;
            if (json != null) {
                Data incidence = new Data();
                incidence.setId(json.optInt("uniqueId"));
                incidenceDelete = new ArrayList<Data>();
                incidenceDelete.add(incidence);
            } else {

                JSONArray incidenceDeleteArray = jsonObject
                        .optJSONArray("IncidenciaBorrada");
                if (incidenceDeleteArray != null) {
                    incidenceDelete = new ArrayList<Data>();
                    for (int indexDeletesArray = 0; indexDeletesArray < incidenceDeleteArray
                            .length(); indexDeletesArray++) {
                        Data incidence = new Data();
                        JSONObject jsonIncidence = incidenceDeleteArray
                                .optJSONObject(indexDeletesArray);
                        incidence.setId(jsonIncidence.optInt("uniqueId"));
                        incidenceDelete.add(incidence);
                    }
                }
            }
            minToMinData.setIncidencesDelete(incidenceDelete);
        }

        jsonObject = data.optJSONObject("Officials");
        if (jsonObject != null) {
            JSONObject json = jsonObject.optJSONObject("Official");
            List<Official> official = null;
            if (json != null) {
                official = new ArrayList<Official>();
                official.add(ParserOfficial(jsonObject));
            } else {

                JSONArray gameArray = jsonObject.optJSONArray("Official");
                if (gameArray != null) {
                    official = new ArrayList<Official>();
                    for (int indexGoalsArray = 0; indexGoalsArray < gameArray
                            .length(); indexGoalsArray++) {
                        official.add(ParserOfficial(gameArray
                                .optJSONObject(indexGoalsArray)));
                    }
                }
            }
            minToMinData.setOfficials(official);
        }
        minToMinData.setStat(ParserStats(data.optJSONObject("stats")));
        minToMinData.setReview(parseInt(data, "revision"));
        minToMinData.setId(parseInt(data, "id"));
        minToMinData.setDate(data.optString("date"));
        minToMinData.setScheduledStart(parseInt(data, "scheduledStart"));
        minToMinData.setGmt(parseInt(data, "gmt"));
        minToMinData.setLegue(data.optString("league", ""));
        minToMinData.setLegueId(parseInt(data, "leagueId"));
        minToMinData.setCompetition(data.optString("competition", ""));
        minToMinData.setCompetitionId(parseInt(data, "competition"));
        minToMinData.setCompetitionId(parseInt(data, "competitionId"));
        minToMinData.setWeek(data.optString("week", ""));
        minToMinData.setWeekId(parseInt(data, "weekId"));
        minToMinData.setLevelLoad(parseInt(data, "nivelCarga"));
        minToMinData.setLevelCoverage(parseInt(data, "nivelCobertura"));
        minToMinData.setCanal(data.optString("canal"));
        minToMinData.setLevelName(data.optString("levelName"));
        minToMinData.setFn(data.opt("fn"));

        return minToMinData;

    }

    private static Stats ParserStats(JSONObject data) {
        if (data == null)
            return null;
        Stats stats = new Stats();
        JSONObject json = data.optJSONObject("venue");
        if (json != null) {
            Value venue = new Value();
            venue.setId(parseInt(json, "venueId"));
            venue.setValue(json.optString("valor", ""));
            stats.setVenue(venue);
        }
        stats.setCapacity(parseInt(data, "capacity"));
        json = data.optJSONObject("stadiumName");
        if (json != null) {
            Value stadiumName = new Value();
            stadiumName.setId(parseInt(json, "stadiumId"));
            stadiumName.setValue(json.optString("valor", ""));
            stats.setStadiumName(stadiumName);
        }
        stats.setAddress(data.optString("address", ""));
        stats.setAttendance(data.opt("attendance"));
        stats.setState(data.optString("state", ""));
        stats.setRevenue(data.opt("revenue"));
        stats.setDayName("dayName");
        stats.setSportName(data.optString("spotName"));
        stats.setCalifPartido(data.opt("califPartido"));
        stats.setCalifReferee(data.opt("califReferee"));
        stats.setTvp(data.opt("tvp"));
        return stats;

    }

    public static FootBallGameFull ParserFootBallGameFull(JSONObject data) {
        if (data == null)
            return null;
        FootBallGameFull footBallGameFull = new FootBallGameFull();
        ParserDataExpand(data, footBallGameFull);
        footBallGameFull.setFootballGame(ParserFootballGame(data
                .optJSONObject("fichapartido")));
        return footBallGameFull;

    }

    private static FootballGame ParserFootballGame(JSONObject data) {

        if (data == null)
            return null;
        FootballGame footballGame = new FootballGame();
        footballGame.setTimeOfGame(data.optString("tiempoDeJuego", ""));
        footballGame.setTimeStart(data.optString("horaInicio", ""));
        footballGame.setTimeEvent(parseInt(data, "tiempoEvento"));
        footballGame.setMinEvent(parseInt(data, "minutosEvento"));
        footballGame.setSecondsEvent(parseInt(data, "segundosEvento"));
        footballGame.setTimeStateEvent(data.optString("horaEstadoEvento", ""));
        footballGame.setCalifGame(data.opt("califpartido"));
        footballGame.setSpectators(data.opt("espectadores"));
        footballGame.setCollection(data.opt("recaudacion"));
        footballGame.setId(parseInt(data, "id"));
        footballGame.setDate(data.optString("fecha", ""));
        footballGame.setDay(data.optString("dia", ""));
        footballGame.setTime(data.optString("horario", ""));
        footballGame.setGmt(parseInt(data, "gmt"));
        footballGame.setDayName(data.optString("nombreDia", ""));
        footballGame.setLevelName(data.optString("nombrenivel", ""));
        footballGame.setFn(parseInt(data, "fn"));
        footballGame.setLevelLoad(parseInt(data, "nivelCarga"));
        footballGame.setLevelName(data.optString("nombrenivel", ""));
        footballGame.setLevelCoverage(parseInt(data, "nivelCovertura"));
        JSONObject json = data.optJSONObject("estadoEvento");
        if (json != null) {
            Value stateEvent = new Value();
            stateEvent.setId(parseInt(json, "idestado"));
            stateEvent.setValue(json.optString("valor", ""));
            footballGame.setStateEvent(stateEvent);
        }
        JSONArray teamArray = data.optJSONArray("equipo");
        if (teamArray != null) {
            List<Team> teams = new ArrayList<Team>();
            for (int i = 0; i < teamArray.length(); i++) {
                teams.add(ParserTeam(teamArray.optJSONObject(i)));
            }
            footballGame.setTeams(teams);
        }

        json = data.optJSONObject("incidencias");
        if (json != null) {
            JSONArray jsonIncidenceArray = json.optJSONArray("incidencia");
            if (jsonIncidenceArray != null) {

                List<IncidenceFootballGame> teams = new ArrayList<IncidenceFootballGame>();
                for (int indexTeamArray = 0; indexTeamArray < jsonIncidenceArray
                        .length(); indexTeamArray++) {
                    teams.add(ParserIncidenceFootballGame(jsonIncidenceArray
                            .optJSONObject(indexTeamArray)));
                }
                footballGame.setIncidences(teams);
            }

        }

        json = data.optJSONObject("medios");
        if (json != null) {
            List<Value> mediaList = null;
            JSONObject media = json.optJSONObject("medio");
            if (media != null) {
                mediaList = new ArrayList<Value>();
                Value med = new Value();
                med.setId(parseInt(media, "id"));
                med.setValue(media.optString("nombre", ""));
            } else {
                JSONArray jsonMediaArray = json.optJSONArray("medio");
                if (jsonMediaArray != null) {
                    mediaList = new ArrayList<Value>();
                    for (int indexMediaArray = 0; indexMediaArray < jsonMediaArray
                            .length(); indexMediaArray++) {
                        JSONObject media_ = jsonMediaArray
                                .optJSONObject(indexMediaArray);
                        if (media_ != null) {
                            Value med = new Value();
                            med.setId(parseInt(media, "id"));
                            med.setValue(media_.optString("nombre", ""));
                            mediaList.add(med);
                        }
                    }
                    footballGame.setMedia(mediaList);
                }

            }

        }
        List<Team> teams = footballGame.getTeams();
        for (int i = 0; i < teams.size(); i++) {
            loadTypeIncidenceInPlayer(teams.get(i).getPlayers(), footballGame.getIncidences());
            loadTypeIncidenceInPlayer(teams.get(i).getPlayerSubstitutes(), footballGame.getIncidences());
        }
        footballGame.setReferee(ParserReferee(data.optJSONObject("arbitro")));
        footballGame.setStadium(ParserStadium(data
                .optJSONObject("estadioapodo")));

        return footballGame;
    }

    private static void loadTypeIncidenceInPlayer(List<Player> players,
                                                  List<IncidenceFootballGame> incidences) {
        if (incidences == null || players == null)
            return;

        for (Player player : players) {

            for (IncidenceFootballGame incidence : incidences) {
                Value playerEnter = incidence.getPlayerEnter();
                Value playerLeave = incidence.getPlayerLeave();
                if (incidence.getId() == 6) {
                    //Log.i("Parser", "Cambio");
                }
                if (playerEnter != null
                        && playerEnter.getId() == player.getId()
                        || playerLeave != null
                        && playerLeave.getId() == player.getId())

                    switch (incidence.getId()) {
                        case EventTypeChange:
                            player.setChange(true);
                            break;
                        // case EventTypePlayedGoal:
                        // case EventTypeFreekickGoal:
                        // case EventTypeHeadGoal:
                        // case EventTypePenaltyGoal:
                        // case EventTypePenaltyTimeGoal:
                        // player.goals = [NSNumber numberWithInt:[player.goals
                        // intValue]+1];
                        // break;
                        // case EventTypeRedCard:
                        // case EventTypeRedCard2:
                        // player.redCard = [NSNumber numberWithBool:YES];
                        // break;
                        // case EventTypeYellowCard:
                        // player.yellowCard = [NSNumber numberWithBool:YES];
                        // break;
                        default:
                            break;

                    }

            }
        }

    }

    public static void loadFilePlayer(Player player, JSONObject data) {

        player.setFileGame(parserFileGame(data.optJSONObject("fichaJuego")));
        player.setFileIncidence(parserFileIncidence(data
                .optJSONObject("fichaIncidencias")));
        player.setFilePersonal(parserFilePersonal(data
                .optJSONObject("fichaPersonal")));

    }

    private static FileIncidence parserFileIncidence(JSONObject data) {
        if (data == null)
            return null;
        FileIncidence fileIncidence = new FileIncidence();

        fileIncidence.setGoalsTotal(data.optInt("golesTotal"));
        fileIncidence.setGoalsPenal(data.optInt("golesPenal"));
        fileIncidence.setGoalsFreeKick(data.optInt("golesTiroLibre"));
        fileIncidence.setGoalsPlay(data.optInt("golesJugada"));
        fileIncidence.setGoalShot(data.optInt("tirosArco"));
        fileIncidence.setDeflectedShot(data.optInt("tirosDesviado"));
        fileIncidence.setGoalpostShot(data.optInt("tirosPalo"));
        fileIncidence.setYellowCards(data.optInt("yellowCards"));
        fileIncidence.setRedCards(data.optInt("redCards"));
        fileIncidence.setFoulsCommitted(data.optInt("foulsCometidos"));
        fileIncidence.setFoulsReceived(data.optInt("foulsRecibidos"));
        return fileIncidence;
    }

    private static FileGame parserFileGame(JSONObject data) {
        if (data == null)
            return null;
        FileGame fileGame = new FileGame();
        JSONObject object = data.optJSONObject("Name");
        if (object != null) {
            fileGame.setFirstName(object.optString("FirstName"));
            fileGame.setFirstName(object.optString("MiddleName"));
            fileGame.setLastName(object.optString("LastName"));
            fileGame.setNickName(object.optString("NickName"));
        }

        // "Name":{
        // "FirstName":"Wagner",

        // },
        fileGame.setId(data.optInt("id"));
        fileGame.setSquadNumbre(data.optInt("squadNumber"));
        fileGame.setPosition(data.optString("position"));
        fileGame.setPositionId(data.optInt("positionId"));
        fileGame.setSubstitute(data.optString("substitute"));
        fileGame.setOrder(data.optInt("orden"));
        fileGame.setHitch(data.optString("enganche"));
        fileGame.setCaptain(data.optString("captain"));

        return fileGame;
    }

    private static FilePersonal parserFilePersonal(JSONObject data) {

        if (data == null)
            return null;
        FilePersonal filePersonal = new FilePersonal();
        filePersonal.setName(data.optString("nombre"));

        filePersonal.setLastName(data.optString("apellido"));
        filePersonal.setSkillfulSide(data.optString("ladoHabil"));
        filePersonal.setDateBirth(data.optString("fechaNacimiento"));
        filePersonal.setHourBirth(data.optString("horaNacimiento"));
        filePersonal.setAge(data.optInt("edad", 0));
        filePersonal.setWeight(data.optInt("peso", 0));
        filePersonal.setHeight(data.optInt("altura", 0));

        filePersonal.setNickName(data.optString("apodo"));
        Value value = new Value();
        JSONObject object = data.optJSONObject("rol");
        if (object != null) {
            value.setId(object.optInt("idrol", 0));
            value.setValue(object.optString("valor"));
            filePersonal.setRol(value);
        }
        filePersonal.settShirtNumbre(data.optInt("camiseta"));

        object = data.optJSONObject("pais");
        if (object != null) {
            value.setId(object.optInt("paisId", 0));
            value.setValue(object.optString("valor"));
            filePersonal.setCountry(value);
        }
        filePersonal.setProvince(data.optString("provincia"));
        object = data.optJSONObject("clubActual");
        if (object != null) {
            filePersonal.setTeamActual(ParserTeam(object));
        }

        filePersonal.setLocality(data.optString("localidad"));
        filePersonal.setId(data.optInt("id"));

        return filePersonal;
    }

    private static Stadium ParserStadium(JSONObject data) {
        if (data == null)
            return null;
        Stadium stadium = new Stadium();
        stadium.setId(parseInt(data, "id"));
        stadium.setCapacity(parseInt(data, "capacidad"));
        stadium.setClubStadium(data.optString("clubEstadio", ""));
        stadium.setValue(data.optString("valor", ""));
        return stadium;

    }

    private static Official ParserOfficial(JSONObject data) {
        if (data == null)
            return null;
        Official official = new Official();
        official.setId(parseInt(data, "id"));
        official.setType(data.optString("type", ""));
        JSONObject objectName = data.optJSONObject("Name");
        if (objectName == null)
            return null;
        official.setFirstName(objectName.optString("FirstName", ""));
        official.setMiddleName(objectName.optString("MiddleName", ""));
        official.setNickName(objectName.optString("NickName", ""));
        official.setLastName(objectName.optString("LastName", ""));
        return official;
    }

    private static IncidenceFootballGame ParserIncidenceFootballGame(
            JSONObject data) {
        if (data == null)
            return null;
        IncidenceFootballGame incidence = new IncidenceFootballGame();
        incidence.setMin(parseInt(data, "minuto"));
        incidence.setTime(data.optString("tiempo", ""));
        Value value = new Value();
        JSONObject object = data.optJSONObject("jugador");
        if (object != null) {
            value.setId(parseInt(object, "id"));
            value.setValue(object.optString("valor", ""));
            incidence.setPlayer(value);
        }
        incidence.setCodeExtra(data.opt("codigoExtra"));
        incidence.setTeamShortName(data.optString("equiponomcorto", ""));
        object = data.optJSONObject("key");
        if (object != null) {
            value = new Value();
            value.setId(parseInt(object, "id"));
            value.setValue(object.optString("valor", ""));
            incidence.setKey(value);
        }
        incidence.setType(data.optString("tipo", ""));
        incidence.setId(parseInt(data, "inciid"));
        incidence.setIdIncidence(parseInt(data, "id"));
        incidence.setOrder(parseInt(data, "orden"));
        object = data.optJSONObject("jugadorentra");
        if (object != null) {
            value = new Value();
            value.setId(parseInt(object, "id"));
            value.setValue(object.optString("valor", ""));
            incidence.setPlayerEnter(value);
        }
        object = data.optJSONObject("jugadorsale");
        if (object != null) {
            value = new Value();
            value.setId(parseInt(object, "id"));
            value.setValue(object.optString("valor", ""));
            incidence.setPlayerLeave(value);
        }

        return incidence;

    }

    private static Incidence ParserIncidence(JSONObject data) {
        if (data == null)
            return null;

        Incidence incidence = new Incidence();
        incidence.setComment(data.optString("comment", ""));
        incidence.setMinutes(parseInt(data, "minutes"));
        incidence.setSeconds(parseInt(data, "seconds"));
        incidence.setPlayerId(parseInt(data, "playerId"));
        incidence.setHalf(parseInt(data, "half"));
        incidence.setId(parseInt(data, "uniqueId"));
        //if (incidence.getId() == 6 || incidence.getId() == 7)
        //Log.i("", "");

        incidence.setCatchedById(parseInt(data, "catchedById"));
        incidence.setCatchedBy(data.optString("catchedBy", ""));
        incidence.setPlayerName(data.optString("playerName", ""));
        incidence.setKnownPlayerName(data.optString("knownPlayerName", ""));
        incidence.setReceivedId(parseInt(data, "receivedId"));
        incidence.setReceivedName(data.optString("receivedName", ""));
        incidence.setTeamId(parseInt(data, "teamId"));
        incidence.setType(data.optString("type", ""));
        incidence.setTypeId(parseInt(data, "typeId"));
        incidence.setX1(parseDouble(data, "x1"));
        incidence.setY1(parseDouble(data, "y1"));
        incidence.setZ1(parseDouble(data, "z1"));
        incidence.setX2(parseDouble(data, "x2"));
        incidence.setY2(parseDouble(data, "y2"));
        incidence.setZ2(parseDouble(data, "z2"));
        incidence.setRelativeMinutes(parseInt(data, "relativeMinutes"));
        incidence.setAssistanceBy(data.optString("assistanceBy", ""));
        incidence.setAssistanceById(parseInt(data, "assistanceById"));
        incidence.setKnownInName(data.optString("knownInName", ""));
        incidence.setKnownOffName(data.optString("knownOffName", ""));

        // "assistanceById":"18810",
        // "knownPlayerName":"Diego",
        // "comment":"",
        // "assistanceBy":"Airton Ribeiro Santos",

        // "catchedBy":"Rafael Cabral Barbosa",
        // "catchedById":"45569",
        // "receivedName":"Marcos Arouca da Silva",
        // "receivedId":"7761",
        // "offId":"3495",
        // "offName":"Paolo Guerrero",
        // "inId":"17264",
        // "inName":"Alexandre Pato",
        incidence.setOffName(data.optString("offName", ""));
        incidence.setInName(data.optString("inName", ""));
        incidence.setOffId(parseInt(data, "offId"));
        incidence.setInId(parseInt(data, "inId"));
        incidence.setTime(data.optString("hora", ""));
        return incidence;
    }

    private static Status ParserStatus(JSONObject data) {
        if (data == null)
            return null;
        Status status = new Status();
        status.setComment(data.optString("Comment", ""));
        status.setValue(data.optString("value", ""));
        status.setId(parseInt(data, "statusId"));
        status.setCurrentMinutes(parseInt(data, "CurrentMinutes"));
        status.setCurrentPeriod(parseInt(data, "CurrentPeriod"));

        return status;
    }

    public static List<Championship> ParserChampionships(JSONObject data) {
        if (data == null)
            return null;
        Championship championship = null;
        JSONArray championshipsArray = data.optJSONArray("campeonato");
        if (championshipsArray == null)
            return null;
        List<Championship> championshipList = new ArrayList<Championship>();
        for (int indexChampionshipArray = 0; indexChampionshipArray < championshipsArray
                .length(); indexChampionshipArray++) {
            championship = new Championship();
            JSONObject championshipObject = championshipsArray
                    .optJSONObject(indexChampionshipArray);
            championship.setDescription(championshipObject.optString(
                    "campeonatoDescripcion", ""));
            championship.setNameAlternative(championshipObject.optString(
                    "campeonatoNombreAlternativo", ""));
            championship.setId(parseInt(championshipObject, "id"));
            championship.setFinished(parseInt(championshipObject, "terminado"));
            championship.setValue(championshipObject.optString("valor", ""));
            championship.setDescriptionRelegation(championshipObject.optString(
                    "campeonatoDescripcionDescenso", ""));
            championship.setDates(ParserDate(championshipObject
                    .optJSONArray("fecha")));
            championshipList.add(championship);

        }
        return championshipList;
    }

    public static Scorers ParserScorers(JSONObject data) {
        if (data == null)
            return null;

        Scorers scorers = new Scorers();
        ParserDataExpand(data, scorers);
        scorers.setDateName(data.optString("fechaNombre", ""));
        scorers.setDateCurrent(parseInt(data, "fechaActual"));
        JSONArray playersArray = data.optJSONArray("persona");
        if (playersArray == null)
            return scorers;
        List<Player> players = new ArrayList<Player>();
        for (int indexPlayerArray = 0; indexPlayerArray < playersArray.length(); indexPlayerArray++) {
            JSONObject playerElement = playersArray
                    .optJSONObject(indexPlayerArray);
            players.add(ParserPlayer(playerElement));
        }
        scorers.setPlayers(players);
        return scorers;
    }

    public static Positions ParserPosition(JSONObject data) {
        if (data == null)
            return null;
        JSONObject jObject = null;
        Positions positions = new Positions();
        ParserDataExpand(data, positions);
        Data valueSimple = new Data();

        jObject = data.optJSONObject("campeonatoDescripcion");

        valueSimple.setId(parseInt(jObject, "id"));
        positions.setChampionshipDescription(valueSimple);

        jObject = data.optJSONObject("campeonatoDescripcionDescenso");

        valueSimple.setId(parseInt(jObject, "id"));
        positions.setChampionshipDescriptionRelegation(valueSimple);

        jObject = data.optJSONObject("fecha");
        positions.setDateTo(parseInt(jObject, "fechaHasta"));
        positions.setDateFrom(parseInt(jObject, "fechaDesde"));
        jObject = data.optJSONObject("fechaNombre");
        positions.setFn(parseInt(jObject, "fn"));
        positions.setFnLast(parseInt(jObject, "fnUltima"));
        positions.setValue(jObject.optString("valor", ""));
        positions.setLevel(data.optString("nivel", ""));
        positions.setIncidenceChampionship(data.opt("incidenciasCampeonato"));

        JSONArray teamArray = data.optJSONArray("equipo");
        List<Team> teams = new ArrayList<Team>();
        for (int indexTeamArray = 0; indexTeamArray < teamArray.length(); indexTeamArray++) {
            teams.add(ParserTeam(teamArray.optJSONObject(indexTeamArray)));
        }
        positions.setTeams(teams);
        return positions;
    }

    private static void ParserDataExpand(JSONObject dataObject, DataExpand data) {
        JSONObject jObject = null;
        Value value = new Value();
        Category category = new Category();
        jObject = dataObject.optJSONObject("deporte");
        value.setId(parseInt(jObject, "id"));
        if (jObject != null)
            value.setValue(jObject.optString("valor", ""));
        data.setSport(value.copy());
        jObject = dataObject.optJSONObject("categoria");
        if (jObject != null) {
            category.setCanal(jObject.optString("canal"));
            category.setId(parseInt(jObject, "id"));
            category.setValue(jObject.optString("valor"));
        }
        data.setCategory(category);
        jObject = dataObject.optJSONObject("campeonato");
        if (jObject != null)
            value.setValue(jObject.optString("valor", ""));
        value.setId(parseInt(jObject, "id"));
        data.setChampionship(value.copy());
        jObject = dataObject.optJSONObject("campeonatoNombreAlternativo");
        if (jObject != null)
            value.setValue(jObject.optString("valor", ""));
        value.setId(parseInt(jObject, "id"));
        data.setChampionshipNameAlternative(value.copy());

    }

    public static Fixture ParserFixture(JSONObject data) {
        if (data == null)
            return null;
        JSONObject jObject = null;
        Fixture fixture = new Fixture();
        Value value = new Value();
        Category category = new Category();
        jObject = data.optJSONObject("deporte");
        value.setId(parseInt(jObject, "id"));
        if (jObject != null)
            value.setValue(jObject.optString("valor", ""));
        fixture.setSport(value.copy());
        jObject = data.optJSONObject("categoria");
        if (jObject != null) {
            category.setCanal(jObject.optString("canal"));
            category.setId(parseInt(jObject, "id"));
            category.setValue(jObject.optString("valor"));
        }
        fixture.setCategory(category);

        jObject = data.optJSONObject("campeonato");
        if (jObject != null)
            value.setValue(jObject.optString("valor", ""));
        value.setId(parseInt(jObject, "id"));
        fixture.setChampionship(value.copy());

        jObject = data.optJSONObject("campeonatoNombreAlternativo");
        if (jObject != null)
            value.setValue(jObject.optString("valor", ""));
        value.setId(parseInt(jObject, "id"));
        fixture.setChampionshipNameAlternative(value.copy());
        Data valueSimple = new Data();

        jObject = data.optJSONObject("campeonatoDescripcion");

        valueSimple.setId(parseInt(jObject, "id"));
        fixture.setChampionshipDescription(valueSimple);

        jObject = data.optJSONObject("campeonatoDescripcionDescenso");

        valueSimple.setId(parseInt(jObject, "id"));
        fixture.setChampionshipDescriptionRelegation(valueSimple);
        fixture.setDateCurrent(parseInt(data, "fechaActual"));
        jObject = data.optJSONObject("horaActual");
        fixture.setTimeCurrentGmt(parseInt(jObject, "gmt"));
        fixture.setTimeCurrentValue(jObject.optString("valor", ""));
        JSONArray datesArray = data.optJSONArray("fecha");
        List<Date> dates = ParserDate(datesArray);
        if (dates != null && !dates.isEmpty() )
            fixture.setDates(dates.get(0));

        return fixture;
    }

    public static Game ParserGame(JSONObject data) {
        if (data == null)
            return null;
        JSONObject jObject = data.optJSONObject("estado");
        Game game = new Game();
        Value value = new Value();
        value.setId(parseInt(jObject, "id"));
        value.setValue(jObject.optString("valor", ""));
        game.setState(value.copy());
        game.setTimeOfGame(data.optString("tiempoDeJuego", ""));
        game.setId(parseInt(data, "id"));
        game.setDetails(parseInt(data,"detalle"));
        game.setTimeState(data.optString("horaEstado"));
        game.setHomeTeam(ParserTeam(data.optJSONObject("local")));
        game.setGoalHome(parseInt(data, "goleslocal"));
        game.setGoalDefPenaltiesHome(data.opt("golesDefPenaleslocal"));
        game.setAwayTeam(ParserTeam(data.optJSONObject("visitante")));
        game.setGoalAway(parseInt(data, "golesvisitante"));
        game.setGoalDefPenaltiesAway(data.opt("golesDefPenalesvisitante"));
        game.setReferee(ParserReferee(data.optJSONObject("arbitro")));
        game.setDate(parseInt(data, "fecha"));
        game.setTime(data.optString("hora"));
        game.setCity(data.optString("lugarCiudad"));
        game.setClubStadium(data.optString("clubEstadio"));
        game.setNameStadium(data.optString("nombreEstadio"));
        game.setIdStadium(parseInt(data, "idEstadio"));
        game.setNameDay(data.optString("nombreDia"));
        game.setLevelCoverage(parseInt(data, "nivelCobertura"));
        game.setLevelLoad(parseInt(data, "nivelCarga"));
        game.setOrderDiary(parseInt(data, "ordenAgenda"));
        game.setNumber(parseInt(data, "nro"));
        game.setNd(data.opt("nd"));
        game.setIdGan(parseInt(data, "idGan"));
        game.setPtsWin(parseInt(data, "ptsGan"));
        game.setNomWin(data.optString("nomGan"));
        // "goleadoreslocal":"",
        // "goleadoresvisitante":"",
        // "medios":"",

        return game;
    }

    public static List<Date> ParserDate(JSONArray dateArray) {

        if (dateArray == null)
            return null;
        List<Date> dateList = new ArrayList<Date>(0);
        Date date = null;

        for (int i = 0; i < dateArray.length(); i++) {
            JSONObject dateObject = dateArray.optJSONObject(i);
            date = new Date();
            date.setId(parseInt(dateObject, "id"));
            date.setDate(parseInt(dateObject, "fecha"));
            date.setDateFrom(parseInt(dateObject, "fechadesde"));
            date.setDateTo(parseInt(dateObject, "fechahasta"));
            date.setName(dateObject.optString("nombre", ""));
            date.setNameLevel(dateObject.optString("nombrenivel", ""));
            date.setLevel(parseInt(dateObject, "nivel"));
            date.setOrder(parseInt(dateObject, "orden"));
            date.setNameDay(dateObject.optString("nombreDia", ""));
            date.setState(dateObject.optString("estado", ""));
            date.setFn(parseInt(dateObject, "fn"));
            JSONObject gameObj = dateObject.optJSONObject("partido");
            List<Game> games = null;
            if (gameObj != null) {
                games = new ArrayList<Game>();
                games.add(ParserGame(gameObj));
            } else {

                JSONArray gameArray = dateObject.optJSONArray("partido");
                if (gameArray != null) {
                    games = new ArrayList<Game>();
                    for (int indexGoalsArray = 0; indexGoalsArray < gameArray
                            .length(); indexGoalsArray++) {
                        games.add(ParserGame(gameArray
                                .optJSONObject(indexGoalsArray)));
                    }
                }
            }
            date.setMatchs(games);
            dateList.add(date);

        }
        return dateList;
    }

    public static Team ParserTeam(JSONObject data) {
        if (data == null)
            return null;
        Team team = new Team();
        team.setAverage(parseDouble(data, "promedio"));
        team.setName(data.optString("nombre", ""));
        if (team.getName() == null || team.getName().equalsIgnoreCase(""))
            team.setName(data.optString("name", ""));
        team.setScore(parseInt(data, "score"));
        if (team.getScore() == -1)
            team.setScore(parseInt(data, "goles"));
        team.setAbbreviation(data.optString("abreviacion", ""));
        team.setScorePenaltyRow(parseInt(data, "scorePenaltyRow"));
        if (team.getScorePenaltyRow() == -1)
            team.setScorePenaltyRow(parseInt(data, "golesDefPenales"));
        team.setScorePenaltyRowD(parseInt(data, "scorePenaltyRowD"));
        team.setScoreTribunalDisciplina(parseInt(data,
                "scoreTribunalDisciplina"));
        if (team.getScoreTribunalDisciplina() == -1)
            team.setScoreTribunalDisciplina(parseInt(data,
                    "golesTribDisciplina"));
        team.setHomeOrAway(data.optString("homeOrAway", ""));
        if (team.getHomeOrAway().equalsIgnoreCase(""))
            team.setHomeOrAway(data.optString("condicion", ""));

        team.setSubscribedForNotifications(parseInt(data,
                "isSubscribedForNotifications") == 1);

        team.setPoints(parseInt(data, "puntos"));
        team.setPlayed(parseInt(data, "jugados"));
        team.setPlayedHome(parseInt(data, "jugadoslocal"));
        team.setPlayedAway(parseInt(data, "jugadosvisitante"));
        team.setWins(parseInt(data, "ganados"));
        team.setLost(parseInt(data, "perdidos"));
        team.setTied(parseInt(data, "empatados"));
        team.setWinsHome(parseInt(data, "ganadoslocal"));
        team.setWinsAway(parseInt(data, "ganadosvisitante"));
        team.setLostHome(parseInt(data, "perdidoslocal"));
        team.setLostAway(parseInt(data, "perdidosvisitante"));
        team.setTiedAway(parseInt(data, "empatadosvisitante"));
        team.setTiedHome(parseInt(data, "empatadoslocal"));

        team.setGoalsScored(parseInt(data, "golesfavor"));
        team.setGoalScoredAway(parseInt(data, "golesfavorvisitante"));
        team.setGoalScoredHome(parseInt(data, "golesfavorlocal"));
        team.setGoalAgainstAway(parseInt(data, "golescontravisitante"));
        team.setGoalAgainstHome(parseInt(data, "golescontralocal"));
        team.setGoalsAgainst(parseInt(data, "golescontra"));
        team.setDifGoals(parseInt(data, "difgol"));
        team.setPointsAway(parseInt(data, "puntosvisitante"));
        team.setPointsHome(parseInt(data, "puntoslocal"));
        team.setPointsPrevious1(parseInt(data, "puntosanterior1"));
        team.setPointsPrevious2(parseInt(data, "puntosanterior2"));
        team.setPlayedPrevious1(parseInt(data, "jugadosanterior1"));
        team.setPlayedPrevious1(parseInt(data, "jugadosanterior2"));

        team.setNombreActual(data.optString("nombreactual", ""));
        team.setNombreAnterior1(data.optString("nombreanterior1", ""));
        team.setNombreAnterior2(data.optString("nombreanterior2", ""));

        team.setPointsCurrent(parseInt(data, "puntosactual"));
        team.setPlayedCurrent(parseInt(data, "jugadosactual"));
        team.setPointsRelegation(parseInt(data, "puntosdescenso"));

        team.setPlayedRelegation(parseInt(data, "jugadosdescenso"));
        team.setAverageRelegation(parseDouble(data, "promediodescenso"));
        team.setAverageRelegation2(parseDouble(data, "promediodescenso2"));
        team.setDifGoalCurrent(parseInt(data, "difgolactual"));
        team.setAm(parseInt(data, "am"));
        team.setReds(parseInt(data, "rojas"));

        team.setRedsX2am(parseInt(data, "rojaX2am"));
        team.setFoulPen(parseInt(data, "faltasPen"));
        team.setHandballPen(parseInt(data, "manoPen"));
        team.setFoultCom(parseInt(data, "faltasCom"));
        team.setFoultRec(parseInt(data, "faltasRec"));
        team.setFoulPenRec(parseInt(data, "faltasPenRec"));
        team.setLevel(parseInt(data, "nivel"));
        team.setLevelDescription(data.optString("nivelDesc", ""));
        team.setOrder(parseInt(data, "orden"));
        team.setOrderDescription(data.optString("ordenDesc", ""));
        team.setDescriptionTribDisc(data.optString("descripcionTribDisc", ""));
        team.setGameDefTribDisc(data.opt("partidosDefTribDisc"));
        team.setParent(parseInt(data, "padre"));
        team.setKey(data.optString("key", ""));

        team.setId(parseInt(data, "id"));
        team.setCountry(data.optString("pais", ""));
        team.setIdCountry(parseInt(data, "paisId"));
        team.setCountryAcronym(data.optString("paisSigla", ""));
        team.setNameShort(data.optString("nombreCorto", ""));
        team.setNameAssociation(data.optString("nombreasociacion", ""));
        team.setAcronym(data.optString("sigla", ""));
        team.setNc(data.optString("nc", ""));
        team.setValue(data.optString("valor", ""));
        team.setEmblem(data.optString("escudo", ""));

        team.setShirt(data.optString("camisa", ""));
        team.setShirtGoalKeeper(data.optString("arquero", ""));
        JSONObject json = data.optJSONObject("Players");
        JSONArray arrayPlayers = null;
        if (json != null) {
            arrayPlayers = json.optJSONArray("Player");
        } else {
            arrayPlayers = data.optJSONArray("integrante");
        }

        if (arrayPlayers != null) {
            List<Player> players = new ArrayList<Player>();
            List<Player> playerSubstitutes = new ArrayList<Player>();
            for (int indexPlayerArray = 0; indexPlayerArray < arrayPlayers
                    .length(); indexPlayerArray++) {

                Player player = ParserPlayer(arrayPlayers.optJSONObject(indexPlayerArray), team);
                if (player.getType().equals("Titular")) {
                    players.add(player);
                } else {
                    playerSubstitutes.add(player);
                }

            }
            team.setPlayers(players);
            team.setPlayerSubstitutes(playerSubstitutes);
        }

        team.setGoals(parseInt(data, "goals"));
        team.setGoalsHalf1(parseInt(data, "goalsHalf1"));
        team.setGoalAttempts(parseInt(data, "goalAttempts"));
        team.setGoalAttemptsHalf1(parseInt(data, "goalAttemptsHalf1"));
        team.setFouls(parseInt(data, "fouls"));
        team.setFoulsHalf1(parseInt(data, "foulsHalf1"));
        team.setGoalShots(parseInt(data, "goalShots"));
        team.setGoalShotsHalf1(parseInt(data, "goalShotsHalf1"));
        team.setYellowCards(parseInt(data, "yellowCards"));
        team.setYellowCardsHalf1(parseInt(data, "yellowCardsHalf1"));
        team.setYellowCardsHalf2(parseInt(data, "yellowCardsHalf2"));
        team.setRedCards(parseInt(data, "redCards"));
        team.setRedCardsHalf1(parseInt(data, "redCardsHalf1"));
        team.setRedCardsHalf2(parseInt(data, "redCardsHalf2"));
        team.setCornerKicks(parseInt(data, "cornerKicks"));
        team.setCornerKicksHalf1(parseInt(data, "cornerKicksHalf1"));
        team.setFoulsHalf2(parseInt(data, "foulsHalf2"));
        team.setGoalAttemptsHalf2(parseInt(data, "goalAttemptsHalf2"));
        team.setGoalShotsHalf2(parseInt(data, "goalShotsHalf2"));
        team.setCornerKicksHalf2(parseInt(data, "cornerKicksHalf2"));
        team.setOffsidesHalf1(parseInt(data, "offsidesHalf1"));
        team.setOffsidesHalf2(parseInt(data, "offsidesHalf2"));
        team.setOffsides(parseInt(data, "offsides"));
        return team;
    }

    public static Referee ParserReferee(JSONObject data) {
        if (data == null)
            return null;
        Referee referee = new Referee();
        referee.setId(parseInt(data, "id"));
        referee.setName(data.optString("nombre", ""));
        referee.setNc(data.optString("nc", ""));
        referee.setCountry(data.optString("pais", ""));
        return referee;
    }

    public static Player ParserPlayer(JSONObject data) {
        return ParserPlayer(data, null);
    }

    public static Player ParserPlayer(JSONObject data, Team team) {
        if (data == null)
            return null;

        Player player = new Player();
        player.setName(data.optString("nombre", ""));
        player.setId(parseInt(data, "jugadorId"));
        if (player.getId() == -1)
            player.setId(parseInt(data, "id"));
        player.setNom(data.optString("nom", ""));
        player.setApe(data.optString("ape", ""));
        player.setNickName(data.optString("apodo", ""));
        player.setGames(parseInt(data, "jugada"));
        player.setHeader(parseInt(data, "cabeza"));
        player.setFreeKick(parseInt(data, "tirolibre"));
        player.setPenalty(parseInt(data, "penal"));
        player.setPicture(data.optString("foto", ""));
        player.setNameKnown(data.optString("nombreConocido", ""));
        JSONObject jsonName = data.optJSONObject("Name");

        if (jsonName == null)
            jsonName = data.optJSONObject("nombre");

        if (jsonName != null) {
            player.setMiddleName(jsonName.optString("MiddleName", ""));
            if (player.getMiddleName().equalsIgnoreCase(""))
                player.setMiddleName(jsonName.optString("valor", ""));

            player.setNom(jsonName.optString("FirstName", ""));
            if (player.getNom().equalsIgnoreCase(""))
                player.setNom(jsonName.optString("nom", ""));

            player.setApe(jsonName.optString("LastName", ""));
            if (player.getApe().equalsIgnoreCase(""))
                player.setApe(jsonName.optString("ape", ""));
            player.setNickName(jsonName.optString("NickName", ""));
            // if(player.getNickName().equalsIgnoreCase(""))
            // player.setNickName(jsonName.optString("apodo",""));

            if (player.getNameKnown().equalsIgnoreCase(""))
                player.setNameKnown(jsonName.optString("knownPlayerName", ""));

        }
        player.setPointClarin(parseInt(data, "puntosclarin"));
        player.setSquadNumber(data.opt("squadNumber"));
        if (player.getSquadNumber() == null)
            player.setSquadNumber(data.opt("camiseta"));

        player.setRol(data.optString("position", ""));
        if (player.getRol().equalsIgnoreCase(""))
            player.setRol(data.optString("rol", ""));

        player.setRolId(parseInt(data, "positionId"));
        if (player.getRolId() == -1)

            player.setRolId(parseInt(data, "idrol"));

        player.setType(data.optString("tipo", "Titular"));
        player.setSubstitute(data.optString("substitute", ""));
        if (player.getSubstitute().equalsIgnoreCase(""))
            player.setSubstitute(data.optString("tipo"));

        player.setOrder(parseInt(data, "orden"));
        player.setHitch(data.optString("enganche", ""));

        JSONObject countryObject = data.optJSONObject("pais");
        if (countryObject != null) {
            Value country = new Value();
            country.setId(parseInt(countryObject, "id"));
            country.setValue(countryObject.optString("valor", ""));
            player.setCountry(country);
        }
        player.setNameComplete(data.optString("nombreCompleto", ""));
        JSONObject goalsObj = data.optJSONObject("goles");
        if (goalsObj != null) {
            JSONObject goalObj = goalsObj.optJSONObject("gol");
            List<Goal> goals = new ArrayList<Goal>();

            if (goalObj != null) {
                goals.add(ParserGoal(goalObj));
            } else {
                JSONArray goalsArray = goalsObj.optJSONArray("gol");
                if (goalsArray == null) {
                    player.setGoals(parseInt(data, "goles"));
                    if (player.getGoals() == -1) {
                        JSONObject json = data.optJSONObject("goles");
                        player.setGoals(parseInt(json, "cant"));
                    }
                } else {
                    for (int indexGoalsArray = 0; goalsArray != null
                            && indexGoalsArray < goalsArray.length(); indexGoalsArray++) {
                        goals.add(ParserGoal(goalsArray
                                .optJSONObject(indexGoalsArray)));
                    }
                }
                player.setGoalsList(goals);
            }

        } else {
            player.setGoals(parseInt(data, "goles"));
            if (player.getGoals() == -1) {
                JSONObject json = data.optJSONObject("goles");
                player.setGoals(parseInt(json, "cant"));
            }
        }
        JSONObject json = data.optJSONObject("amarilla");
        if (json != null) {
            player.setYellowCard(parseInt(json, "cant"));
        }
        json = data.optJSONObject("roja");
        if (json != null) {
            player.setRedCard(parseInt(json, "cant"));
        }

        player.setTeam(ParserTeam(data.optJSONObject("equipo")));
        if (player.getTeam() == null && team != null) {
            player.setTeam(team);
        }

        return player;
    }

    public static SplashDynamic parserSplashDynamic(JSONObject data) {
        if (data == null)
            return null;
        SplashDynamic splashDynamic = new SplashDynamic();
        splashDynamic.setComeUrl(parseInt(data, "loadUrl"));
        splashDynamic.setUrl(data.optString("url", ""));
        return splashDynamic;
    }


    public static Goal ParserGoal(JSONObject data) {
        if (data == null)
            return null;
        Goal goal = new Goal();
        goal.setType(data.optString("tipo", ""));
        goal.setNumberGoals(parseInt(data, "cantidad"));
        return goal;
    }

    private static int parseInt(JSONObject jObject, String key) {
        if (jObject == null)
            return -1;
        try {
            return Integer.parseInt(jObject.optString(key, "-1"));
        } catch (Exception e) {
            return -1;
        }

    }

    private static double parseDouble(JSONObject jObject, String key) {
        if (jObject == null)
            return -1;
        try {
            return Double.parseDouble(jObject.optString(key, "-1"));
        } catch (Exception e) {
            return -1;
        }

    }

}
