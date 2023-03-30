package com.example.hunterqrhunter;

import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.provider.Settings;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.data.FbRepository;

import com.example.hunterqrhunter.data.FbRepository;

import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.page.MenuScreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;


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

        // Initialize Firebase Firestore and FbRepository
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FbRepository fb = new FbRepository(db);

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

        // Initializing back end variables for user sign up
        Button signBtn = (Button) findViewById(R.id.btn_signup);
        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        EditText username = findViewById((R.id.username_sign_up));
        EditText email = findViewById((R.id.email_sign_up));

        // Initializing database collections
        CollectionReference usersCollection = db.collection("User");
        CollectionReference usernameCollection = db.collection("Usernames");
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String usernameStr = username.getText().toString();
                final String emailStr = email.getText().toString();

                HashMap<String, String> userData = new HashMap<>();
                HashMap<String, String> usernameData = new HashMap<>();

                if (usernameStr.length() > 0 && emailStr.length() > 0) {

                    userData.put("username", usernameStr);
                    userData.put("email", emailStr);
                    userData.put("uid", userID);

                    usernameData.put("username", usernameStr);

                    usersCollection.document(userID).set(userData);
                    usernameCollection.document(usernameStr).set(usernameData);
                }
                openMenuScreen();
            }
        });
    }
    private void openMenuScreen() {
        Intent intent = new Intent(getApplicationContext(), MenuScreen.class);
        startActivity(intent);
    }


}