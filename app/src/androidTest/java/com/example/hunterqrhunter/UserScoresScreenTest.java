package com.example.hunterqrhunter;

import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.BrowseQRScreen;
import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.UserQRScreen;
import com.example.hunterqrhunter.page.UserScoresScreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserScoresScreenTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<UserScoresScreen> rule =
            new ActivityTestRule<>(UserScoresScreen.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void testHighestPointsButton() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference userCollection = database.collection("User");

        Button highestPointsButton = (Button) solo.getView(R.id.sort_by_total_score_button);
        solo.clickOnView(highestPointsButton);

        // Wait for the list to update
        solo.waitForView(R.id.username_list);

        // Retrieve the list of usernames
        ListView userListView = (ListView) solo.getView(R.id.username_list);
        List<String> userList = new ArrayList<>();
        for (int i = 0; i < userListView.getCount(); i++) {
            View listRow = userListView.getChildAt(i);
            TextView playerName = listRow.findViewById(R.id.player_name_button);
            userList.add(playerName.getText().toString());
            System.out.println(userList);
        }

        // Check that the list is sorted by total score
        boolean isSorted = true;
        int previousScore = Integer.MAX_VALUE;
        for (String username : userList) {
            int score = getScore(userCollection, username, "Total Score");
            if (score > previousScore) {
                isSorted = false;
                break;
            }
            previousScore = score;
        }
        assertTrue(isSorted);

    }

    @Test
    public void testHighestQRButton() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference userCollection = database.collection("User");

        Button highestQRButton = (Button) solo.getView(R.id.sort_by_highest_uniqueQR_button);
        solo.clickOnView(highestQRButton);

        // Wait for the list to update
        solo.waitForView(R.id.username_list);

        // Retrieve the list of usernames
        ListView userListView = (ListView) solo.getView(R.id.username_list);
        List<String> userList = new ArrayList<>();
        for (int i = 0; i < userListView.getCount(); i++) {
            View listRow = userListView.getChildAt(i);
            TextView playerName = listRow.findViewById(R.id.player_name_button);
            userList.add(playerName.getText().toString());
        }

        // Check that the list is sorted by total score
        boolean isSorted = true;
        int previousScore = Integer.MAX_VALUE;
        for (String username : userList) {
            int score = getScore(userCollection, username, "Highest Unique Score");
            if (score > previousScore) {
                isSorted = false;
                break;
            }
            previousScore = score;
        }
        assertTrue(isSorted);

    }

    @Test
    public void testUserSearchBar() {

        // Testing search bar for users that should exist based on search filter
        solo.clickOnView(solo.getView(R.id.user_search));
        solo.clearEditText(0);
        solo.enterText(0,"doh");

        ListView userList = (ListView) solo.getView(R.id.username_list);

        for (int i = 0; i < userList.getChildCount(); i++) {
            View listRow = userList.getChildAt(i);
            TextView playerName = listRow.findViewById(R.id.player_name_button);

            // Check that the username contains the search query
            assertTrue(playerName.getText().toString().toLowerCase().contains("doh"));
        }

        // Testing search bar for something that should not exist
        solo.clearEditText(0);
        solo.clickOnView(solo.getView(R.id.user_search));
        solo.enterText(0,"dnnn");

        for (int i = 0; i < userList.getChildCount(); i++) {
            View listRow = userList.getChildAt(i);
            TextView playerName = listRow.findViewById(R.id.player_name_button);

            // Check that the username contains the search query
            assertTrue(playerName.getText().toString().toLowerCase().contains("dnnn"));
        }

    }

    @Test
    public void testSwitchButton() {
        solo.clickOnButton("Switch");
        solo.assertCurrentActivity("Still on user scores page", BrowseQRScreen.class);
    }

    @Test
    public void testUsernameButtons() {

        ListView userList = (ListView) solo.getView(R.id.username_list);

        for (int i = 0; i < userList.getChildCount(); i++) {
            View listRow = userList.getChildAt(i);
            Button usernameButton = listRow.findViewById(R.id.player_name_button);
            solo.clickOnView(usernameButton);
            solo.assertCurrentActivity("Still on user scores page", UserQRScreen.class);
            solo.goBack();
        }

    }

    @Test
    public void testExitButton() {
        solo.clickOnButton("Exit");
        solo.assertCurrentActivity("Still on user scores screen", MenuScreen.class);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }



    // Method used in testHighestPointsButton() to get the total score from a username+score string
    private int getScore(CollectionReference collection, String username, String fieldValue) {

        AtomicInteger totalScore = new AtomicInteger();

        collection.whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                DocumentSnapshot document = documents.get(0);
                Long longFormatScore = (Long) document.getData().get(fieldValue);
                totalScore.set(longFormatScore.intValue());
            }
            else {
                Log.d("getTotalScoreTestMethod", "error getting documents");
            }
        });

        return totalScore.get();
    }


}