package com.example.hunterqrhunter.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<com.example.hunterqrhunter.page.Qrcode> {

    private ArrayList<com.example.hunterqrhunter.page.Qrcode> hashcodes;
    private Context context;

    /**
     * handle the data that is received from previous screen
     * @param context used to receive the data from previous screen
     * @param context used to receive the data from previous screen
     */
    public CustomList(Context context, ArrayList<com.example.hunterqrhunter.page.Qrcode> hashcodes) {
        super(context, 0, hashcodes);
        this.hashcodes = hashcodes;
        this.context = context;
    }
}
