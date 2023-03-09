package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class QRCreature {
    String name;
    int hashcode;

    public QRCreature(String name, int hashcode){
        this.name = name;
        this.hashcode = hashcode;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("QR Name", this.name);
        result.put("QR Hash", this.hashcode);

        return result;
    }

    public String getCollectionName() {
        return "QR Creatures";
    }

}
