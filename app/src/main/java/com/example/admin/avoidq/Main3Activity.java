package com.example.admin.avoidq;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main3Activity extends Activity {

    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //String[] foods = {"Bacon", "Ham", "Tuna", "Candy", "Meatball", "Potato"};

        ListView buckysListView = (ListView) findViewById(R.id.buckysListView);

        dbHandler = new MyDBHandler(getApplicationContext(), null, null, 12);
        int dbString = dbHandler.databaseToString();
        if (dbString != 0) {
            String[] bills = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
            for (int i = 0; i < dbString; i++) {
                bills[i] = String.valueOf(i + 1);
            }
            for (int i = 0; i < dbString; i++) {
                Log.i("hjjhh", "bills[i] = " + bills[i]);
            }
            ArrayList<String> billsList = new ArrayList<String>();
            for (int i = 0; i < dbString; i++) {
                billsList.add(i, "Bill number : " + bills[i]);
            }
            ListAdapter buckysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, billsList);

            buckysListView.setAdapter(buckysAdapter);

            buckysListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String food = String.valueOf(parent.getItemAtPosition(position));
                            Toast.makeText(Main3Activity.this, food, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Main3Activity.this, com.example.admin.avoidq.Main2Activity.class);
                            intent.putExtra("history", position + 1);
                            Bundle bndlanimation =
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                            startActivity(intent, bndlanimation);
                        }
                    }
            );
        }
        else {
            Toast.makeText(getApplicationContext(),"No history found!",Toast.LENGTH_LONG).show();
        }
    }

}