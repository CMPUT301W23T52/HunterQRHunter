package com.example.hunterqrhunter.page;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.HashQR;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditProfileScreen extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    HashQR hashQR = new HashQR();
    CollectionReference usersCollection = database.collection("User");
    CollectionReference usernameCollection = database.collection("Usernames");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
//        ImageView imageView = findViewById(R.id.imageView);
//        int hashCode = 1802651831;
//        Bitmap faceBitmap = hashQR.generateImageFromHashcode(hashCode);
//        imageView.setImageBitmap(faceBitmap);


        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String[] currentUsername = new String[1];
        getUsernameByID(userID, currentUsername);
        EditText username = findViewById(R.id.edit_username);
        EditText email = findViewById(R.id.edit_email);

        setCurrentUserInfo(userID, username, email); // Set the edit texts to be the current user info


        Button editButton = findViewById(R.id.edit_account_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newUsername = username.getText().toString();
                final String newEmail = email.getText().toString();

                HashMap<String, String> userData = new HashMap<>();
                HashMap<String, String> usernameData = new HashMap<>();

                if (newUsername.length()>0 && newEmail.length()>0) {

                    userData.put("username", newUsername);
                    userData.put("email", newEmail);
                    userData.put("uid", userID);

                    usernameData.put("username", newUsername);

                    usersCollection.document(userID).set(userData);
                    usernameCollection.document(currentUsername[0]).delete();
                    usernameCollection.document(newUsername).set(usernameData);

                    getUsernameByID(userID, currentUsername);
                    Toast.makeText(EditProfileScreen.this,"Profile successfully edited!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditProfileScreen.this,"Please enter a username and email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Functionality of the cancel button kills the edit profile screen.
        Button cancelButton = findViewById(R.id.cancel_edit_profile_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    // This code came from https://firebase.google.com/docs/firestore/query-data/get-data
    // CITE PROPERLY
    private void getUsernameByID(String userID, String[] username) {

        DocumentReference docRef = database.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username[0] = document.getString("username");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setCurrentUserInfo(String userID, EditText username, EditText email) {

        DocumentReference docRef = database.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username.setText(document.getString("username"));
                        email.setText(document.getString("email"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}