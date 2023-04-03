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
    private AtomicReference<Integer> score;
    private String userID;
    private AtomicReference<Integer> highScore = new AtomicReference<>(0);


    public FaceListAdapter(Context context, List<Bitmap> faceList, AtomicReference<Integer> score, String userID, AtomicReference<Integer> highScore) {
        this.context = context;
        this.faceList = faceList;
        this.score = score;
        this.userID = userID;
        this.highScore = highScore;
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
        //get database reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //if the score is greater than or equal to the highest score, inflate the high_face_item layout
        if(score.get() >= highScore.get()){
            // Inflate the layout for this fragment and high_face_item is the layout file
            convertView = inflater.inflate(R.layout.high_face_item, parent, false);
        }
        else{
            // Inflate the layout for this fragment and face_list_item is the layout file
            convertView = inflater.inflate(R.layout.face_list_item, parent, false);
        }
        ImageView faceImageView = convertView.findViewById(R.id.faceImage);
        faceImageView.setImageBitmap(faceList.get(position));

        return convertView;
    }
}
