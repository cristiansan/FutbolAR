package com.pmovil.soccer.entities;

public class Stats extends Data {

    private Value venue = null;
    private int capacity = -1;
    private Value stadiumName = null;
    private String address = null;
    private String state = null;
    private Object attendance = null;
    private Object revenue = null;
    private String dayName = new String();
    private String sportName = new String();
    private Object califPartido = null;
    private Object califReferee = null;
    private Object tvp = null;

    public Value getVenue() {
        return venue;
    }

    public void setVenue(Value venue) {
        this.venue = venue;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Value getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(Value stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getAttendance() {
        return attendance;
    }

    public void setAttendance(Object attendance) {
        this.attendance = attendance;
    }

    public Object getRevenue() {
        return revenue;
    }

    public void setRevenue(Object revenue) {
        this.revenue = revenue;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public Object getCalifPartido() {
        return califPartido;
    }

    public void setCalifPartido(Object califPartido) {
        this.califPartido = califPartido;
    }

    public Object getCalifReferee() {
        return califReferee;
    }

    public void setCalifReferee(Object califReferee) {
        this.califReferee = califReferee;
    }

    public Object getTvp() {
        return tvp;
    }

    public void setTvp(Object tvp) {
        this.tvp = tvp;
    }

}
