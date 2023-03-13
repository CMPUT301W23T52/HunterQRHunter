package com.example.hunterqrhunter.page;

import com.example.hunterqrhunter.page.QRMapScreen;
import com.google.firebase.firestore.GeoPoint;

public class Qrcode extends QRMapScreen {
    public String hashName;
    public GeoPoint location;
    public int score;
    public String[] ownedBy;

    public Qrcode(String hashName, GeoPoint location, int score, String[] ownedBy) {
        this.hashName = hashName;
        this.location = location;
        this.score = score;
        this.ownedBy = ownedBy;
    }

}