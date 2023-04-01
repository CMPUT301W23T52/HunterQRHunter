package com.example.hunterqrhunter.page;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.HashQR;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class QRScreen extends AppCompatActivity {

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashQR hashQR = new HashQR();
        setContentView(R.layout.activity_qr);
        ListView listView = findViewById(R.id.qr_qr_comment_list);
        Button addText = (Button) findViewById(R.id.qr_add_button);
        Button scanned = (Button) findViewById(R.id.qr_scanned_number);
        Button score = (Button) findViewById(R.id.qr_qr_score) ;
        EditText editText = (EditText) findViewById(R.id.qr_add_comment);
        ImageView imageView = findViewById(R.id.qr_image);
        String hashCode = "1802651831";
        byte[] hash = hashQR.hashObject(hashCode);
        Bitmap faceBitmap = hashQR.generateImageFromHashcode(hash);
        imageView.setImageBitmap(faceBitmap);



        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        ArrayList<String> list = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> list3 = new ArrayList<>(Arrays.asList());
//
//        QR qrObject = new QR("5", "Lingfeng", 300, 2001, list, list2);
//        fb.writeQR(qrObject);
//
//        int qr2 = 300;
//        QR qrObject = new QR("qrlingfeng", "L", 0, 0, list3, list3);
//        ArrayList<String> comments = qrObject.getComments();
//        ArrayAdapter<String> commentAdapter = new ArrayAdapter<>((Context) this,R.layout.activity_qr_comment, comments);
//        listView.setAdapter(commentAdapter);
//
//
//
//        DocumentReference docRef = db.collection("QR Creatures").document(Integer.toString(qr2));
//        docRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // set QR object
//                    qrObject.setHashName((String) document.get("HashName"));
//                    qrObject.setHashName((String) document.get("HashImage"));
//                    qrObject.setHashCode(((Long) Objects.requireNonNull(document.get("HashCode"))).intValue());
//                    qrObject.setScore(((Long) Objects.requireNonNull(document.get("Score"))).intValue());
//                    qrObject.setOwnedBy((ArrayList<String>) document.get("OwnedBy"));
//                    qrObject.setComments((ArrayList<String>) document.get("Comments"));
//
//                    score.setText(Integer.toString(qrObject.getScore()));
//                    scanned.setText(Integer.toString(qrObject.getOwnedBy().size()));
//                    //for (int i = 0; i < (qr.getComments()).size(); i++) {
//                    //comments.set(i, qr.getComments().get(i));
//                    // }
//                    commentAdapter.notifyDataSetChanged();
//
//
//                    // here you can use the callback function to do the work with the get QR.
//                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//
//                } else {
//                    Log.d(TAG, "No such document");
//                }
//            } else {
//                Log.d(TAG, "get failed with ", task.getException());
//            }
//        });
//        addText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<String> commentList;
//                commentList = qrObject.getComments();
//                commentList.add(editText.getText().toString());
//                qrObject.setComments(commentList);
//                fb.writeQR(qrObject);
//                comments.add(editText.getText().toString());
//                commentAdapter.notifyDataSetChanged();
//
//            }
//        });
    }
}
