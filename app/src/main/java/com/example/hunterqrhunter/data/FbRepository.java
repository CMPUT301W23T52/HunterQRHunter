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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void getUser(User user) {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // set user object
                    user.setFirstName((String) document.get("firstName"));
                    user.setLastName((String) document.get("lastName"));
                    user.setBorn(((Long) Objects.requireNonNull(document.get("born"))).intValue());
                    user.setHash(((Long) Objects.requireNonNull(document.get("hash"))).intValue());
                    // here you can use the callback function to do the work with the get user.
                    // Log.d(TAG, "Get User's LastName is " + user.lastName);
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}