package com.example.macintosh.bookstoreapp;

/**
 * source:http://androidmkab.com/2016/02/28/how-to-add-splash-screen-in-android-with-kenburnsview-part-2/
 */

public class IncompatibleRatioException extends RuntimeException {

    private static final long serialVersionUID = 234608108593115395L;

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}
