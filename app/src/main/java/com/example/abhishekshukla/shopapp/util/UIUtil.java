package com.example.abhishekshukla.shopapp.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {
    /**
     * Dismiss the soft keyboard.
     * 
     * @param view
     *            any view associated with the window for which the keyboard should be dismissed
     */
    public static void closeSoftKeyboard(final View view) {
        if (null == view) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != imm) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    /**
     * Show the soft keyboard and give its input to the given view.
     * 
     * @param view
     *            the view that will receive the input from the soft keyboard
     */
    public static void showSoftKeyboard(final View view) {
        if (null == view) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != imm) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
