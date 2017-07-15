package com.example.admin.avoidq;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Admin on 26-09-2016.
 */
public class CartActivity extends Activity implements View.OnClickListener {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    LinearLayout relativeLayout;

    String content;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    CartAdapter contactAdapter;
    ListView listView;
    double tota;

    int i=0,m;
    Button bill;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    CartActivity(String scanContent){
        this.content = scanContent;
    }
    public CartActivity() {}
    String product,Price;
    double total=0;
    String prod,mfd_date,exp_date;
    double pri,discount,weight,tax;
    int quantity;
    double pri1,weight1;

    Intent intent;
    MyDBHandler dbHandler;
    MyDBHandler dbHandler1;
    String address;

    String email;
    String id;
    String mobile_no;


    TextView tot;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        relativeLayout=(LinearLayout)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);
        listView = (ListView) findViewById(R.id.cart_list_view);
      //  LayoutInflater inflater = (LayoutInflater) getApplicationContext()
      //          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  View view = inflater.inflate(R.layout.activity_cart, null);

        tot = (TextView) findViewById(R.id.total);

        intent = getIntent();
        address = intent.getStringExtra("a");
        email = intent.getStringExtra("emaill");
        id = intent.getStringExtra("customer_id");
        mobile_no = intent.getStringExtra("mobile_no");
        CartDBHandler cartDBHandler = new CartDBHandler(this,null,null,12);
        Cart[] cart= cartDBHandler.databaseToArray();
        if(cart != null ) {
            ArrayList<Cart> cartList = new ArrayList<Cart>();
            cartList.addAll(Arrays.asList(cart));

            CartAdapter buckysAdapter = new CartAdapter(getApplicationContext(), cartList);
            listView.setAdapter(buckysAdapter);


            final CartDBHandler crtdb = new CartDBHandler(this, null, null, 12);

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // update TextView here!

                                    Cart[] crt = crtdb.databaseToArray();
                                    for (Cart c : crt) {
                                        String na = c.getItemname();
                                        double pr = c.getPrice();
                                        double p = Double.valueOf(pr);
                                        total = total + p;
                                    }
                                    tot.setText("Total price Rs. " + String.valueOf(total));
                                    //view.invalidate();
                                    total = 0;
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            t.start();


            bill = (Button) findViewById(R.id.bill_button);
            bill.setOnClickListener(this);
        }
        else {
            Toast.makeText(getApplicationContext(),"Cart Empty",Toast.LENGTH_LONG).show();
        }

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
   public void onClick(View v) {
        if (v == bill) {
            dummy();
        }
    }

    Cart[] carts;
    //Cart[] cat;
    public void dummy() {
        String dbString;
        int dbint;
        double total_price,total_weight;
        int paid_flag;
        CartDBHandler cartDBHandler = new CartDBHandler(getApplicationContext(), null, null, 12);
        dbHandler1 = new MyDBHandler(getApplicationContext(), null, null, 12);
        //dbHandler1.drop();
        //Bill bill1 = new Bill();
        dbint = dbHandler1.databaseToString();

        dbint++;
        dbString = String.valueOf(dbint);
        Log.i("dbString", dbString);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        String formattedDate = df.format(calendar.getTime());
        formattedDate = id + dbString + formattedDate;
        dbString = formattedDate;
        carts = cartDBHandler.databaseToArray1();
        //cat = cartDBHandler.databaseToArray1();
        cartDBHandler.drop();
        double wei = 0, pr = 0;

        for (Cart c : carts) {
            pri1 = c.getPrice();
            weight1 = c.getWeight();
            Log.i("weight1","in cartactivity before sum weight1 "+String.valueOf(weight1));
            Log.i("pri1",String.valueOf(pri1));
            //Log.i("weight1",String.valueOf(weight1));
            wei = wei + weight1;
            Log.i("wei","in cartactivity after sum weight1 "+String.valueOf(wei));
            pr = pr + pri1;
            //Bill bill = new Bill(dbint,prod,pri);
            //dbHandler1.addBills(bill);
        }

        total_weight = wei;
        Log.i("total_weight","in cartactivity after loop total_weight "+String.valueOf(wei));
        total_price = pr;
        Log.i("total_price",String.valueOf(total_price));
        Log.i("total_weight"," in cart activity "+String.valueOf(total_weight));
        paid_flag = 0;
        //DecimalFormat df = new DecimalFormat("0.000");

        mainBill(id,dbString,total_price,total_weight,paid_flag);

        Cart[] c1;
        c1 = new Cart[carts.length];
        Log.i("length","length = "+String.valueOf(carts.length));
        c1[0] = carts[0];
        int j=0,count=1;
        double tp=0,tw=0;
        for(int i=1;i<carts.length;i++) {

            if((carts[i-1].getBarcode()).equals(carts[i].getBarcode()) ) {
                count++;
                double p = carts[i-1].getPrice();
                double w = carts[i-1].getWeight();
                tp = tp + count* p;
                tw = tw + count*w;
                c1[j].setQuantity(count);
                c1[j].setPrice(tp);
                c1[j].setWeight(tw);
                tp=0;
                tw=0;
                Log.i("count", String.valueOf(count));
            }
            else {

                Log.i("object",String.valueOf(c1[j].getQuantity()));
                j++;
                count=1;
                tp=0;
                tw=0;
                c1[j] = carts[i];
            }
        }
        c1 = Arrays.copyOf(c1,j+1);
        for(Cart cc : c1) {
            Log.i("cc","cc = "+cc.getItemname()+" "+cc.getWeight());
        }
        carts = null;
        carts = c1;

        for(Cart c : carts) {

             String prod1 = c.getItemname();
            prod = prod1.replaceAll(" ","@@");
            Log.i("prod","in cart = "+prod);
            pri = c.getPrice();
            quantity = c.getQuantity();
            weight = c.getWeight();
            discount = c.getDiscount();
            String barcode = c.getBarcode();
            tax = c.getTax();
            Bill bill = new Bill(dbint,prod,pri);
            dbHandler1.addBills(bill);
            generate(id,dbString,prod, pri,quantity,discount,email,mobile_no,String.valueOf(total_price),barcode);
        }

    }

    private void mainBill(String id,final String dbString, double total_price, double total_weight,int paid_flag) {
        String urlSuffix = "?id="+id+"&billid="+dbString+"&total_price="+total_price+"&total_weight="+total_weight+"&paid_flag="+paid_flag;

        class AsyncGenerateBill extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                //  super.onPreExecute();
                loading = ProgressDialog.show(CartActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                // super.onPostExecute(s);
                Log.i("s","value of "+s);
                loading.dismiss();
                /*
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(intent);
                if (s.equalsIgnoreCase("exception") ) {

                    Toast.makeText(CartActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"Bill Generated "+s, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(CartActivity.this,billGenerated.class));
                    Intent intnt = new Intent(CartActivity.this,billGenerated.class);
                    intnt.putExtra("finalbillid", dbString);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                    startActivity(intnt, bndlanimation);
                }
                CartActivity.this.finish();
                */
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                // Intent in = getIntent();
                // String address = in.getStringExtra("add");
                String REGISTER_URL = "http://"+address+"/avoidq/bill/mainBill.php";
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    Log.i("REGISTER_URL+s","REGISTER_URL+s  = "+REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    Log.i("con","con  = "+con);
                    con.setReadTimeout(READ_TIMEOUT);
                    con.setConnectTimeout(CONNECTION_TIMEOUT);
                    Log.i("getInputStream","getInputStream  = "+con.getInputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();
                    Log.i("result","result = "+result);

                    return result;
                }catch(Exception e){

                    return "exception";

                }

            }
        }


        AsyncGenerateBill ru = new AsyncGenerateBill();
        ru.execute(urlSuffix);
    }

    private void generate(final String id, final String dbString, String itemname, double pric, int quantity, double discount,
                          final String email, final String mobile_no, final String total_price, String barcode) {
        String urlSuffix = "?id="+id+"&billid="+dbString+"&barcode="+barcode+"&itemname="+itemname+"&price="+pric+"&quantity="+quantity+"&discount="+discount;

        class AsyncGenerateBill extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
              //  super.onPreExecute();
                loading = ProgressDialog.show(CartActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
               // super.onPostExecute(s);
                Log.i("s","value of "+s);
                loading.dismiss();
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(intent);
                if (s.equalsIgnoreCase("exception") ) {

                    Toast.makeText(CartActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"Bill Generated "+s, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(CartActivity.this,billGenerated.class));
                    Intent intnt = new Intent(CartActivity.this,billGenerated.class);
                    Log.i("finalbillid", dbString);
                    Log.i("cust_id", id);
                    Log.i("ipaddress", address);
                    Log.i("mobile_no", mobile_no);
                    Log.i("total_price", total_price);
                    intnt.putExtra("finalbillid", dbString);
                    intnt.putExtra("cust_id", id);
                    intnt.putExtra("ipaddress", address);
                    intnt.putExtra("mobile_no", mobile_no);
                    intnt.putExtra("email", email);
                    intnt.putExtra("total_price", total_price);
                    Bundle bundle = new Bundle();
                    ArrayList<Cart> cartLis = new ArrayList<Cart>();
                    cartLis.addAll(Arrays.asList(carts));
                    bundle.putParcelableArrayList("numbers", cartLis);
                    intnt.putExtras(bundle);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                    startActivity(intnt, bndlanimation);
                }
                CartActivity.this.finish();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
               // Intent in = getIntent();
               // String address = in.getStringExtra("add");
                String REGISTER_URL = "http://"+address+"/avoidq/bill/insert.php";
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    Log.i("REGISTER_URL+s","REGISTER_URL+s  = "+REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    Log.i("con","con  = "+con);
                    con.setReadTimeout(READ_TIMEOUT);
                    con.setConnectTimeout(CONNECTION_TIMEOUT);
                    Log.i("getInputStream","getInputStream  = "+con.getInputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();
                    Log.i("result","result = "+result);

                    return result;
                }catch(Exception e){

                    return "exception";

                }

            }
        }


        AsyncGenerateBill ru = new AsyncGenerateBill();
        ru.execute(urlSuffix);
    }


    }


