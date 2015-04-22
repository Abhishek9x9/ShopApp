package com.example.abhishekshukla.shopapp.util;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class ImageSub implements ImageLoader.ImageLoaderSubscriber
    {
        private ImageView imageView;

        private Activity activity;

        public ImageSub(ImageView imageView, Activity activity) {
            this.activity = activity;
            this.imageView = imageView;
        }

        public void onLoadStarted()
        {
            Log.d("ImageLoader", "download start");
        }

        public void onLoadCompleted(final Bitmap bitmap)
        {
            Log.d("ImageLoader", "download complete");
            Runnable runnable = new Runnable() {
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            };
            runnable.run();
        }
        public void onLoadFailed()
        {
            Log.e("ImageLoader", "download failed");
        }
    }