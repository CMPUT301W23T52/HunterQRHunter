package com.example.hunterqrhunter;

import android.widget.EditText;
import android.widget.TextView;

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
    public ActivityTestRule<EditProfileScreen> rule =
            new ActivityTestRule<>(EditProfileScreen.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void properCurrentUser() throws Exception{


        TextView usernameText = (TextView) solo.getView(R.id.edit_username);
        TextView emailText = (TextView) solo.getView(R.id.edit_email);




    }



    @Test
    public void cancelButton() {
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Still on edit screen", MenuScreen.class);
    }

    @Test
    public void editButton() {

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}