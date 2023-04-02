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
import android.widget.SearchView;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.QR;
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

    private ArrayList<String> QRList;
    private ArrayAdapter<String> QRListAdapter;
    private ListView QRListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qrscreen);

        database = FirebaseFirestore.getInstance();
        QRCollection = database.collection("QR");

        QRList = new ArrayList<>();
        QRListAdapter = new ArrayAdapter<String>(this, R.layout.username_item, R.id.player_name_button, new ArrayList<>());
        QRListView = findViewById(R.id.QR_list);

        Button exitButton = findViewById(R.id.exit_button);
        Button switchButton = findViewById(R.id.switch_button);
        Button sortByPointsButton = findViewById(R.id.sort_by_total_score_button);
        SearchView QRSearchView = findViewById(R.id.user_search);

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

        QRSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<String> filteredUsernameList = new ArrayList<>();

                for (String username : QRList) {
                    if (username.toLowerCase().contains(userInput)) {
                        filteredUsernameList.add(username);
                    }
                }

                // Update the list adapter with the filtered results
                ArrayAdapter<String> filteredListAdapter = new ArrayAdapter<String>(BrowseQRScreen.this, R.layout.username_item, R.id.player_name_button, filteredUsernameList);
                QRListView.setAdapter(filteredListAdapter);
                return true;
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

                    QRList.clear();

                    for (DocumentSnapshot document : QRDocsList) {
                        QRList.add(document.getString("name") + "," + document.get("score"));
                    }
                    QRListAdapter.notifyDataSetChanged();
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