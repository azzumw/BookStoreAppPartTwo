package com.example.macintosh.bookstoreapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.macintosh.bookstoreapp.data.ProductContract.*;
import static com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry.TABLE_NAME;

/**
 * Created by macintosh on 14/05/2018.
 */

public class BookProvider extends ContentProvider {

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    ProductDbHelper productDbHelper;

    /** Tag for the log messages */
    public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCTS,PRODUCTS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCTS+"/#",PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = productDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case PRODUCTS:
                cursor = database.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);break;
            case PRODUCT_ID:
                selection = ProductEntry.PRODUCT_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);break;
            default:
                    throw new IllegalArgumentException("Unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        // Get writeable database
        SQLiteDatabase database = productDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                     rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);break;

            case PRODUCT_ID:
                // Delete a single row given by the ID in the URI
                selection = ProductEntry.PRODUCT_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted =  database.delete(TABLE_NAME, selection, selectionArgs);break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

            return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                // For the PRODUCT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(ProductEntry.NAME)) {
            String name = contentValues.getAsString(ProductEntry.NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        if (contentValues.containsKey(ProductEntry.PRICE)) {
            Integer price = contentValues.getAsInteger(ProductEntry.PRICE);
            if (price == null || price<0) {
                throw new IllegalArgumentException("Price invalid");
            }
        }

        // TODO: Update the selected products in the products database table with the given ContentValues
        SQLiteDatabase database = productDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(TABLE_NAME,contentValues,selection,selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // TODO: Return the number of rows that were affected
        return rowsUpdated;
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        String productName = values.getAsString(ProductEntry.NAME);
        String productPrice = values.getAsString(ProductEntry.PRICE);
        Integer quantity = values.getAsInteger(ProductEntry.QUANTITY);
        String supplierName = values.getAsString(ProductEntry.SUPPLIER_NAME);
        Integer supplierPhoneNum = values.getAsInteger(ProductEntry.SUPPLIER_PHONE_NUMBER);

        if(productName==null || productName.matches("^[a-zA-Z]")){
            throw new IllegalArgumentException("Cannot be empty. Text only");
        }

//        if (productPrice<0){
//            throw new IllegalArgumentException("Negative numbers not allowed");
//        }

        if(supplierName==null){
            throw new IllegalArgumentException("Supplier name required");
        }

        // TODO: Insert a new product into the products database table with the given ContentValues
        SQLiteDatabase database = productDbHelper.getWritableDatabase();

        long id = database.insert(TABLE_NAME,null,values);

        if(id == -1){
            Log.e(LOG_TAG,"Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
}
