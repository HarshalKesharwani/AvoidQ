package com.example.admin.avoidq;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class register extends ActionBarActivity implements View.OnClickListener{
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    LinearLayout relativeLayout;
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextcPassword;
    private EditText editTextAddress;
    private EditText editTextMobile;
    private static String dob;
    static Button dob_button;


    private Button buttonRegister;
    String addr;
  //  private static final String REGISTER_URL = "http://192.168.0.105/insert/register.php";

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        relativeLayout=(LinearLayout)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);
        TextView txt = (TextView) findViewById(R.id.AddReg);
        Intent in = getIntent();
        addr = in.getStringExtra("add");
        txt.setText(in.getStringExtra("add"));
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextcPassword = (EditText)findViewById(R.id.editTextcPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAddress= (EditText)findViewById(R.id.editTextAddress) ;
        editTextMobile = (EditText)findViewById(R.id.editTextMobile);
        dob_button = (Button)findViewById(R.id.dob);
        dob_button.setText("DOB");



        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
    }

    private boolean registerUser() {
        String name = editTextName.getText().toString().trim().toLowerCase();
        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String cpassword = editTextcPassword.getText().toString().trim().toLowerCase();
        String mobile = editTextMobile.getText().toString().trim().toLowerCase();
        String address = editTextAddress.getText().toString().trim().toLowerCase();

        String email = editTextEmail.getText().toString().trim().toLowerCase();
        //////////////////recent changes start/////////////
        if(TextUtils.isEmpty(address) == false) {

            if(address.contains(" ")) {
                String address1 = address;
               address =  address1.replaceAll(" ","_");
                Log.i("fghh",address);
            }
        }

        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextcPassword.setError(null);
        editTextName.setError(null);
        editTextUsername.setError(null);
        editTextAddress.setError(null);
        editTextMobile.setError(null);

        // Store values at the time of the login attempt.
        //String email = mEmailView.getText().toString();
        ////String password = mPasswordView.getText().toString();

        //pritesh change..............//////////////////////////////////////////////////
       // String confermPass = cPasswordView.getText().toString();
        //String fullname = mFullName.getText().toString();
        //String mobileNo = mMobno.getText().toString();

        boolean cancel1 = false;
        boolean cancel2 = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel2 = true;
        }
        else if(TextUtils.isEmpty(password)){
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel2 = true;
        }
        //Check for valid mobile



        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel1 = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel1 = true;
        }

        //pritesh change for password and confirm password,...........//////////////////////
        if (!checkPassWordAndConfirmPassword(password,cpassword))
        {
            editTextcPassword.setError(getString(R.string.passwords_do_not_match));
            focusView = editTextcPassword;
            cancel2 = true;
        }
        //pritesh change for checking your full name...........///////////////////////
        if(TextUtils.isEmpty(name)){
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel2 = true;
        }
        if(TextUtils.isEmpty(username)){
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername;
            cancel2 = true;
        }
        if(TextUtils.isEmpty(address)){
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername;
            cancel2 = true;
        }

        //checking 10 digit valid mobile no..................../////////////////
        if (!isMobilenoValid(mobile))
        {
            editTextMobile.setError(getString(R.string.enter_10_digit_mob_no));
            focusView = editTextMobile;
            cancel2 = true;
        }
        else if(TextUtils.isEmpty(mobile)){
            editTextMobile.setError(getString(R.string.error_field_required));
            focusView = editTextMobile;
            cancel2 = true;
        }


        if ((cancel1 == false) && (cancel2 == false)) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            // mAuthTask = new UserRegisterTask(email, password);
            // mAuthTask.execute((Void) null);
            register(name, username, password, email,mobile,address,dob);
            return true;


        } else  {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }
        /////////////////recent changes end/////////////////

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
           DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;
             dob= String.valueOf(year) +"-"+String.valueOf(month)+"-"+String.valueOf(day);
            dob_button.setText(dob);
            Log.i("ggg1",String.valueOf(year));
            Log.i("ggg2",String.valueOf(month));
            Log.i("ggg3",String.valueOf(day));

            Log.i("ggg",dob);
        }

    }


    //pritesh change......../////////////////////////////
    public boolean checkPassWordAndConfirmPassword(String password,String confermPass)
    {
        boolean pstatus = false;
        if (confermPass != null && password != null)
        {
            if (password.equals(confermPass))
            {
                pstatus = true;
            }
        }
        return pstatus;
    }
   /* private boolean isMobilenoValid(String mobileNo)
    {
        return mobileNo.length() == 10;
    }*/

    //pritesh change end here................////////////////////////////

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isMobilenoValid(String mobile) {

        return mobile.length() ==10;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void register(String name, String username, String password, String email,String mobile,String address,String dob) {
        String urlSuffix = "?name="+name+"&surname="+username+"&password="+password+"&email="+email+"&mobile_no="+mobile+"&address="+address+"&dob="+dob;
        Log.i("asddg",urlSuffix);
        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("location",addr);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);
                //startActivity(intent);
                if (s.equalsIgnoreCase("exception") ) {

                    Toast.makeText(register.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
                register.this.finish();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                Intent in = getIntent();
                String address = in.getStringExtra("add");
                String REGISTER_URL = "http://"+address+"/avoidq/register/register.php";
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(READ_TIMEOUT);
                    con.setConnectTimeout(CONNECTION_TIMEOUT);
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){

                    return "exception";
                }
            }
        }


        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
}
