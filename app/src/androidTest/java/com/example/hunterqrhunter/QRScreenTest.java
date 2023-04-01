package com.example.hunterqrhunter;

import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.QRScreen;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRScreenTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoadingScreen> rule =
            new ActivityTestRule<>(LoadingScreen.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }



    @Test
    public void testAddComment() {
        if (solo.getCurrentActivity().equals(MainActivity.class)) {
            solo.enterText((EditText) solo.getView(R.id.username_sign_up), "user1");
            solo.enterText((EditText) solo.getView(R.id.email_sign_up), "email@gmail.com");
            solo.clickOnButton("signup");
        }
        Button CTButton = (Button) solo.getView("btn_qr");
        solo.clickOnView(CTButton);
        solo.assertCurrentActivity("Not QRScreen", QRScreen.class);
        solo.clickOnText("write your comment");
        solo.clearEditText(0);
        solo.enterText(0,"patrick");
        solo.clickOnButton("add comment");
        assertTrue(solo.searchText("patrick"));
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
