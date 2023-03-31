package com.example.hunterqrhunter.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;

public class MenuScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button scanBtn = (Button) findViewById(R.id.btn_qr_screen);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRCameraScreen.class);
                startActivity(intent);
            }
        });

        Button userScreenBtn = (Button) findViewById(R.id.btn_user_screen) ;
        userScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserScoresScreen.class);
                startActivity(intent);
            }
        });

        Button mapScreenBtn = (Button) findViewById(R.id.btn_map_screen);
        mapScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRMapScreen.class);
                startActivity(intent);
            }
        });

        Button MyQRBtn = (Button) findViewById(R.id.btn_my_qr);
        MyQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyQRScreen.class);
                startActivity(intent);
            }
        });

        Button QRBtn = (Button) findViewById(R.id.btn_qr);
        QRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRScreen.class);
                intent.putExtra("qrCode","5105c4c7-6b94-4f03-99a1-8b3ce6a3462b");
                startActivity(intent);
            }
        });

        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileScreen.class);
                startActivity(intent);
            }
        });

    }


}
