package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRCreature {
    String hashName;
    String hashImage;
    int hashCode;
    int score;
    ArrayList<String> ownedBy;
    ArrayList<String> comments;

    public QRCreature(String name,String hashImage, int hashcode,int score,ArrayList<String> ownedBy,ArrayList<String> comments){
        this.hashName = name;
        this.hashImage = hashImage;
        this.hashCode = hashcode;
        this.score = score;
        this.ownedBy = ownedBy;
        this.comments = comments;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("HashName", this.hashName);
        result.put("HashImage",this.hashImage);
        result.put("HashCode", this.hashCode);
        result.put("Score",this.score);
        result.put("OwnedBy",this.ownedBy);
        result.put("Comments",this.comments);

        return result;
    }

    public String getCollectionName() {
        return "QR Creatures";
    }

    public int getScore() {
        return score;
    }

    public String getHashName() {
        return hashName;
    }

    public void setHashName(String hashName) {
        this.hashName = hashName;
    }

    public String getHashImage() {
        return hashImage;
    }

    public void setHashImage(String hashImage) {
        this.hashImage = hashImage;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
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

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}
