package com.example.hunterqrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import static java.util.regex.Pattern.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hunterqrhunter.page.AddQRScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddQRScreenTest {

    @Rule
    public ActivityScenarioRule<AddQRScreen> activityRule =
            new ActivityScenarioRule<>(AddQRScreen.class);

    @Test
    public void testAddQR() {
        // Type a name for the QR
        onView(withId(R.id.name_text))
                .perform(typeText("Test QR"), closeSoftKeyboard());

        // Click the save button
        onView(withId(R.id.save_button)).perform(click());

        // Verify that the QR was added to the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("QR");
        collectionReference.whereEqualTo("name", "Test QR")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        assertEquals(1, queryDocumentSnapshots.size());
                    }
                });
    }
};

