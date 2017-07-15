package com.example.admin.avoidq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 21-03-2017.
 */
public class CustomInnerAdapter extends ArrayAdapter<InnerBill> {
    private final List<InnerBill> foods;
    // public CustomAdapter2(Context context, Cart[] foods) {
    //    super(context, R.layout.custom_row2 ,foods);
    // }

    public CustomInnerAdapter(Context context, List<InnerBill> foods) {
        super(context, 0);
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public InnerBill getItem(int position) {
        return foods.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.custom_inner_history, parent, false);

        InnerBill innerBill = getItem(position);
        String product_name = innerBill.getProduct_name();
        double price = innerBill.getPrice();
        int quantity = innerBill.getQuantity();
        // Log.i("rtyiykj","item2 = "+name+" price2 = "+price);

        TextView productname = (TextView) customView.findViewById(R.id.product_name);
        TextView price1 = (TextView) customView.findViewById(R.id.price);
        TextView quantity1 = (TextView) customView.findViewById(R.id.quantity);

        productname.setText(product_name);
        price1.setText(String.valueOf(price));
        quantity1.setText(String.valueOf(quantity));
        return customView;
    }
}
