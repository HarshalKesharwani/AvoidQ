package com.example.admin.avoidq;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter2 extends ArrayAdapter<Cart> {
    private final List<Cart> foods;
   // public CustomAdapter2(Context context, Cart[] foods) {
    //    super(context, R.layout.custom_row2 ,foods);
   // }

    public CustomAdapter2(Context context, List<Cart> foods) {
        super(context, 0);
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Cart getItem(int position) {
        return foods.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.custom_row2, parent, false);

        Cart singleFoodItem = getItem(position);
        String name = singleFoodItem.getItemname();
        double price = singleFoodItem.getPrice();
        Log.i("rtyiykj","item2 = "+name+" price2 = "+price);

        TextView buckysText = (TextView) customView.findViewById(R.id.buckysText2);
        TextView buckysText1 = (TextView) customView.findViewById(R.id.buckysText3);

        buckysText.setText(name);
        buckysText1.setText(String.valueOf(price));
        return customView;
    }
}
