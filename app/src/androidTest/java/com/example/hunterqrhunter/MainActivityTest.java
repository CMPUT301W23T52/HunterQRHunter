package com.example.hunterqrhunter;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.MenuScreen;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSignUpButton() {

        solo.enterText((EditText) solo.getView(R.id.username_sign_up), "userSignUpTest");
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), "email@gmail.com");
        solo.clickOnButton("sign_up_button");

        solo.assertCurrentActivity("Not MenuScreen", MenuScreen.class);
    }

    @Test
    public void testUniqueUsername() {

    }


    @Test
    public void testValidUsername() {

    }


    @Test
    public void testValidEmail() {

    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }


}