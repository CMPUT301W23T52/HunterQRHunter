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

        CollectionReference qrCollection = database.collection("QR");


        Button highestPointsButton = (Button) solo.getView(R.id.sort_by_score_button);
        solo.clickOnView(highestPointsButton);

        // Wait for the list to update
        solo.waitForView(R.id.QR_list);

        // Retrieve the list of usernames
        ListView qrListView = (ListView) solo.getView(R.id.QR_list);
        List<String> qrScoreList = new ArrayList<>();
        for (int i = 0; i < qrListView.getCount(); i++) {
            View listRow = qrListView.getChildAt(i);
            TextView qrScore = listRow.findViewById(R.id.player_rank_text);
            qrScoreList.add(qrScore.getText().toString());
        }

        // Check that the list is sorted by total score
        boolean isSorted = true;
        int previousScore = Integer.MAX_VALUE;
        for (String qrScore : qrScoreList) {
            int score = Integer.parseInt(qrScore);
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

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        CollectionReference qrCollection = database.collection("QR");


        Button mostScanned = (Button) solo.getView(R.id.sort_by_times_scanned_button);
        solo.clickOnView(mostScanned);

        // Wait for the list to update
        solo.waitForView(R.id.QR_list);

        // Retrieve the list of usernames
        ListView qrListView = (ListView) solo.getView(R.id.QR_list);
        List<String> qrList = new ArrayList<>();
        for (int i = 0; i < qrListView.getCount(); i++) {
            View listRow = qrListView.getChildAt(i);
            TextView qrScans = listRow.findViewById(R.id.player_rank_text);
            qrList.add(qrScans.getText().toString());
        }

        // Check that the list is sorted by total score
        boolean isSorted = true;
        int previousScore = Integer.MAX_VALUE;
        for (String qrScans : qrList) {
            int score = Integer.parseInt(qrScans);
            if (score > previousScore) {
                isSorted = false;
                break;
            }
            previousScore = score;
        }
        assertTrue(isSorted);


    }

    @Test
    public void testQRSearchBar() {

        // Testing search bar for users that should exist based on search filter
        solo.clickOnView(solo.getView(R.id.user_search));
        solo.clearEditText(0);
        solo.enterText(0,"ra");

        ListView qrList = (ListView) solo.getView(R.id.QR_list);

        for (int i = 0; i < qrList.getChildCount(); i++) {
            View listRow = qrList.getChildAt(i);
            TextView qrName = listRow.findViewById(R.id.player_name_button);

            // Check that the username contains the search query
            assertTrue(qrName.getText().toString().toLowerCase().contains("ra"));
        }

        // Testing search bar for something that should not exist
        solo.clearEditText(0);
        solo.clickOnView(solo.getView(R.id.user_search));
        solo.enterText(0,"alsdnnv");

        for (int i = 0; i < qrList.getChildCount(); i++) {
            View listRow = qrList.getChildAt(i);
            TextView playerName = listRow.findViewById(R.id.player_name_button);

            // Check that the username contains the search query
            assertTrue(playerName.getText().toString().toLowerCase().contains("alsdnnv"));
        }
    }

    @Test
    public void testSwitchButton() {

        solo.clickOnButton("Switch");
        solo.assertCurrentActivity("Still on browse QR page", BrowseQRScreen.class);

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



}