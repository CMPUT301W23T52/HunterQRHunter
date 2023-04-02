package com.example.hunterqrhunter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.model.HashQR;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {


    String hashVal = "dragonasf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get a reference to the ImageView
        ImageView imageView = findViewById(R.id.QrCreatureImage);
        // Generate hash and hash name
        byte[] hash = HashQR.hashObject(hashVal);
        String HashName = HashQR.giveQrName(hash);
//        Bitmap HashImage = hashQR.generateImageFromHashcode(hash);

// Generate the bitmap from the hash code
//        Bitmap bitmap = Bitmap.createBitmap(HashImage);

// Set the bitmap on the ImageView
//        imageView.setImageBitmap(bitmap);

        // Initialize Firebase Firestore and UpdateCommend
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize the button and set an OnClickListener
        Button mButton = findViewById(R.id.btn1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate hash and hash name
                byte[] hash = HashQR.hashObject(hashVal);
                String HashName = HashQR.giveQrName(hash);

                // Create a new user with a first and last name, born year, hash, and hash name
//                QRCreature qrCreature = new QRCreature(HashName, hash);
//                fb.writeQR(qrCreature);
            }
        });
    }
}