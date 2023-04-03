package com.example.hunterqrhunter;

import com.example.hunterqrhunter.model.QR;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class QRUnitTest {

    @Test
    public void testQRInitialization() {
        String qrcode = "ABCDE12345";
        GeoPoint location = new GeoPoint(40.7128, -74.0060);
        int score = 12345;
        String ownedBy = "user123";
        String name = "hashedname";
        String qid = "qr123";

        QR qr = new QR(qrcode, location, score, ownedBy, name, qid);

        assertEquals(qrcode, qr.getQrcode());
        assertEquals(location, qr.getLocation());
        assertEquals(score, qr.getScore());
        assertEquals(ownedBy, qr.getUid());
        assertEquals(name, qr.getName());
        assertEquals(qid, qr.getQid());
        assertEquals(new ArrayList<String>(), qr.getComments());
    }

    @Test
    public void testQRImageUrl() {
        QR qr = new QR();
        String imageUrl = "images/qr123.jpg";

        qr.setImageUrl(imageUrl);

        assertEquals(imageUrl, qr.getImageUrl());
    }

    @Test
    public void testQRComments() {
        QR qr = new QR();
        ArrayList<String> comments = new ArrayList<>();
        comments.add("This QR is awesome!");
        comments.add("I love this QR!");
        comments.add("This QR is the best!");

        qr.setComments(comments);

        assertEquals(comments, qr.getComments());
    }
}
