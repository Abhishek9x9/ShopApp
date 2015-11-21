package com.example.abhishekshukla.shopapp.activity.cart.carouselview;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.dto.CartItem;

/**
 * Created by nishantgupta on 6/9/15.
 */
public class OnProductFocusedListener implements CoverAdapterView.OnItemSelectedListener {

    private CartDetailCarouselAcitivity.ImageAdapter imageAdapter;
    private Intent intent;
    private View productView;

    public OnProductFocusedListener(CartDetailCarouselAcitivity.ImageAdapter imageAdapter
    , Intent intent, View productView){
        this.imageAdapter = imageAdapter;
        this.intent = intent;
        this.productView = productView;
    }

    @Override
    public void onItemSelected(final CoverAdapterView<?> parent, final View view, final int position, long id) {
        CartItem cartItem =  (CartItem)parent.getItemAtPosition(position);
        TextView nameView = (TextView)productView.findViewById(R.id.item_review_name);
        nameView.setText(cartItem.getTitle());

        TextView rateView = (TextView)productView.findViewById(R.id.item_review_rate);
        rateView.setText(cartItem.getSellingPrice() + " Rs/-");

        TextView discountView = (TextView)productView.findViewById(R.id.item_review_discount);
        discountView.setText("You save ₹ " + id);

        productView.setVisibility(View.VISIBLE);
        //Toast.makeText(parent.getContext(), "Shown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(CoverAdapterView<?> parent) {

    }
}
