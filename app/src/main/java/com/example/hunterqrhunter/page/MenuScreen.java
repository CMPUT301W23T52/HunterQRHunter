package com.example.hunterqrhunter.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;

/**

 The MenuScreen activity displays the main menu of the application and allows the user to navigate to other screens.
 */
public class MenuScreen extends AppCompatActivity {

    /**
     * Sets up the initial state of the activity
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Set up the click listener for the QR Scanner button
        Button scanBtn = (Button) findViewById(R.id.btn_qr_screen);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRCameraScreen.class);
                startActivity(intent);
            }
        });

        // Set up the click listener for the User Scores button
        Button userScreenBtn = (Button) findViewById(R.id.btn_user_screen) ;
        userScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserScoresScreen.class);
                startActivity(intent);
            }
        });

        // Set up the click listener for the QR Map button
        Button mapScreenBtn = (Button) findViewById(R.id.btn_map_screen);
        mapScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRMapScreen.class);
                startActivity(intent);
            }
        });

        // Set up the click listener for the My QR Code button
        Button MyQRBtn = (Button) findViewById(R.id.btn_my_qr);
        MyQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyQRScreen.class);
                startActivity(intent);
            }
        });

        // Set up the click listener for the Edit Profile button
        ImageButton editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileScreen.class);
                startActivity(intent);
            }
        });
    }



}