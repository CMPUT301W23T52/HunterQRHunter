package com.example.hunterqrhunter;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import android.app.Instrumentation;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.hunterqrhunter.page.Qrcode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Map test
 *
 */
public class MapTest {

    public MapTest() {
        // This constructor is empty, but it satisfies the requirement of having a public zero-argument constructor.
    }


    @Test
    public void maxscoretest() {
        List<Integer> scoreList = new ArrayList<Integer>();
        scoreList.clear();
        scoreList.add(0);
        assertEquals(0, maxscore(scoreList));
        scoreList.add(1);
        scoreList.add(2);
        scoreList.add(3);
        scoreList.add(4);
        assertEquals(4, maxscore(scoreList));
    }
    public int maxscore(List<Integer> scoremax) {
        int highestScore = 0;
        for(int i=0;i<scoremax.size();i++){
            int num = scoremax.get(i);
            if (num > highestScore) {
                highestScore = i;
            }
        }
        return highestScore;
    }
}