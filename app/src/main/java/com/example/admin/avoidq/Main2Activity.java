package com.example.admin.avoidq;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Main2Activity extends Activity {
    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        final int position = intent.getIntExtra("history",1);
        Log.i("positin","position = "+String.valueOf(position));
        dbHandler = new MyDBHandler(getApplicationContext(),null,null,12);
        Cart[] carts = dbHandler.databaseToArray(position);
        for (Cart c : carts)
        Log.i("in main2activity","item1 = "+c.getItemname()+" price1 = "+c.getPrice());
       // ListAdapter buckysAdapter = new ArrayAdapter<Cart>(this, android.R.layout.simple_list_item_1, carts);

        ArrayList<Cart> cartList = new ArrayList<Cart>();
        cartList.addAll( Arrays.asList(carts) );
        CustomAdapter2 buckysAdapter = new CustomAdapter2(this, cartList);
        ListView buckysListView = (ListView) findViewById(R.id.buckysListView2);
        buckysListView.setAdapter(buckysAdapter);
    /*
        buckysListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String food = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(Main2Activity.this, food, Toast.LENGTH_LONG).show();
                    }
                }
        );*/
    }
}
