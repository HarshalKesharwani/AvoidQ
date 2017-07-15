package com.example.admin.avoidq;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class InnerHistory extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String json_url;
    String address;
    int customer_id;
    String bill_id,date;
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    LinearLayout relativeLayout;
    BillHandler billHandler = new BillHandler(this,null,null,12);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_history);

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
        bill_id = intent.getStringExtra("bill_id");
        date = intent.getStringExtra("date");

        TextView dat = (TextView)findViewById(R.id.date);
        dat.setText("Date : "+date);
        ImageView barcode = (ImageView)findViewById(R.id.barcode);
        String barcode_data = bill_id;
       // barcode_data = String.valueOf(str);

        // barcode image
        Bitmap bitmap = null;
        // ImageView iv = new ImageView(this);


        try {

            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            barcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        BackgroundTask backgroundTask = (BackgroundTask) new BackgroundTask(getApplicationContext()).execute(String.valueOf(customer_id),bill_id);


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

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            BillidHandler billidHandler = new BillidHandler(this,null,null,12);
            billidHandler.drop();
        }



    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }



    class BackgroundTask extends AsyncTask<String, String, String> {

        //String json_url;
        String JSON_STRING;

        String content;
        String json_string;
        JSONObject jsonObject;
        JSONArray jsonArray;
        CartAdapter contactAdapter;
        ListView listView;
        //InnerBill bill[];



        public BackgroundTask(Context applicationContext) {

        }


        @Override
        protected void onPreExecute() {
            json_url = "http://"+address+"/avoidq/history/bill.php";
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
                        .appendQueryParameter("customer_id", voids[0]).appendQueryParameter("bill_id",voids[1]);
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
                jsonArray = jsonObject.getJSONArray("bill");
                //if (jsonArray != null && jsonArray.length() > 0) {

                int count = 0;

                String product_name,p,q;
                int quantity;
                double price;
               // bill = new InnerBill[jsonArray.length()];
                Log.i("length","length = "+String.valueOf(jsonArray.length()));
                billHandler.drop();
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    product_name = JO.getString("item_name");
                    q = JO.getString("quantity");
                    p = JO.getString("price");
                    Log.i("array","array = "+product_name+" "+p+" "+q);

                    if(p.equals(null) || p.equals("null")) {
                        Toast.makeText(getApplicationContext(),"Product not found! Please scan the correct product!!",Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        price = Double.parseDouble(p);
                        quantity = Integer.parseInt(q);
                    }

                    if ((product_name == "null") || (product_name == null)) {

                        Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        InnerBill outerBill = new InnerBill();
                        outerBill.setProduct_name(product_name);
                        outerBill.setPrice(price);
                        outerBill.setQuantity(quantity);
                        //BillHandler billHandler = new BillHandler(getApplicationContext(),null,null,12);
                        billHandler.addBills(outerBill);
                        count++;
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            InnerBill[] bills = billHandler.databaseToArray();// = backgroundTask.getBills();
            for (InnerBill o : bills) {
                Log.i("prductname",o.getProduct_name());
            }
            Log.i("positin","position = "+String.valueOf(customer_id));

            ArrayList<InnerBill> innerBillList = new ArrayList<>();
            innerBillList.addAll( Arrays.asList(bills) );
            CustomInnerAdapter customInnerAdapter = new CustomInnerAdapter(getApplicationContext(), innerBillList);
            ListView buckysListView = (ListView) findViewById(R.id.inner_history_list_view);
            buckysListView.setAdapter(customInnerAdapter);



        }
            /*public OuterBill[] getBills(){
                for (OuterBill o : bill) {
                    Log.i("getBills","in getBills "+o.getBill_id());
                }
                return bill;
            }*/

    }


}
