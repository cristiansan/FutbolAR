package com.pmovil.soccer.entities;

public class Winer extends Data {

    private int position = -1;
    private int pointWiner = -1;
    private String team = new String();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPointWiner() {
        return pointWiner;
    }

    public void setPointWiner(int pointWiner) {
        this.pointWiner = pointWiner;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}
