package com.example.hunterqrhunter.model;

import junit.framework.TestCase;


import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QRCreatureTest extends TestCase {
    ArrayList<String> ownedArr = new ArrayList<>(Arrays.asList("ab", "c"));
    ArrayList<String> commentArr = new ArrayList<>(Collections.singletonList("hi"));
    QRCreature testQR = new QRCreature("Ricky","ABC", 150, 30, ownedArr,commentArr);
    public void testSetAndToMap() {
        testQR.setHashName("Dave");
        testQR.setHashImage("Flower");
        testQR.setHashCode(200);
        testQR.setScore(1000);
        ownedArr.add("d");
        testQR.setOwnedBy(ownedArr);
        commentArr.add("ok");
        testQR.setComments(commentArr);
        Map<String,Object> result = new HashMap<>();
        result.put("HashName", "Dave");
        result.put("HashImage","Flower");
        result.put("HashCode", 200);
        result.put("Score",1000);
        result.put("OwnedBy",new ArrayList<>(Arrays.asList("ab", "c","d")));
        result.put("Comments",new ArrayList<>(Arrays.asList("hi", "ok")));
        assertEquals(result,testQR.toMap());
        assertEquals("QR Creatures",testQR.getCollectionName());
    }
}