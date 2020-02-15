package com.example.projectpacsport.player;

public class Player {
    private String name;
    private String birthday;
    private String birthPlace;
    private String team;

    private double height;
    private double weight;

    public Player() {
    }

    public Player(String name, String birthday, String birthPlace, String team, double height, double weight) {
        this.name = name;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.team = team;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
