package com.example.hunterqrhunter.page;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

/**
 * This activity allows the user to edit their profile information
 */
public class EditProfileScreen extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = database.collection("User");
    CollectionReference usernameCollection = database.collection("Usernames");

    /**
     * Sets up the initial state of the activity
     * display the user's current information, and sets up the Edit and Cancel buttons.
     * @param savedInstanceState the saved instance state bundle (if any) from the previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Initialize the layout for the activity
        setContentView(R.layout.activity_edit_profile);

        // Get the user ID using Android ID
        String userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Create a User object for the current user with null username and email
        User oldUser = new User(userID, null, null);

        // Get the current user's username using the getUserByID method
        fetchUsernameFromDatabase(userID, oldUser);

        // Get references to the EditTexts for username and email
        EditText username = findViewById(R.id.edit_username);
        EditText email = findViewById(R.id.edit_email);

        // Set the EditTexts to display the current user's username and email
        setCurrentUserInfo(userID, username, email);

        // Set up the Edit button to update the user's information
        Button editButton = findViewById(R.id.edit_account_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the new username and email from the EditTexts
                final String newUsername = username.getText().toString();
                final String newEmail = email.getText().toString();

                // Create a new User object with the new username and email
                User newUser = new User(userID, newUsername, newEmail);

                // Validate the new user info and handle any errors
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

        // Set up the Cancel button to close the Edit Profile screen
        Button cancelButton = findViewById(R.id.cancel_edit_profile_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenuScreen();
                finish();
            }
        });
    }

    /**
     * This method updates the user profile with the new user information if it is valid.
     * @param currentUser The user object representing the current user's information.
     * @param newUser The user object representing the new user's information.
     */
    public void editUserProfile(User currentUser, User newUser) {

        // Check if the new username is already taken
        DocumentReference docRef = database.collection("Usernames").document(newUser.getUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // If the new username is already taken, show a message to the user
                        Log.d("EditUserProfile", "Username already exists");
                        Toast.makeText(EditProfileScreen.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // If the new username is available, update the user profile
                        HashMap<String, Object> userData = new HashMap<>();
                        HashMap<String, String> usernameData = new HashMap<>();

                        // Update the username in the username collection
                        usernameData.put("username", newUser.getUsername());
                        usernameCollection.document(currentUser.getUsername()).delete();
                        usernameCollection.document(newUser.getUsername()).set(usernameData);

                        // Update the user information in the user collection
                        userData.put("username", newUser.getUsername());
                        userData.put("email", newUser.getEmail());
                        usersCollection.document(newUser.getUid()).set(userData, SetOptions.merge());

                        // Show a success message to the user
                        Toast.makeText(EditProfileScreen.this, "Profile successfully edited!", Toast.LENGTH_SHORT).show();
                        Log.d("EditUserProfile", "Profile updated");
                    }
                }
                else {
                    // If there is an error, log the exception
                    Log.d("EditUserProfile", "Error getting documents", task.getException());
                }
            }
        });
    }

    /**
     * Retrieves the username from the Firestore database for a given user ID.
     * @param userID the ID of the user whose username is to be fetched
     * @param user the User object whose username is to be set
     */
    private void fetchUsernameFromDatabase(String userID, User user) {

        // Get a reference to the Firestore document for the user with the given ID
        DocumentReference docRef = database.collection("User").document(userID);

        // Retrieve the document from the Firestore database
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                    // Set the username of the user object to the value retrieved from the database
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

    /**
     * Sets the current user info in the EditText fields on the Edit Profile screen.
     * @param userID the unique ID of the user whose information is being displayed
     * @param username the EditText field for the user's username
     * @param email the EditText field for the user's email
     */
    private void setCurrentUserInfo(String userID, EditText username, EditText email) {

        // Get the user's information from the database
        DocumentReference docRef = database.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Set the EditText fields to the user's current username and email
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

    public void openMenuScreen() {
        Intent intent = new Intent(this, MenuScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


}