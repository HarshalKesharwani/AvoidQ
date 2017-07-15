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

import java.text.DecimalFormat;

public class CartDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "billI.db";
    private static final String TABLE_BILLS = "cart";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_DISCOUNT = "discount";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_TAX = "tax";
    private static final String COLUMN_BARCODE = "barcode";


    public CartDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    // public MyDBHandler(){}

    @Override
    public void onCreate(SQLiteDatabase db) {

        //String query = "CREATE TABLE `" + DATABASE_NAME + "`.`" + TABLE_BILLS + "` ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRICE + "` VARCHAR(50) NOT NULL , `" + COLUMN_PRODUCTNAME + "` VARCHAR(50) NOT NULL );";
        // String query=  "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INTEGER AUTO_INCREMENT , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL , `"+COLUMN_PRICE+"` VARCHAR(50) NOT NULL, PRIMARY KEY ("+COLUMN_ID+") );";
        String query = "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INTEGER PRIMARY KEY AUTOINCREMENT , `"
                        +COLUMN_PRICE+"` DOUBLE , `"
                        +COLUMN_QUANTITY+"` INTEGER , `"
                        +COLUMN_DISCOUNT+"` DOUBLE , `"
                        +COLUMN_WEIGHT+"` DOUBLE , `"
                        +COLUMN_BARCODE+"` VARCHAR(50) , `"
                        +COLUMN_TAX+"` DOUBLE , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL);";

        Log.i("cartdb", "query = " + query);

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public void addBills(Bill bill) {

        ContentValues values = new ContentValues();
        DecimalFormat df = new DecimalFormat("0.000");
        //values.put(COLUMN_ID, bill.get_id());
        values.put(COLUMN_PRICE, Double.valueOf(df.format(bill.get_price())));
        values.put(COLUMN_PRODUCTNAME, bill.get_productname());
        values.put(COLUMN_QUANTITY, bill.getQuantity());
        values.put(COLUMN_DISCOUNT, Double.valueOf(df.format(bill.getDiscount())));
        values.put(COLUMN_TAX, Double.valueOf(df.format(bill.getTax())));
        Log.i("weight","in cartdbhandler w = "+df.format(bill.getWeight()));
        values.put(COLUMN_WEIGHT, Double.valueOf(df.format(bill.getWeight())));
        values.put(COLUMN_BARCODE, bill.getBarcode());


        SQLiteDatabase db = getWritableDatabase();
        long q = db.insert(TABLE_BILLS, null, values);
        Log.i("kykmjhn1", "query = " + q);
        db.close();
    }

    public int count() {

        int dbString = -1;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT count("+COLUMN_ID+") FROM " + TABLE_BILLS +";";
        Cursor c = db.rawQuery(query, null);
        //if ((c.isBeforeFirst() != true))
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            dbString = c.getInt(0);
            //dbString = c.getInt(c.getColumnIndex("_id"));
        } else
            dbString = 0;
        db.close();
        return dbString;
    }

    public Cart[] databaseToArray() {
        //int position = pos;
        Cursor crs = null;
        Cart[] array1 = null;
        SQLiteDatabase db = getWritableDatabase();
        try {
            crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS + "`;", null);
        } catch (Exception e) {
            onCreate(db);
            db.close();
        }
        if (crs != null) {


        Cart[] array = new Cart[crs.getCount()];
        int i = 0;
        while (crs.moveToNext()) {
            String name = crs.getString(crs.getColumnIndex(COLUMN_PRODUCTNAME));
            String barcode = crs.getString(crs.getColumnIndex(COLUMN_BARCODE));
            double price = crs.getDouble(crs.getColumnIndex(COLUMN_PRICE));
            double weight = crs.getDouble(crs.getColumnIndex(COLUMN_WEIGHT));

            int quantity = crs.getInt(crs.getColumnIndex(COLUMN_QUANTITY));
            double discount = crs.getDouble(crs.getColumnIndex(COLUMN_DISCOUNT));
            double tax = crs.getDouble(crs.getColumnIndex(COLUMN_TAX));
            // String id = crs.getString(crs.getColumnIndex(COLUMN_ID));
            Cart cart = new Cart();
            cart.setItemname(name);
            cart.setPrice(price);
            cart.setWeight(weight);
            cart.setQuantity(quantity);
            cart.setDiscount(discount);
            cart.setTax(tax);
            cart.setBarcode(barcode);
            array[i] = cart;
            Log.i("jhnbvb", "in dbtoarray item = " + cart.getItemname() + " price = " + cart.getPrice()+ " weight = " + cart.getWeight());
            i++;
            //System.out.println(uname);
        }
        db.close();
        return array;
    }

        return array1;
    }



    public Cart[] databaseToArray1() {
        //int position = pos;
        Cursor crs = null;
        Cart[] array1 = null;
        SQLiteDatabase db = getWritableDatabase();
        try {
            crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS + "` order by "+COLUMN_PRODUCTNAME+";", null);
        } catch (Exception e) {
            onCreate(db);
            db.close();
        }
        if (crs != null) {


            Cart[] array = new Cart[crs.getCount()];
            int i = 0;
            while (crs.moveToNext()) {
                String name = crs.getString(crs.getColumnIndex(COLUMN_PRODUCTNAME));
                String barcode = crs.getString(crs.getColumnIndex(COLUMN_BARCODE));
                double price = crs.getDouble(crs.getColumnIndex(COLUMN_PRICE));
                double weight = crs.getDouble(crs.getColumnIndex(COLUMN_WEIGHT));
                int quantity = crs.getInt(crs.getColumnIndex(COLUMN_QUANTITY));
                double discount = crs.getDouble(crs.getColumnIndex(COLUMN_DISCOUNT));
                double tax = crs.getDouble(crs.getColumnIndex(COLUMN_TAX));
                // String id = crs.getString(crs.getColumnIndex(COLUMN_ID));
                Cart cart = new Cart();
                cart.setItemname(name);
                cart.setPrice(price);
                cart.setWeight(weight);
                cart.setQuantity(quantity);
                cart.setDiscount(discount);
                cart.setTax(tax);
                cart.setBarcode(barcode);
                array[i] = cart;
                Log.i("jhnbvb", " in dbtoarray1 item = " + cart.getItemname() + " price = " + cart.getPrice()+ " weight = " + String.valueOf(cart.getWeight()));
                i++;
                //System.out.println(uname);
            }
            db.close();
            return array;
        }

        return array1;
    }

    public void remove(int pos) {

        int position = pos+1;
        Log.i("posi","position = "+String.valueOf(position));
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM `" + TABLE_BILLS + "` WHERE `" + COLUMN_ID + "` = " + position + ";");

        Cursor crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS+"`;", null);
        int i = 0;
        while (crs.moveToNext()) {
            i++;
            if(i == position) {
                int ids = crs.getInt(crs.getColumnIndex(COLUMN_ID));
                int j = db.delete(TABLE_BILLS, COLUMN_ID+ "="+String.valueOf(ids),null);
                Log.i("posa","posa = "+j);
                break;
            }

        }
        db.close();
    }


    public void drop() {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BILLS,null,null);
        db.close();
    }
    public void updateQuantity(int quantity,int pos) {

        int position = pos+1;
        Log.i("posi","position = "+String.valueOf(position));
        SQLiteDatabase db = getWritableDatabase();

        Cursor crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS+"`;", null);
        int i = 0;
        while (crs.moveToNext()) {
            i++;
            if(i == position) {
                int ids = crs.getInt(crs.getColumnIndex(COLUMN_ID));
                String quer = "UPDATE `"+TABLE_BILLS+"` SET `"+COLUMN_QUANTITY+"`="+quantity+" WHERE `"+COLUMN_ID+"` = "+ids;
                db.execSQL(quer);
                break;
            }

        }
        db.close();

    }


}
