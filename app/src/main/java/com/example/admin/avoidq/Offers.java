package com.example.admin.avoidq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Offers extends AppCompatActivity {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    LinearLayout relativeLayout;
    String json_string;
    String json_url;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterFish mAdapter;
    String ipaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        relativeLayout=(LinearLayout)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);

        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        //Make call to AsyncTask
        Intent intent = getIntent();
        ipaddress = intent.getStringExtra("ipaddress");
        new AsyncLogin(getApplicationContext()).execute();
    }
    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
//Since we want to change something which is on hte UI
//so we have to run on UI thread..
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {

                    Random random=new Random();//this is random generator
                    //  for(int i=0;i<500;i++) {
                    //System.out.println("***"+i);
                    relativeLayout.setBackgroundResource(images[random.nextInt(3)]);
                    //System.out.println("***"+((random.nextInt(1))%1000000000)%3);
                    System.out.println("no"+random.nextInt(3));
                    // }
                    //  relativeLayout.setBackgroundColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//you have to stop the timer when is your activity has stopped
//otherwise it will keep running in the background
        timer.cancel();
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        //String json_url;
        String JSON_STRING;

        String content;
        String json_string;
        JSONObject jsonObject;
        JSONArray jsonArray;
        CartAdapter contactAdapter;
        ListView listView;

        public AsyncLogin(Context applicationContext) {

        }


        @Override
        protected void onPreExecute() {
            json_url = "http://"+ipaddress+"/avoidq/offer/offer.php";
            Log.i("json_url","in dialog "+json_url);
            // json_url = "http://"+address+"/barcode/barcode.php";
        }


        @Override
        protected String doInBackground(String... voids) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(READ_TIMEOUT);
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setRequestMethod("POST");


                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                //Uri.Builder builder = new Uri.Builder()
                  //      .appendQueryParameter("barcode", voids[0]);
                //String query = builder.build().getEncodedQuery();
                //Log.i("query","in dialog "+query);


                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
               // writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            List<DataFish> data=new ArrayList<>();
            json_string = result;
            try {
                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("offer");
                //if (jsonArray != null && jsonArray.length() > 0) {

                int count = 0;

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    fishData.offerImage= json_data.getString("price");
                    fishData.itemName= json_data.getString("item_name");
                    fishData.describe= json_data.getString("discount") + " % OFF";
                    // fishData.describe= json_data.getString("describe");

                    // ***********describe for json file and info for php file ***************

                    //    fishData.price= json_data.getInt("price");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(Offers.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Offers.this));

            } catch (JSONException e) {
                Toast.makeText(Offers.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}


