package com.example.hunterqrhunter;

import static org.junit.Assert.assertEquals;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.SignUpScreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignUpScreenTest {
    private Solo solo;
    private FirebaseFirestore database;

    @Rule
    public ActivityTestRule<SignUpScreen> rule =
            new ActivityTestRule<>(SignUpScreen.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSignUpButtonValidInput() throws Exception { // TODO

        // Initialize unique test user info
        String testUsername = "a" + System.currentTimeMillis();
        String testEmail = System.currentTimeMillis() + "@gmail.com";

        // Initialize database and Collections (and queries to collections)
        database = FirebaseFirestore.getInstance();
        CollectionReference userCollection = database.collection("User");
        CollectionReference usernameCollection = database.collection("Usernames");
        Query queryUsers = userCollection.whereEqualTo("username", testUsername);
        Query queryEmail = userCollection.whereEqualTo("email", testEmail);
        Query queryUsernames = usernameCollection.whereEqualTo("username", testUsername);




        // Adding the test user
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");

        solo.assertCurrentActivity("Not MenuScreen", MenuScreen.class);

        queryUsers.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                assertEquals(1, snapshot.size());
                DocumentSnapshot document = snapshot.getDocuments().get(0);
                if (! document.exists()) {
                    // Test failed
                    try {
                        throw new Exception("Document does not exist, first query failed");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        queryUsernames.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                assertEquals(1, snapshot.size());
                DocumentSnapshot document = snapshot.getDocuments().get(0);
                if (! document.exists()) {
                    // Test failed
                    try {
                        throw new Exception("Document does not exist, third query failed");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        queryEmail.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                DocumentSnapshot document = snapshot.getDocuments().get(0);
                if (document.exists()) {
                    //Test passed, delete document to clear database
                    String docId = document.getId();
                    database.collection("Usernames").document(docId).delete();
                }
                else{
                    // Test failed
                    try {
                        throw new Exception("Document does not exist, second query failed");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        usernameCollection.document(testUsername).delete();


    }

    @Test
    public void testUniqueUsername() { //TODO

        String testUsername = "b" + System.currentTimeMillis();
        String testEmail = System.currentTimeMillis() + "@gmail.com";

        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");



        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");

    }


    @Test
    public void testValidUsername() {

        // Placeholder email
        String testEmail = System.currentTimeMillis() + "@gmail.com";
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);

        // Testing for usernames that are too long
        String testUsername = "abcdefghijklmnopqrstuvwxyz";
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.username_sign_up));

        // Testing for usernames that are too short
        testUsername = "a";
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.username_sign_up));

        // Testing for usernames with illegal characters
        testUsername = "a%^+=b";
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.username_sign_up));

        // Testing for usernames that don't start with alphanumeric character
        testUsername = "  asdf";
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.username_sign_up));

        // Testing for usernames that don't end with alphanumeric character
        testUsername = "asdf  ";
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.username_sign_up));

        // Testing for usernames with repeated dots


        // Testing for usernames with repeated underscores
        testUsername = "asdf.__s";
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
    }


    @Test
    public void testValidEmail() {

        String testUsername = "c" + System.currentTimeMillis();
        solo.enterText((EditText) solo.getView(R.id.username_sign_up), testUsername);

        // Testing email with no @ symbol
        String testEmail = "ThisIsNotAnEmail";
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.email_sign_up));

        // Testing email with illegal characters
        testEmail = "%%@#d.com";
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.email_sign_up));

        // Testing email with no username
        testEmail = "@.Howclay";
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.email_sign_up));

        // Testing email with no domain name
        testEmail = "geoffry@";
        solo.enterText((EditText) solo.getView(R.id.email_sign_up), testEmail);
        solo.clickOnButton("signup");
        solo.assertCurrentActivity("Not sign up page", SignUpScreen.class);
        solo.clearEditText((EditText) solo.getView(R.id.email_sign_up));

    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }


}