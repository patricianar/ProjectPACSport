package com.example.projectpacsport;

import java.io.Serializable;

public class Lineup implements Serializable {
    private Player away;
    private Player home;

    public Player getAway() {
        return away;
    }

    public void setAway(Player away) {
        this.away = away;
    }

    public Player getHome() {
        return home;
    }

    public void setHome(Player home) {
        this.home = home;
    }
}
