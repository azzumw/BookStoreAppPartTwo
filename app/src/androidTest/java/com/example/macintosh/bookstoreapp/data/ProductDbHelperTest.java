package com.example.macintosh.bookstoreapp.data;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProductDbHelperTest {

    private ProductDbHelper target;

    @Ignore
    public void setUp() throws Exception {
        target = new ProductDbHelper(InstrumentationRegistry.getTargetContext());

        target.deleteAllProducts();
        target.deleteAllSuppliers();
    }

    @Ignore
    public void test_insert_one_product() throws Exception {
        target.insertProduct(1);

        Cursor cursor = target.getAllProducts();

        int count = cursor.getCount();

        assertEquals(1, count);
    }

    @Ignore
    public void test_insert_two_products() throws Exception {
        target.insertProduct(1);
        target.insertProduct(1);

        Cursor cursor = target.getAllProducts();

        int count = cursor.getCount();

        assertEquals(2, count);
    }

    @Ignore
    public void test_insert_one_supplier() throws Exception {
        target.insertSupplier();

        Cursor cursor = target.getAllSuppliers();

        int count = cursor.getCount();

        assertEquals(1, count);
    }

    @Test
    public void test_insert_two_suppliers() throws Exception {
        target.insertSupplier();
        target.insertSupplier();

        Cursor cursor = target.getAllSuppliers();

        int count = cursor.getCount();

        assertEquals(2, count);
    }


}
