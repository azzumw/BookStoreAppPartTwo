package com.example.macintosh.bookstoreapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bookNameTxtView = view.findViewById(R.id.nameTextView);
        TextView qtyTxtView = view.findViewById(R.id.quantityTextView);
        TextView priceTxtView = view.findViewById(R.id.pricetextview);
        TextView stockTxtView = view.findViewById(R.id.stock_statustextview);

        int indexBookNameCol = cursor.getColumnIndex(ProductEntry.NAME);
        int indexPriceCol = cursor.getColumnIndex(ProductEntry.PRICE);
        int indexQtyCol = cursor.getColumnIndex(ProductEntry.QUANTITY);
//        int indexSuppName = cursor.getColumnIndex(ProductEntry.SUPPLIER_NAME);
        int indexStockStatCol = cursor.getColumnIndex(ProductEntry.STOCK_STATUS);

        String name_book = cursor.getString(indexBookNameCol);
        int qty = cursor.getInt(indexQtyCol);
//        String suppName = context.getString(indexSuppName);
        double price_book = cursor.getDouble(indexPriceCol);
        int stock_status = cursor.getInt(indexStockStatCol);

        bookNameTxtView.setText(name_book);
        qtyTxtView.setText(String.valueOf(qty));
        priceTxtView.setText(String.valueOf(price_book));
        stockTxtView.setText((stock_status==1?"IN  STOCK":"OUT OF STOCK"));

    }
}
