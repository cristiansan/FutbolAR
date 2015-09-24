package com.pmovil.soccer.entities;

import java.util.List;

public class Scorers extends DataExpand {

    private String dateName = new String();
    private int dateCurrent = -1;
    private List<Player> players = null;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public int getDateCurrent() {
        return dateCurrent;
    }

    public void setDateCurrent(int dateCurrent) {
        this.dateCurrent = dateCurrent;
    }

}
