package com.example.hunterqrhunter.model;

import android.graphics.Bitmap;

import com.google.firebase.firestore.Exclude;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCreature {
    String name;
    byte[] hashcode;
    Bitmap bitmap;

    List<Integer> inverseTransform;




    public QRCreature(String name, byte[] hashcode){
        this.name = name;
        this.hashcode = hashcode;
//        this.bitmap = bitmap;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("QR Name", this.name);
        result.put("QR Hash", Arrays.asList(ByteBuffer.wrap(this.hashcode).getInt()));
//        result.put("QR Creature", this.bitmap);
        return result;
    }
    public void setInverseTransform(List<Integer> inverseTransform) {
        this.inverseTransform = inverseTransform;
    }

    public String getCollectionName() {
        return "QR Creatures";
    }

}
