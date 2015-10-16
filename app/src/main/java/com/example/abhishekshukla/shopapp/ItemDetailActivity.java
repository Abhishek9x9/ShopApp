package com.example.abhishekshukla.shopapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.*;
import android.widget.Toast;

import com.example.abhishekshukla.shopapp.carousel.CartDetailCarouselAcitivity;
import com.example.abhishekshukla.shopapp.dto.Product;
import com.example.abhishekshukla.shopapp.util.ImageLoader;
import com.example.abhishekshukla.shopapp.util.ImageSub;
import com.github.channguyen.rsv.RangeSliderView;

/**
 * Created by abhishekshukla on 3/4/15.
 */
public class ItemDetailActivity  extends Activity {


    private RangeSliderView smallSlider;

    private Product product;
    private TextView cartTextView;
    private ImageView imageView;
    private Button button;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;

    private int itemCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_detail);
        cartTextView = (TextView) findViewById(R.id.itemDetailCartTextView);
        imageView = (ImageView) findViewById(R.id.itemImageView);
        button = (Button) findViewById(R.id.button1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);

        ImageView cartView = (ImageView) findViewById(R.id.item_image_cart);

        cartView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CartActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserCart.getInstance().addItem(product, itemCount);
                Toast.makeText(getApplicationContext(), product.getTitle() + " added in the cart",
                        Toast.LENGTH_SHORT).show();
                cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
            }
        });

        if(null != getIntent() && null!= getIntent().getSerializableExtra(ResultsManager.PRODUCT_CLICKED)) {
            product = (Product) getIntent().getSerializableExtra(ResultsManager.PRODUCT_CLICKED);
            //Bitmap bitmap =  ImageLoader.getInstance().loadImageAsync("http:" + product.getImageUrl(), new ImageSub(imageView, this),"" + product.getId(), false);
            Bitmap bitmap =  ImageLoader.getInstance().loadImageAsync(product.getImageUrl(), new ImageSub(imageView, this), "" + product.getId(), false);
            if(null != bitmap)
            {
                imageView.setImageBitmap(bitmap);
            }
            textView2.setText(product.getTitle());
            textView3.setText("" + itemCount);
            textView4.setText(product.getPrice());
            textView5.setText(product.getPrice());
            textView6.setText(product.getSaving());
        }
        if (savedInstanceState != null && null != savedInstanceState.getSerializable(ResultsManager.PRODUCT_CLICKED)) {
            product = (Product) savedInstanceState.getSerializable(ResultsManager.PRODUCT_CLICKED);
            Bitmap bitmap =  ImageLoader.getInstance().loadImageAsync(product.getImageUrl(), new ImageSub(imageView, this),"" + product.getId(), false);
            //Bitmap bitmap = ImageLoader.getInstance().loadImageAsync("http:" + product.getImageUrl(), new ImageSub(imageView, this),"" + product.getId(), false);
            if(null != bitmap)
            {
                imageView.setImageBitmap(bitmap);
            }
            textView2.setText(product.getTitle());
            textView3.setText("" + itemCount);
            textView4.setText(product.getPrice());
            textView5.setText(product.getPrice());
            textView6.setText(product.getSaving());
        }

        cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));

        smallSlider = (RangeSliderView) findViewById(
                R.id.rsv_small);

        final RangeSliderView.OnSlideListener listener = new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                itemCount= index + 1;
                textView3.setText("" + itemCount);
            }
        };
        textView5.setPaintFlags(textView5.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        smallSlider.setOnSlideListener(listener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(null != product) {
            outState.putSerializable(ResultsManager.PRODUCT_CLICKED, product);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected  void  onResume () {
        super.onResume();
        cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
    }
}
