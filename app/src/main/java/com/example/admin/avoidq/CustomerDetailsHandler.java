package com.example.admin.avoidq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CustomerDetailsHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 18;
    private static final String DATABASE_NAME = "customer.db";
    private static final String TABLE_BILLS = "customer";
    private static final String COLUMN_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_FIRST_NAME= "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL_ID= "email_id";
    private static final String COLUMN_MOBILE_NO= "mobile_no";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_DOB = "dob";


    public CustomerDetailsHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    // public MyDBHandler(){}

    @Override
    public void onCreate(SQLiteDatabase db) {

        //String query = "CREATE TABLE `" + DATABASE_NAME + "`.`" + TABLE_BILLS + "` ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRICE + "` VARCHAR(50) NOT NULL , `" + COLUMN_PRODUCTNAME + "` VARCHAR(50) NOT NULL );";
        // String query=  "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INTEGER AUTO_INCREMENT , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL , `"+COLUMN_PRICE+"` VARCHAR(50) NOT NULL, PRIMARY KEY ("+COLUMN_ID+") );";
        String query = "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_CUSTOMER_ID+"` INTEGER  , `"
                +COLUMN_FIRST_NAME+"` VARCHAR(50) , `"
                +COLUMN_LAST_NAME+"` VARCHAR(50) , `"
                +COLUMN_MOBILE_NO+"` VARCHAR(50) , `"
                +COLUMN_ADDRESS+"` VARCHAR(50) , `"
                +COLUMN_EMAIL_ID+"` VARCHAR(50) , `"
                +COLUMN_DOB+"` VARCHAR(50) , `"+COLUMN_PASSWORD+"` VARCHAR(50) NOT NULL);";

        Log.i("cartdb", "query = " + query);

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public void addCustomer(Customer customer) {

        ContentValues values = new ContentValues();
        //values.put(COLUMN_ID, bill.get_id());
        values.put(COLUMN_CUSTOMER_ID, customer.getId());
        values.put(COLUMN_FIRST_NAME, customer.getName());
        values.put(COLUMN_LAST_NAME, customer.getSurname());
        values.put(COLUMN_EMAIL_ID, customer.getEmail());
        values.put(COLUMN_PASSWORD, customer.getPassword());
        values.put(COLUMN_MOBILE_NO, customer.getMobile_no());
        values.put(COLUMN_DOB, customer.getDob());
        values.put(COLUMN_ADDRESS, customer.getAddress());


        SQLiteDatabase db = getWritableDatabase();
        long q = db.insert(TABLE_BILLS, null, values);
        Log.i("kykmjhn1", "query = " + q);
        db.close();
    }

    public Customer databaseToCustomer() {
        //int position = pos;
        Cursor crs = null;
        Customer customer = new Customer();
        SQLiteDatabase db = getWritableDatabase();
        try {
            crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS + "`;", null);

        } catch (Exception e) {
            onCreate(db);
            db.close();
        }
        if (crs != null && crs.getCount() > 0) {
            crs.moveToFirst();
            String fname = crs.getString(crs.getColumnIndex(COLUMN_FIRST_NAME));
            String lname = crs.getString(crs.getColumnIndex(COLUMN_LAST_NAME));
            String email = crs.getString(crs.getColumnIndex(COLUMN_EMAIL_ID));
            String password = crs.getString(crs.getColumnIndex(COLUMN_PASSWORD));
            String id = crs.getString(crs.getColumnIndex(COLUMN_CUSTOMER_ID));
            String address = crs.getString(crs.getColumnIndex(COLUMN_ADDRESS));
            String dob = crs.getString(crs.getColumnIndex(COLUMN_DOB));
            String mobile_no = crs.getString(crs.getColumnIndex(COLUMN_MOBILE_NO));
            customer.setId(id);          customer.setName(fname);        customer.setSurname(lname);
            customer.setEmail(email);    customer.setPassword(password); customer.setAddress(address);
            customer.setDob(dob);        customer.setMobile_no(mobile_no);
        }
        db.close();
            return customer;
        }


    public void drop() {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BILLS,null,null);
        db.close();
    }


}
