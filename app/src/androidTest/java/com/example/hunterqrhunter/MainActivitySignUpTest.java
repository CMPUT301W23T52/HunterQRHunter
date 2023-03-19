package com.example.hunterqrhunter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.MenuScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivitySignUpTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void signUpButton() {

        solo.enterText((EditText) solo.getView(R.id.username_sign_up), "userSignUpTest");
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), "email@gmail.com");
        solo.clickOnButton("btn_signup");

        solo.assertCurrentActivity("Not MenuScreen", MenuScreen.class);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }


}