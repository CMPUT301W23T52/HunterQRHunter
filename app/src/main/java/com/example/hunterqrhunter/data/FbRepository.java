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

import com.example.hunterqrhunter.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

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
        db.collection("users").add(user).addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
}