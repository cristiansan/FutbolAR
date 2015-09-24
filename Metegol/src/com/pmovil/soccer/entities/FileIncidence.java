package com.pmovil.soccer.entities;

public class FileIncidence extends Data {

    private int goalsTotal = 0;
    private int goalsPenal = 0;
    private int goalsFreeKick = 0;
    private int goalsPlay = 0;
    private int deflectedShot = 0;
    private int goalpostShot = 0;
    private int goalShot = 0;
    private int yellowCards = 0;
    private int redCards = 0;
    private int foulsCommitted = 0;
    private int foulsReceived = 0;

    public int getGoalsTotal() {
        return goalsTotal;
    }

    public void setGoalsTotal(int goalsTotal) {
        this.goalsTotal = goalsTotal;
    }

    public int getGoalsPenal() {
        return goalsPenal;
    }

    public void setGoalsPenal(int goalsPenal) {
        this.goalsPenal = goalsPenal;
    }

    public int getGoalsFreeKick() {
        return goalsFreeKick;
    }

    public void setGoalsFreeKick(int goalsFreeKick) {
        this.goalsFreeKick = goalsFreeKick;
    }

    public int getGoalsPlay() {
        return goalsPlay;
    }

    public void setGoalsPlay(int goalsPlay) {
        this.goalsPlay = goalsPlay;
    }

    public int getDeflectedShot() {
        return deflectedShot;
    }

    public void setDeflectedShot(int deflectedShot) {
        this.deflectedShot = deflectedShot;
    }

    public int getGoalShot() {
        return goalShot;
    }

    public void setGoalShot(int goalShot) {
        this.goalShot = goalShot;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public int getFoulsCommitted() {
        return foulsCommitted;
    }

    public void setFoulsCommitted(int foulsCommitted) {
        this.foulsCommitted = foulsCommitted;
    }

    public int getFoulsReceived() {
        return foulsReceived;
    }

    public void setFoulsReceived(int foulsReceived) {
        this.foulsReceived = foulsReceived;
    }

    public int getGoalpostShot() {
        return goalpostShot;
    }

    public void setGoalpostShot(int goalpostShot) {
        this.goalpostShot = goalpostShot;
    }

}
