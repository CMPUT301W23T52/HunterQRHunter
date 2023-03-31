package com.example.hunterqrhunter.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hunterqrhunter.R;

import java.util.ArrayList;

public class FaceListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> faceList;

    public FaceListAdapter(Context context, ArrayList<Bitmap> faceList) {
        this.context = context;
        this.faceList = faceList;
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.face_list_item, parent, false);
        }

        ImageView faceImageView = convertView.findViewById(R.id.QRList);
        Bitmap faceBitmap = faceList.get(position);
        faceImageView.setImageBitmap(faceBitmap);

        return convertView;
    }
}
