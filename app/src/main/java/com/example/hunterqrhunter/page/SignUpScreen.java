package com.example.hunterqrhunter.page;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * The SignUpScreen class represents the screen where users can sign up for the application.
 */
public class SignUpScreen extends AppCompatActivity {

    // Firebase Firestore database initialization and user collection references
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = database.collection("User");
    CollectionReference usernameCollection = database.collection("Usernames");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initializing back end variables for user sign up
        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        EditText username = findViewById((R.id.username_sign_up));
        EditText email = findViewById((R.id.email_sign_up));

        // Set OnClickListener for sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get user input from EditText fields
                final String usernameStr = username.getText().toString();
                final String emailStr = email.getText().toString();

                // Create a new user object with user input
                User user = new User(userID, usernameStr, emailStr);

                // Create HashMaps for user data and username data for Firestore database
                HashMap<String, String> userData = new HashMap<>();
                HashMap<String, String> usernameData = new HashMap<>();

                // Check if user input is valid and take action accordingly
                switch (user.validateUserInfo()) {
                    case 0:
                        // If user input is valid, create a user profile in the database
                        createUserProfile(user);
                        break;
                    case 1:
                        // If username length is invalid, display an error message
                        Toast.makeText(SignUpScreen.this,"Username must be between 3-20 characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        // If username does not start with a letter or number, display an error message
                        Toast.makeText(SignUpScreen.this,"Username must start with a number or letter", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        // If username does not end with a letter or number, display an error message
                        Toast.makeText(SignUpScreen.this,"Username must end with a number or letter", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        // If username contains consecutive . or _ characters, display an error message
                        Toast.makeText(SignUpScreen.this,"Username must not repeat . or _ character", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        // If username contains illegal characters, display an error message
                        Toast.makeText(SignUpScreen.this,"Username contains illegal characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        // If email is invalid, display an error message
                        Toast.makeText(SignUpScreen.this,"Invalid email", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * This method creates a user profile in the Firebase Firestore database.
     * @param user The User object containing the user's information.
     */
    public void createUserProfile(User user) {

        // Get a reference to the document in the "Usernames" collection with the same username as the user being created
        DocumentReference docRef = database.collection("Usernames").document(user.getUsername());

        // Check if the document exists
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    // If the document exists, display an error message and do not create the profile
                    if (document.exists()) {
                        Log.d("CreateUserProfile", "Username already exists");
                        Toast.makeText(SignUpScreen.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    }
                    // If the document does not exist, create the profile
                    else {
                        // Create a HashMaps to hold user data and username
                        HashMap<String, Object> userData = new HashMap<>();
                        HashMap<String, String> usernameData = new HashMap<>();

                        // Add the user's information to the userData HashMap
                        userData.put("uid", user.getUid());
                        userData.put("username", user.getUsername());
                        userData.put("email", user.getEmail());
                        userData.put("Total Score", 0); // Setting initial scores to be 0
                        userData.put("Highest Unique Score", 0);

                        // Add the user's username to the usernameData HashMap
                        usernameData.put("username", user.getUsername());

                        // Set the user's username document in the "Usernames" collection
                        usernameCollection.document(user.getUsername()).set(usernameData);
                        // Set the user's data document in the "Users" collection
                        usersCollection.document(user.getUid()).set(userData);

                        // Log that the profile was successfully created
                        Log.d("CreateUserProfile", "Profile created");

                        // Open the menu screen and finish the current activity
                        openMenuScreen();
                        finish();
                    }
                }
                else {
                    // If there was an error getting the document, log the error
                    Log.d("CreateUserProfile", "Error getting documents ", task.getException());
                }
            }
        });
    }

    /**
     * Opens the UserScoresScreen activity.
     */
    private void openMenuScreen() {
        Intent intent = new Intent(getApplicationContext(), MenuScreen.class);
        startActivity(intent);
    }


}