package com.pmovil.soccer.entities;

import java.io.Serializable;

public class Incidence extends Data implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1543608421238578740L;
    private String type = new String();
    private int typeId = -1;
    private String playerName = new String();
    private int playerId = -1;
    private int teamId = -1;
    private int half = -1;
    private int minutes = -1;
    private int seconds = -1;
    private String comment = new String();
    private double x1 = -1;
    private double y1 = -1;
    private double z1 = -1;
    private double x2 = -1;
    private double y2 = -1;
    private double z2 = -1;
    private String time = new String();
    private String offName = new String();
    private int offId = -1;
    private String inName = new String();
    private int inId = -1;
    private String assistanceBy = new String();
    private int assistanceById = -1;
    private int relativeMinutes = -1;
    private String receivedName = new String();
    private int receivedId = -1;
    private String catchedBy = new String();
    private String knownPlayerName = new String();
    private int catchedById = -1;

    private String knownInName = new String();
    private String knownOffName = new String();

    private int counter = -1;
    private int counterHalf1 = -1;
    private int counterHalf2 = -1;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getHalf() {
        return half;
    }

    public void setHalf(int half) {
        this.half = half;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getZ1() {
        return z1;
    }

    public void setZ1(double z1) {
        this.z1 = z1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getZ2() {
        return z2;
    }

    public void setZ2(double z2) {
        this.z2 = z2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCatchedBy() {
        return catchedBy;
    }

    public void setCatchedBy(String catchedBy) {
        this.catchedBy = catchedBy;
    }

    public int getCatchedById() {
        return catchedById;
    }

    public void setCatchedById(int catchedById) {
        this.catchedById = catchedById;
    }

    public String getReceivedName() {
        return receivedName;
    }

    public void setReceivedName(String receivedName) {
        this.receivedName = receivedName;
    }

    public int getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(int receivedId) {
        this.receivedId = receivedId;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public int getOffId() {
        return offId;
    }

    public void setOffId(int offId) {
        this.offId = offId;
    }

    public String getInName() {
        return inName;
    }

    public void setInName(String inName) {
        this.inName = inName;
    }

    public int getInId() {
        return inId;
    }

    public void setInId(int inId) {
        this.inId = inId;
    }

    public String getAssistanceBy() {
        return assistanceBy;
    }

    public void setAssistanceBy(String assistanceBy) {
        this.assistanceBy = assistanceBy;
    }

    public int getAssistanceById() {
        return assistanceById;
    }

    public void setAssistanceById(int assistanceById) {
        this.assistanceById = assistanceById;
    }

    public int getRelativeMinutes() {
        return relativeMinutes;
    }

    public void setRelativeMinutes(int relativeMinutes) {
        this.relativeMinutes = relativeMinutes;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounterHalf1() {
        return counterHalf1;
    }

    public void setCounterHalf1(int counterHalf1) {
        this.counterHalf1 = counterHalf1;
    }

    public int getCounterHalf2() {
        return counterHalf2;
    }

    public void setCounterHalf2(int counterHalf2) {
        this.counterHalf2 = counterHalf2;
    }

    public String getKnownPlayerName() {
        return knownPlayerName;
    }

    public void setKnownPlayerName(String knownPlayerName) {
        this.knownPlayerName = knownPlayerName;
    }

    public String getKnownInName() {
        return knownInName;
    }

    public void setKnownInName(String knownInName) {
        this.knownInName = knownInName;
    }

    public String getKnownOffName() {
        return knownOffName;
    }

    public void setKnownOffName(String knownOffName) {
        this.knownOffName = knownOffName;
    }

}
