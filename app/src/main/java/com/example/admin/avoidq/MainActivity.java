package com.example.admin.avoidq;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    RelativeLayout relativeLayout;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout=(RelativeLayout)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);
        TextView textView1 = (TextView)findViewById(R.id.textView3);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("location");
        textView1.setText(str);
        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        TextView registerclick = (TextView) findViewById(R.id.registerlink);
        registerclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this,register.class);
                intent.putExtra("add",str);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);
                //startActivity(intent);
                MainActivity.this.finish();
            }
        });
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

    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {

        // Get text from email and passord field
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        ////////////////recent changes start////////////////

        etEmail.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        //String email = etEmail.getText().toString();
        //String password = mPasswordView.getText().toString();

        boolean cancel1 = false;
        boolean cancel2 = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel2 = true;
        }
        else if(TextUtils.isEmpty(password)){
            etPassword.setError(getString(R.string.error_field_required));
            focusView = etPassword;
            cancel2 = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel1 = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel1 = true;
        }


        if ((cancel1 == false) && (cancel2 == false)) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);

            // Initialize  AsyncLogin() class with email and password
            new AsyncLogin().execute(email,password);

        } else  {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        ////////////////recent changes end////////////////



    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String address;
        String JSON_STRING;
        String json_string;
        JSONObject jsonObject;
        JSONArray jsonArray;
        String content;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {


                Intent intent = getIntent();
                String str = intent.getStringExtra("location");

             //  String address  = "192.168.0.104";
                address = str;
                // Enter URL address where your php file resides
                url = new URL("http://"+address+"/avoidq/login/login.inc.php");
                Log.i("url",String.valueOf(url));

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();
                Log.i("query",query);

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                ////changes///////////
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                return stringBuilder.toString().trim();
                //////done///////////

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            /*

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            */finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {


            // textView.setText(result);
            json_string = result;
            pdLoading.dismiss();

            // listView = (ListView) findViewById(R.id.cart_list_view);
            //json_string = getIntent().getExtras().getString("json_data");

            //contactAdapter = new CartAdapter(this, R.layout.activity_cart_list_view);
            //listView.setAdapter(contactAdapter);
            //json_string = getIntent().getExtras().getString("json_data");
            try {
                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("customer");
                //if (jsonArray != null && jsonArray.length() > 0) {
                if(jsonArray.isNull(0)) {
                    Toast.makeText(getApplicationContext(),"Login Failed! Please try again!",Toast.LENGTH_LONG).show();
                    return;
                }

                int count = 0;

                String id,name,surname,email,password,dob,mobile_no,adrs;

                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    id = JO.getString("customer_id");
                    name = JO.getString("name");
                    surname = JO.getString("surname");
                    email = JO.getString("email");
                    password = JO.getString("password");
                    dob = JO.getString("dob");
                    mobile_no = JO.getString("mobile_no");
                    adrs = JO.getString("address");

                    if ((id == "null") || (id == null)) {

                        Toast.makeText(getApplicationContext(),"Please enter details correctly!!",Toast.LENGTH_LONG).show();
                        return;

                    } else {

                        Customer customer = new Customer();
                        customer.setAddress(adrs);
                        customer.setDob(dob);
                        customer.setEmail(email);
                        customer.setId(id);
                        customer.setMobile_no(mobile_no);
                        customer.setName(name);
                        customer.setSurname(surname);
                        customer.setPassword(password);
                        CustomerDetailsHandler handler = new CustomerDetailsHandler(getApplicationContext(),null,null,12);
                        handler.addCustomer(customer);

                        Toast.makeText(getApplicationContext(), " Login successfull", Toast.LENGTH_LONG).show();

                        count++;
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, welcome_page.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("email", etEmail.getText().toString());
                        intent.putExtra("addre",address );
                        intent.putExtra("id",id);
                        intent.putExtra("mobile_no",mobile_no);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                        context.startActivity(intent, bndlanimation);
                        //context.startActivity(intent);
                         /*Intent intent = new Intent(MainActivity.this,welcome_page.class);
                intent.putExtra("email", etEmail.getText().toString());
                startActivity(intent);*/
                MainActivity.this.finish();


                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
/*

            //this method will be running on UI thread

            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {
                */ /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
            /*
                Context context = getApplicationContext();
                Intent intent = new Intent(context, welcome_page.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("email", etEmail.getText().toString());
                intent.putExtra("addre",address );
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                context.startActivity(intent, bndlanimation);
                //context.startActivity(intent);
                */ /*Intent intent = new Intent(MainActivity.this,welcome_page.class);
                intent.putExtra("email", etEmail.getText().toString());
                startActivity(intent);*/ /*
                MainActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
            else {

                Toast.makeText(MainActivity.this, "Something happened", Toast.LENGTH_LONG).show();
                Log.i("result",result);
            }
            */
        }

    }
}