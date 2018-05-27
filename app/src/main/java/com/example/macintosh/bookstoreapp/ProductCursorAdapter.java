package com.example.macintosh.bookstoreapp;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.bookstoreapp.data.ProductContract.ProductEntry;

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
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView bookNameTxtView = view.findViewById(R.id.productnameTextView);
        TextView qtyTxtView = view.findViewById(R.id.quantityTextView);
        TextView priceTxtView = view.findViewById(R.id.priceBtn);
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
        priceTxtView.setText(String.valueOf(price_book));
        stockTxtView.setText((stock_status==1?"IN  STOCK":"OUT OF STOCK"));

        buy_TxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "button clicked" + cursor.getPosition() , Toast.LENGTH_SHORT).show();

            }
        });

    }
}
