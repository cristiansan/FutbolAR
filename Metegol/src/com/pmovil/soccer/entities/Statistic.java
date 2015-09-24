package com.pmovil.soccer.entities;

public class Statistic {

    private String valueHome = new String();
    private String valueAway = new String();
    private String valueTitle = new String();
    private String minutes = new String();
    private Boolean isSummary = false;
    private int summaryImage = -1;
    private int pos = -1;

    public String getValueHome() {
        return valueHome;
    }

    public void setValueHome(String valueHome) {
        this.valueHome = valueHome;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getValueAway() {
        return valueAway;
    }

    public void setValueAway(String valueAway) {
        this.valueAway = valueAway;
    }

    public String getValueTitle() {
        return valueTitle;
    }

    public void setValueTitle(String valueTitle) {
        this.valueTitle = valueTitle;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Boolean getIsSummary() {
        return this.isSummary;
    }

    public void setIsSummary(Boolean isSummary) {
        this.isSummary = isSummary;
    }

    public int getSummaryImage() {
        return this.summaryImage;
    }

    public void setSummaryImage(int summaryImage) {
        this.summaryImage = summaryImage;
    }

}
