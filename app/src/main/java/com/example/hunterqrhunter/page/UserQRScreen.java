package com.example.hunterqrhunter.page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.HashQR;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class UserQRScreen extends AppCompatActivity {
    {/**
     [v]MyQRScreen has all the QRs the player acquired
     [v]Number of the Qrs
     [v]Score of the player
     [v]Player can delete the QR if they want to
     */}
    private FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        // retrieve the user ID from the intent
        String userID = getIntent().getStringExtra("userID");

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = database.collection("User");
        // get user's name
        usersCollection.whereEqualTo(FieldPath.of("uid"), userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String name = document.getString("username");
                    TextView nameText = findViewById(R.id.title_text);
                    nameText.setText(name + "'s Collection");
                }
            }
        });
        //Initialize firestore database
        db = FirebaseFirestore.getInstance();

        //get the total score of the user
        AtomicInteger totalScore = new AtomicInteger(0);

        //total number of the QRs
        AtomicInteger totalQRs = new AtomicInteger();

        //get the QR collection
        CollectionReference qrRef = db.collection("QR");

        //arrayList to store the QRs
        ArrayList<Bitmap> qrList = new ArrayList<>();

        //arrayList to store the qid
        ArrayList<String> qidList = new ArrayList<>();

        //arrayList to store the scores
        ArrayList<Integer> scoreList = new ArrayList<>();

        //Initialize the HashQR class
        HashQR hashQR = new HashQR();

        //button for delete the QR
        GridView faceList = findViewById(R.id.QRList);
        TextView qrText = findViewById(R.id.num_qr_text);
        TextView scoreText = findViewById(R.id.qr_points_text);
        AtomicReference<Integer> highScore = new AtomicReference<>(0);
        AtomicReference<Integer> lowScore = new AtomicReference<>(Integer.MAX_VALUE);
        db.collection("User").document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    highScore.set(document.getLong("Highest Unique Score").intValue());

                }
            }
        });

        FaceListAdapter adapter = new FaceListAdapter(this, qrList, scoreList, userID, highScore, lowScore);


        //when the user click any item in the facelist Listview
        faceList.setOnItemClickListener((parent, view, position, id) -> {
            //print id of the QR that the user clicked
            String qid = qidList.get(position);
            Intent intent = new Intent(getApplicationContext(), QRScreen.class);
            intent.putExtra("qrCode",qid);
            startActivity(intent);
        });

        //if userID and qrRef's uid is the same, then get the score of the user
        qrRef.whereEqualTo(FieldPath.of("uid"), userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ImageView faceImageView = null;
                Bitmap faceBitmap = null;
                Bitmap bitmap = null;
                for (DocumentSnapshot document : task.getResult()) {

                    //get the score of the user and add it to the total score
                    Integer score = Integer.parseInt(document.getData().get("score").toString());

                    //update scoreList
                    scoreList.add(score);
                    if (score < lowScore.get()) {
                        lowScore.set(score);
                    }

                    //update the total score
                    totalScore.addAndGet(score);

                    //update the total number of the QRs
                    totalQRs.getAndIncrement();

                    //get the qrcode field from the QR document
                    String qrCode = document.getData().get("qrcode").toString();

                    //get the qid field from the QR document
                    String qid = document.getData().get("qid").toString();

                    //add the qid to the qidList
                    qidList.add(qid);

                    //create a face class based on the qrCode
                    byte[] hash = hashQR.hashObject(qrCode);
                    faceBitmap = hashQR.generateImageFromHashcode(hash);
                    bitmap = Bitmap.createBitmap(faceBitmap);
                    qrList.add(bitmap);

                    Log.d("MyQRScreen", document.getId() + " => " + document.getData());
                }

                //update the total score
                scoreText.setText(totalScore + " points");

                //update the total number of the QRs
                qrText.setText("# " + totalQRs);


                faceList.setAdapter(adapter);

            } else {
                Log.d("MyQRScreen", "Error getting documents: ", task.getException());
            }
        });


    }
}
