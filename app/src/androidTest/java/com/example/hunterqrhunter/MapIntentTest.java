package com.example.hunterqrhunter;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.hunterqrhunter.page.MenuScreen;
import com.example.hunterqrhunter.page.QRMapScreen;
import com.example.hunterqrhunter.page.QRScreen;
import com.example.hunterqrhunter.page.UserScoresScreen;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.After;

import java.util.List;

public class MapIntentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<QRMapScreen> rule =
            new ActivityTestRule<>(QRMapScreen.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void intenttest() {
            Context context = getApplicationContext();
            Intent intent = new Intent(getApplicationContext(), QRMapScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(context, intent, null);
            ActivityManager activityManager = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            String currentActivity = taskInfo.get(0).topActivity.getClassName();
            assertEquals(QRMapScreen.class.getName(), currentActivity);
        }
}