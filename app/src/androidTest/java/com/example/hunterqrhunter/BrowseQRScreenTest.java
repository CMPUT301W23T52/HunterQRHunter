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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BrowseQRScreenTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<BrowseQRScreen> rule =
            new ActivityTestRule<>(BrowseQRScreen.class, true, true);

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
            userList.add(playerName.toString());
        }

        // Check that the list is sorted by total score
        boolean isSorted = true;
        int previousScore = Integer.MAX_VALUE;
        for (String username : userList) {
            int score = getTotalScore(userCollection, username);
            if (score > previousScore) {
                isSorted = false;
                break;
            }
            previousScore = score;
        }
        assertTrue(isSorted);

    }

    @Test
    public void testMostScannedButton() {


    }

    @Test
    public void testSearchBar() {

        solo.clickOnView(solo.getView(R.id.user_search));
        solo.clearEditText(0);
        solo.enterText(0,"ra");
        solo.goBack();

        ListView userList = (ListView) solo.getView(R.id.username_list);

        for (int i = 0; i < userList.getChildCount(); i++) {
            View listRow = userList.getChildAt(i);
            TextView playerName = listRow.findViewById(R.id.player_name_button);

            // Check that the username contains the search query
            assertTrue(playerName.getText().toString().toLowerCase().contains("ra"));
        }

        solo.clearEditText(0);

    }

    @Test
    public void testSwitchButton() {

        solo.clickOnButton("Switch");
        solo.assertCurrentActivity("Still on browse QR page", BrowseQRScreen.class);

    }

    @Test
    public void testQRButtons() {

        ListView qrList = (ListView) solo.getView(R.id.QR_list);

        for (int i = 0; i < qrList.getChildCount(); i++) {
            View listRow = qrList.getChildAt(i);
            Button qrButton = listRow.findViewById(R.id.player_name_button);
            solo.clickOnView(qrButton);
            solo.assertCurrentActivity("Still on browse QR page", BrowseQRScreen.class);
            solo.goBack();
        }
    }


    @Test
    public void testExitButton() {

        solo.clickOnButton("Exit");

        solo.assertCurrentActivity("Still on browse QR page", MenuScreen.class);

    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    // Method used in testHighestPointsButton() to get the total score from a username+score string
    private int getTotalScore(CollectionReference collection, String username) {

        AtomicInteger totalScore = new AtomicInteger();

        collection.document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                totalScore.set((int) document.getData().get("Total Score"));
            }
            else {
                Log.d("getTotalScoreTestMethod", "error getting documents");
            }
        });

        return totalScore.get();
    }


}