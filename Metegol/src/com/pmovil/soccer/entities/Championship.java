package com.pmovil.soccer.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Championship extends Data {

    private String nameAlternative = new String();
    private String description = new String();
    private int finished = 0;
    private String descriptionRelegation = new String();
    private String value = new String();
    private int position = -1;
    private List<Date> dates = null;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getNameAlternative() {
        return nameAlternative;
    }

    public void setNameAlternative(String nameAlternative) {
        this.nameAlternative = nameAlternative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getDescriptionRelegation() {
        return descriptionRelegation;
    }

    public void setDescriptionRelegation(String descriptionRelegation) {
        this.descriptionRelegation = descriptionRelegation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCurrentDateIndex() {

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new java.util.Date());

        boolean searching = true;
        for (int indexDates = 0; searching && indexDates < dates.size(); indexDates++) {
            Date date = dates.get(indexDates);
            int dateFromInt = date.getDateFrom();
            int dateToInt = date.getDateTo();

            int dateCurrent = currentDate.get(Calendar.YEAR) * 10000
                    + (currentDate.get(Calendar.MONTH) + 1) * 100
                    + currentDate.get(Calendar.DAY_OF_MONTH);

            if ((dateCurrent - dateFromInt) <= -1) {
                searching = false;
                return indexDates - 1;
            } else if ((dateCurrent - dateToInt) <= -1) {
                searching = false;
                return indexDates;
            }
        }
        if (searching)
            return dates.size() - 1;
        else
            return 0;

    }

}
