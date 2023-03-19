package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents QR object that will be pushed to our database
 *
 */
public class QR {
    private String qrcode;
    private GeoPoint location;
    private int score;
    private ArrayList<String> ownedBy;
    private String hashName;
    private ArrayList<String> comments;

    /**
     *
     * @param qrcode unique code that will be a key
     * @param location location of the user
     * @param score score generated using hash
     * @param ownedBy list of users that own qr
     * @param hashName hashed name
     */
    public QR(String qrcode, GeoPoint location, int score, ArrayList<String> ownedBy, String hashName,ArrayList<String> comments) {
        this.qrcode = qrcode;
        this.location = location;
        this.score = score;
        this.ownedBy = ownedBy;
        this.hashName = hashName;
        this.comments = comments;
    }
    public QR(String qrcode, GeoPoint location, int score, ArrayList<String> ownedBy, String hashName) {
        this.qrcode = qrcode;
        this.location = location;
        this.score = score;
        this.ownedBy = ownedBy;
        this.hashName = hashName;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("hashName", this.hashName);
        result.put("qrcode", this.qrcode);
        result.put("score",this.score);
        result.put("ownedBy",this.ownedBy);
        result.put("comments",this.comments);

        return result;
    }
    public String getCollectionName() {
        return "QR";
    }

    public QR(String qrcode) {
        this.qrcode = qrcode;
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

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}

