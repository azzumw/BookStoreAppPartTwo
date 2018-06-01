package com.example.macintosh.bookstoreapp;

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
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
        TextView bookNameTxtView = view.findViewById(R.id.productnameTextView);
        TextView qtyTxtView = view.findViewById(R.id.quantityTextView);
        TextView stockTxtView = view.findViewById(R.id.stock_statustextview);
        TextView buy_TxtView = view.findViewById(R.id.priceBtn);

        int indexBookNameCol = cursor.getColumnIndex(ProductEntry.NAME);
        int indexPriceCol = cursor.getColumnIndex(ProductEntry.PRICE);
        int indexQtyCol = cursor.getColumnIndex(ProductEntry.QUANTITY);
        int indexStockStatCol = cursor.getColumnIndex(ProductEntry.STOCK_STATUS);

        String name_book = cursor.getString(indexBookNameCol);
        int qty = cursor.getInt(indexQtyCol);
        double price_book = cursor.getDouble(indexPriceCol);
        int stock_status = cursor.getInt(indexStockStatCol);

        bookNameTxtView.setText(name_book);
        qtyTxtView.setText(String.valueOf(qty));
        buy_TxtView.setText(context.getString(R.string.buy_pound).concat(String.valueOf(price_book)));
        stockTxtView.setText((stock_status==1?context.getString(R.string.in_stock_status):context.getString(R.string.out_of_stock_status)));

        if(stock_status==1){
            stockTxtView.setTextColor(Color.GREEN);
        }
        else if(stock_status==0){
            stockTxtView.setTextColor(Color.RED);
        }

        final Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI,cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_ID)));
        final int newqty = cursor.getInt(cursor.getColumnIndex(ProductEntry.QUANTITY));

        buy_TxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();

                int temp = newqty;

                if(temp>0){
                    temp--;
                    if(temp==0) values.put(ProductEntry.STOCK_STATUS,ProductEntry.OUT_OF_STOCK);
                }
                else if(temp==0) Snackbar.make(view, R.string.outOfStock_message,Snackbar.LENGTH_SHORT).show();

                values.put(ProductEntry.QUANTITY,temp);
                context.getContentResolver().update(currentProductUri,values,null,null);

            }
        });
    }
}
