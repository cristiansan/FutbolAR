package com.pmovil.soccer.entities;

public class FilePersonal extends Data {

    private String name = new String();
    private String nickName = new String();
    private String lastName = new String();
    private String skillfulSide = new String();
    private String dateBirth = new String();
    private String hourBirth = new String();
    private int age = 0;
    private int weight = 0;
    private int height = 0;
    private Value rol = null;
    private Value country = null;
    private Team teamActual = null;
    private int tShirtNumbre = 0;
    private String locality = new String();
    private String province = new String();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSkillfulSide() {
        return skillfulSide;
    }

    public void setSkillfulSide(String skillfulSide) {
        this.skillfulSide = skillfulSide;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getHourBirth() {
        return hourBirth;
    }

    public void setHourBirth(String hourBirth) {
        this.hourBirth = hourBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Value getRol() {
        return rol;
    }

    public void setRol(Value rol) {
        this.rol = rol;
    }

    public Value getCountry() {
        return country;
    }

    public void setCountry(Value country) {
        this.country = country;
    }

    public Team getTeamActual() {
        return teamActual;
    }

    public void setTeamActual(Team teamActual) {
        this.teamActual = teamActual;
    }

    public int gettShirtNumbre() {
        return tShirtNumbre;
    }

    public void settShirtNumbre(int tShirtNumbre) {
        this.tShirtNumbre = tShirtNumbre;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

}
