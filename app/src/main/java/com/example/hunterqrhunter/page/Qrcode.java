package com.example.hunterqrhunter.page;

import com.google.firebase.firestore.GeoPoint;

public class Qrcode extends QRMapScreen {
    public String hashName;
    public GeoPoint location;
    public int score;
    public String qid;
//    public String[] ownedBy;

    public Qrcode(String hashName, GeoPoint location, int score, String qid) {
        this.hashName = hashName;
        this.location = location;
        this.score = score;
        this.qid = qid;
//        this.ownedBy = ownedBy;
    }

//    public Qrcode() {
//
//    }
//
//    protected List mockscoreList() {
//        List<Integer> scoreList = null;
//        scoreList.add(0);
//        return scoreList;
//    }



}