package com.pmovil.soccer.entities;

import java.util.List;

public class Team extends Data {

    private String homeOrAway = new String();
    private String abbreviation = new String();
    private int score = -1;
    private double average = -1;
    private int scorePenaltyRow = -1;
    private int scorePenaltyRowD = -1;
    private int scoreTribunalDisciplina = -1;
    private int idCountry = -1;
    private String name = new String();
    private int points = -1;
    private int played = -1;
    private int playedHome = -1;
    private int playedAway = -1;
    private int wins = -1;
    private int lost = -1;
    private int tied = -1;
    private int winsHome = -1;
    private int lostHome = -1;
    private int tiedHome = -1;
    private int winsAway = -1;
    private int lostAway = -1;
    private int tiedAway = -1;
    private int goalAgainstHome = -1;
    private int goalAgainstAway = -1;
    private int goalScoredHome = -1;
    private int goalScoredAway = -1;
    private int goalsScored = -1;
    private int goalsAgainst = -1;
    private int difGoals = -1;
    private int pointsHome = -1;
    private int pointsAway = -1;
    private int pointsPrevious1 = -1;
    private int pointsCurrent = -1;
    private int pointsPrevious2 = -1;
    private int playedPrevious1 = -1;
    private int playedPrevious2 = -1;
    private int difGoalCurrent = -1;
    private int playedCurrent = -1;
    private int pointsRelegation = -1;
    private int playedRelegation = -1;
    private double averageRelegation = -1;
    private double averageRelegation2 = -1;
    private int am = -1;
    private int reds = -1;
    private int redsX2am = -1;
    private int foulPen = -1;
    private int handballPen = -1;
    private int foultCom = -1;
    private int foultRec = -1;
    private int foulPenRec = -1;
    private int level = -1;
    private String levelDescription = new String();
    private int order = -1;
    private String orderDescription = new String();
    private Object descriptionTribDisc = null;
    private Object gameDefTribDisc = null;
    private int strick = -1;
    private Object parent = null;
    private String key = new String();
    private String country = new String();
    private String countryAcronym = new String();
    private String nameShort = new String();
    private String nameAssociation = new String();
    private String acronym = new String();
    private String nc = new String();
    private String value = new String();
    private String emblem = new String();
    private int position = -1;
    private List<Player> players = null;
    private List<Player> playersSubstitutes = null;
    private List<Incidence> incidences = null;
    private String shirt = new String();
    private String shirtGoalKeeper = new String();
    private int goalAttempts = -1;
    private int goalAttemptsHalf1 = -1;
    private int goalShots = -1;
    private int goalShotsHalf1 = -1;
    private int cornerKicks = -1;
    private int cornerKicksHalf1 = -1;
    private int fouls = -1;
    private int foulsHalf1 = -1;
    private int yellowCards = -1;
    private int yellowCardsHalf1 = -1;
    private int yellowCardsHalf2 = -1;
    private int redCards = -1;
    private int redCardsHalf1 = -1;
    private int redCardsHalf2 = -1;
    private int goals = -1;
    private int goalsHalf1 = -1;
    private int offsides = -1;
    private int offsidesHalf1 = -1;
    private int offsidesHalf2 = -1;
    private int goalAttemptsHalf2 = -1;
    private int foulsHalf2 = -1;
    private int goalsHalf2 = -1;
    private int goalShotsHalf2 = -1;
    private int cornerKicksHalf2 = -1;
    private boolean isSubscribedForNotifications = false;

    private String nombreActual;
    private String nombreAnterior1;
    private String nombreAnterior2;

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryAcronym() {
        return countryAcronym;
    }

    public void setCountryAcronym(String countryAcronym) {
        this.countryAcronym = countryAcronym;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameAssociation() {
        return nameAssociation;
    }

    public void setNameAssociation(String nameAssociation) {
        this.nameAssociation = nameAssociation;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEmblem() {
        return emblem;
    }

    public void setEmblem(String emblem) {
        this.emblem = emblem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getPlayedHome() {
        return playedHome;
    }

    public void setPlayedHome(int playedHome) {
        this.playedHome = playedHome;
    }

    public int getPlayedAway() {
        return playedAway;
    }

    public void setPlayedAway(int playedAway) {
        this.playedAway = playedAway;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getTied() {
        return tied;
    }

    public void setTied(int tied) {
        this.tied = tied;
    }

    public int getWinsHome() {
        return winsHome;
    }

    public void setWinsHome(int winsHome) {
        this.winsHome = winsHome;
    }

    public int getLostHome() {
        return lostHome;
    }

    public void setLostHome(int lostHome) {
        this.lostHome = lostHome;
    }

    public int getTiedHome() {
        return tiedHome;
    }

    public void setTiedHome(int tiedHome) {
        this.tiedHome = tiedHome;
    }

    public int getWinsAway() {
        return winsAway;
    }

    public void setWinsAway(int winsAway) {
        this.winsAway = winsAway;
    }

    public int getLostAway() {
        return lostAway;
    }

    public void setLostAway(int lostAway) {
        this.lostAway = lostAway;
    }

    public int getTiedAway() {
        return tiedAway;
    }

    public void setTiedAway(int tiedAway) {
        this.tiedAway = tiedAway;
    }

    public int getGoalAgainstHome() {
        return goalAgainstHome;
    }

    public void setGoalAgainstHome(int goalAgainstHome) {
        this.goalAgainstHome = goalAgainstHome;
    }

    public int getGoalAgainstAway() {
        return goalAgainstAway;
    }

    public void setGoalAgainstAway(int goalAgainstAway) {
        this.goalAgainstAway = goalAgainstAway;
    }

    public int getGoalScoredHome() {
        return goalScoredHome;
    }

    public void setGoalScoredHome(int goalScoredHome) {
        this.goalScoredHome = goalScoredHome;
    }

    public int getGoalScoredAway() {
        return goalScoredAway;
    }

    public void setGoalScoredAway(int goalScoredAway) {
        this.goalScoredAway = goalScoredAway;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getDifGoals() {
        return difGoals;
    }

    public void setDifGoals(int difGoals) {
        this.difGoals = difGoals;
    }

    public int getPointsHome() {
        return pointsHome;
    }

    public void setPointsHome(int pointsHome) {
        this.pointsHome = pointsHome;
    }

    public int getPointsAway() {
        return pointsAway;
    }

    public void setPointsAway(int pointsAway) {
        this.pointsAway = pointsAway;
    }

    public int getPointsPrevious1() {
        return pointsPrevious1;
    }

    public void setPointsPrevious1(int pointsPrevious1) {
        this.pointsPrevious1 = pointsPrevious1;
    }

    public int getPointsPrevious2() {
        return pointsPrevious2;
    }

    public void setPointsPrevious2(int pointsPrevious2) {
        this.pointsPrevious2 = pointsPrevious2;
    }

    public int getPlayedPrevious1() {
        return playedPrevious1;
    }

    public void setPlayedPrevious1(int playedPrevious1) {
        this.playedPrevious1 = playedPrevious1;
    }

    public int getPlayedPrevious2() {
        return playedPrevious2;
    }

    public void setPlayedPrevious2(int playedPrevious2) {
        this.playedPrevious2 = playedPrevious2;
    }

    public int getDifGoalCurrent() {
        return difGoalCurrent;
    }

    public void setDifGoalCurrent(int difGoalCurrent) {
        this.difGoalCurrent = difGoalCurrent;
    }

    public int getPlayedCurrent() {
        return playedCurrent;
    }

    public void setPlayedCurrent(int playedCurrent) {
        this.playedCurrent = playedCurrent;
    }

    public int getPointsRelegation() {
        return pointsRelegation;
    }

    public void setPointsRelegation(int pointsRelegation) {
        this.pointsRelegation = pointsRelegation;
    }

    public int getPlayedRelegation() {
        return playedRelegation;
    }

    public void setPlayedRelegation(int playedRelegation) {
        this.playedRelegation = playedRelegation;
    }

    public double getAverageRelegation() {
        return averageRelegation;
    }

    public void setAverageRelegation(double averageRelegation) {
        this.averageRelegation = averageRelegation;
    }

    public double getAverageRelegation2() {
        return averageRelegation2;
    }

    public void setAverageRelegation2(double averageRelegation2) {
        this.averageRelegation2 = averageRelegation2;
    }

    public int getAm() {
        return am;
    }

    public void setAm(int am) {
        this.am = am;
    }

    public int getReds() {
        return reds;
    }

    public void setReds(int reds) {
        this.reds = reds;
    }

    public int getRedsX2am() {
        return redsX2am;
    }

    public void setRedsX2am(int redsX2am) {
        this.redsX2am = redsX2am;
    }

    public int getFoulPen() {
        return foulPen;
    }

    public void setFoulPen(int foulPen) {
        this.foulPen = foulPen;
    }

    public int getHandballPen() {
        return handballPen;
    }

    public void setHandballPen(int handballPen) {
        this.handballPen = handballPen;
    }

    public int getFoultCom() {
        return foultCom;
    }

    public void setFoultCom(int foultCom) {
        this.foultCom = foultCom;
    }

    public int getFoultRec() {
        return foultRec;
    }

    public void setFoultRec(int foultRec) {
        this.foultRec = foultRec;
    }

    public int getFoulPenRec() {
        return foulPenRec;
    }

    public void setFoulPenRec(int foulPenRec) {
        this.foulPenRec = foulPenRec;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public void setLevelDescription(String levelDescription) {
        this.levelDescription = levelDescription;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public Object getDescriptionTribDisc() {
        return descriptionTribDisc;
    }

    public void setDescriptionTribDisc(Object descriptionTribDisc) {
        this.descriptionTribDisc = descriptionTribDisc;
    }

    public Object getGameDefTribDisc() {
        return gameDefTribDisc;
    }

    public void setGameDefTribDisc(Object gameDefTribDisc) {
        this.gameDefTribDisc = gameDefTribDisc;
    }

    public int getStrick() {
        return strick;
    }

    public void setStrick(int strick) {
        this.strick = strick;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPointsCurrent() {
        return pointsCurrent;
    }

    public void setPointsCurrent(int pointsCurrent) {
        this.pointsCurrent = pointsCurrent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHomeOrAway() {
        return homeOrAway;
    }

    public void setHomeOrAway(String homeOrAway) {
        this.homeOrAway = homeOrAway;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScorePenaltyRow() {
        return scorePenaltyRow;
    }

    public void setScorePenaltyRow(int scorePenaltyRow) {
        this.scorePenaltyRow = scorePenaltyRow;
    }

    public int getScorePenaltyRowD() {
        return scorePenaltyRowD;
    }

    public void setScorePenaltyRowD(int scorePenaltyRowD) {
        this.scorePenaltyRowD = scorePenaltyRowD;
    }

    public int getScoreTribunalDisciplina() {
        return scoreTribunalDisciplina;
    }

    public void setScoreTribunalDisciplina(int scoreTribunalDisciplina) {
        this.scoreTribunalDisciplina = scoreTribunalDisciplina;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayerSubstitutes() {
        return playersSubstitutes;
    }

    public void setPlayerSubstitutes(List<Player> playersSubstitutes) {
        this.playersSubstitutes = playersSubstitutes;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public List<Incidence> getIncidences() {
        return incidences;
    }

    public void setIncidences(List<Incidence> incidences) {
        this.incidences = incidences;
    }

    public int getGoalAttempts() {
        return goalAttempts;
    }

    public void setGoalAttempts(int goalAttempts) {
        this.goalAttempts = goalAttempts;
    }

    public int getGoalAttemptsHalf1() {
        return goalAttemptsHalf1;
    }

    public void setGoalAttemptsHalf1(int goalAttemptsHalf1) {
        this.goalAttemptsHalf1 = goalAttemptsHalf1;
    }

    public int getGoalShots() {
        return goalShots;
    }

    public void setGoalShots(int goalShots) {
        this.goalShots = goalShots;
    }

    public int getGoalShotsHalf1() {
        return goalShotsHalf1;
    }

    public void setGoalShotsHalf1(int goalShotsHalf1) {
        this.goalShotsHalf1 = goalShotsHalf1;
    }

    public int getCornerKicks() {
        return cornerKicks;
    }

    public void setCornerKicks(int cornerKicks) {
        this.cornerKicks = cornerKicks;
    }

    public int getCornerKicksHalf1() {
        return cornerKicksHalf1;
    }

    public void setCornerKicksHalf1(int cornerKicksHalf1) {
        this.cornerKicksHalf1 = cornerKicksHalf1;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getFoulsHalf1() {
        return foulsHalf1;
    }

    public void setFoulsHalf1(int foulsHalf1) {
        this.foulsHalf1 = foulsHalf1;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getYellowCardsHalf1() {
        return yellowCardsHalf1;
    }

    public void setYellowCardsHalf1(int yellowCardsHalf1) {
        this.yellowCardsHalf1 = yellowCardsHalf1;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getGoalsHalf1() {
        return goalsHalf1;
    }

    public void setGoalsHalf1(int goalsHalf1) {
        this.goalsHalf1 = goalsHalf1;
    }

    public int getOffsides() {
        return offsides;
    }

    public void setOffsides(int offsides) {
        this.offsides = offsides;
    }

    public int getOffsidesHalf1() {
        return offsidesHalf1;
    }

    public void setOffsidesHalf1(int offsidesHalf1) {
        this.offsidesHalf1 = offsidesHalf1;
    }

    public int getGoalAttemptsHalf2() {
        return goalAttemptsHalf2;
    }

    public void setGoalAttemptsHalf2(int goalAttemptsHalf2) {
        this.goalAttemptsHalf2 = goalAttemptsHalf2;
    }

    public int getFoulsHalf2() {
        return foulsHalf2;
    }

    public void setFoulsHalf2(int foulsHalf2) {
        this.foulsHalf2 = foulsHalf2;
    }

    public int getGoalsHalf2() {
        return goalsHalf2;
    }

    public void setGoalsHalf2(int goalsHalf2) {
        this.goalsHalf2 = goalsHalf2;
    }

    public int getGoalShotsHalf2() {
        return goalShotsHalf2;
    }

    public void setGoalShotsHalf2(int goalShotsHalf2) {
        this.goalShotsHalf2 = goalShotsHalf2;
    }

    public int getCornerKicksHalf2() {
        return cornerKicksHalf2;
    }

    public void setCornerKicksHalf2(int cornerKicksHalf2) {
        this.cornerKicksHalf2 = cornerKicksHalf2;
    }

    public int getYellowCardsHalf2() {
        return yellowCardsHalf2;
    }

    public void setYellowCardsHalf2(int yellowCardsHalf2) {
        this.yellowCardsHalf2 = yellowCardsHalf2;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public int getRedCardsHalf1() {
        return redCardsHalf1;
    }

    public void setRedCardsHalf1(int redCardsHalf1) {
        this.redCardsHalf1 = redCardsHalf1;
    }

    public int getRedCardsHalf2() {
        return redCardsHalf2;
    }

    public void setRedCardsHalf2(int redCardsHalf2) {
        this.redCardsHalf2 = redCardsHalf2;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getOffsidesHalf2() {
        return offsidesHalf2;
    }

    public void setOffsidesHalf2(int offsidesHalf2) {
        this.offsidesHalf2 = offsidesHalf2;
    }

    public boolean isSubscribedForNotifications() {
        return isSubscribedForNotifications;
    }

    public void setSubscribedForNotifications(
            boolean isSubscribedForNotifications) {
        this.isSubscribedForNotifications = isSubscribedForNotifications;
    }

    public String getShirt() {
        return shirt;
    }

    public void setShirt(String shirt) {
        this.shirt = shirt;
    }

    public String getShirtGoalKeeper() {
        return shirtGoalKeeper;
    }

    public void setShirtGoalKeeper(String shirtGoalKeeper) {
        this.shirtGoalKeeper = shirtGoalKeeper;
    }

    public String getNombreActual() {
        return nombreActual;
    }

    public void setNombreActual(String nombreActual) {
        this.nombreActual = nombreActual;
    }

    public String getNombreAnterior1() {
        return nombreAnterior1;
    }

    public void setNombreAnterior1(String nombreAnterior1) {
        this.nombreAnterior1 = nombreAnterior1;
    }

    public String getNombreAnterior2() {
        return nombreAnterior2;
    }

    public void setNombreAnterior2(String nombreAnterior2) {
        this.nombreAnterior2 = nombreAnterior2;
    }
}
