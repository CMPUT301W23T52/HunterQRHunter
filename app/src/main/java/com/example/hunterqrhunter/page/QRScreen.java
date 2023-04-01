package com.example.hunterqrhunter.page;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.data.UpdateCommend;
import com.example.hunterqrhunter.model.QR;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * this is a screen for the individual QR
 *
 * and it will get the qrname, score, scanned time and comments from the firebase
 * people also can write comments and upload it onto the database
 * this page need to pass in a qid from the intent page.
 */
public class QRScreen extends AppCompatActivity {

    private UpdateCommend fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qr);
        ListView listView = findViewById(R.id.qr_qr_comment_list);
        TextView qrName = findViewById(R.id.qr_qr_name);
        Button addText = (Button) findViewById(R.id.qr_add_button);
        TextView scanned = (TextView) findViewById(R.id.qr_scanned_number);
        TextView score = (TextView) findViewById(R.id.qr_qr_score);
        EditText editText = (EditText) findViewById(R.id.qr_add_comment);

        Intent intent = getIntent();
        String qrCode = intent.getStringExtra("qrCode");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fb = new UpdateCommend(db);
        ArrayList<String> commentList = new ArrayList<>();
        ArrayAdapter<String> commentAdapter = new ArrayAdapter<String>((Context) this, R.layout.activity_qr_comment, commentList);
        listView.setAdapter(commentAdapter);
        DocumentReference docRef = db.collection("QR").document(qrCode);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    QR qr = new QR(qrCode);
                    // set QR object
                    qr.setName((String) document.get("name"));
                    qr.setQrcode((String) document.get("qrcode"));
                    qr.setScore(((Long) Objects.requireNonNull(document.get("score"))).intValue());
                    qr.setUid((String) document.get("uid"));
                    if(document.get("comments") == null) {
                        qr.setComments(new ArrayList<>());
                    }

                    else{
                        qr.setComments((ArrayList<String>) document.get("comments"));
                    }
                    db.collection("QR").whereEqualTo("qrcode",qr.getQrcode()).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {
                                        int scanNum = 0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            scanNum += 1;

                                        }
                                        scanned.setText(Integer.toString(scanNum));
                                    } else {
                                        scanned.setText(Integer.toString(1));
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                               }
                           });
                    commentList.addAll(qr.getComments());
                    score.setText(Integer.toString(qr.getScore()));

                    qrName.setText(qr.getName());
                    commentAdapter.notifyDataSetChanged();


                    // here you can use the callback function to do the work with the get QR.
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newComment = editText.getText().toString();
                if (!newComment.isEmpty()) {
                    commentList.add(newComment);
                }
                commentAdapter.notifyDataSetChanged();
                fb.updateQRComments(qrCode, commentList);
            }
        });

    }

}
