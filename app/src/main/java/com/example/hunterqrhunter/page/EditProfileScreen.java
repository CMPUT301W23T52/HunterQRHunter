package com.example.hunterqrhunter.page;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class EditProfileScreen extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = database.collection("User");
    CollectionReference usernameCollection = database.collection("Usernames");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        User oldUser = new User(userID, null, null);
        getUsernameByID(userID, oldUser);

        EditText username = findViewById(R.id.edit_username);
        EditText email = findViewById(R.id.edit_email);
        setCurrentUserInfo(userID, username, email); // Set the edit texts to be the current user info


        Button editButton = findViewById(R.id.edit_account_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newUsername = username.getText().toString();
                final String newEmail = email.getText().toString();

                User newUser = new User(userID, newUsername, newEmail);

                switch (newUser.validateUserInfo()) {
                    case 0:
                        editUserProfile(oldUser, newUser);
                        break;
                    case 1:
                        Toast.makeText(EditProfileScreen.this, "Username must be between 3-20 characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(EditProfileScreen.this, "Username must start with a number or letter", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(EditProfileScreen.this, "Username must end with a number or letter", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(EditProfileScreen.this, "Username must not repeat . or _ character", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(EditProfileScreen.this, "Username contains illegal characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(EditProfileScreen.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        break;
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


    public void editUserProfile(User oldUser, User newUser) {

        DocumentReference docRef = database.collection("Usernames").document(newUser.getUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("EditUserProfile", "Username already exists");
                        Toast.makeText(EditProfileScreen.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        HashMap<String, Object> userData = new HashMap<>();
                        HashMap<String, String> usernameData = new HashMap<>();

                        usernameData.put("username", newUser.getUsername());

                        userData.put("username", newUser.getUsername());
                        userData.put("email", newUser.getEmail());

                        usernameCollection.document(oldUser.getUsername()).delete();
                        usernameCollection.document(newUser.getUsername()).set(usernameData);
                        usersCollection.document(newUser.getUid()).set(userData, SetOptions.merge());

                        Toast.makeText(EditProfileScreen.this, "Profile successfully edited!", Toast.LENGTH_SHORT).show();
                        Log.d("EditUserProfile", "Profile updated");
                    }
                }
                else {
                    Log.d("EditUserProfile", "Error getting documents", task.getException());
                }
            }
        });
    }


    // This code came from https://firebase.google.com/docs/firestore/query-data/get-data
    // CITE PROPERLY
    private void getUsernameByID(String userID, User user) {

        DocumentReference docRef = database.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("username");
                        user.setUsername(username);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Error getting document ", task.getException());
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