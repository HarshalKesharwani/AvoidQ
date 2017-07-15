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
public class CustomOuterAdapter extends ArrayAdapter<OuterBill> {
    private final List<OuterBill> foods;
    // public CustomAdapter2(Context context, Cart[] foods) {
    //    super(context, R.layout.custom_row2 ,foods);
    // }

    public CustomOuterAdapter(Context context, List<OuterBill> foods) {
        super(context, 0);
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public OuterBill getItem(int position) {
        return foods.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.custom_outer_history, parent, false);

        OuterBill outerBills = getItem(position);
        String billId = outerBills.getBill_id();
        String date1 = outerBills.getDate();
        double totalPrice = outerBills.getTotal_price();
       // Log.i("rtyiykj","item2 = "+name+" price2 = "+price);

        TextView bill_id = (TextView) customView.findViewById(R.id.bill_id);
        TextView date = (TextView) customView.findViewById(R.id.date);
        TextView total_price = (TextView) customView.findViewById(R.id.total_price);

        bill_id.setText(billId);
        date.setText(date1);
        total_price.setText(String.valueOf(totalPrice));
        return customView;
    }
}
