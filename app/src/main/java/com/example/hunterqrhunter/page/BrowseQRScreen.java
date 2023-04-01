package com.example.hunterqrhunter.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.hunterqrhunter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrowseQRScreen extends AppCompatActivity {

    FirebaseFirestore database;
    CollectionReference QRCollection;

    private ArrayAdapter<String> QRListAdapter;
    private ListView QRListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qrscreen);

        database = FirebaseFirestore.getInstance();
        QRCollection = database.collection("QR");

        QRListAdapter = new ArrayAdapter<String>(this, R.layout.username_item, R.id.player_name_button, new ArrayList<>());
        QRListView = findViewById(R.id.QR_list);

        Button exitButton = findViewById(R.id.exit_button);
        Button switchButton = findViewById(R.id.switch_button);
        Button sortByPointsButton = findViewById(R.id.sort_by_total_score_button);

        sortByTotalScore();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserScores();
                finish();
            }
        });

        sortByPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByTotalScore();
            }
        });
    }

    public void sortByTotalScore() {
        sortScoresBy("score");
    }

    public void sortScoresBy(String fieldName) {

        QRCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {
                if (querySnapshotTask.isSuccessful()) {
                    List<DocumentSnapshot> QRDocsList = querySnapshotTask.getResult().getDocuments();

                    QRDocsList.sort(new Comparator<DocumentSnapshot>() {
                        @Override
                        public int compare(DocumentSnapshot doc1, DocumentSnapshot doc2) {

                            int score1 = doc1.getLong(fieldName).intValue();
                            int score2 = doc2.getLong(fieldName).intValue();

                            return score2 - score1;
                        }
                    });

                    QRListAdapter.clear();

                    for (DocumentSnapshot document : QRDocsList) {
                        QRListAdapter.add(document.getString("name") + "," + document.get("score"));
                    }
                    QRListView.setAdapter(QRListAdapter);


                }
                else {
                    Log.d("Creating documentList", "Error getting documents: ", querySnapshotTask.getException());
                }
            }
        });

    }

    public void openUserScores() {
        Intent intent = new Intent(this, UserScoresScreen.class);
        startActivity(intent);
    }
}