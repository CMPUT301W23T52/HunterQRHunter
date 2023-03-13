package com.example.hunterqrhunter.page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.LocationUtils;


public class AddQRScreen extends AppCompatActivity {

    private LocationUtils mLocationUtils;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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

//        set textViews
        pointText.setText("Congrats! You scored 513 points!");
        nameText.setText(HashQR.giveQrName(HashedValue));

//        Don't add the scanned QR to user's collection and navigate back to the main screen
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationUtils.requestLocationPermission();

                double longitude = mLocationUtils.getLongitude();
                double latitude = mLocationUtils.getLatitude();

                Log.d("Location", "Longitude: " + longitude + ", Latitude: " + latitude);

            }
        });

    }

}
