package com.example.hunterqrhunter.page;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.R;

import com.example.hunterqrhunter.data.UpdateCommand;
import com.example.hunterqrhunter.data.setImageHelper;
import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.model.QR;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * this is a screen for the individual QR
 *
 * and it will get the qrname, score, scanned time and comments from the firebase
 * people also can write comments and upload it onto the database
 * this page need to pass in a qid from the intent page.
 */
public class QRScreen extends AppCompatActivity {

    private UpdateCommand fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qr);
        ListView listView = findViewById(R.id.qr_qr_comment_list);
        TextView qrName = findViewById(R.id.qr_qr_name);
        Button addText = findViewById(R.id.qr_add_button);
        TextView scanned = findViewById(R.id.qr_scanned_number);
        TextView score = findViewById(R.id.qr_qr_score);
        EditText editText = findViewById(R.id.qr_add_comment);
        ImageView qrImage = findViewById(R.id.qr_face);
        Button button = findViewById(R.id.qr_image_button);

        Intent intent = getIntent();
        String qrCode = intent.getStringExtra("qrCode");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fb = new UpdateCommand(db);
        ArrayList<String> commentList = new ArrayList<>();
        ArrayAdapter<String> commentAdapter = new ArrayAdapter<String>(this, R.layout.activity_qr_comment, commentList);
        listView.setAdapter(commentAdapter);
        DocumentReference docRef = db.collection("QR").document(qrCode);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    QR qr = new QR(qrCode);
                    // set QR object
                    qr.setName((String) document.get("name"));
                    qr.setQrcode((String) document.get("qrcode"));
                    qr.setScore(((Long) Objects.requireNonNull(document.get("score"))).intValue());
                    qr.setUid((String) document.get("uid"));
                    qr.setImageUrl((String) document.get("imageUrl"));
                    qr.setQid((String) document.get("qid"));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Inflate the popup layout
                            View popupView = getLayoutInflater().inflate(R.layout.popup_image_layout, null);

                            // Create the popup window
                            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                            // Set the image in the ImageView
                            ImageView popupImageView = popupView.findViewById(R.id.popup_image_view);
                            String storagePath = document.get("imageUrl").toString();
                            setImageHelper.setImageFromStoragePath(storagePath, popupImageView);

                            // Show the popup window
                            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                        }
                    });

                    if(document.get("comments") == null) {
                        qr.setComments(new ArrayList<>());
                    }

                    else{
                        qr.setComments((ArrayList<String>) document.get("comments"));
                    }
                    db.collection("QR").whereEqualTo("qrcode",qr.getQrcode()).get().addOnCompleteListener(
                            task1 -> {

                                if (task1.isSuccessful()) {
                                    int scanNum = 0;
                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                        scanNum += 1;

                                    }
                                    scanned.setText(Integer.toString(scanNum));
                                } else {
                                    scanned.setText(Integer.toString(1));
                                    Log.d(TAG, "Error getting documents: ", task1.getException());
                                }
                           });
                    commentList.addAll(qr.getComments());
                    score.setText(Integer.toString(qr.getScore()));

                    qrName.setText(qr.getName());
                    commentAdapter.notifyDataSetChanged();

                    qrImage.setImageBitmap(HashQR.generateImageFromHashcode(HashQR.hashObject(qr.getQrcode())));


                    // here you can use the callback function to do the work with the get QR.
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });




        addText.setOnClickListener(v -> {
            String newComment = editText.getText().toString();
            if (!newComment.isEmpty()) {
                commentList.add(newComment);
                editText.setText("");
            }
            commentAdapter.notifyDataSetChanged();
            fb.updateQRComments(qrCode, commentList);
        });

    }

}
