package com.example.macintosh.bookstoreapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry;
import com.example.macintosh.bookstoreapp.data.ProductDbHelper;

import static com.example.macintosh.bookstoreapp.MainActivity.PROJECTION;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText editTextBookname;
    private EditText editTextBookPrice;
    private EditText editTextSupplierName;
    private EditText editTextSupplierPhone;
    private int quantity = ProductEntry.DEFAULT_QUANTITY; //1

    private Spinner qtySpinner;

    private Uri currentProductUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        currentProductUri = getIntent().getData();


        if (currentProductUri == null) {
            this.setTitle(R.string.edit_activity_title_new_book);
        }
// get data via the key
        else setTitle(R.string.edit_activity_title_existing_book);

        editTextBookname = findViewById(R.id.edit_book_name);
        editTextBookPrice = findViewById(R.id.edit_price);
        editTextSupplierName = findViewById(R.id.edit_supp_name);
        editTextSupplierPhone = findViewById(R.id.edit_supp_phone);

        qtySpinner = findViewById(R.id.spinner_quantity);

        setupSpinner();

        getSupportLoaderManager().initLoader(0,null,this);

    }

    private void insertBook(){
        String bookName = editTextBookname.getText().toString().trim();
        double price = Double.parseDouble(editTextBookPrice.getText().toString().trim());
        String supplierName = editTextSupplierName.getText().toString().trim();
        long supplierPhone = Long.parseLong(editTextSupplierPhone.getText().toString().trim());

//        ProductDbHelper productDbHelper = new ProductDbHelper(this);
//        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();

        ContentValues  values = new ContentValues();
        values.put(ProductEntry.NAME,bookName);
        values.put(ProductEntry.PRICE,price);
        values.put(ProductEntry.QUANTITY,quantity);
        values.put(ProductEntry.STOCK_STATUS,ProductEntry.IN_STOCK);
        values.put(ProductEntry.SUPPLIER_NAME,supplierName);
        values.put(ProductEntry.SUPPLIER_PHONE_NUMBER,supplierPhone);

        // Insert a new row for pet in the database, returning the ID of that new row.
//        long newRowId = sqLiteDatabase.insert(ProductEntry.TABLE_NAME, null, values);

        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI,values);

        if (uri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter quantitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.quantity_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        quantitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        qtySpinner.setAdapter(quantitySpinnerAdapter);

        qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quantity = Integer.parseInt(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                quantity = ProductEntry.DEFAULT_QUANTITY; //0
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
//        	You must return true for the menu to be displayed; if you return false it will not be shown.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                 insertBook();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductEntry.PRODUCT_ID,
                ProductEntry.NAME,
                ProductEntry.PRICE,
                ProductEntry.QUANTITY,
                ProductEntry.STOCK_STATUS,
                ProductEntry.SUPPLIER_NAME,
                ProductEntry.SUPPLIER_PHONE_NUMBER};

        return new android.support.v4.content.CursorLoader(this,currentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            int namecolindex = cursor.getColumnIndex(ProductEntry.NAME);
            int pricecolindex = cursor.getColumnIndex(ProductEntry.PRICE);
            int qtycolindex = cursor.getColumnIndex(ProductEntry.QUANTITY);
            int suppnamecolindex = cursor.getColumnIndex(ProductEntry.SUPPLIER_NAME);
            int suppPhonecolindex = cursor.getColumnIndex(ProductEntry.SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(namecolindex);
            double price = cursor.getDouble(pricecolindex);
            int qty = cursor.getInt(qtycolindex);
            String suppName = cursor.getString(suppnamecolindex);
            int suppPhone = cursor.getInt(suppPhonecolindex);

            editTextBookname.setText(name);
            editTextBookPrice.setText(String.valueOf(price));
            qtySpinner.setSelection(qty);
            editTextSupplierName.setText(suppName);
            editTextSupplierPhone.setText(String.valueOf(suppPhone));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        editTextBookname.setText("");
        editTextBookPrice.setText("");
        editTextSupplierName.setText("");
        editTextSupplierPhone.setText("");
        qtySpinner.setSelection(0);
    }
}
