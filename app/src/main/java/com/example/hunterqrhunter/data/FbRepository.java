package com.example.hunterqrhunter.data;

// need a create user function for firebase
// need a get user function with firstName lastName
// need a delete user function
// need a update user function
// need a get all users function

// QR code need to be a separate model in firebase database
// QR code getter setter need to be import for firebase as well

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.hunterqrhunter.model.QR;
import com.example.hunterqrhunter.model.QRCreature;
import com.example.hunterqrhunter.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

//package com.example.hunterqrhunter.data;

import android.util.Log;

import com.example.hunterqrhunter.model.QRCreature;
import com.example.hunterqrhunter.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class FbRepository {
    final private FirebaseFirestore db;

    public FbRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public CollectionReference getCollectionRef(String colName) {
        return db.collection(colName);
    }

    public DocumentReference getDocumentRef(String colName, String docName) {
        return db.collection(colName).document(docName);
    }

    public void createUser(User user) {
        Map<String, Object> userValues = user.toMap();
        db.collection("users").add(userValues).addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
    public void writeQR(QR qr) {
        Map<String, Object> qrValues = qr.toMap();
        db.collection(qr.getCollectionName()).document(qr.getQrcode()).set(qrValues).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
    public void updateQRComments(String qrCode, ArrayList<String> comments){
        db.collection("QR").document(qrCode).update("comments", comments).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

}