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
import android.widget.Toast;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.UsernameItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BrowseQRScreen extends AppCompatActivity {

    FirebaseFirestore database;
    CollectionReference QRCollection;

    private ArrayList<String> QRList;
    private UsernameItemAdapter QRListAdapter;
    private ListView QRListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qrscreen);

        database = FirebaseFirestore.getInstance();
        QRCollection = database.collection("QR");

        QRList = new ArrayList<>();
        QRListAdapter = new UsernameItemAdapter(this, R.layout.username_item, QRList);
        QRListView = findViewById(R.id.QR_list);

        Button exitButton = findViewById(R.id.exit_button);
        Button switchButton = findViewById(R.id.switch_button);
        Button sortByPointsButton = findViewById(R.id.sort_by_score_button);
        Button sortByNumberScannedButton = findViewById(R.id.sort_by_times_scanned_button);
        SearchView QRSearchView = findViewById(R.id.user_search);

        sortByScore();

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
                sortByScore();
            }
        });

        sortByNumberScannedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByTimesScanned();
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
                List<String> filteredQRList = new ArrayList<>();

                for (String QR : QRList) {
                    String[] parts = QR.split(", ");

                    String qrName = parts[0];

                    if (qrName.toLowerCase().contains(userInput)) {
                        filteredQRList.add(QR);
                    }
                }

                // Update the list adapter with the filtered results
                UsernameItemAdapter filteredListAdapter = new UsernameItemAdapter(BrowseQRScreen.this, R.layout.username_item, filteredQRList);
                QRListView.setAdapter(filteredListAdapter);
                return true;
            }
        });
    }


    public void sortByTimesScanned() {

        QRCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {
                if (querySnapshotTask.isSuccessful()) {
                    List<DocumentSnapshot> QRDocsList = querySnapshotTask.getResult().getDocuments();

                    QRList.clear();
                    HashMap<String, Integer> qrScanCount = new HashMap<>();
                    HashMap<String, String> qrNameByCode = new HashMap<>();
                    for (DocumentSnapshot document : QRDocsList) {
                        String qrCode = document.getString("qrcode");
                        String qrName = document.getString("name");

                        if (qrScanCount.containsKey(qrCode)) {
                            qrScanCount.put(qrCode, qrScanCount.get(qrCode) + 1);
                        } else {
                            qrScanCount.put(qrCode, 1);
                            qrNameByCode.put(qrCode, qrName);
                        }
                    }

                    for (String qrCode : qrScanCount.keySet()) {
                        QRList.add(qrNameByCode.get(qrCode) + ", " + qrScanCount.get(qrCode) + " scans");
                    }

                    QRList.sort(new Comparator<String>() {

                        @Override
                        public int compare(String numScans1, String numScans2) {

                            String[] parts1 = numScans1.split(", ");
                            String[] parts2 = numScans2.split(", ");
                            int scanCount1 = Integer.parseInt(parts1[1].replace(" scans", ""));
                            int scanCount2 = Integer.parseInt(parts2[1].replace(" scans", ""));

                            return Integer.compare(scanCount2, scanCount1);
                        }
                    });

                    QRListView.setAdapter(QRListAdapter);


                } else {
                    Log.d("Sorting by times scanned", "Error getting documents: ", querySnapshotTask.getException());
                }
            }
        });
    }

    public void sortByScore() {

        QRCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {
                if (querySnapshotTask.isSuccessful()) {
                    List<DocumentSnapshot> QRDocsList = querySnapshotTask.getResult().getDocuments();

                    QRDocsList.sort(new Comparator<DocumentSnapshot>() {
                        @Override
                        public int compare(DocumentSnapshot doc1, DocumentSnapshot doc2) {

                            int score1 = doc1.getLong("score").intValue();
                            int score2 = doc2.getLong("score").intValue();

                            return score2 - score1;
                        }
                    });

                    QRList.clear();
                    HashMap<String, String> qrInfo = new HashMap<>();
                    for (DocumentSnapshot document : QRDocsList) {
                        String listItem = document.getString("name") + ", " + document.get("score") + " points";
                        String qrCode = document.getString("qrcode");

                        if (!qrInfo.containsKey(qrCode)) {
                            qrInfo.put(qrCode, listItem);
                            QRList.add(qrInfo.get(qrCode));
                        }
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