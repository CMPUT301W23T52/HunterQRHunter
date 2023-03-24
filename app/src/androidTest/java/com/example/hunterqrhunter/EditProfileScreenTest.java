package com.example.hunterqrhunter;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.EditProfileScreen;
import com.example.hunterqrhunter.page.MenuScreen;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EditProfileScreenTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoadingScreen> rule =
            new ActivityTestRule<>(LoadingScreen.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void properCurrentUser() throws Exception{

    }



    @Test
    public void cancelButton() {
        if (solo.getCurrentActivity().equals(MainActivity.class)) {
            solo.enterText((EditText) solo.getView(R.id.username_sign_up), "user1");
            solo.enterText((EditText) solo.getView(R.id.email_sign_up), "email@gmail.com");
            solo.clickOnButton("btn_signup");
        }

        solo.clickOnButton("Edit Profile");
        solo.assertCurrentActivity("Not EditProfileScreen", EditProfileScreen.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Not MenuScreen", MenuScreen.class);
    }

    @Test
    public void editButton() {

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}