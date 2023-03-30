package com.example.hunterqrhunter.page;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyQRScreen extends AppCompatActivity {
    {/**
     []MyQRScreen has all the QRs the player acquired
     []Number of the Qrs
     []Score of the player
     []Player can delete the QR if they want to
     []Player can sort
     */}
    private FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);
        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get the device user ID
        System.out.println(userID);
        db = FirebaseFirestore.getInstance();//Initialize firestore database

        CollectionReference qrRef = db.collection("QR");//get the QR collection
        //if userID and qrRef's uid is the same, then get the score of the user
        qrRef.whereEqualTo(FieldPath.of("uid", userID), userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (DocumentSnapshot document : task.getResult()) {
                    System.out.println(document.getData());
                    Log.d("MyQRScreen", document.getId() + " => " + document.getData());
                }
            } else {
                Log.d("MyQRScreen", "Error getting documents: ", task.getException());
            }
        });
    }
}
