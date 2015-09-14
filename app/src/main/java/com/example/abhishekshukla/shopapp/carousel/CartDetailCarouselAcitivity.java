package com.example.abhishekshukla.shopapp.carousel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.StrictMode;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.util.ImageLoader;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Source of this code is - http://www.inter-fuser.com/2010/01/android-coverflow-widget.html
 */
public class CartDetailCarouselAcitivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        super.onCreate(savedInstanceState);

        CoverFlow coverFlow;
        coverFlow = new CoverFlow(this);

        ImageAdapter coverImageAdapter = new ImageAdapter(this);

        coverImageAdapter.createReflectedImages();

        coverFlow.setAdapter(coverImageAdapter);
        coverFlow.setOnItemSelectedListener(new OnProductFocusedListener(coverImageAdapter, getIntent()));
        coverFlow.setSpacing(-15);
        coverFlow.setSelection(8, true);
        coverFlow.setGravity(Gravity.TOP);
        setContentView(R.layout.activity_cart_details_carousel);

        CoverFlow.LayoutParams params = new CoverFlow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(coverFlow, params);

        Button button = (Button)findViewById(R.id.btnCheckout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Checkout has been clicked", Toast.LENGTH_SHORT).show();
            }
        });
        button.setFocusable(true);
    }

    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        private FileInputStream fis;

        private String[] mImageIds = {
                "http://bigbasket.com/media/uploads/p/m/100008139_4-brown-polson-corn-flour.jpg",
                "http://bigbasket.com/media/uploads/p/l/100285920_1-eagle-yeast-active-dry.jpg",
                "http://bigbasket.com/media/uploads/p/l/100349327_1-betty-crocker-mix-pancake.jpg",
                "http://bigbasket.com/media/uploads/p/l/100005522_1-weikfield-powder-baking.jpg",
                "http://bigbasket.com/media/uploads/p/l/100005747_2-weikfield-powder-cocoa.jpg",
                "http://bigbasket.com/media/uploads/p/l/40008378_1-weikfield-custard-powder-vanilla-flavour.jpg",
                "http://bigbasket.com/media/uploads/p/l/265970_1-mtr-mix-gulab-jamun.jpg",
                "http://bigbasket.com/media/uploads/p/l/100012255_1-pillsbury-cooker-cake-chocolate-eggless.jpg",
                "http://bigbasket.com/media/uploads/p/l/100005522_1-weikfield-powder-baking.jpg",
        };

        private ImageView[] mImages;
        private View[] views;
        private Map<ImageView, TextView> imageViewTextViewMap;

        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[mImageIds.length];
            views = new ViewGroup[mImageIds.length];
            this.imageViewTextViewMap = new HashMap<>();
        }

        public boolean createReflectedImages() {
            //The gap we want between the reflection and the original image
            final int reflectionGap = 0;


            int index = 0;
            for (String imageUrl : mImageIds) {
                final int widthOriginal = mContext.getResources().getDimensionPixelSize(R.dimen.item_image_width);
                final int heightOriginal = mContext.getResources().getDimensionPixelSize(R.dimen.item_image_height);
                Bitmap originalImage = ImageLoader.getInstance().loadImage(imageUrl, widthOriginal, heightOriginal,
                        imageUrl.substring(imageUrl.lastIndexOf("/"), imageUrl.length()), false);
                //BitmapFactory.decodeResource(getResources(), imageId);
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();


                //This will not scale but will flip on the Y axis
                Matrix matrix = new Matrix();
                matrix.preScale(1, -1);

                //Create a Bitmap with the flip matrix applied to it.
                //We only want the bottom half of the image
                Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 4, width, height / 4, matrix, false);

                //Create a new bitmap with same width but taller to fit reflection
                Bitmap bitmapWithReflection = Bitmap.createBitmap(width
                        , (height), Bitmap.Config.ARGB_8888);

                //Create a new Canvas with the bitmap that's big enough for
                //the image plus gap plus reflection
                Canvas canvas = new Canvas(bitmapWithReflection);
                //Draw in the original image
                canvas.drawBitmap(originalImage, 0, 0, null);
                //Draw in the gap
                Paint deafaultPaint = new Paint();
                canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
                //Draw in the reflection
                //canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

                //Create a shader that is a linear gradient that covers the reflection
                Paint paint = new Paint();
                LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
                        bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                        Shader.TileMode.CLAMP);
                //Set the paint to use this shader (linear gradient)
                paint.setShader(shader);
                //Set the Transfer mode to be porter duff and destination in
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                //Draw a rectangle using the paint with our linear gradient
                canvas.drawRect(0, height, width,
                        bitmapWithReflection.getHeight() + reflectionGap, paint);

//                TextView tv = new TextView(mContext);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                tv.setLayoutParams(layoutParams);
//                tv.setText("Colgate: " + index);
//                tv.setTextColor(Color.BLACK);
//                tv.setBackgroundColor(Color.TRANSPARENT);
//                //tv.setVisibility(View.GONE);
//
//                Bitmap testB;
//
//                testB = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(testB);
//                tv.layout(0, 0, 80, 100);
//                tv.draw(c);
//
//                canvas.drawBitmap(testB, 10, height + height/2, null);


                ImageView imageView = new ImageView(mContext);
                imageView.setImageBitmap(bitmapWithReflection);
                CoverFlow.LayoutParams layoutParams = new CoverFlow.LayoutParams(500, 600);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setPadding(0, 100, 0, 0 );
                mImages[index] = imageView;

//                ImageView imageView = (ImageView)findViewById(R.id.item_image_corousel);
//                imageView.setImageBitmap(bitmapWithReflection);
//                imageView.setScaleType(ImageView.ScaleType.MATRIX);
//                mImages[index] = imageView;

//                View parentView = findViewById(R.id.image_review);
//                parentView.setLayoutParams(new CoverFlow.LayoutParams(400, 600));

//                ImageView imageViewCross = new ImageView(mContext);
//                imageViewCross.setImageDrawable(getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015));
//                //ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mContext, )
//                CoverFlow.LayoutParams params = new CoverFlow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.gravity = Gravity.CENTER_HORIZONTAL;
//                imageViewCross.setLayoutParams(params);

//
                TextView textView = new TextView(mContext);
                textView.setText("Colgate: " + index);
                textView.setLayoutParams(new CoverFlow.LayoutParams(300, 300));

//                CustomLayout customLayout = new CustomLayout(mContext);
//                customLayout.addView(imageView);
//                customLayout.addView(imageViewCross);
                //customLayout.setLayoutParams(new CoverFlow.LayoutParams(600, 800));
//                views[index] = parentView;

                index++;
                imageViewTextViewMap.put(imageView, textView);

               // ViewGroup viewGroup = new ViewGroup(mContext);

            }
            return true;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            //Use this code if you want to load from resources
            //ImageView i = new ImageView(mContext);
            //i.setImageResource(mImageIds[position]);
            //i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
            //i.setScaleType(ImageView.ScaleType.MATRIX);
            //return i;

            return mImages[position];
            //return views[position];
        }

        public Map<ImageView, TextView> getFlipViews(){
            return imageViewTextViewMap;
        }

        public ImageView[] getOriginalViews(){
            return mImages;
        }
        /**
         * Returns the size (0.0f to 1.0f) of the views
         * depending on the 'offset' to the center.
         */
        public float getScale(boolean focused, int offset) {
        /* Formula: 1 / (2 ^ offset) */
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
        }

    }

}
