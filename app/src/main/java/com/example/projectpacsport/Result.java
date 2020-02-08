package com.example.projectpacsport;

import java.io.Serializable;

class Result implements Serializable {
    private Team awayTeam;
    private Team homeTeam;

    Team getAwayTeam() {
        return awayTeam;
    }

    void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    Team getHomeTeam() {
        return homeTeam;
    }

    void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }
}
