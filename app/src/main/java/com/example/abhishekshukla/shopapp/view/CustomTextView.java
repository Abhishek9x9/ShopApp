package com.example.abhishekshukla.shopapp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by nishantgupta on 14/10/15.
 */
public class CustomTextView extends TextView {

    private static Typeface tf ;
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if(tf == null){
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Rg.ttf");
        }
        setTypeface(tf);
    }
}