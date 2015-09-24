package com.pmovil.soccer.entities;

public class Goal extends Data {

    private String type = new String();
    private int numberGoals = -1;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberGoals() {
        return numberGoals;
    }

    public void setNumberGoals(int numberGoals) {
        this.numberGoals = numberGoals;
    }

}
