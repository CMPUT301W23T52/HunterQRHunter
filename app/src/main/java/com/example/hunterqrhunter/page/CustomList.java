package com.example.mapcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Qrcode> {

    private ArrayList<Qrcode> hashcodes;
    private Context context;

    public CustomList(Context context, ArrayList<Qrcode> hashcodes) {
        super(context, 0, hashcodes);
        this.hashcodes = hashcodes;
        this.context = context;
    }
}
