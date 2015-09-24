package com.pmovil.soccer.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Player extends Data implements Comparator<Player>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2978055526561926703L;
    private Object squadNumber = null;
    private String name = new String();
    private String nom = new String();
    private String nameKnown = new String();

    private String ape = new String();
    private String nameComplete = new String();
    private List<Goal> goalsList = null;
    private String nickName = new String();
    private String middleName = new String();
    private Value country = null;
    private Team team = null;
    private int goals = -1;
    private int header = -1;
    private int penalty = -1;
    private int move = -1;
    private int freeKick = -1;
    private String picture = new String();
    private int games = -1;
    private int pointClarin = -1;
    private int position = -1;
    private int rolId = -1;
    private String rol = new String();
    private int redCard = -1;
    private int yellowCard = -1;
    private String type = new String();
    private String substitute = new String();
    private int order = -1;
    private String hitch = new String();
    private boolean change = false;
    private FileGame fileGame = null;
    private FileIncidence fileIncidence = null;
    private FilePersonal filePersonal = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getNameComplete() {
        return nameComplete;
    }

    public void setNameComplete(String nameComplete) {
        this.nameComplete = nameComplete;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public List<Goal> getGoalsList() {
        return goalsList;
    }

    public void setGoalsList(List<Goal> goalsList) {
        this.goalsList = goalsList;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getFreeKick() {
        return freeKick;
    }

    public void setFreeKick(int freeKick) {
        this.freeKick = freeKick;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public Value getCountry() {
        return country;
    }

    public void setCountry(Value country) {
        this.country = country;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getSquadNumber() {
        return squadNumber;
    }

    public void setSquadNumber(Object squadNumber) {
        this.squadNumber = squadNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubstitute() {
        return substitute;
    }

    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getHitch() {
        return hitch;
    }

    public void setHitch(String hitch) {
        this.hitch = hitch;
    }

    public int getPointClarin() {
        return pointClarin;
    }

    public void setPointClarin(int pointClarin) {
        this.pointClarin = pointClarin;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getRedCard() {
        return redCard;
    }

    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }

    public int getYellowCard() {
        return yellowCard;
    }

    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int compare(Player player1, Player player2) {
        Integer i1 = player1.getOrder();
        Integer i2 = player2.getOrder();
        if (i1 > i2) {
            return 1;
        } else if (i1 < i2) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getNameKnown() {
        return nameKnown;
    }

    public void setNameKnown(String nameKnown) {
        this.nameKnown = nameKnown;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public FileGame getFileGame() {
        return fileGame;
    }

    public void setFileGame(FileGame fileGame) {
        this.fileGame = fileGame;
    }

    public FileIncidence getFileIncidence() {
        return fileIncidence;
    }

    public void setFileIncidence(FileIncidence fileIncidence) {
        this.fileIncidence = fileIncidence;
    }

    public FilePersonal getFilePersonal() {
        return filePersonal;
    }

    public void setFilePersonal(FilePersonal filePersonal) {
        this.filePersonal = filePersonal;
    }

    @Override
    public String toString() {
        return "Player [squadNumber=" + squadNumber + ", name=" + name
                + ", nom=" + nom + ", nameKnown=" + nameKnown + ", ape=" + ape
                + ", nameComplete=" + nameComplete + ", goalsList=" + goalsList
                + ", nickName=" + nickName + ", middleName=" + middleName
                + ", country=" + country + ", team=" + team + ", goals="
                + goals + ", header=" + header + ", penalty=" + penalty
                + ", move=" + move + ", freeKick=" + freeKick + ", picture="
                + picture + ", games=" + games + ", pointClarin=" + pointClarin
                + ", position=" + position + ", rolId=" + rolId + ", rol="
                + rol + ", redCard=" + redCard + ", yellowCard=" + yellowCard
                + ", substitute=" + substitute + ", order=" + order
                + ", hitch=" + hitch + ", change=" + change + ", fileGame="
                + fileGame + ", fileIncidence=" + fileIncidence
                + ", filePersonal=" + filePersonal + "]";
    }
}
