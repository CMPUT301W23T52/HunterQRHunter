package com.example.hunterqrhunter.page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.UsernameItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * This activity allows user to browse QR codes by displaying them in a listview
 */

public class BrowseQRScreen extends AppCompatActivity {

    FirebaseFirestore database;
    CollectionReference QRCollection;

    private ArrayList<String> QRList;
    private UsernameItemAdapter QRListAdapter;
    private ListView QRListView;

    /**
     * Sets up the initial state of the activity
     * Sorts the list of QR codes by score (descending order) by default
     * @param savedInstanceState the saved instance state bundle (if any) from the previous state of the activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qrscreen);

        // Get a reference to the Firebase Firestore database
        database = FirebaseFirestore.getInstance();
        // Get a reference to the "QR" collection in the database
        QRCollection = database.collection("QR");

        // Create an empty list to hold the QR codes
        QRList = new ArrayList<>();
        // Create an adapter to display the QR codes in a ListView
        QRListAdapter = new UsernameItemAdapter(BrowseQRScreen.this, R.layout.username_item, QRList);
        QRListView = findViewById(R.id.QR_list);

        // Get references to various UI elements
        Button exitButton = findViewById(R.id.exit_button);
        Button switchButton = findViewById(R.id.switch_button);
        Button sortByPointsButton = findViewById(R.id.sort_by_score_button);
        Button sortByNumberScannedButton = findViewById(R.id.sort_by_times_scanned_button);
        SearchView QRSearchView = findViewById(R.id.user_search);

        // Sort the list of QR codes by score (descending order) by default
        sortByScore();

        // Set up click listeners for various buttons

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity when the "Exit" button is clicked
                openMenuScreen();
                finish();
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the user scores activity when the "Switch to User Scores" button is clicked
                openUserScores();
                finish();
            }
        });

        sortByPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort the list of QR codes by score (descending order) when the "Sort by Score" button is clicked
                sortByScore();
            }
        });

        sortByNumberScannedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort the list of QR codes by number of times scanned (descending order) when the "Sort by Times Scanned" button is clicked
                sortByTimesScanned();
            }
        });

        QRSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Don't do anything when the search button is clicked (only text changes are handled, not text submissions)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes in the search
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

    /**
     * Sorts QR codes in the QRCollection by score and displays them in a ListView.
     * Each QR code is represented in the ListView as a string in the format "{name}, {scanCount} scans".
     */
    public void sortByTimesScanned() {

        // Call to Firestore to retrieve all documents in the QRCollection
        QRCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {
                if (querySnapshotTask.isSuccessful()) {

                    // Get a list of DocumentSnapshot objects representing each document in the collection
                    List<DocumentSnapshot> QRDocsList = querySnapshotTask.getResult().getDocuments();

                    // Clear the list of QR codes/names and scan counts
                    QRList.clear();
                    HashMap<String, Integer> qrScanCount = new HashMap<>();
                    HashMap<String, String> qrNameByCode = new HashMap<>();

                    // Loop through each document and increment the scan count for the associated QR code
                    // Also add the QR name to the HashMap if it doesn't already exist
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

                    // Create a list of QR codes/names and scan counts for display in the ListView
                    for (String qrCode : qrScanCount.keySet()) {
                        QRList.add(qrNameByCode.get(qrCode) + ", " + qrScanCount.get(qrCode) + " scans");
                    }

                    // Sort the list in descending order based on the number of scans
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

                    // Set the ListView adapter to display the sorted QR code/names and scan counts
                    QRListView.setAdapter(QRListAdapter);


                } else {
                    // Handle errors while getting the documents
                    Log.d("Sorting by times scanned", "Error getting documents: ", querySnapshotTask.getException());
                }
            }
        });
    }

    /**
     * Sorts the QR codes by their score, and displays the sorted list in the QRListView.
     * Each QR code is represented in the ListView as a string in the format "{name}, {score} points".
     */
    public void sortByScore() {

        // get a reference to the QRCollection and add a listener for when the data is retrieved
        QRCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {

                // check if the data retrieval was successful
                if (querySnapshotTask.isSuccessful()) {

                    // get the list of DocumentSnapshots returned by the query
                    List<DocumentSnapshot> QRDocsList = querySnapshotTask.getResult().getDocuments();

                    // sort the DocumentSnapshots in descending order based on their "score" field
                    QRDocsList.sort(new Comparator<DocumentSnapshot>() {
                        @Override
                        public int compare(DocumentSnapshot doc1, DocumentSnapshot doc2) {
                            int score1 = doc1.getLong("score").intValue();
                            int score2 = doc2.getLong("score").intValue();
                            return score2 - score1;
                        }
                    });

                    // clear the QRList and create a HashMap to store QR code and name info
                    QRList.clear();
                    HashMap<String, String> qrInfo = new HashMap<>();

                    // loop through the sorted DocumentSnapshots and add each unique QR code and name pair to qrInfo and QRList
                    for (DocumentSnapshot document : QRDocsList) {
                        String listItem = document.getString("name") + ", " + document.get("score") + " points";
                        String qrCode = document.getString("qrcode");

                        if (!qrInfo.containsKey(qrCode)) {
                            qrInfo.put(qrCode, listItem);
                            QRList.add(qrInfo.get(qrCode));
                        }
                    }

                    // set the adapter for the QRListView to show the sorted QRList
                    QRListView.setAdapter(QRListAdapter);

                } else {
                    // log an error message if there was a problem getting the documents
                    Log.d("Creating documentList", "Error getting documents: ", querySnapshotTask.getException());
                }
            }
        });

    }

    /**
     * Opens the UserScoresScreen activity.
     */
    public void openUserScores() {
        Intent intent = new Intent(this, UserScoresScreen.class);
        startActivity(intent);
    }

    public void openMenuScreen() {
        Intent intent = new Intent(this, MenuScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

}