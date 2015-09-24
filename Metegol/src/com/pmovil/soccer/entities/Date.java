package com.pmovil.soccer.entities;

import java.util.List;

public class Date extends Data {

    private int date = -1;
    private int dateFrom = -1;
    private int dateTo = -1;
    private String name = new String();
    private Object nameLevel = new Object();
    private int level = -1;
    private int order = -1;
    private String nameDay = new String();
    private int fn = 1;
    private Object state = new String();
    private List<Game> matchs = null;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom) {
        this.dateFrom = dateFrom;
    }

    public int getDateTo() {
        return dateTo;
    }

    public void setDateTo(int dateTo) {
        this.dateTo = dateTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(Object nameNivel) {
        this.nameLevel = nameNivel;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getNameDay() {
        return nameDay;
    }

    public void setNameDay(String nameDay) {
        this.nameDay = nameDay;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public List<Game> getMatchs() {
        return matchs;
    }

    public void setMatchs(List<Game> matchs) {
        this.matchs = matchs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
