package com.pmovil.soccer.entities;

import java.util.List;

public class FootballGame extends Data {

    private List<Team> teams = null;
    private List<IncidenceFootballGame> incidences = null;
    private String timeOfGame = new String();
    private String timeStart = new String();
    private Value stateEvent = null;
    private int timeEvent = -1;
    private int minEvent = -1;
    private int secondsEvent = -1;
    private Object descEvent = -1;
    private String timeStateEvent = new String();
    private Referee referee = null;
    private Object califGame = null;
    private List<String> calif = null;
    private Stadium stadium = null;
    private Object spectators = null;
    private Object collection = null;
    private List<Value> media = null;
    private String date = new String();
    private String day = new String();
    private String time = new String();
    private int gmt = -1;
    private String dayName = new String();
    private String levelName = new String();
    private int fn = -1;
    private int levelLoad = -1;
    private int levelCoverage = -1;

    public void clear() {
        if (teams != null)
            teams.clear();
        teams = null;
        if (incidences != null)
            incidences.clear();
        incidences = null;
        if (calif != null)
            calif.clear();
        calif = null;
        if (media != null)
            media.clear();
        media = null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<IncidenceFootballGame> getIncidences() {
        return incidences;
    }

    public void setIncidences(List<IncidenceFootballGame> incidences) {
        this.incidences = incidences;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public Value getStateEvent() {
        return stateEvent;
    }

    public void setStateEvent(Value stateEvent) {
        this.stateEvent = stateEvent;
    }

    public int getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(int timeEvent) {
        this.timeEvent = timeEvent;
    }

    public int getMinEvent() {
        return minEvent;
    }

    public void setMinEvent(int minEvent) {
        this.minEvent = minEvent;
    }

    public int getSecondsEvent() {
        return secondsEvent;
    }

    public void setSecondsEvent(int secondsEvent) {
        this.secondsEvent = secondsEvent;
    }

    public Object getDescEvent() {
        return descEvent;
    }

    public void setDescEvent(Object descEvent) {
        this.descEvent = descEvent;
    }

    public String getTimeStateEvent() {
        return timeStateEvent;
    }

    public void setTimeStateEvent(String timeStateEvent) {
        this.timeStateEvent = timeStateEvent;
    }

    public Referee getReferees() {
        return referee;
    }

    public void setReferee(Referee referees) {
        this.referee = referees;
    }

    public Object getCalifGame() {
        return califGame;
    }

    public void setCalifGame(Object califGame) {
        this.califGame = califGame;
    }

    public List<String> getCalif() {
        return calif;
    }

    public void setCalif(List<String> calif) {
        this.calif = calif;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public Object getSpectators() {
        return spectators;
    }

    public void setSpectators(Object spectators) {
        this.spectators = spectators;
    }

    public Object getCollection() {
        return collection;
    }

    public void setCollection(Object collection) {
        this.collection = collection;
    }

    public List<Value> getMedia() {
        return media;
    }

    public void setMedia(List<Value> media) {
        this.media = media;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getGmt() {
        return gmt;
    }

    public void setGmt(int gmt) {
        this.gmt = gmt;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
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

    public String getTimeOfGame() {
        return timeOfGame;
    }

    public void setTimeOfGame(String timeOfGame) {
        this.timeOfGame = timeOfGame;
    }
}
