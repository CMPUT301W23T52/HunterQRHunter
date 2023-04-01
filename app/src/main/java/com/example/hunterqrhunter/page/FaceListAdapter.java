package com.example.hunterqrhunter.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hunterqrhunter.R;

import java.util.List;

public class FaceListAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> faceList;

    public FaceListAdapter(Context context, List<Bitmap> faceList) {
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
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Inflate the layout for this fragment and face_list_item is the layout file
            convertView = inflater.inflate(R.layout.face_list_item, parent, false);
        }

        ImageView faceImageView = convertView.findViewById(R.id.faceImage);
        faceImageView.setImageBitmap(faceList.get(position));

        return convertView;
    }

}
