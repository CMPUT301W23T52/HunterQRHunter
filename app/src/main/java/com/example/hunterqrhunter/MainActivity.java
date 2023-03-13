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


import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.QRCreature;

import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.UserScoresScreen;
import com.example.hunterqrhunter.page.QRMapScreen;
import com.example.hunterqrhunter.page.QRCameraScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;


public class MainActivity extends AppCompatActivity {


    String hashVal = "dragonasf";
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
        byte[] hash = hashQR.hashObject(hashVal);
        String HashName = hashQR.giveQrName(hash);
//        Bitmap HashImage = hashQR.generateImageFromHashcode(hash);

// Generate the bitmap from the hash code
//        Bitmap bitmap = Bitmap.createBitmap(HashImage);

// Set the bitmap on the ImageView
//        imageView.setImageBitmap(bitmap);

        // Initialize Firebase Firestore and FbRepository
        db = FirebaseFirestore.getInstance();
        fb = new FbRepository(db);

        // Initialize the button and set an OnClickListener
        mButton = findViewById(R.id.btn1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate hash and hash name
                byte[] hash = hashQR.hashObject(hashVal);
                String HashName = hashQR.giveQrName(hash);

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
        CollectionReference usersCollection = db.collection("Users (shafi)");
        CollectionReference usernameCollection = db.collection("Usernames (shafi)");
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String usernameStr = username.getText().toString();
                final String emailStr = email.getText().toString();

                HashMap<String, String> userData = new HashMap<>();
                HashMap<String, String> usernameData = new HashMap<>();

                if (usernameStr.length() > 0 && emailStr.length() > 0) {

                    userData.put("Username", usernameStr);
                    userData.put("Email", emailStr);
                    userData.put("user ID", userID);

                    usernameData.put("Username", usernameStr);

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