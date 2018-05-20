package com.example.macintosh.bookstoreapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the BookStore app.
 */

public final class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.macintosh.bookstoreapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);     //content://com.example.macintosh.bookstoreapp

    public static final String PATH_PRODUCTS = "products";


    //private constructor
    private ProductContract(){}

    //inner class that defines the schema of bookstore database
    public static final class ProductEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PRODUCTS);     //content://com.example.macintosh.bookstoreapp/products

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;


        /**TABLE NAME*/
        public final static String TABLE_NAME = "products";

        /**UNIQUE ID NUMBER*/
        public final static String PRODUCT_ID = BaseColumns._ID; //"_id"

        /**COLUMNS*/
        public final static String NAME = "name";
        public final static String PRICE = "price";
        public final static String QUANTITY = "quantity";
        public final static String SUPPLIER_NAME = "supplier_name";
        public final static String SUPPLIER_PHONE_NUMBER = "supplier_phone";
        public final static String STOCK_STATUS ="stock_status";

        /**POSSIBLE VALUES FOR IN_STOCK*/
        public final static int IN_STOCK = 1;
        public final static int OUT_OF_STOCK = 0;
        public final static int DEFAULT_QUANTITY = 0;

    }
}
