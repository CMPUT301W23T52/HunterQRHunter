package com.example.hunterqrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.example.hunterqrhunter.model.QR;
import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.SignUpScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class LoadingScreen extends AppCompatActivity {
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Get a document reference using the user id to see if this id exists in our database
        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference docRef = database.collection("User").document(userID);

        // Once the .get() method is complete, decide what to do depending on whether or not
        // the document exists
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        openMenuScreen(); // If document exists, open the main menu...
                    }
                    else {
                        openSignUpScreen(); // ...otherwise prompt the user to sign up
                    }
                }
                finish(); // Kill the loading screen activity.
            }
        });
    }

    private void openSignUpScreen() {
        Intent intent = new Intent(this, SignUpScreen.class);
        startActivity(intent);
    }
    private void openMenuScreen() {
        Intent intent = new Intent(this, MenuScreen.class);
        startActivity(intent);
    }
}