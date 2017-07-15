package com.example.admin.avoidq;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Random;
        import java.util.Timer;
        import java.util.TimerTask;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.EditText;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.Toast;

        import com.paytm.pgsdk.PaytmClientCertificate;
        import com.paytm.pgsdk.PaytmMerchant;
        import com.paytm.pgsdk.PaytmOrder;
        import com.paytm.pgsdk.PaytmPGService;
        import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MerchantActivity extends Activity {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    ScrollView relativeLayout;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    EditText order_id,customer_id,cust_email_id,cust_mobile_no,transaction_amount;

    String billid;
    String ipaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantapp);
        relativeLayout=(ScrollView)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);
       // initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Intent intent = getIntent();
        billid = intent.getStringExtra("finalbillid");
        final String cust_id = intent.getStringExtra("cust_id");
        final String mobile_no = intent.getStringExtra("mobile_no");
        final String email = intent.getStringExtra("email");
        ipaddress = intent.getStringExtra("ipaddress");
        final String total_price = intent.getStringExtra("total_price");
        Log.i("mobile_no",mobile_no);

          order_id = (EditText) findViewById(R.id.order_id);
          customer_id =(EditText) findViewById(R.id.customer_id);
          cust_email_id =(EditText) findViewById(R.id.cust_email_id);
          cust_mobile_no =(EditText) findViewById(R.id.cust_mobile_no);
          transaction_amount =(EditText) findViewById(R.id.transaction_amount);

        order_id.setText(billid);
        customer_id.setText(cust_id);
        cust_email_id.setText(email);
        cust_mobile_no.setText(mobile_no);
        transaction_amount.setText(total_price);

/*
        EditText order_id =(EditText) findViewById(R.id.channel_id);
        EditText order_id =(EditText) findViewById(R.id.industry_type_id);
        EditText order_id =(EditText) findViewById(R.id.website);
        EditText order_id =(EditText) findViewById(R.id.theme);
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

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart(){
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void initOrderId() {
        /*Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
        orderIdEditText.setText(orderId);
        */
    }

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", ((EditText) findViewById(R.id.order_id)).getText().toString());
        paramMap.put("MID", ((EditText) findViewById(R.id.merchant_id)).getText().toString());
        paramMap.put("CUST_ID", ((EditText) findViewById(R.id.customer_id)).getText().toString());
        paramMap.put("CHANNEL_ID", ((EditText) findViewById(R.id.channel_id)).getText().toString());
        paramMap.put("INDUSTRY_TYPE_ID", ((EditText) findViewById(R.id.industry_type_id)).getText().toString());
        paramMap.put("WEBSITE", ((EditText) findViewById(R.id.website)).getText().toString());
        paramMap.put("TXN_AMOUNT", ((EditText) findViewById(R.id.transaction_amount)).getText().toString());
        paramMap.put("THEME", ((EditText) findViewById(R.id.theme)).getText().toString());
        paramMap.put("EMAIL", ((EditText) findViewById(R.id.cust_email_id)).getText().toString());
        paramMap.put("MOBILE_NO", ((EditText) findViewById(R.id.cust_mobile_no)).getText().toString());
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                        Toast.makeText(getApplicationContext(),"someUIErrorOccurred",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        mainBill(billid,ipaddress);

                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.

                        Toast.makeText(getApplicationContext(),"networkNotAvailable",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                        Toast.makeText(getApplicationContext(),"clientAuthenticationFailed",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Toast.makeText(getApplicationContext(),"onErrorLoadingWebPage",Toast.LENGTH_LONG).show();
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),"onBackPressedCancelTransaction",Toast.LENGTH_LONG).show();
                    }

                });
    }

    private void mainBill(String billid, final String ipaddress) {
        String urlSuffix = "?billid="+billid;

        class AsyncGenerateBill extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                //  super.onPreExecute();
                loading = ProgressDialog.show(MerchantActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                // super.onPostExecute(s);
                Log.i("s","value of "+s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                // Intent in = getIntent();
                // String address = in.getStringExtra("add");
                String REGISTER_URL = "http://"+ipaddress+"/avoidq/bill/payment.php";
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
