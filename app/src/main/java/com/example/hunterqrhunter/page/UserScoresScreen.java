package com.example.hunterqrhunter.page;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserScoresScreen extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = database.collection("User");
    CollectionReference usernameCollection = database.collection("Usernames");
    CollectionReference QRCollection = database.collection("QR");

    private ArrayList<String> usernameDataList = new ArrayList<>();
    private ArrayAdapter<String> usernameAdapter = new ArrayAdapter<>(this, R.layout.content_username, R.id.username_button, usernameDataList);
    private ListView usernameListView = findViewById(R.id.username_list);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scores);




        usernameCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        usernameDataList.add(document.getId());
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                usernameListView.setAdapter(usernameAdapter);
            }
        });

        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sortByHighestTotalScore() {
        ArrayList<String> userIDList = new ArrayList<>();
        usersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        userIDList.add(document.getId());
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        for (int i = 0; i < userIDList.size(); i++) {
            QRCollection.whereEqualTo(FieldPath.of("uid"), userIDList).get().addOnCompleteListener(task -> {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userIDList.add(document.getId());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    }
                    else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }


 qrRef.whereEqualTo(FieldPath.of("uid"), userID).get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            for (DocumentSnapshot document : task.getResult()) {

                //get the score of the user and add it to the total score
                Integer score = Integer.parseInt(document.getData().get("score").toString());

                //update the total score
                totalScore.addAndGet(score);

                //update the total number of the QRs
                totalQRs.getAndIncrement();

                ListView myQRList = findViewById(R.id.QRList);

                //get the qrcode field from the QR document
                String qrCode = document.getData().get("qrcode").toString();

                //create a face class based on the qrCode
                byte[] hash = hashQR.hashObject(qrCode);
                Bitmap faceBitmap = hashQR.generateImageFromHashcode(hash);

                //store the faceBitmap to the arrayList
                qrList.add(faceBitmap);

                Log.d("MyQRScreen", document.getId() + " => " + document.getData());
            }
}