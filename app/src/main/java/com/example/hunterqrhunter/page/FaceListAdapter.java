package com.example.hunterqrhunter.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hunterqrhunter.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FaceListAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> faceList;
    private List<Integer> scores;
    private String userID;
    private AtomicReference<Integer> highScore = new AtomicReference<>(0);
    private AtomicReference<Integer> lowScore = new AtomicReference<>(Integer.MAX_VALUE);


    public FaceListAdapter(Context context, List<Bitmap> faceList, List<Integer> scores, String userID, AtomicReference<Integer> highScore, AtomicReference<Integer> lowScore) {
        this.context = context;
        this.faceList = faceList;
        this.scores = scores;
        this.userID = userID;
        this.highScore = highScore;
        this.lowScore = lowScore;
    }

    @Override
    public int getCount() {
        return faceList.size();
    }

    @Override
    public Object getItem(int position) {
        return faceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the score for the current position
        int currentScore = scores.get(position);

        // If the current score is the highest or lowest, inflate the corresponding layout
        if (currentScore >= highScore.get()) {
            convertView = inflater.inflate(R.layout.high_face_item, parent, false);
        } else if (currentScore <= lowScore.get()) {
            convertView = inflater.inflate(R.layout.low_face_item, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.face_list_item, parent, false);
        }

        ImageView faceImageView = convertView.findViewById(R.id.faceImage);
        faceImageView.setImageBitmap(faceList.get(position));

        return convertView;
    }

}
