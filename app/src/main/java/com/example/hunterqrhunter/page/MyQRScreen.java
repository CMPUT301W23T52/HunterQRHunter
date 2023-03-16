package com.example.hunterqrhunter.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.data.FbRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyQRScreen extends AppCompatActivity {

    private int pointSum;
    private int totalQR;

    private FirebaseFirestore db;

    private FbRepository fb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        db = FirebaseFirestore.getInstance();
        fb = new FbRepository(db);
    }

    public void findTotalQR() {
        pointSum = 0;
        for (int i = 0; i < db.length; i++) {
            totalQR++;
        }
        // change the value of xml id "totalQR" to the value of totalQR
    }

    public void displayTotalPoints() {
        pointSum = 0;
        for (int i = 0; i < totalQR; i++) {
            pointSum += dbCurrent;
        }
        // change the display value of xml id "totalScore" to the value of pointSum
    }

}
