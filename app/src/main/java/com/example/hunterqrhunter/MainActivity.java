package com.example.hunterqrhunter;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.HashQR;
import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Object hashVal = "yongbin@gmail.com";
    HashQR hashQR = new HashQR();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hash = hashQR.hashObject(hashVal);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FbRepository fb = new FbRepository(db);


        User user = new User("5","Lingfeng","Zhu",2001,hash);
        fb.createUser(user);
    }
}