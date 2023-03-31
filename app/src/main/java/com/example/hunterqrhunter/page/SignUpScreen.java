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

public class SignUpScreen extends AppCompatActivity {

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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String usernameStr = username.getText().toString();
                final String emailStr = email.getText().toString();
                User user = new User(userID, usernameStr, emailStr);

                HashMap<String, String> userData = new HashMap<>();
                HashMap<String, String> usernameData = new HashMap<>();

                switch (user.validateUserInfo()) {
                    case 0:
                        createUserProfile(user);
                        break;
                    case 1:
                        Toast.makeText(SignUpScreen.this,"Username must be between 3-20 characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(SignUpScreen.this,"Username must start with a number or letter", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(SignUpScreen.this,"Username must end with a number or letter", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(SignUpScreen.this,"Username must not repeat . or _ character", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(SignUpScreen.this,"Username contains illegal characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(SignUpScreen.this,"Invalid email", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    public void createUserProfile(User user) {

        DocumentReference docRef = database.collection("Usernames").document(user.getUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Username already exists");
                        Toast.makeText(SignUpScreen.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        HashMap<String, String> userData = new HashMap<>();
                        HashMap<String, String> usernameData = new HashMap<>();

                        usernameData.put("username", user.getUsername());

                        userData.put("uid", user.getUid());
                        userData.put("username", user.getUsername());
                        userData.put("email", user.getEmail());

                        usernameCollection.document(user.getUsername()).set(usernameData);
                        usersCollection.document(user.getUid()).set(userData);
                        Log.d(TAG, "Profile created");
                        openMenuScreen();
                        finish();
                    }
                }
                else {
                    Log.d(TAG, "Error creating profile ", task.getException());
                }
            }
        });
    }

    private void openMenuScreen() {
        Intent intent = new Intent(getApplicationContext(), MenuScreen.class);
        startActivity(intent);
    }


}