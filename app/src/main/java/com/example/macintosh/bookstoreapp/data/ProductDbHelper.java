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

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // do something
    }
}
