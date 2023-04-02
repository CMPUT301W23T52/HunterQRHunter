package com.example.hunterqrhunter.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hunterqrhunter.R;

import java.util.List;

/**
 * A custom ArrayAdapter used to display a list of usernames and corresponding ranks in a ListView.
 */
public class UsernameItemAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resourceId;
    private List<String> items;

    /**
     * Constructor for UsernameItemAdapter.
     *
     * @param context The context of the application.
     * @param resourceId The resource ID of the layout used for each item in the ListView.
     * @param items The list of usernames and corresponding ranks to be displayed in the ListView.
     */
    public UsernameItemAdapter(Context context, int resourceId, List<String> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
        this.items = items;
    }

    /**
     * Overrides the getView method to inflate the layout and populate it with the correct data.
     *
     * @param position The position of the current item in the ListView.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return The view used for each item in the ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        }

        // Split the current item string into separate parts.
        String item = items.get(position);
        String[] parts = item.split(", ");

        // Extract the username and corresponding rank from the parts array.
        String playerNameStr = parts[0];
        String playerRankStr = parts[1];

        // Get references to the TextViews in the layout and set their text.
        TextView playerName = convertView.findViewById(R.id.player_name_button);
        TextView playerRank = convertView.findViewById(R.id.player_rank_text);
        playerName.setText(playerNameStr);
        playerRank.setText(playerRankStr);

        return convertView;
    }


}