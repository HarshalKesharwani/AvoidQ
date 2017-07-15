
package com.example.admin.avoidq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BillHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 18;
    private static final String DATABASE_NAME = "eachBill.db";
    private static final String TABLE_BILLS = "eachBill";
    private static final String COLUMN_PRODUCT_NAME = "productname";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";

    public BillHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    // public MyDBHandler(){}

    @Override
    public void onCreate(SQLiteDatabase db) {

        //String query = "CREATE TABLE `" + DATABASE_NAME + "`.`" + TABLE_BILLS + "` ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRICE + "` VARCHAR(50) NOT NULL , `" + COLUMN_PRODUCTNAME + "` VARCHAR(50) NOT NULL );";
        // String query=  "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INTEGER AUTO_INCREMENT , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL , `"+COLUMN_PRICE+"` VARCHAR(50) NOT NULL, PRIMARY KEY ("+COLUMN_ID+") );";
        String query = "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_PRODUCT_NAME+"` VARCHAR(50) , `"
                +COLUMN_PRICE+"` INTEGER , `"
                +COLUMN_QUANTITY+"` INTEGER"
                +");";

        Log.i("cartdb", "query = " + query);

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public void addBills(InnerBill bill) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, bill.getProduct_name());
        values.put(COLUMN_PRICE, bill.getPrice());
        values.put(COLUMN_QUANTITY, bill.getQuantity());
        SQLiteDatabase db = getWritableDatabase();
        long q = db.insert(TABLE_BILLS, null, values);
        Log.i("kykmjhn1", "query = " + q);
        db.close();
    }

    public InnerBill[] databaseToArray() {
        //int position = pos;
        Cursor crs = null;
        InnerBill[] array1 = null;
        SQLiteDatabase db = getWritableDatabase();
        try {
            crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS + "`;", null);
        } catch (Exception e) {
            onCreate(db);
            db.close();
        }
        if (crs != null) {


            InnerBill[] array = new InnerBill[crs.getCount()];
            int i = 0;
            while (crs.moveToNext()) {
                String name = crs.getString(crs.getColumnIndex(COLUMN_PRODUCT_NAME));
                int price = crs.getInt(crs.getColumnIndex(COLUMN_PRICE));
                int weight = crs.getInt(crs.getColumnIndex(COLUMN_QUANTITY));
                // String id = crs.getString(crs.getColumnIndex(COLUMN_ID));
                InnerBill cart = new InnerBill();
                cart.setProduct_name(name);
                cart.setPrice(price);
                cart.setQuantity(weight);
                array[i] = cart;
                // Log.i("jhnbvb", "item = " + cart.getItemname() + " price = " + cart.getPrice()+ " weight = " + cart.getWeight());
                i++;
                //System.out.println(uname);
            }
            drop();
            db.close();
            return array;
        }

        return array1;
    }

    public void drop() {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BILLS,null,null);
        db.close();
    }


}
