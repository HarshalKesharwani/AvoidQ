package com.example.admin.avoidq;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by Admin on 26-10-2016.
 */
public class CartAdapter extends ArrayAdapter<Cart> {
    List list = new ArrayList();

    int ind,x=0;

   // public  CartAdapter(CartActivity context, int resource) {
   //     super(context, resource);

   // }
    private final List<Cart> foods;
    // public CustomAdapter2(Context context, Cart[] foods) {
    //    super(context, R.layout.custom_row2 ,foods);
    // }

    public CartAdapter(Context context, List<Cart> foods) {
        super(context, 0);
        this.foods = foods;
    }



    public void add(Cart object) {
        super.add(object);
        foods.add(object);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.activity_cart_list_view, parent, false);

        Cart singleFoodItem = getItem(position);
        String name = singleFoodItem.getItemname();
        double price = singleFoodItem.getPrice();
        int quantity = singleFoodItem.getQuantity();

        Log.i("in getView","item2 = "+name+" price2 = "+price);
        /*
        final Spinner spinner = (Spinner)customView.findViewById(R.id.quantity_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
            {
                int item = Integer.parseInt(parent.getItemAtPosition(position).toString());


                //quantity is in variable "item".
                CartDBHandler cartDBHandler = new CartDBHandler(getContext(),null,null,12);
                cartDBHandler.updateQuantity(item,position);

                // Showing selected spinner item
                // Toast.makeText(parent.getContext(), "Quantity "+item+" Selected", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");
        categories.add("13");
        categories.add("14");
        categories.add("15");
        categories.add("16");
        categories.add("17");
        categories.add("18");
        categories.add("19");
        categories.add("20");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(customView.getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(quantity);
        */
        //final EditText quantity_spinner = (EditText)customView.findViewById(R.id.quantity_spinner);
        TextView buckysText = (TextView) customView.findViewById(R.id.product_name);
        TextView buckysText1 = (TextView) customView.findViewById(R.id.price);
        ImageButton imageButton = (ImageButton)customView.findViewById(R.id.cancel_button);
        imageButton.setTag(position);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer index = (Integer) view.getTag();

                ind = index.intValue();
                foods.remove(index.intValue());
                notifyDataSetChanged();
                CartDBHandler cartDBHandler = new CartDBHandler(getContext(),null,null,12);
                cartDBHandler.remove(ind);

            }
        });


        buckysText.setText(name);
        buckysText1.setText(String.valueOf(price));

       /* quantity_spinner.setText(String.valueOf(quantity));
        quantity_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final String qnty = quantity_spinner.getText().toString();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String q = quantity_spinner.getText().toString();
                                if(qnty == q) {
                                    int q1 = Integer.parseInt(q);

                                    CartDBHandler cartDBHandler = new CartDBHandler(getContext(),null,null,12);
                                    cartDBHandler.updateQuantity(q1,position);
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
*/

        return customView;
    }
}
