package com.pmovil.soccer.entities;

import org.joda.time.DateTime;

import java.util.List;

public class Game extends Data {

    private String timeOfGame = new String();
    private Value state = null;
    private String timeState = new String();
    private Team homeTeam = null;
    private Team awayTeam = null;
    private int goalHome = -1;
    private int goalAway = -1;
    private List<Player> goalScorer = null;
    private Object goalDefPenaltiesAway = null;
    private Object goalDefPenaltiesHome = null;
    private Referee referee = null;
    private List<Media> media = null;
    private int date = -1;
    private DateTime dateOfPlay = null;
    private String time = new String();
    private String city = new String();
    private String nameStadium = new String();
    private String clubStadium = new String();
    private int idStadium = -1;
    private String nameDay = new String();
    private int levelLoad = -1;
    private int levelCoverage = -1;
    private int orderDiary = -1;
    private int number = -1;
    private Object type = null;
    private Object nd = null;
    private int idGan = -1;
    private String nomWin = new String();
    private int ptsWin = -1;
    private int details = 0;

    public void clear() {
        if (media != null)
            media.clear();
        if (goalScorer != null)
            goalScorer.clear();
        media = null;
        goalScorer = null;
    }

    public Value getState() {
        return state;
    }

    public void setState(Value state) {
        this.state = state;
    }

    public String getTimeState() {
        return timeState;
    }

    public void setTimeState(String timeState) {
        this.timeState = timeState;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public int getGoalHome() {
        return goalHome;
    }

    public void setGoalHome(int goalHome) {
        this.goalHome = goalHome;
    }

    public List<Player> getGoalScorer() {
        return goalScorer;
    }

    public void setGoalScorer(List<Player> goalScorer) {
        this.goalScorer = goalScorer;
    }

    public Object getGoalDefPenaltiesAway() {
        return goalDefPenaltiesAway;
    }

    public void setGoalDefPenaltiesAway(Object goalDefPenaltiesAway) {
        this.goalDefPenaltiesAway = goalDefPenaltiesAway;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNameStadium() {
        return nameStadium;
    }

    public void setNameStadium(String nameStadium) {
        this.nameStadium = nameStadium;
    }

    public String getClubStadium() {
        return clubStadium;
    }

    public void setClubStadium(String cludStadium) {
        this.clubStadium = cludStadium;
    }

    public int getIdStadium() {
        return idStadium;
    }

    public void setIdStadium(int idStadium) {
        this.idStadium = idStadium;
    }

    public String getNameDay() {
        return nameDay;
    }

    public void setNameDay(String nameDay) {
        this.nameDay = nameDay;
    }

    public int getLevelLoad() {
        return levelLoad;
    }

    public void setLevelLoad(int levelLoad) {
        this.levelLoad = levelLoad;
    }

    public int getLevelCoverage() {
        return levelCoverage;
    }

    public void setLevelCoverage(int levelCoverage) {
        this.levelCoverage = levelCoverage;
    }

    public int getOrderDiary() {
        return orderDiary;
    }

    public void setOrderDiary(int orderDiary) {
        this.orderDiary = orderDiary;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getNd() {
        return nd;
    }

    public void setNd(Object nd) {
        this.nd = nd;
    }

    public int getIdGan() {
        return idGan;
    }

    public void setIdGan(int idGan) {
        this.idGan = idGan;
    }

    public String getNomWin() {
        return nomWin;
    }

    public void setNomWin(String nomWin) {
        this.nomWin = nomWin;
    }

    public int getPtsWin() {
        return ptsWin;
    }

    public void setPtsWin(int ptsWin) {
        this.ptsWin = ptsWin;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getGoalAway() {
        return goalAway;
    }

    public void setGoalAway(int goalAway) {
        this.goalAway = goalAway;
    }

    public Object getGoalDefPenaltiesHome() {
        return goalDefPenaltiesHome;
    }

    public void setGoalDefPenaltiesHome(Object goalDefPenaltiesHome) {
        this.goalDefPenaltiesHome = goalDefPenaltiesHome;
    }

    public DateTime getDateOfPlay() {
        return dateOfPlay;
    }

    public void setDateOfPlay(DateTime dateOfPlay) {
        this.dateOfPlay = dateOfPlay;
    }

    public String getTimeOfGame() {
        return timeOfGame;
    }

    public void setTimeOfGame(String timeOfGame) {
        this.timeOfGame = timeOfGame;
    }

    public int getDetails() {
        return details;
    }

    public void setDetails(int details) {
        this.details = details;
    }


}
