package com.example.admin.avoidq;

//import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.view.View.OnClickListener;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class welcome_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    RelativeLayout relativeLayout;
    private Menu menu;
    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String json_string;
    String json_url;
    String productname[]=new String[10];
    String price[]=new String[10];
    int x=0,i=0;

    String addres;
    String emaill;
    String customer_id;
    String mobile_no;
    public static int r[]=new int[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        relativeLayout=(RelativeLayout) findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);


        TextView textViewWelcome = (TextView)findViewById(R.id.textViewWelcome);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textViewWelcome.startAnimation(anim);
        ////////////////////////////////////////////////////////////////////
        scanBtn = (Button) findViewById(R.id.scan_item_button);

        scanBtn.setOnClickListener(this);
        ////////////////////////////////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //mTitle.setText("Avoid Q");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setImageResource(R.drawable.your_order);
        fab.setBackgroundColor(Color.RED);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        Intent i = getIntent();
        emaill = i.getStringExtra("email");
        addres = i.getStringExtra("addre");
        customer_id = i.getStringExtra("id");
        mobile_no = i.getStringExtra("mobile_no");
        Log.i("y","email = "+emaill);
        TextView textView = (TextView)header.findViewById(R.id.user);
        textView.setText(emaill);
        for(int z=0;z<10;z++) {
            Log.i("arrayr", String.valueOf(r[z]));
        }
   /*     CartDBHandler cartDBHandler = new CartDBHandler(getApplicationContext(),null,null,10);
        int num = cartDBHandler.num();
        TextView textView1 = (TextView)findViewById(R.id.numbers);
        textView1.setText(String.valueOf(num));
*/
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


    public  void cartFloating(View view) {
        int nonnull=0;

            Context context = getApplicationContext();
            Intent intent = new Intent(context, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("a", addres);
            intent.putExtra("emaill", emaill);
            intent.putExtra("customer_id",customer_id);
            intent.putExtra("mobile_no",mobile_no);
            Bundle bndlanimation =
                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
            context.startActivity(intent, bndlanimation);

        }

    public void onClick(View v) {
        if (v.getId() == R.id.scan_item_button) {
           // IntentIntegrator scanIntegrator = new IntentIntegrator(this);
           // scanIntegrator.initiateScan();
            Intent intent = new Intent(v.getContext(), BarcodeScanner.class);
            intent.putExtra("address",addres);
            startActivity(intent);

        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            CartActivity ca = new CartActivity(scanContent);
            if(scanContent == null || scanContent == "null")
            { Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {}

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.welcome_page, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.your_account));
        menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.your_order));
        menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.log_out));

        //noinspection SimplifiableIfStatement
        ////////////////////////////////////////////////


        ////////////////////////////////////////////////////

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_your_order) {

                Context context = getApplicationContext();
                Intent intent = new Intent(context, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ad", addres);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);

            }
       else if (id == R.id.nav_offer_zone) {
           Context context = getApplicationContext();
           Intent intent = new Intent(context, Offers.class);
           //intent.putExtra("customer_id", Integer.parseInt(customer_id));
           intent.putExtra("ipaddress",addres );
           Bundle bndlanimation =
                   ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
           startActivity(intent, bndlanimation);

       }
       else if (id == R.id.nav_order_history) {
           Context context = getApplicationContext();
           Intent intent = new Intent(context, OuterHistory.class);
           intent.putExtra("customer_id", Integer.parseInt(customer_id));
           intent.putExtra("address",addres );
           Bundle bndlanimation =
                   ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
           startActivity(intent, bndlanimation);

       } else if (id == R.id.nav_logout) {
           Context context = getApplicationContext();
           CustomerDetailsHandler handler = new CustomerDetailsHandler(context,null,null,12);
           handler.drop();
           Intent intent = new Intent(context, start.class);
           Bundle bndlanimation =
                   ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
           startActivity(intent, bndlanimation);

        } else if (id == R.id.nav_share) {
            ///////on click on share text
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "If want to avoid queueing in lines download \"Avoid Q \".");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_about_us) {
           Context context = getApplicationContext();
           Intent intent = new Intent(context, AboutUsActivity.class);
           Bundle bndlanimation =
                   ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
           startActivity(intent, bndlanimation);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


}

