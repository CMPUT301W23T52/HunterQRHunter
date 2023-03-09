package com.example.hunterqrhunter.model;

import android.graphics.Bitmap;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCreature {
    String name;
    int hashcode;
    Bitmap bitmap;

    List<Integer> inverseTransform;



    public QRCreature(String name, int hashcode, Bitmap bitmap){
        this.name = name;
        this.hashcode = hashcode;
        this.bitmap = bitmap;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("QR Name", this.name);
        result.put("QR Hash", this.hashcode);
        result.put("QR Creature", this.bitmap);
        return result;
    }
    public void setInverseTransform(List<Integer> inverseTransform) {
        this.inverseTransform = inverseTransform;
    }

    public String getCollectionName() {
        return "QR Creatures";
    }

}
