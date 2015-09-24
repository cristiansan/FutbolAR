package com.pmovil.soccer.entities;

import java.util.List;

public class MinToMinData extends Data {

    private Status status = null;
    private List<Team> teams = null;
    private List<Official> officials = null;
    private List<Data> incidencesDelete = null;
    private List<Incidence> incidences = null;
    private Stats stat = null;
    private int review = -1;
    private String date = new String();
    private int scheduledStart = -1;
    private int gmt = -1;
    private String legue = new String();
    private int legueId = -1;
    private String competition = new String();
    private int competitionId = -1;
    private String week = new String();
    private int weekId = -1;
    private int levelLoad = -1;
    private String levelName = new String();
    private int levelCoverage = -1;
    private Object fn = null;
    private String canal = new String();

    public void clear() {
        if (teams != null)
            teams.clear();
        teams = null;
        if (officials != null)
            officials.clear();
        officials = null;
        if (incidences != null)
            incidences.clear();
        incidences = null;
        if (incidencesDelete != null)
            incidencesDelete.clear();
        incidencesDelete = null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Official> getOfficials() {
        return officials;
    }

    public void setOfficials(List<Official> officials) {
        this.officials = officials;
    }

    public List<Data> getIncidencesDelete() {
        return incidencesDelete;
    }

    public void setIncidencesDelete(List<Data> incidencesDelete) {
        this.incidencesDelete = incidencesDelete;
    }

    public List<Incidence> getIncidences() {
        return incidences;
    }

    public void setIncidences(List<Incidence> incidences) {
        this.incidences = incidences;
    }

    public Stats getStat() {
        return stat;
    }

    public void setStat(Stats stat) {
        this.stat = stat;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(int scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public int getGmt() {
        return gmt;
    }

    public void setGmt(int gmt) {
        this.gmt = gmt;
    }

    public String getLegue() {
        return legue;
    }

    public void setLegue(String legue) {
        this.legue = legue;
    }

    public int getLegueId() {
        return legueId;
    }

    public void setLegueId(int legueId) {
        this.legueId = legueId;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public int getLevelLoad() {
        return levelLoad;
    }

    public void setLevelLoad(int levelLoad) {
        this.levelLoad = levelLoad;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevelCoverage() {
        return levelCoverage;
    }

    public void setLevelCoverage(int levelCoverage) {
        this.levelCoverage = levelCoverage;
    }

    public Object getFn() {
        return fn;
    }

    public void setFn(Object fn) {
        this.fn = fn;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

}
