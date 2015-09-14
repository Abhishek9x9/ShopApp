package com.example.abhishekshukla.shopapp.carousel;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.itempopup.EasyDialog;

/**
 * Created by nishantgupta on 6/9/15.
 */
public class OnProductFocusedListener implements CoverAdapterView.OnItemSelectedListener {

    private CartDetailCarouselAcitivity.ImageAdapter imageAdapter;
    private Intent intent;

    public OnProductFocusedListener(CartDetailCarouselAcitivity.ImageAdapter imageAdapter
    , Intent intent){
        this.imageAdapter = imageAdapter;
        this.intent = intent;
    }

    @Override
    public void onItemSelected(final CoverAdapterView<?> parent, final View view, final int position, long id) {
        Toast.makeText(parent.getContext(), "Shown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(CoverAdapterView<?> parent) {

    }
}
