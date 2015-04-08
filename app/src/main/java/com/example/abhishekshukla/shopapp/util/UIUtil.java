package com.example.abhishekshukla.shopapp.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {

    public static void closeSoftKeyboard(final View view) {
        if (null == view) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != imm) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

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
