package com.example.hunterqrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.QRCreature;

import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.UserScoresScreen;
import com.example.hunterqrhunter.page.QRMapScreen;
import com.example.hunterqrhunter.page.QRCameraScreen;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Object hashVal = "yongbin@gmail.com";
    HashQR hashQR = new HashQR();

    private Button mButton;
    private FirebaseFirestore db;
    private FbRepository fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get a reference to the ImageView
        ImageView imageView = findViewById(R.id.QrCreatureImage);
        // Generate hash and hash name
        int hash = hashQR.hashObject(hashVal);
        String HashName = hashQR.giveQrName(hash);
        Bitmap HashImage = hashQR.generateImageFromHashcode(hash);

// Generate the bitmap from the hash code
        Bitmap bitmap = Bitmap.createBitmap(HashImage);

// Set the bitmap on the ImageView
        imageView.setImageBitmap(bitmap);

        // Initialize Firebase Firestore and FbRepository
        db = FirebaseFirestore.getInstance();
        fb = new FbRepository(db);

        // Initialize the button and set an OnClickListener
        mButton = findViewById(R.id.btn1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new user with a first and last name, born year, hash, and hash name
                QRCreature qrCreature = new QRCreature(HashName, hash, HashImage);
                fb.createQR(qrCreature);
            }
        });

        Button signBtn = (Button) findViewById(R.id.btn_signup);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuScreen.class);
                startActivity(intent);
            }
        });




    }
}