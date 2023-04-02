package com.example.hunterqrhunter.page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserScoresScreen extends AppCompatActivity {

    FirebaseFirestore database;
    CollectionReference usersCollection;
    CollectionReference QRCollection;

    private ArrayList<String> usernameList;
    private UsernameItemAdapter usernameListAdapter;
    private ListView usernameListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scores);

        database = FirebaseFirestore.getInstance();
        usersCollection = database.collection("User");
        QRCollection = database.collection("QR");

        usernameList = new ArrayList<>();
        usernameListAdapter = new UsernameItemAdapter(this, R.layout.username_item, usernameList);
        usernameListView = findViewById(R.id.username_list);

        Button exitButton = findViewById(R.id.exit_button);
        Button switchButton = findViewById(R.id.switch_button);
        Button sortByTotalScoreButton = findViewById(R.id.sort_by_total_score_button);
        Button sortByHighestUniqueQRButton = findViewById(R.id.sort_by_highest_uniqueQR_button);
        SearchView userSearchView = findViewById(R.id.user_search);

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
                openBrowseQR();
                finish();
            }
        });

        sortByTotalScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByTotalScore();
            }
        });

        sortByHighestUniqueQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



    public void sortByTotalScore() {
        sortScoresBy("Total Score");
    }

    public void sortByUniqueQRScore() {
        sortScoresBy("Highest Unique Score");
    }

    public void sortScoresBy(String fieldName) {

        usersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> querySnapshotTask) {
                if (querySnapshotTask.isSuccessful()) {
                    List<DocumentSnapshot> userDocsList = querySnapshotTask.getResult().getDocuments();

                    userDocsList.sort(new Comparator<DocumentSnapshot>() {

                        @Override
                        public int compare(DocumentSnapshot doc1, DocumentSnapshot doc2) {

                            int score1 = doc1.getLong(fieldName).intValue();
                            int score2 = doc2.getLong(fieldName).intValue();

                            return score2 - score1;
                        }
                    });

                    usernameList.clear();
                    for (int i = 0; i < userDocsList.size(); i++) {

                        DocumentSnapshot document = userDocsList.get(i);
                        usernameList.add(document.getString("username") + ", " + "Rank " + (i+1));
                    }
                    usernameListView.setAdapter(usernameListAdapter);


                }
                else {
                    Log.d("Creating documentList", "Error getting documents: ", querySnapshotTask.getException());
                }
            }
        });

    }



    private Task<Void> calculateUserScores(String userID) {
        TaskCompletionSource<Void> completionSource = new TaskCompletionSource<>();
        AtomicInteger totalScore = new AtomicInteger(0);
        AtomicInteger highestUniqueQRScore = new AtomicInteger(0);

        QRCollection.whereEqualTo(FieldPath.of("uid"), userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> userIDQueryTask) {

                if (userIDQueryTask.isSuccessful()) {
                    List<DocumentSnapshot> QRCollectionDocumentList = userIDQueryTask.getResult().getDocuments();

                    for (DocumentSnapshot QRdocument : QRCollectionDocumentList) {
                        int score = QRdocument.getLong("score").intValue();
                        totalScore.addAndGet(score);
                        if (score > highestUniqueQRScore.get()) {
                            highestUniqueQRScore.set(score);
                        }
                        Log.d("Calculating all users scores", QRdocument.getId() + " => " + QRdocument.getData());
                    }
                }
                else {
                    Log.d("Calculating all users scores", "Error getting user scores");
                }

                HashMap<String, Integer> userScoreData = new HashMap<>();
                userScoreData.put("Total Score", totalScore.get());
                userScoreData.put("Highest Unique Score", highestUniqueQRScore.get());

                usersCollection.document(userID).set(userScoreData, SetOptions.merge()).addOnCompleteListener(updateUserScoreTask -> {
                    completionSource.setResult(null);
                });
            }
        });

        return completionSource.getTask();
    }

public void openBrowseQR() {
    Intent intent = new Intent(this, BrowseQRScreen.class);
    startActivity(intent);
    }



}