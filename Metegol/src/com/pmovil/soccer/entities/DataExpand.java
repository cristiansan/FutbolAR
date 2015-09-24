package com.pmovil.soccer.entities;

public class DataExpand extends Data {

    private Value sport = null;
    private Category category = null;
    private Value championship = null;
    private Value championshipNameAlternative = null;
    private Data championshipDescription = null;
    private Data championshipDescriptionRelegation = null;

    public Value getSport() {
        return sport;
    }

    public void setSport(Value sport) {
        this.sport = sport;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Value getChampionship() {
        return championship;
    }

    public void setChampionship(Value championship) {
        this.championship = championship;
    }

    public Value getChampionshipNameAlternative() {
        return championshipNameAlternative;
    }

    public void setChampionshipNameAlternative(Value championshipNameAlternative) {
        this.championshipNameAlternative = championshipNameAlternative;
    }

    public Data getChampionshipDescription() {
        return championshipDescription;
    }

    public void setChampionshipDescription(Data championshipDescription) {
        this.championshipDescription = championshipDescription;
    }

    public Data getChampionshipDescriptionRelegation() {
        return championshipDescriptionRelegation;
    }

    public void setChampionshipDescriptionRelegation(
            Data championshipDescriptionRelegation) {
        this.championshipDescriptionRelegation = championshipDescriptionRelegation;
    }
}
