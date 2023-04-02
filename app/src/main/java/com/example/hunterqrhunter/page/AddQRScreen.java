package com.example.hunterqrhunter.page;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterqrhunter.data.setImageHelper;
import com.example.hunterqrhunter.model.HashQR;
import com.example.hunterqrhunter.R;
import com.example.hunterqrhunter.model.LocationUtils;
import com.example.hunterqrhunter.model.QR;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;


public class AddQRScreen extends AppCompatActivity {

    private LocationUtils mLocationUtils;
    private Bitmap surroundingImage;
    QR qr = new QR();
    //        Initialize Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * handle the data that is received from previous screen
     * @param savedInstanceState used to receive the data from previous screen
     */
    @SuppressLint("SetTextI18n")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // I want to ask user if user wants to take a picture of surroundings and add it to the QR
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a picture?");
        builder.setMessage("Would you like to add a picture of your surroundings to this QR?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
                })
                .setNegativeButton("Cancel", null)
                .show();



        mLocationUtils = new LocationUtils(this);
        mLocationUtils.requestLocationPermission();
        mLocationUtils.getLocation();

        String scannedData = getIntent().getStringExtra("SCANNED_DATA");
        byte[] HashedValue = HashQR.hashObject(scannedData);

//        get all the views
        Button saveBtn = findViewById(R.id.save_button);
        Button deleteBtn = findViewById(R.id.delete_button);
        TextView pointText = findViewById(R.id.score_text);
        TextView nameText = findViewById(R.id.name_text);
        ImageView faceImage = findViewById(R.id.face_image);
        Bitmap faceBitmap = HashQR.generateImageFromHashcode(HashedValue);

        String hashedName = HashQR.giveQrName(HashedValue);
        Integer hashedScore = HashQR.scoreGen(HashedValue);
        @SuppressLint("HardwareIds") String uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

//        set textViews
        faceImage.setImageBitmap(faceBitmap);
        pointText.setText("Congrats! You scored " + HashQR.scoreGen(HashedValue) +" points!");
        nameText.setText(HashQR.giveQrName(HashedValue));


//        image button that shows the image that is taken
        Button button = findViewById(R.id.image_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the popup layout
                View popupView = getLayoutInflater().inflate(R.layout.popup_image_layout, null);

                // Create the popup window
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                // Set the image in the ImageView
                ImageView popupImageView = popupView.findViewById(R.id.popup_image_view);
                String storagePath = qr.getImageUrl();
                setImageHelper.setImageFromStoragePath(storagePath, popupImageView);

                // Show the popup window
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });


//        initialize the qr object
        double longitude = mLocationUtils.getLongitude();
        double latitude = mLocationUtils.getLatitude();
        GeoPoint location = new GeoPoint(latitude, longitude);
        String qid = UUID.randomUUID().toString();
        qr.setLocation(location);
        qr.setQrcode(scannedData);
        qr.setScore(hashedScore);
        qr.setUid(uid);
        qr.setName(hashedName);
        qr.setQid(qid);
/**
 * Don't add the scanned QR to user's collection and navigate back to the main screen
 */
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        /**
         * user adds qr to the database
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if the user has the same qr in the database
                CollectionReference collectionReference = db.collection("QR");
                collectionReference.whereEqualTo("uid", uid).whereEqualTo("qrcode", scannedData).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        Toast.makeText(AddQRScreen.this, "You already have this QR!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        // update user's collection with the new qr add user's total score and if user's Highest qr code score is higher than the current one update it
                                        db.collection("User").document(uid).update("Total Score", FieldValue.increment(hashedScore))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        db.collection("User").document(uid).get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        int highestScore = documentSnapshot.getLong("Highest Unique Score").intValue();
                                                                        if (highestScore < hashedScore) {
                                                                            db.collection("Users").document(uid).update("Highest Unique Score", hashedScore);
                                                                        }
                                                                    }
                                                            });
                                                    }
                                                });

                                        db.collection("QR").document(qid).set(qr)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    /**
                                                     * when adding was successful toast and navigate back to menu Screen
                                                     * @param aVoid
                                                     */
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AddQRScreen.this, "New QR Added!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                })
                                                /**
                                                 * when adding fails toast the status and navigate back to menu screen
                                                 */
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AddQRScreen.this, "Error adding QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(AddQRScreen.this, MenuScreen.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                });
                                    }
                                }
                            }
                        });

            }
        });

    }
    // Handle camera intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            // Get the returned image from extra
            surroundingImage = (Bitmap) data.getExtras().get("data");

            // Compress the image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            surroundingImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] imageData = baos.toByteArray();

            // Initialize Firebase Storage and create a reference for the image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + qr.getQid() + ".jpg");

            // Upload the compressed image to Firebase Storage and retrieve the download URL
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            // Use imageUrl for further processing
                            qr.setImageUrl("images/" + qr.getQid() + ".jpg");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors
                    qr.setImageUrl("images/No_Image_Available.jpg");
                }
            });
        }
    }

}
