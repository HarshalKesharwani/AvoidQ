
package com.example.admin.avoidq;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class BillidHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 18;
    private static final String DATABASE_NAME = "billIds.db";
    private static final String TABLE_BILLS = "billid";
    private static final String COLUMN_BILL_ID = "bill_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TOTAL_PRICE = "total_price";

    public BillidHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    // public MyDBHandler(){}

    @Override
    public void onCreate(SQLiteDatabase db) {

        //String query = "CREATE TABLE `" + DATABASE_NAME + "`.`" + TABLE_BILLS + "` ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRICE + "` VARCHAR(50) NOT NULL , `" + COLUMN_PRODUCTNAME + "` VARCHAR(50) NOT NULL );";
        // String query=  "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_ID+"` INTEGER AUTO_INCREMENT , `"+COLUMN_PRODUCTNAME+"` VARCHAR(50) NOT NULL , `"+COLUMN_PRICE+"` VARCHAR(50) NOT NULL, PRIMARY KEY ("+COLUMN_ID+") );";
        String query = "CREATE TABLE `"+TABLE_BILLS+"` ( `"+COLUMN_BILL_ID+"` VARCHAR(50) , `"
                +COLUMN_TOTAL_PRICE+"` INTEGER , `"
                +COLUMN_DATE+"` VARCHAR(50)"
                +");";

        Log.i("cartdb", "query = " + query);

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public void addBills(OuterBill bill) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_ID, bill.getBill_id());
        values.put(COLUMN_TOTAL_PRICE, bill.getTotal_price());
        values.put(COLUMN_DATE, bill.getDate());
        SQLiteDatabase db = getWritableDatabase();
        long q = db.insert(TABLE_BILLS, null, values);
        Log.i("kykmjhn1", "query = " + q);
        db.close();
    }

    public OuterBill[] databaseToArray() {
        //int position = pos;
        Cursor crs = null;
        OuterBill[] array1 = null;
        SQLiteDatabase db = getWritableDatabase();
        try {
            crs = db.rawQuery("SELECT * FROM `" + TABLE_BILLS + "`;", null);
        } catch (Exception e) {
            onCreate(db);
            db.close();
        }
        if (crs != null) {


            OuterBill[] array = new OuterBill[crs.getCount()];
            int i = 0;
            while (crs.moveToNext()) {
                String name = crs.getString(crs.getColumnIndex(COLUMN_BILL_ID));
                int price = crs.getInt(crs.getColumnIndex(COLUMN_TOTAL_PRICE));
                String weight = crs.getString(crs.getColumnIndex(COLUMN_DATE));
                // String id = crs.getString(crs.getColumnIndex(COLUMN_ID));
                OuterBill cart = new OuterBill();
                cart.setBill_id(name);
                cart.setTotal_price(price);
                cart.setDate(weight);
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
