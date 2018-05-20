package com.example.macintosh.bookstoreapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry;


/**
 * Created by macintosh on 04/03/2018.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "bookstore.db";

    private final String CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("+
            ProductEntry.PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            ProductEntry.NAME+ " TEXT NOT NULL, " +
            ProductEntry.PRICE + " REAL DEFAULT 0.0, " +
            ProductEntry.QUANTITY + " INTEGER DEFAULT 0, " +
            ProductEntry.STOCK_STATUS + " INTEGER DEFAULT "+ ProductEntry.OUT_OF_STOCK +", "+
            ProductEntry.SUPPLIER_NAME + " INTEGER DEFAULT UNKNOWN, " +
            ProductEntry.SUPPLIER_PHONE_NUMBER + " INTEGER);";

//    "FOREIGN KEY("+ProductEntry.SUPPLIER_ID+") REFERENCES " + SupplierEntry.SUP_ID+");"

//    private final String CREATE_SUPPLIER_TABLE = "CREATE TABLE "+ SupplierEntry.TABLE_NAME + " ("
//            + SupplierEntry.SUP_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
//            + SupplierEntry.NAME + " TEXT NOT NULL, "
//            + SupplierEntry.PHONE + " TEXT)";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE_SUPPLIER_TABLE);
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // do something
    }

    public Cursor getAllProducts() {

        return getReadableDatabase().rawQuery("SELECT * FROM " +ProductEntry.TABLE_NAME, null);
    }

//    public Cursor getAllSuppliers() {
//        return getReadableDatabase().rawQuery("SELECT * FROM " +SupplierEntry.TABLE_NAME, null);
//    }

    /*public long insertProduct() {
        ContentValues values = new ContentValues();

        values.put(ProductEntry.NAME,"Quiet - The Power of Introverts");
        values.put(ProductEntry.PRICE,6);
        values.put(ProductEntry.QUANTITY,5);
        values.put(ProductEntry.STOCK_STATUS,ProductEntry.IN_STOCK);
        values.put(ProductEntry.SUPPLIER_NAME,"Amazon");
        values.put(ProductEntry.SUPPLIER_PHONE_NUMBER,"08004657935");
//        values.put(ProductEntry.SUPPLIER_ID,supplierId);

        // Insert the new row, returning the primary key value of the new row,e lse -1
        //null here means it does not create any row if you didnt provide values otherwise
        //it will insert row with null values.
        return getWritableDatabase().insert(ProductEntry.TABLE_NAME, null, values);
    }*/

//    public long insertSupplier() {
//        ContentValues values = new ContentValues();
//
//        values.put(SupplierEntry.NAME,"Amazon");
//        values.put(SupplierEntry.PHONE,"02056897464");
//
//        return getWritableDatabase().insert(SupplierEntry.TABLE_NAME,null,values);
//    }


//    public int deleteAllSuppliers() {
//        return getWritableDatabase().delete(SupplierEntry.TABLE_NAME, null, null);
//    }
}
