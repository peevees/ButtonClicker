package com.mikecoding.buttonclicker;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class HighScoreItem {

    private String name;
    private int score;

    public HighScoreItem() {

    }

    public HighScoreItem(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
