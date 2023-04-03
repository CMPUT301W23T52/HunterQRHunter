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
import java.util.List;

/**
 * This class represents the screen that displays the scores of all users in the app.
 */
public class UserScoresScreen extends AppCompatActivity {

    // Firebase Firestore instance and collections
    FirebaseFirestore database;
    CollectionReference usersCollection;
    CollectionReference QRCollection;

    // List of usernames and adapter to display them in a ListView
    private ArrayList<String> usernameList;
    private UsernameItemAdapter usernameListAdapter;
    private ListView usernameListView;

    /**
     * Sets up the initial state of the activity
     * Sorts the list of users by total score (descending order) by defualt.
     * @param savedInstanceState The saved instance state of the activity, if any.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scores);

        // Initialize Firebase Firestore and collections
        database = FirebaseFirestore.getInstance();
        usersCollection = database.collection("User");
        QRCollection = database.collection("QR");

        // Initialize the list of usernames and adapter
        usernameList = new ArrayList<>();
        usernameListAdapter = new UsernameItemAdapter(this, R.layout.username_item, usernameList);
        usernameListView = findViewById(R.id.username_list);

        // Get UI elements and set up event handlers
        Button exitButton = findViewById(R.id.exit_button);
        Button switchButton = findViewById(R.id.switch_button);
        Button sortByTotalScoreButton = findViewById(R.id.sort_by_total_score_button);
        Button sortByHighestUniqueQRButton = findViewById(R.id.sort_by_highest_uniqueQR_button);
        SearchView userSearchView = findViewById(R.id.user_search);

        // Sort the list by total score initially
        sortByTotalScore();

        // Set up event handlers for UI elements
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity when the exit button is clicked
                openMenuScreen();
                finish();
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the browse QR screen and finish this activity when the switch button is clicked
                openBrowseQR();
                finish();
            }
        });

        sortByTotalScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort the list by total score when the sort by total score button is clicked
                sortByTotalScore();
            }
        });

        sortByHighestUniqueQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort the list by highest unique QR score when the sort by highest unique QR button is clicked
                sortByUniqueQRScore();
            }
        });

        userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list of usernames when the user types into the search bar
                String userInput = newText.toLowerCase();
                List<String> filteredUsernameList = new ArrayList<>();

                for (String username : usernameList) {
                    String[] parts = username.split(", ");

                    String player = parts[0];

                    if (player.toLowerCase().contains(userInput)) {
                        filteredUsernameList.add(username);
                    }
                }

                // Update the list adapter with the filtered results
                UsernameItemAdapter filteredListAdapter = new UsernameItemAdapter(UserScoresScreen.this, R.layout.username_item, filteredUsernameList);
                usernameListView.setAdapter(filteredListAdapter);
                return true;
            }
        });
    }

    /**
     * Sorts the user scores by total score and updates the UI accordingly.
     */
    public void sortByTotalScore() {
        sortScoresBy("Total Score");
    }

    /**
     * Sorts the user scores by highest unique QR score and updates the UI accordingly.
     */
    public void sortByUniqueQRScore() {
        sortScoresBy("Highest Unique Score");
    }

    /**
     * Sorts the user scores by the specified field name and updates the UI accordingly.
     * @param fieldName the name of the field to sort by
     */
    public void sortScoresBy(String fieldName) {

        // Retrieve all user documents from Firestore
        usersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {
                if (querySnapshotTask.isSuccessful()) {
                    // Retrieve the list of user documents
                    List<DocumentSnapshot> userDocsList = querySnapshotTask.getResult().getDocuments();

                    // Sort the user documents by the specified field
                    userDocsList.sort(new Comparator<DocumentSnapshot>() {

                        @Override
                        public int compare(DocumentSnapshot doc1, DocumentSnapshot doc2) {
                            int score1 = doc1.getLong(fieldName).intValue();
                            int score2 = doc2.getLong(fieldName).intValue();
                            return score2 - score1;
                        }
                    });

                    // Update the usernameList with the sorted results
                    usernameList.clear();
                    for (int i = 0; i < userDocsList.size(); i++) {

                        DocumentSnapshot document = userDocsList.get(i);
                        usernameList.add(document.getString("username") + ", " + "Rank " + (i+1));
                    }

                    // Update the UI with the sorted username list
                    usernameListView.setAdapter(usernameListAdapter);

                }
                else {
                    Log.d("Creating documentList", "Error getting documents: ", querySnapshotTask.getException());
                }
            }
        });

    }

    /**
     * Opens the BrowseQRScreen activity.
     */
    public void openBrowseQR() {
        Intent intent = new Intent(this, BrowseQRScreen.class);
        startActivity(intent);
    }

    public void openMenuScreen() {
        Intent intent = new Intent(this, MenuScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }



}