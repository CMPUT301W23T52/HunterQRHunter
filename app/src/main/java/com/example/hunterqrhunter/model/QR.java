package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents QR object that will be pushed to our database
 */
public class
QR {
    private String qrcode;
    private GeoPoint location;
    private int score;
    private String uid;
    private ArrayList<String> comments;
    private String name;
    private String qid;

    /**
     * @param qrcode unique code that will be a key
     * @param location location of the user
     * @param score score generated using hash
     * @param ownedBy list of users that own qr
     * @param name hashed name
     */
    public QR(String qrcode, GeoPoint location, int score, String ownedBy, String name, String qid) {
        this.qrcode = qrcode;
        this.location = location;
        this.score = score;
        this.uid = ownedBy;
        this.name = name;
        this.qid = qid;
        this.comments = new ArrayList<>(); // initialize comments list
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void addComment(String comment){
        comments.add(comment);
    }

    public void deleteComment(String comment){
        comments.remove(comment);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public List<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
