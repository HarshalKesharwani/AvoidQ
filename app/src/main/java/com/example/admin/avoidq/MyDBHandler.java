package com.example.admin.avoidq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Admin on 28-10-2016.
 */
public class MyDBHandler extends SQLiteOpenHelper  {

    private static  final int DATABASE_VERSION = 20;
    private static  final String DATABASE_NAME = "billI.db";
    private static  final String TABLE_BILLS = "bills";
    private static  final String COLUMN_ID = "_id";
    private static  final String COLUMN_PRODUCTNAME = "productname";
    private static  final String COLUMN_PRICE = "price";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME,factory,DATABASE_VERSION);

    }
   // public MyDBHandler(){}

    @Override
    public void onCreate(SQLiteDatabase db) {
       // String query = "CREATE TABLE `"+DATABASE_NAME+"`.`"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INTEGER , `"+COLUMN_PRICE+"` VARCHAR(50) NOT NULL , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL);";
        //String query = "CREATE TABLE `"+DATABASE_NAME+"`.`"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INT(10) NOT NULL , `"+COLUMN_PRICE+"` VARCHAR(50) NOT NULL , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL );";


        String query = "CREATE TABLE " + TABLE_BILLS + " (" +
                        COLUMN_ID + " INTEGER," +
                COLUMN_PRICE+ " DOUBLE," +
                COLUMN_PRODUCTNAME + " VARCHAR " +
                        ");";
        Log.i("in mydbhandler","query = "+query);
        db.execSQL(query);
     //   db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public  void addBills(Bill bill) {

        ContentValues values = new ContentValues();
        DecimalFormat df = new DecimalFormat("0.000");
        values.put(COLUMN_ID,bill.get_id());
        values.put(COLUMN_PRICE,bill.get_price());
        values.put(COLUMN_PRODUCTNAME,bill.get_productname());



        SQLiteDatabase db = getWritableDatabase();
        long q =  db.insert(TABLE_BILLS,null,values);
        Log.i("addbills","query = "+q);
        db.close();
    }

    public  int databaseToString() {

        int dbString = -1;
        Cursor c = null;
        SQLiteDatabase db = getWritableDatabase();
        //onCreate(db);
     //   Bill bill = new Bill(0,"vaselin","500");
        //addBills(bill);
        String query = "SELECT * FROM " + TABLE_BILLS + " WHERE _id = (SELECT MAX(_id) FROM " + TABLE_BILLS + ");";
        //String query1 = "SELECT MAX(_id) FROM bills;";
        //Cursor c1 = db.rawQuery(query1, null);
       // if (c1 == null) {
           // return 0;
       // } else {
           // c1.moveToNext();
          //  String id = c1.getString(c1.getColumnIndex(COLUMN_ID));
           // String query = "SELECT * FROM bills WHERE _id = "+id+";";
        try {
            c = db.rawQuery(query, null);

        }catch (Exception sqLiteDatabase){

            onCreate(db);
            db.close();
        }
            //if ((c.isBeforeFirst() != true))
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                dbString = c.getInt(c.getColumnIndex("_id"));
            } else
                dbString = 0;
            db.close();
            return dbString;
        }

   public Cart[] databaseToArray( int pos) {
       int position = pos;
       SQLiteDatabase db = getWritableDatabase();
       Cursor crs = db.rawQuery("SELECT * FROM `"+TABLE_BILLS+"` WHERE `"+COLUMN_ID+"` = "+position+";", null);
       Cart[] array = new Cart[crs.getCount()];
       int i = 0;
       while(crs.moveToNext())
       {
           String name = crs.getString(crs.getColumnIndex(COLUMN_PRODUCTNAME));
           double price = crs.getInt(crs.getColumnIndex(COLUMN_PRICE));
           Cart cart = new Cart();
           cart.setItemname(name);
           cart.setPrice(price);
           array[i] = cart;
           Log.i("databasetoarray","item = "+cart.getItemname()+" price = "+cart.getPrice());
           i++;
           //System.out.println(uname);
       }
       db.close();
       return array;
    }

    public void drop() {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        db.close();
    }
}
