package com.example.hunterqrhunter.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.google.firebase.firestore.CollectionReference;
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
        db = FirebaseFirestore.getInstance();
        CollectionReference qrRef = db.collection("QR");

    }
}
