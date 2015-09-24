package com.pmovil.soccer.entities;

import java.util.List;

public class Positions extends DataExpand {

    private int dateTo = -1;
    private int dateFrom = -1;
    private int fn = -1;
    private int fnLast = -1;
    private String value = new String();
    private String level = new String();
    private Object incidenceChampionship = null;
    private List<Team> teams = null;

    public int getDateTo() {
        return dateTo;
    }

    public void setDateTo(int dateTo) {
        this.dateTo = dateTo;
    }

    public int getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom) {
        this.dateFrom = dateFrom;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public int getFnLast() {
        return fnLast;
    }

    public void setFnLast(int fnLast) {
        this.fnLast = fnLast;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Object getIncidenceChampionship() {
        return incidenceChampionship;
    }

    public void setIncidenceChampionship(Object incidenceChampionship) {
        this.incidenceChampionship = incidenceChampionship;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
