package com.example.hunterqrhunter.page;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import static androidx.test.InstrumentationRegistry.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hunterqrhunter.page.Qrcode;
import com.example.hunterqrhunter.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.color.utilities.Score;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hunterqrhunter.databinding.ActivityQrMapBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Displays the map with lowest and highest score differentiated
 *
 */
public class QRMapScreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityQrMapBinding binding;
    FirebaseFirestore db;
    ArrayList<com.example.hunterqrhunter.page.Qrcode> QRDataList;
    ArrayList<com.example.hunterqrhunter.page.Qrcode> RealList;
    List<Integer> maxes;
    EditText newName;
    ArrayList<String> SearchList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivityQrMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Initialize arrays with some data
        String[] hashnames = { "Hash1", "Hash2"};
        GeoPoint[] locations = { new GeoPoint(0, 0), new GeoPoint(1, 1)};
        int[] scores = {-999999, 999999};
        QRDataList = new ArrayList<>();
        RealList = new ArrayList<>();
        SearchList = new ArrayList<>();
        maxes = new ArrayList<Integer>();
        QRDataList.add((new com.example.hunterqrhunter.page.Qrcode(hashnames[0], locations[0], scores[0], "0")));
        QRDataList.add((new com.example.hunterqrhunter.page.Qrcode(hashnames[1], locations[1], scores[1], "0")));



        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("QR");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                Qrcode highestScore = QRDataList.get(0);
                Qrcode lowestScore = QRDataList.get(1);
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String qrName = doc.getString("name");
                    Log.d(TAG, "hashName: " + qrName);

                    GeoPoint location = doc.getGeoPoint("location");
                    Log.d(TAG, "Location: " + location);

                    int qrScore = doc.getLong("score").intValue();
                    Log.d(TAG, "QR Score: " + qrScore);
                    maxes.add(qrScore);

                    String qid = doc.getString("qid");
                    Log.d(TAG, "qid: " + qid);


                    com.example.hunterqrhunter.page.Qrcode qrCode = new com.example.hunterqrhunter.page.Qrcode(qrName, location, qrScore, qid);
                    RealList.add(qrCode);
                    if (qrScore > highestScore.score) {
                        highestScore = qrCode;
                    }
                    if (qrScore < lowestScore.score) {
                        lowestScore = qrCode;
                    }

                    // Add a marker for each location to the map
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(qrName + " Score = " + qrScore));
                }
                LatLng latLng2 = new LatLng(lowestScore.location.getLatitude(), lowestScore.location.getLongitude());
                Marker markername2 = mMap.addMarker(new MarkerOptions().position(latLng2).title(lowestScore.hashName + " Score = " + minscore(maxes)));
                markername2.remove();
                mMap.addMarker(new MarkerOptions().position(latLng2).title(lowestScore.hashName + " Score = " + lowestScore.score).icon(getMarkerIcon("#0000ff")));

                LatLng latLng = new LatLng(highestScore.location.getLatitude(), highestScore.location.getLongitude());
                Marker markername = mMap.addMarker(new MarkerOptions().position(latLng).title(highestScore.hashName + " Score = " + highestScore.score));
                markername.remove();
                mMap.addMarker(new MarkerOptions().position(latLng).title(highestScore.hashName + " Score = " + highestScore.score).icon(getMarkerIcon("#ffe922")));


                LatLng edmontonLatLng = new LatLng(53.5444, -113.4909);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmontonLatLng, 10));
            } });
        mMap = googleMap;
        newName  = findViewById(R.id.editText_name);
        // Define the onMarkerClick listener for your Google Map
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                String mostRecentItem = "";
                for (int i = 0; i < RealList.size(); i++) {
                    Qrcode code = RealList.get(i);
                    if ((code.location.getLatitude() == position.latitude) && (code.location.getLongitude() == position.longitude)) {
                        if (SearchList.size() > 0) {
                            mostRecentItem = SearchList.get(SearchList.size() - 1);
                            if (mostRecentItem.equals(code.hashName)) {
                                System.out.println(code.qid);
                                String title = code.hashName + " score = " + code.score;
                                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        }
                    }
                }

                for (int i = 0; i < RealList.size(); i++) {
                    Qrcode code = RealList.get(i);
                    if ((code.location.getLatitude() == position.latitude) && (code.location.getLongitude() == position.longitude)) {
                        System.out.println(code.qid);
                        String title = code.hashName + " score = " + code.score;
                        Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                // Return true to indicate that the event has been consumed
                return true;
            }
        });

        final Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String placename = newName.getText().toString();
                if (placename != null && placename instanceof String) {
                    SearchList.add(placename);
                    mMap.clear();
                    LatLng locmatch = null;
                    int idk = 0;
                    for (int i = 0; i < RealList.size(); i++) {
                        Qrcode code = RealList.get(i);
                        if (code.hashName.equals(placename)) {
                            idk += 1;
                            locmatch = new LatLng(code.location.getLatitude(), code.location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(locmatch));
                        }
                    }
                    newName.getText().clear();
                    if (locmatch == null){
                        String errorText = "";
                        if (placename.length() == 0) {
                            errorText = "Search bar is empty, Please enter a location";
                        }
                        else {
                            errorText = "This location isn't on the map";
                        }
                        int duration = Toast.LENGTH_SHORT;
                        Context context = getBaseContext();
                        Toast toast = Toast.makeText(context, errorText, duration);
                        toast.show();
                    }
                    else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locmatch, 10));
                    }
                }
            }
        });

        final Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Qrcode highestScore = QRDataList.get(0);
                Qrcode lowestScore = QRDataList.get(1);
                mMap.clear();
                LatLng locmatch = null;
                for (int i = 0; i < RealList.size(); i++) {
                    Qrcode code = RealList.get(i);
                    locmatch = new LatLng(code.location.getLatitude(), code.location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(locmatch));
                    if (code.score > highestScore.score) {
                        highestScore = code;
                    }
                    if (code.score < lowestScore.score) {
                        lowestScore = code;
                    }
                }
                LatLng latLng2 = new LatLng(lowestScore.location.getLatitude(), lowestScore.location.getLongitude());
                Marker markername2 = mMap.addMarker(new MarkerOptions().position(latLng2).title(lowestScore.hashName + " Score = " + lowestScore.score));
                markername2.remove();
                mMap.addMarker(new MarkerOptions().position(latLng2).title(lowestScore.hashName + " Score = " + lowestScore.score).icon(getMarkerIcon("#0000ff")));

                LatLng latLng = new LatLng(highestScore.location.getLatitude(), highestScore.location.getLongitude());
                Marker markername = mMap.addMarker(new MarkerOptions().position(latLng).title(highestScore.hashName + " Score = " + highestScore.score));
                markername.remove();
                mMap.addMarker(new MarkerOptions().position(latLng).title(highestScore.hashName + " Score = " + highestScore.score).icon(getMarkerIcon("#ffe922")));



                newName.getText().clear();
                LatLng edmontonLatLng = new LatLng(53.5444, -113.4909);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmontonLatLng, 10));
            }
        });


    }

    /**
     * Returns the highest score in an array
     * @return highestScore, which is the highest score in the List of scores
     * @param scoremax
     */
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

    /**
     * Returns the lowest score in an array
     * @return lowestscore, which is the lowest score in the List of scores
     * @param scoremax
     */
    public int minscore(List<Integer> scoremax) {
        int lowestscore = 0;
        for(int i=0;i<scoremax.size();i++){
            int num = scoremax.get(i);
            if (num < lowestscore) {
                lowestscore = i;
            }
        }
        return lowestscore;
    }

    /**
     * Allows me to change marker color
     * @return intended color from hsv code
     * @param color
     */
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
