package com.example.abhishekshukla.shopapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SearchEditText extends EditText{

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * We override this method to be sure and show the soft keyboard if
     * appropriate when the EditText has focus.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
           
            final InputMethodManager inputManager = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(SearchEditText.this, 0);
        }
    }

}
