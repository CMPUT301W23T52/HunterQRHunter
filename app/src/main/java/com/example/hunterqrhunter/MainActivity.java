package com.example.hunterqrhunter;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.HashQR;
import com.example.hunterqrhunter.data.FbRepository;
import com.example.hunterqrhunter.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Object hashVal = "yongbin@gmail.com";
    HashQR hashQR = new HashQR();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hash = hashQR.hashObject(hashVal);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FbRepository fb = new FbRepository(db);

        // Create a new user with a first and last name
        //Map<String, Object> user = new HashMap<>();
        //user.put("first", "Ada");
        //user.put("last", "Lovelace");
       // user.put("born", 1815);
        //user.put("Hash?", hash);

        // Add a new document with a generated ID
        //db.collection("users")
                //.add(user)
                //.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    //@Override
                    //public void onSuccess(DocumentReference documentReference) {
                       // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                   // }
               // })
                //.addOnFailureListener(new OnFailureListener() {
                    //@Override
                   // public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    //}
                //});
        User user = new User("5","Lingfeng","Zhu",2001,hash);
        fb.createUser(user);
    }
}