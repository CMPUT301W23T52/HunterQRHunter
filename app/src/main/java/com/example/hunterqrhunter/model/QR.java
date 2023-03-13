package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class QR {
    private String qrcode;
    private GeoPoint location;
    private int score;
    private ArrayList<String> ownedBy;
    private String hashName;

    public QR(String qrcode, GeoPoint location, int score, ArrayList<String> ownedBy, String hashName) {
        this.qrcode = qrcode;
        this.location = location;
        this.score = score;
        this.ownedBy = ownedBy;
        this.hashName = hashName;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<String> getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(ArrayList<String> ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getHashName() {
        return hashName;
    }

    public void setHashName(String hashName) {
        this.hashName = hashName;
    }
}

