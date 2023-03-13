package com.example.hunterqrhunter.page;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;

import org.w3c.dom.Text;

public class AddQRScreen extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String scannedData = getIntent().getStringExtra("SCANNED_DATA");

//        get all the views
        Button saveBtn = (Button) findViewById(R.id.save_button);
        Button deleteBtn = (Button) findViewById(R.id.delete_button);

    }
}
