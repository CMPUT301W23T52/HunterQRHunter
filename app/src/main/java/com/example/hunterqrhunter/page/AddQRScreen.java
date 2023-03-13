package com.example.hunterqrhunter.page;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;

public class AddQRScreen extends AppCompatActivity {


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        byte[] scannedData = getIntent().getByteArrayExtra("SCANNED_DATA");


    }
}
