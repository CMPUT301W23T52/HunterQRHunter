package com.example.hunterqrhunter;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.EditProfileScreen;
import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.SignUpScreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

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
    public void testProperCurrentUser() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        String userID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = database.collection("User");
        DocumentReference userDocRef = usersCollection.document(userID);

        TextView usernameText = (TextView) solo.getView(R.id.edit_username);
        TextView emailText = (TextView) solo.getView(R.id.edit_email);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    assertEquals(usernameText.getText().toString(), document.getString("username"));
                    assertEquals(emailText.getText().toString(), document.getString("email"));
                }
            }
        });
    }

    @Test
    public void testCancelButton() {
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Still on edit screen", MenuScreen.class);
    }

    @Test
    public void testEditButton() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        String userID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = database.collection("User");
        DocumentReference userDocRef = usersCollection.document(userID);

        String testUsername = "b" + System.currentTimeMillis();
        String testEmail = System.currentTimeMillis() + "@gmail.com";
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));
        solo.clearEditText((EditText) solo.getView(R.id.edit_email));
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);
        solo.clickOnButton("Edit");

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    TextView usernameText = (TextView) solo.getView(R.id.edit_username);
                    TextView emailText = (TextView) solo.getView(R.id.edit_email);
                    assertEquals(usernameText.getText().toString(), testUsername);
                    assertEquals(emailText.getText().toString(), testEmail);
                }
            }
        });
    }

    @Test
    public void testUniqueUsernameOnEdit() { //TODO
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        String userID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference userCollection = database.collection("User");
        CollectionReference usernameCollection = database.collection("Usernames");

        String testUsername = "a" + System.currentTimeMillis();
        String testEmail = System.currentTimeMillis() + "@gmail.com";

        HashMap<String, String> testInfo = new HashMap<>();
        testInfo.put("username", testUsername);
        usernameCollection.document(testUsername).set(testInfo);

        solo.clearEditText((EditText) solo.getView(R.id.edit_username));
        solo.clearEditText((EditText) solo.getView(R.id.edit_email));
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);
        solo.clickOnButton("Edit");
        Log.d("TEST", "Current view hierarchy: " + solo.getView(0));

        // Wait for the toast message to appear
        boolean isToast = solo.waitForLogMessage("Username already exists");
        if (!isToast) {
            // Delete document in usernameCollection, test failed
            usernameCollection.document(testUsername).delete();
            throw new RuntimeException("Expected toast message not displayed!");
        }

        // Delete document in usernameCollection
        usernameCollection.document(testUsername).delete();
    }


    @Test
    public void testInvalidUsernameEdit() {

        // Placeholder email
        String testEmail = System.currentTimeMillis() + "@gmail.com";
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);

        // Testing for usernames that are too long
        String testUsername = "abcdefghijklmnopqrstuvwxyz";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));

        // Testing for usernames that are too short
        testUsername = "a";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));

        // Testing for usernames with illegal characters
        testUsername = "a%^+=b";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));

        // Testing for usernames that don't start with alphanumeric character
        testUsername = "  asdf";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));

        // Testing for usernames that don't end with alphanumeric character
        testUsername = "asdf  ";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));

        // Testing for usernames with repeated dots

        testUsername = "asdf.._s";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_username));

        // Testing for usernames with repeated underscores
        testUsername = "asdf.__s";
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);
        solo.clickOnButton("Edit");
    }


    @Test
    public void testInvalidEmailEdit() {

        String testUsername = "b" + System.currentTimeMillis();
        solo.enterText((EditText) solo.getView(R.id.edit_username), testUsername);

        // Testing email with no @ symbol
        String testEmail = "ThisIsNotAnEmail";
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_email));

        // Testing email with illegal characters
        testEmail = "%%@#d.com";
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_email));


        // Testing email with no username
        testEmail = "@.Howclay";
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_email));

        // Testing email with no domain name
        testEmail = "geoffry@";
        solo.enterText((EditText) solo.getView(R.id.edit_email), testEmail);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.edit_email));

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}