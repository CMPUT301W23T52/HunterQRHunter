package com.example.hunterqrhunter;

import com.example.hunterqrhunter.page.QRMapScreen;
import com.google.firebase.firestore.GeoPoint;

public class Qrcode extends QRMapScreen {
    int hashCode;
    String hashName;
    String hashImage;
    public GeoPoint location;
    public int score;
    String[] ownedBy;
    String[] comments;

    public Qrcode(int hashCode, String hashName, String hashImage, GeoPoint location, int score, String[] ownedBy, String[] comments) {
        this.hashCode = hashCode;
        this.hashName = hashName;
        this.hashImage = hashImage;
        this.location = location;
        this.score = score;
        this.ownedBy = ownedBy;
        this.comments = comments;
    }

}