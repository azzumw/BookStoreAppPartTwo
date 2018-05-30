package com.example.macintosh.bookstoreapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry;

import java.util.Properties;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText editTextBookname;
    private EditText editTextBookPrice;
    private EditText editTextSupplierName;
    private EditText editTextSupplierPhone;
    private TextView qtyNumberTxtView;
    private ImageButton incrementQty;
    private ImageButton decrementQty;

    private int quantity; //0

    private Spinner qtySpinner;

    private Uri currentProductUri;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        currentProductUri = getIntent().getData();

        editTextBookname = findViewById(R.id.edit_book_name);
        editTextBookPrice = findViewById(R.id.edit_price);
        editTextSupplierName = findViewById(R.id.edit_supp_name);
        editTextSupplierPhone = findViewById(R.id.edit_supp_phone);
        incrementQty = findViewById(R.id.plus);
        decrementQty = findViewById(R.id.minus);
        qtyNumberTxtView = findViewById(R.id.number);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.call_fab);

        if (currentProductUri == null) {
            this.setTitle(R.string.edit_activity_title_new_book);
            invalidateOptionsMenu();
            fab.hide();
            quantity = ProductEntry.DEFAULT_QUANTITY;
            qtyNumberTxtView.setText(String.valueOf(quantity));
        }
// get data via the key
        else {
            setTitle(R.string.edit_activity_title_existing_book);
            getSupportLoaderManager().initLoader(0,null,this);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    long supplierPhone = Long.parseLong(editTextSupplierPhone.getText().toString().trim());
                    intent.setData(Uri.parse("tel:"+String.valueOf(supplierPhone)));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        }



        editTextBookname.setOnTouchListener(mTouchListener);
        editTextBookPrice.setOnTouchListener(mTouchListener);
        editTextSupplierPhone.setOnTouchListener(mTouchListener);
        editTextSupplierName.setOnTouchListener(mTouchListener);
        incrementQty.setOnTouchListener(mTouchListener);
        decrementQty.setOnTouchListener(mTouchListener);

//        qtySpinner = findViewById(R.id.spinner_quantity);

//        setupSpinner();


        //qtyNumberTxtView.setText(String.valueOf(quantity));

        incrementQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = quantity+1;
                qtyNumberTxtView.setText(String.valueOf(quantity));
            }
        });

        decrementQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>0){
                    quantity--;
                    qtyNumberTxtView.setText(String.valueOf(quantity));
                }
            }
        });


    }

    private void savePet(boolean areAllTextAvailable){

        if(areAllTextAvailable) return;
        else{

            String bookName = editTextBookname.getText().toString().trim();
            double price = Double.parseDouble(editTextBookPrice.getText().toString().trim());
            int quant = Integer.parseInt(qtyNumberTxtView.getText().toString());
            String supplierName = editTextSupplierName.getText().toString().trim();
            long supplierPhone = Long.parseLong(editTextSupplierPhone.getText().toString().trim());


            ContentValues  values = new ContentValues();
            values.put(ProductEntry.NAME,bookName);
            values.put(ProductEntry.PRICE,price);
            values.put(ProductEntry.QUANTITY,quant);
            values.put(ProductEntry.SUPPLIER_NAME,supplierName);
            values.put(ProductEntry.SUPPLIER_PHONE_NUMBER,supplierPhone);

            if(quant==ProductEntry.DEFAULT_QUANTITY) {
                values.put(ProductEntry.STOCK_STATUS,ProductEntry.OUT_OF_STOCK);
            }

            else  {
                values.put(ProductEntry.STOCK_STATUS,ProductEntry.IN_STOCK);
            }

            Uri uri;

            if(currentProductUri==null){

                uri = getContentResolver().insert(ProductEntry.CONTENT_URI,values);

                if (uri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                            Toast.LENGTH_SHORT).show();
//                    Snackbar.make(, R.string.editor_insert_book_failed,Snackbar.LENGTH_SHORT).show();


                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                            Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(R.id.editor_linear_parent), R.string.editor_insert_book_successful,Snackbar.LENGTH_SHORT).show();

                }
            }else{
//            String selection = ProductEntry._ID + "=?";
//            String [] selectionArgs = new String[] { String.valueOf(ContentUris.parseId(currentProductUri)) };

                int rowsAffected = getContentResolver().update(currentProductUri,values,null,null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.editor_update_prod_failed),
                            Toast.LENGTH_SHORT).show();
//                    Snackbar.make(, R.string.editor_update_prod_failed,Snackbar.LENGTH_SHORT).show();

                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_update_prod_successful),
                            Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(R.id.relative_layout), R.string.editor_update_prod_successful,Snackbar.LENGTH_SHORT).show();

                }
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
//        	You must return true for the menu to be displayed; if you return false it will not be shown.
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                String bookName = editTextBookname.getText().toString().trim();
                String price = editTextBookPrice.getText().toString().trim();
                String supplierName = editTextSupplierName.getText().toString().trim();
                String supplierPhone = editTextSupplierPhone.getText().toString().trim();

                boolean allFieldMissing = TextUtils.isEmpty(bookName)&&TextUtils.isEmpty(supplierName)
                    &&TextUtils.isEmpty(price) && TextUtils.isEmpty(supplierPhone);

                if(allFieldMissing) {finish();return false;}
                else{
                    //all fields filled or none empty
                    if(!TextUtils.isEmpty(bookName) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(supplierName) && !TextUtils.isEmpty(supplierPhone)){
                        //save
                        savePet(false);finish();return true;
                    }
                    else{
//                        Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(R.id.editor_linear_parent), R.string.editor_info_miss_msg,Snackbar.LENGTH_SHORT).show();
                    }
                }
//                 savePet(anyFieldMissing);
                // Exit activity
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
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
            quantity = cursor.getInt(qtycolindex);
            String suppName = cursor.getString(suppnamecolindex);
            int suppPhone = cursor.getInt(suppPhonecolindex);

            editTextBookname.setText(name);
            editTextBookPrice.setText(String.valueOf(price));
            qtyNumberTxtView.setText(String.valueOf(quantity));
            //qtySpinner.setSelection(qty);
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
        qtyNumberTxtView.setText(String.valueOf(0));
        //qtySpinner.setSelection(0);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
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
    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteProduct() {

        int rowsDeleted;
        if(currentProductUri!=null){

            rowsDeleted = getContentResolver().delete(currentProductUri,null,null);
            if(rowsDeleted!=0){
//                Toast.makeText(this, getString(R.string.editor_delete_product_successful), Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.editor_linear_parent), R.string.editor_delete_product_successful,Snackbar.LENGTH_SHORT).show();

            }
            else{
//                Toast.makeText(this, getString(R.string.editor_delete_product_failed), Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.editor_linear_parent), R.string.editor_delete_product_failed,Snackbar.LENGTH_SHORT).show();

            }
            finish();
        }

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /*private void setupSpinner() {
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
    }*/

}
