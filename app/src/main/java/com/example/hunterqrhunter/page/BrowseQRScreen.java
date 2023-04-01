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
import com.example.hunterqrhunter.model.QR;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BrowseQRScreen extends AppCompatActivity {

    FirebaseFirestore database;
    CollectionReference QRCollection;

    private ArrayAdapter<String> usernameListAdapter;
    private ListView usernameListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qrscreen);

        database = FirebaseFirestore.getInstance();
        QRCollection = database.collection("QR");

        usernameListAdapter = new ArrayAdapter<String>(this, R.layout.content_username, R.id.player_name_button, new ArrayList<>());
        usernameListView = findViewById(R.id.username_list);

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
                    List<DocumentSnapshot> userDocsList = querySnapshotTask.getResult().getDocuments();
                    List<Task<Void>> calculateUserIDTasks = new ArrayList<>();

                    for (DocumentSnapshot userDoc : userDocsList) {
                        String userID = userDoc.getId();
                        calculateUserIDTasks.add(calculateUserScores(userID));
                    }

                    Tasks.whenAllComplete(calculateUserIDTasks).addOnCompleteListener(calculateAllUserScoreTask -> {

                        if (calculateAllUserScoreTask.isSuccessful()) {
                            userDocsList.sort(new Comparator<DocumentSnapshot>() {
                                @Override
                                public int compare(DocumentSnapshot doc1, DocumentSnapshot doc2) {

                                    int score1 = doc1.getLong(fieldName).intValue();
                                    int score2 = doc2.getLong(fieldName).intValue();

                                    return score2 - score1;
                                }
                            });

                            usernameListAdapter.clear();
                            for (int i = 0; i < userDocsList.size(); i++) {

                                DocumentSnapshot document = userDocsList.get(i);
                                usernameListAdapter.add(document.getString("username") + ", " + "Rank " + (i+1));
                            }
                            usernameListView.setAdapter(usernameListAdapter);
                        }
                        else {
                            Log.d("Sort users by " + fieldName, "Error getting user scores");
                        }
                    });
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

    public void openUserScores() {
        Intent intent = new Intent(this, UserScoresScreen.class);
        startActivity(intent);
    }
}