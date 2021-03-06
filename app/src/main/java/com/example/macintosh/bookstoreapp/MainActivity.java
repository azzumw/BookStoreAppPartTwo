package com.example.macintosh.bookstoreapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private ProductCursorAdapter mAdapter;
    ListView listView;


    public static final String [] PROJECTION = {ProductEntry.PRODUCT_ID,
                                                ProductEntry.NAME,
                                                ProductEntry.PRICE,
                                                ProductEntry.QUANTITY,
                                                ProductEntry.STOCK_STATUS,
                                                ProductEntry.SUPPLIER_NAME,
                                                ProductEntry.SUPPLIER_PHONE_NUMBER
                                                };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        mAdapter = new ProductCursorAdapter(this,null);

        listView =  findViewById(R.id.listviewMain);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class).setAction(Intent.ACTION_EDIT);

                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI,id);
                intent.setData(currentProductUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(0,null,this);


    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_insert_dummy_data: insertProductData();return true;
            case R.id.action_delete_all:
                if(listView.getCount()>0)
                    showDeleteConfirmationDialog();
                else Snackbar.make(findViewById(R.id.relative_layout), R.string.no_books_snackbar_del_msg,Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.catalog_delete_all_prod_confirmation);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteAllProducts();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllProducts(){
        int rows = getContentResolver().delete(ProductEntry.CONTENT_URI,null,null);
        if(rows != 0){
            Snackbar.make(findViewById(R.id.relative_layout),R.string.catalog_delete_successful,Snackbar.LENGTH_SHORT).show();
        }
        else{
            Snackbar.make(findViewById(R.id.relative_layout),R.string.catalog_error_deleting,Snackbar.LENGTH_SHORT).show();
        }
    }

    /** inserts data in to the products table by fetching supplier Id from the supplier Table*/
    private void insertProductData(){

        ContentValues values = new ContentValues();

        values.put(ProductEntry.NAME,getString(R.string.dummyData_bookname));
        values.put(ProductEntry.PRICE,6.99);
        values.put(ProductEntry.QUANTITY,1);
        values.put(ProductEntry.STOCK_STATUS,ProductEntry.IN_STOCK);
        values.put(ProductEntry.SUPPLIER_NAME,getString(R.string.dummyData_suppName));
        values.put(ProductEntry.SUPPLIER_PHONE_NUMBER,567);

        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI,values);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.

        return new CursorLoader(this, ProductEntry.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }
}
