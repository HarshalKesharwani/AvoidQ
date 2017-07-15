package com.example.admin.avoidq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter3 extends ArrayAdapter<String> {

    CustomAdapter3(Context context, String[] foods) {
        super(context, R.layout.custom_row3 ,foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.custom_row3, parent, false);

        String singleFoodItem = getItem(position);
        TextView buckysText = (TextView) customView.findViewById(R.id.buckysText);

        buckysText.setText(singleFoodItem);
        return customView;
    }
}