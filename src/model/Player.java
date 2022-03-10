/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author typpo
 */
public class Player implements Serializable {

    private int id;
    private String username;
    private String password;
    private String state;
    private float totalScore;
    private int totalMatch;
    private float totalSpareTime;
    private int totalWin;
    private boolean playing;

    public Player() {
        playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalMatch() {
        return totalMatch;
    }

    public void setTotalMatch(int totalMatch) {
        this.totalMatch = totalMatch;
    }

    public float getTotalSpareTime() {
        return totalSpareTime;
    }

    public void setTotalSpareTime(float totalSpareTime) {
        this.totalSpareTime = totalSpareTime;
    }

    public int getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }
}
