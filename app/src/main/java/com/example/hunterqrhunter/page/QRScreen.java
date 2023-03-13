package com.example.hunterqrhunter.page;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.QRCreature;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class QRScreen extends AppCompatActivity {

    private FirebaseFirestore db;
    private FbRepository fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qr);
        ListView listView = findViewById(R.id.qr_qr_comment_list);
        Button addText = (Button) findViewById(R.id.qr_add_button);
        Button scanned = (Button) findViewById(R.id.qr_scanned_number);
        Button score = (Button) findViewById(R.id.qr_qr_score) ;
        EditText editText = (EditText) findViewById(R.id.qr_add_comment);



        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        fb = new FbRepository(db);
        ArrayList<String> list = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> list3 = new ArrayList<>(Arrays.asList());

        QRCreature qr = new QRCreature("5", "Lingfeng", 300, 2001, list, list2);
        fb.writeQR(qr);

        int qr2 = 300;
        QRCreature qrCreature = new QRCreature("qrlingfeng", "L", 0, 0, list3, list3);
        ArrayList<String> comments = qrCreature.getComments();
        ArrayAdapter<String> commentAdapter = new ArrayAdapter<>((Context) this,R.layout.activity_qr_comment, comments);
        listView.setAdapter(commentAdapter);



        DocumentReference docRef = db.collection("QR Creatures").document(Integer.toString(qr2));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // set QR object
                    qrCreature.setHashName((String) document.get("HashName"));
                    qrCreature.setHashName((String) document.get("HashImage"));
                    qrCreature.setHashCode(((Long) Objects.requireNonNull(document.get("HashCode"))).intValue());
                    qrCreature.setScore(((Long) Objects.requireNonNull(document.get("Score"))).intValue());
                    qrCreature.setOwnedBy((ArrayList<String>) document.get("OwnedBy"));
                    qrCreature.setComments((ArrayList<String>) document.get("Comments"));

                    score.setText(Integer.toString(qrCreature.getScore()));
                    scanned.setText(Integer.toString(qrCreature.getOwnedBy().size()));
                    //for (int i = 0; i < (qr.getComments()).size(); i++) {
                    //comments.set(i, qr.getComments().get(i));
                    // }
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
                ArrayList<String> commentList;
                commentList = qrCreature.getComments();
                commentList.add(editText.getText().toString());
                qrCreature.setComments(commentList);
                fb.writeQR(qrCreature);
                comments.add(editText.getText().toString());
                commentAdapter.notifyDataSetChanged();

            }
        });



    }
}
