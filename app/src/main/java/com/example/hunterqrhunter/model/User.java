package com.example.hunterqrhunter.model;

import java.util.ArrayList;

public class User {
    String uid;
    String username;
    String email;
    int totalScore;
    ArrayList<String> scannedQRs;
    public User(String username, String uid, String email, int totalScore) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.totalScore = totalScore;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public ArrayList<String> getScannedQRs() {
        return scannedQRs;
    }

    public void setScannedQRs(ArrayList<String> scannedQRs) {
        this.scannedQRs = scannedQRs;
    }
}