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

/**
 * This class represents the loading screen activity which checks if the user id exists in the database
 * and decides whether to open the main menu or prompt the user to sign up.
 */
public class LoadingScreen extends AppCompatActivity {

    // Initialize a Firestore database instance
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Get the user id and use it to create a document reference in the "User" collection
        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference docRef = database.collection("User").document(userID);

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
                finish();
            }
        });
    }

    /**
     * Opens the sign up screen activity.
     */
    private void openSignUpScreen() {
        Intent intent = new Intent(this, SignUpScreen.class);
        startActivity(intent);
    }

    /**
     * Opens the main menu screen activity.
     */
    private void openMenuScreen() {
        Intent intent = new Intent(this, MenuScreen.class);
        startActivity(intent);
    }


}