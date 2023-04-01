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

import com.google.firebase.firestore.FirebaseFirestore;

//package com.example.hunterqrhunter.data;

import java.util.ArrayList;

public class UpdateCommend {
    final private FirebaseFirestore db;

    public UpdateCommend(FirebaseFirestore db) {
        this.db = db;
    }



//
    public void updateQRComments(String qrCode, ArrayList<String> comments){
        db.collection("QR").document(qrCode).update("comments", comments).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

}