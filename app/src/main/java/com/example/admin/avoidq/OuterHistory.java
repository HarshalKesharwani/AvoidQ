package com.example.admin.avoidq;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OuterHistory extends AppCompatActivity {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    LinearLayout relativeLayout;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String json_url;
    String address;
    int customer_id;
    BillidHandler billidHandler = new BillidHandler(this,null,null,12);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer_history);

        relativeLayout=(LinearLayout)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);
        Intent intent = getIntent();
        customer_id = intent.getIntExtra("customer_id",0);
        address = intent.getStringExtra("address");
        BackgroundTask backgroundTask = (BackgroundTask) new BackgroundTask(getApplicationContext()).execute(String.valueOf(customer_id));


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
                    //System.out.println("no"+random.nextInt(3));
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
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BillidHandler billidHandler = new BillidHandler(this,null,null,12);
        billidHandler.drop();
    }
*/
    class BackgroundTask extends AsyncTask<String, String, String> {

            //String json_url;
            String JSON_STRING;

            String content;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;
            CartAdapter contactAdapter;
            ListView listView;
            OuterBill bill[];



            public BackgroundTask(Context applicationContext) {

            }


            @Override
            protected void onPreExecute() {
                json_url = "http://"+address+"/avoidq/history/billid.php";
                Log.i("address","address = "+json_url);
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


                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("customerid", voids[0]);
                    String query = builder.build().getEncodedQuery();
                    Log.i("query","query = "+query);


                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
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
                    Log.i("json ","json string "+JSON_STRING);
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
                json_string = result;
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("bills");
                    //if (jsonArray != null && jsonArray.length() > 0) {

                    int count = 0;

                    String bill_id,date,pr;
                    double total_price;
                    bill = new OuterBill[jsonArray.length()];
                    Log.i("length","length = "+String.valueOf(jsonArray.length()));
                    billidHandler.drop();
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        bill_id = JO.getString("bill_id");
                        date = JO.getString("date");
                        pr = JO.getString("total_price");
                        Log.i("array","array = "+bill_id+" "+date+" "+pr);

                        if(pr.equals(null) || pr.equals("null")) {
                            Toast.makeText(getApplicationContext(),"Product not found! Please scan the correct product!!",Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            total_price =  Double.parseDouble(pr);
                        }

                        if ((bill_id == "null") || (bill_id == null)) {

                            Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_LONG).show();
                            return;
                        } else {

                            OuterBill outerBill = new OuterBill();
                            outerBill.setBill_id(bill_id);
                            outerBill.setDate(date);
                            outerBill.setTotal_price(total_price);
                            //BillidHandler billidHandler = new BillidHandler(getApplicationContext(),null,null,12);
                            billidHandler.addBills(outerBill);
                            count++;
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                OuterBill[] bills = billidHandler.databaseToArray();// = backgroundTask.getBills();
                for (OuterBill o : bills) {
                    Log.i("billids",o.getBill_id());
                }
                Log.i("positin","position = "+String.valueOf(customer_id));

                ArrayList<OuterBill> outerBillList = new ArrayList<>();
                outerBillList.addAll( Arrays.asList(bills) );
                CustomOuterAdapter customOuterAdapter = new CustomOuterAdapter(getApplicationContext(), outerBillList);
                ListView buckysListView = (ListView) findViewById(R.id.outer_history_list_view);
                buckysListView.setAdapter(customOuterAdapter);
                buckysListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        //BillHandler handler1 = new BillHandler(getApplicationContext(),null,null,12);
                        //handler1.drop();
                        OuterBill outerBill = (OuterBill) adapterView.getItemAtPosition(position);
                        String billid = outerBill.getBill_id();
                        String date = outerBill.getDate();
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, InnerHistory.class);
                        intent.putExtra("customer_id", customer_id);
                        intent.putExtra("address",address );
                        intent.putExtra("bill_id",billid);
                        intent.putExtra("date",date);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                        startActivity(intent, bndlanimation);
                    }
                });


            }
            /*public OuterBill[] getBills(){
                for (OuterBill o : bill) {
                    Log.i("getBills","in getBills "+o.getBill_id());
                }
                return bill;
            }*/

        }


}
