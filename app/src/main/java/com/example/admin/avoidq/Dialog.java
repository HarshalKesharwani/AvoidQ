package com.example.admin.avoidq;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class Dialog extends DialogFragment  {

    Button cancle ,ok;
    TextView Titemname,Tprice,Tdiscount,Ttotprice;
    Spinner spinner;
    String message;

    String product,mfd_date,exp_date,barcode;
    double Price=0,weight=0,discount,tax;
    int quantity;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String json_string;
    String json_url;
    String ipaddress;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final int CONNECTION_TIMEOUT = 10000;
         final int READ_TIMEOUT = 15000;
       // private RecyclerView mRVFishPrice;
        // AdapterFish mAdapter;

        getDialog().setTitle("Product Details");

        View view =inflater.inflate(R.layout.dialog,null);
        cancle = (Button) view.findViewById(R.id.button_cancle);
        ok = (Button) view.findViewById(R.id.button_ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClicked(v);
            }
        });

         Titemname = (TextView)view.findViewById(R.id.itemname);
         Tprice = (TextView)view.findViewById(R.id.originalprice);
         Tdiscount = (TextView)view.findViewById(R.id.discount);
         Ttotprice = (TextView)view.findViewById(R.id.totalprice);
/*
       spinner = (Spinner)view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
            {
                String item = parent.getItemAtPosition(position).toString();


                //quantity is in variable "item".

                // Showing selected spinner item
               // Toast.makeText(parent.getContext(), "Quantity "+item+" Selected", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");
        categories.add("13");
        categories.add("14");
        categories.add("15");
        categories.add("16");
        categories.add("17");
        categories.add("18");
        categories.add("19");
        categories.add("20");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
*/

        //setCancelable(false);
        new BackgroundTask(getActivity()).execute(message,ipaddress);
        return view ;

    }


    public void getProduct(Bill bill) {


    }
    public void okClicked(View view){
        CartDBHandler cartDBHandler = new CartDBHandler(getActivity(), null, null, 12);
        Bill bill = new Bill();
        DecimalFormat df = new DecimalFormat("0.000");
        if((product == null) || (product == "null")) {

        }
        else {
            double d = discount;
            double d1 = d / 100;
            double d2 = 1 - d1;

            double tot = (double) (Price * d2);
            bill.set_price(Double.valueOf(df.format(tot)));
            bill.set_productname(product);
            bill.setQuantity(1);
            bill.setDiscount(Double.valueOf(df.format(discount)));
            bill.setWeight(Double.valueOf(df.format(weight)));
            bill.setTax(Double.valueOf(df.format(tax)));
            bill.setBarcode(barcode);


            Log.i("bill", "bill = " + bill.get_productname() + "  " + bill.get_price() + "  " + bill.getWeight());
            cartDBHandler.addBills(bill);
        }
        dismiss();

    }

/*
    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.button_ok)
        {
            Toast.makeText(getActivity(),"Item added to cart successfully",Toast.LENGTH_LONG).show();
        }
        else {

            dismiss();
            Toast.makeText(getActivity(),"Not added in cart",Toast.LENGTH_LONG).show();
        }

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

        public BackgroundTask(Context applicationContext) {

        }


        @Override
        protected void onPreExecute() {
            json_url = "http://"+ipaddress+"/avoidq/barcode/barcode.php";
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


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("barcode", voids[0]);
                String query = builder.build().getEncodedQuery();
                Log.i("query","in dialog "+query);


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
                jsonArray = jsonObject.getJSONArray("server_response");
                //if (jsonArray != null && jsonArray.length() > 0) {

                int count = 0;



                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    product = JO.getString("item_name");
                    String price1 = JO.getString("price");

                    if(price1.equals(null) || price1.equals("null")) {
                        Toast.makeText(getActivity(),"Product not found! Please scan the correct product!!",Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        Price = Double.parseDouble(price1);
                    }
                    quantity = Integer.parseInt(JO.getString("quantity"));
                    discount = Double.parseDouble(JO.getString("discount"));
                    String wt = JO.getString("weight");
                    if(wt.equals(null) || wt.equals("null")) {
                        Toast.makeText(getActivity(),"Product not found! Please scan the correct product!!",Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        weight = Double.parseDouble(wt);
                        Log.i("weight","in dialog w = "+String.valueOf(weight));
                    }
                    tax = Double.parseDouble(JO.getString("tax"));
                    mfd_date = JO.getString("mfd_date");
                    exp_date = JO.getString("exp_date");
                    barcode = JO.getString("barcode");
                    if ((product == "null") || (product == null)) {

                        Toast.makeText(getActivity(),"Please scan the correct product!!",Toast.LENGTH_LONG).show();
                        return;
                    } else {

                            /*Product product1 = new Product();
                            product1.setProduct(product);
                            product1.setPrice(Price);
                            product1.setQuantity(quantity);
                            product1.setDiscount(discount);
                            product1.setWeight(weight);
                            product1.setTax(tax);
                            product1.setMfd_date(mfd_date);
                            product1.setExp_date(exp_date);*/
                        //Toast.makeText(getActivity(), "" + product + " added successfully", Toast.LENGTH_LONG).show();

                        //i++;
                        //contactAdapter.add(contacts);
                        count++;

                        Log.i("product"," in dialog "+product+" "+String.valueOf(Price));

                        Titemname.setText(product);
                        Tprice.setText(String.valueOf(Price));

                        Tdiscount.setText(String.valueOf(discount)+" % OFF");
                        double d = discount ;
                        double d1 = d / 100;
                        double d2 = 1 - d1;

                        double tot =  (Price *d2);
                        DecimalFormat df = new DecimalFormat("0.000");
                        Log.i("tot1","tot1 = "+String.valueOf(d));
                        Log.i("tot2","tot2 = "+String.valueOf(d1));
                        //Log.i("tot3","tot3 = "+String.valueOf(tot));
                        Log.i("tot","tot = "+String.valueOf(tot));
                        Ttotprice.setText(String.valueOf( df.format(tot)));


                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
