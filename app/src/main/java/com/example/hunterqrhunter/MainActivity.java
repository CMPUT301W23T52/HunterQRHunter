package com.example.hunterqrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.QRCreature;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Initialize Firebase Firestore and FbRepository
        db = FirebaseFirestore.getInstance();
        fb = new FbRepository(db);

        // Initialize the button and set an OnClickListener
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate hash and hash name
                int hash = hashQR.hashObject(hashVal);
                String HashName = hashQR.giveQrName(hash);
                Bitmap HashImage = hashQR.generateImageFromHashcode(hash);

                // Create a new user with a first and last name, born year, hash, and hash name
                QRCreature qrCreature = new QRCreature(HashName, hash, HashImage);
                fb.createQR(qrCreature);
            }
        });

        Button imageButton = (Button) findViewById(R.id.btn1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NextScreen.class);
                startActivity(intent);
            }
        });

    }
}