package com.pmovil.soccer.entities;

import java.util.List;

public class Fixture extends DataExpand {

    private int dateCurrent = -1;
    private String timeCurrentValue = new String();
    private int timeCurrentGmt = -1;
    private Date dates = null;
    private List<Date> datesList = null;

    public int getDateCurrent() {
        return dateCurrent;
    }

    public void setDateCurrent(int dateCurrent) {
        this.dateCurrent = dateCurrent;
    }

    public String getTimeCurrentValue() {
        return timeCurrentValue;
    }

    public void setTimeCurrentValue(String timeCurrentValue) {
        this.timeCurrentValue = timeCurrentValue;
    }

    public int getTimeCurrentGmt() {
        return timeCurrentGmt;
    }

    public void setTimeCurrentGmt(int timeCurrentGmt) {
        this.timeCurrentGmt = timeCurrentGmt;
    }

    public Date getDates() {
        return dates;
    }

    public void setDates(Date dates) {
        this.dates = dates;
    }

    public List<Date> getDatesList() {
        return datesList;
    }

    public void setDatesList(List<Date> datesList) {
        this.datesList = datesList;
    }

}
