package com.example.hunterqrhunter.page;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.LocationUtils;
import com.example.hunterqrhunter.model.QR;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;


public class AddQRScreen extends AppCompatActivity {

    private LocationUtils mLocationUtils;

    /**
     * handle the data that is received from previous screen
     * @param savedInstanceState used to receive the data from previous screen
     */
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

//        Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FbRepository fb = new FbRepository(db);

        mLocationUtils = new LocationUtils(this);
        mLocationUtils.requestLocationPermission();
        mLocationUtils.getLocation();

        String scannedData = getIntent().getStringExtra("SCANNED_DATA");
        byte[] HashedValue = HashQR.hashObject(scannedData);

//        get all the views
        Button saveBtn = (Button) findViewById(R.id.save_button);
        Button deleteBtn = (Button) findViewById(R.id.delete_button);
        TextView pointText =(TextView) findViewById(R.id.score_text);
        TextView nameText = (TextView) findViewById(R.id.name_text);

        String hashedName = HashQR.giveQrName(HashedValue);
        Integer hashedScore = HashQR.scoreGen(HashedValue);
        String uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

//        set textViews
        pointText.setText("Congrats! You scored " + HashQR.scoreGen(HashedValue) +" points!");
        nameText.setText(HashQR.giveQrName(HashedValue));

/**
 * Don't add the scanned QR to user's collection and navigate back to the main screen
 */
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        /**
         * user adds qr to the database
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double longitude = mLocationUtils.getLongitude();
                double latitude = mLocationUtils.getLatitude();
                GeoPoint location = new GeoPoint(latitude, longitude);
                ArrayList<String> ownedBy = new ArrayList<>();
                ownedBy.add(uid);

                CollectionReference qrCollection = FirebaseFirestore.getInstance().collection("QR");
                QR qr = new QR(scannedData, location, hashedScore, ownedBy, hashedName);

                db.collection("QR").document(scannedData.replaceAll("//","")).set(qr)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            /**
                             * when adding was successful toast and navigate back to menu Screen
                             * @param aVoid
                             */
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddQRScreen.this, "Document added successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        /**
                         * when adding fails toast the status and navigate back to menu screen
                         */
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddQRScreen.this, "Error adding document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });



            }
        });

    }

}
