package com.example.hunterqrhunter.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;

public class MyQRScreen extends AppCompatActivity {

    private int pointSum;
    private int totalQR;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

    }

    // I don't know how to pull from the database here -Everett
    public void findTotalQR() {
        pointSum = 0;
        for (int i = 0; i < db.length; i++) {
            totalQR++;
        }
        // change the value of xml id "totalQR" to the value to totalQR
    }

    public void displayTotalPoints() {
        pointSum = 0;
        for (int i = 0; i < totalQR; i++) {
            pointSum += dbCurrent;
        }
        // change the display value of xml id "totalScore" to the value of pointSum
    }

}
