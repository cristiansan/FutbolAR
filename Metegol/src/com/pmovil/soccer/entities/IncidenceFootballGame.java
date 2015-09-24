package com.pmovil.soccer.entities;

public class IncidenceFootballGame extends Data {

    private int min = -1;
    private String time = new String();
    private Value player = null;
    private Object codeExtra = null;
    private String teamShortName = new String();
    private Value key = null;
    private String type = new String();
    private int idIncidence = -1;
    private int order = -1;
    private Value playerLeave = null;
    private Value playerEnter = null;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Value getPlayer() {
        return player;
    }

    public void setPlayer(Value player) {
        this.player = player;
    }

    public Object getCodeExtra() {
        return codeExtra;
    }

    public void setCodeExtra(Object codeExtra) {
        this.codeExtra = codeExtra;
    }

    public String getTeamShortName() {
        return teamShortName;
    }

    public void setTeamShortName(String teamShortName) {
        this.teamShortName = teamShortName;
    }

    public Value getKey() {
        return key;
    }

    public void setKey(Value key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdIncidence() {
        return idIncidence;
    }

    public void setIdIncidence(int idIncidence) {
        this.idIncidence = idIncidence;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Value getPlayerLeave() {
        return playerLeave;
    }

    public void setPlayerLeave(Value playerLeave) {
        this.playerLeave = playerLeave;
    }

    public Value getPlayerEnter() {
        return playerEnter;
    }

    public void setPlayerEnter(Value playerEnter) {
        this.playerEnter = playerEnter;
    }

}
