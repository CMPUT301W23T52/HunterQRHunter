package com.example.hunterqrhunter.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hunterqrhunter.R;

import java.util.List;

public class UserScoreListAdapter extends ArrayAdapter {
    private Context context;
    private int resourceId;
    private List<String> items;

    public UserScoreListAdapter(Context context, int resourceId, List<String> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        }

        String item = items.get(position);
        String[] parts = item.split(", ");
        String playerNameStr = parts[0];
        String playerRankStr = parts[1];
        //TextView playerName = convertView.findViewById(R.id.player_name);
        //TextView playerRank = convertView.findViewById(R.id.player_rank);
        //playerName.setText(playerNameStr);
        //playerRank.setText(playerRankStr);

        return convertView;
    }
}
