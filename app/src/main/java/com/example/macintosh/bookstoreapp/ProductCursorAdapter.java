package com.example.macintosh.bookstoreapp;

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry;

import java.lang.annotation.RetentionPolicy;

/**
 * Created by macintosh on 18/05/2018.
 */

public class ProductCursorAdapter extends CursorAdapter {
    
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
        
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final TextView bookNameTxtView = view.findViewById(R.id.productnameTextView);
        final TextView qtyTxtView = view.findViewById(R.id.quantityTextView);
        final TextView priceTxtView = view.findViewById(R.id.priceBtn);
        TextView stockTxtView = view.findViewById(R.id.stock_statustextview);
        TextView suppNameTxtView = view.findViewById(R.id.edit_supp_name);
        TextView suppPhoneTxtView = view.findViewById(R.id.edit_supp_phone);

        TextView buy_TxtView = view.findViewById(R.id.priceBtn);
        buy_TxtView.setTag(cursor.getPosition());

        int indexBookNameCol = cursor.getColumnIndex(ProductEntry.NAME);
        int indexPriceCol = cursor.getColumnIndex(ProductEntry.PRICE);
        int indexQtyCol = cursor.getColumnIndex(ProductEntry.QUANTITY);
        int indexStockStatCol = cursor.getColumnIndex(ProductEntry.STOCK_STATUS);
        int indexSuppNameCol = cursor.getColumnIndex(ProductEntry.SUPPLIER_NAME);
        int indexSuppPhoneCol = cursor.getColumnIndex(ProductEntry.SUPPLIER_PHONE_NUMBER);

        String name_book = cursor.getString(indexBookNameCol);
        int qty = cursor.getInt(indexQtyCol);
        double price_book = cursor.getDouble(indexPriceCol);
        int stock_status = cursor.getInt(indexStockStatCol);
        final String supplierName = cursor.getString(indexSuppNameCol);
        final int supplierPhone = cursor.getInt(indexSuppPhoneCol);

        bookNameTxtView.setText(name_book);
        qtyTxtView.setText(String.valueOf(qty));
        priceTxtView.setText(String.valueOf(price_book));
        stockTxtView.setText((stock_status==1?"IN  STOCK":"OUT OF STOCK"));

        final Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI,cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_ID)));


        buy_TxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int pos = (Integer) view.getTag();
//                Toast.makeText(context, ""+pos, Toast.LENGTH_SHORT).show();
//                Log.v("CURSOR ID ",""+ pos);
                ContentValues values = new ContentValues();
                int newqty = cursor.getInt(cursor.getColumnIndex(ProductEntry.QUANTITY));
//                before I put the values in contentvalues I need to check what is the quantity in the database stored
                if(newqty>0){
                    newqty--;
                    if(newqty==0) values.put(ProductEntry.STOCK_STATUS,ProductEntry.OUT_OF_STOCK);
                }

                values.put(ProductEntry.QUANTITY,newqty);
                context.getContentResolver().update(currentProductUri,values,null,null);

            }
        });
    }
}
