package com.example.hunterqrhunter;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.UserScoresScreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.After;

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
    public void checkList() {

    }

    @Test
    public void usernameButtonsTest() {

    }

    @Test
    public void exitButtonTest() {

    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }


}