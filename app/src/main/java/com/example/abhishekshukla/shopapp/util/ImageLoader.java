package com.example.abhishekshukla.shopapp.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.abhishekshukla.shopapp.R;

import com.example.abhishekshukla.shopapp.ShopApplication;

/**
 * Manage loading image from content provider or web address.
 *
 * We use LruCache to cache all received images.
 *
 * @author abhishekshukla
 *
 */
public final class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    // http request time out 10 seconds
    private static final int HTTP_TIMEOUT = 10 * 1000;
    // the maximum thread number to load images asynchronously.
    private static final int CURRENT_THREAD_NUM = 12;

    private static ImageLoader sInstance = null;

    // LruCache for images.
    private ImageCache mImageCache = null;

    private Map<String, Object> mPendingBitmap = null;
    private final Context mContext;

    private static final Executor sExecutor = Executors.newFixedThreadPool(CURRENT_THREAD_NUM);

    private AtomicInteger mImageLoadingCounter = new AtomicInteger();

    private ImageLoader() {
        mContext = ShopApplication.getInstance().getBaseContext();
        mPendingBitmap = new ConcurrentHashMap<String, Object>();
        mImageCache = new ImageCache(true);
    }

    public synchronized static ImageLoader getInstance() {
        if ( sInstance == null ){
            if (ShopApplication.DEBUG) {
                Log.d(TAG, "Construct ImageLoader instance.");
            }
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    public Bitmap loadImageAsync(final String url, final ImageLoaderSubscriber subscriber, final String key, final boolean forceReload) throws IllegalArgumentException {
        final int width = mContext.getResources().getDimensionPixelSize(R.dimen.item_image_width);
        final int height = mContext.getResources().getDimensionPixelSize(R.dimen.item_image_height);

        if ( Util.isEmpty(url) ) {
            throw new IllegalArgumentException(TAG + ": image url can not be null or empty");
        }

        // check cached here,
        // return directly if the expected bitmap is cached already.
        final Bitmap result = mImageCache.getImage(key);
        if ((result == null) || forceReload)
        {
            mImageLoadingCounter.incrementAndGet();
            sExecutor.execute(new LoadImageRunnable(url, width, height, subscriber, key, forceReload));
        }
        return result;
    }

    private Bitmap loadImage(final String url, final int width, final int height, final String key, final boolean forceReload) throws IllegalArgumentException {
        if ( Util.isEmpty(url) )
            throw new IllegalArgumentException(TAG + ": image url can not be null or empty.");

        if (! forceReload) {
            // check cache.
            Bitmap bitmap = mImageCache.getImage(key);
            if (bitmap != null) {
                return bitmap;
            }
        }

        boolean waitForLock = false;
        Object obj = null;
        synchronized(mPendingBitmap) {
            // check if the current image is pending to fetch.
            obj = mPendingBitmap.get(url);
            if (obj != null)
                // wait to finish fetching image.
                waitForLock = true;
            else
                // insert to pendling list.
                mPendingBitmap.put(url, new Object());
        }

        if (waitForLock)
        {
            try {
                synchronized(obj) {
                    if (ShopApplication.DEBUG)
                        Log.d(TAG, Thread.currentThread().getId() + " pending wait: " + url);
                    obj.wait();
                }
                if (ShopApplication.DEBUG)
                    Log.d(TAG, Thread.currentThread().getId() + "notified wait: " + url);
                // get bitmap from cache again
                return mImageCache.getImage(key);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        try {
            if (ShopApplication.DEBUG)
                Log.d(TAG,Thread.currentThread().getId() +  "fetching image: " + url);
            final Bitmap originalBitmap = getRawBitmap(url, width, height);
            Bitmap bitmap = null;
            if ( originalBitmap != null ) {
                if ( originalBitmap.getWidth() > width || originalBitmap.getHeight() > height ) {
                    // compress if necessary to save space.
                    bitmap = compressBitmap(originalBitmap, width, height);
                }
                else
                    bitmap = originalBitmap;
                mImageCache.putImage(key, bitmap);
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } finally {
            // no matter what, remove this url from pending list and notify all which are waiting on this object.
            final Object pendingObj = mPendingBitmap.remove(url);
            synchronized(pendingObj) {
                pendingObj.notifyAll();
            }
        }
    }

    /**
     * Compress image to save memory space.
     *
     * @param orgBmp
     * @param width
     * @param height
     * @return
     */
    private static Bitmap compressBitmap(final Bitmap orgBmp, final int width, final int height) {
        final float factorH = height/(float) orgBmp.getHeight();
        final float factorW = width/(float) orgBmp.getWidth();
        final float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(orgBmp, (int) (orgBmp.getWidth() * factorToUse), (int) (orgBmp.getHeight() * factorToUse), false);
    }

    private Bitmap getRawBitmap(final String bitmapUri, final int width, final int height) {
        Bitmap bitmap = null;
        final String extraLogString = "loading image: ";
        try {
            Log.d(TAG, extraLogString + bitmapUri);
                bitmap = getBitmapFromWeb(bitmapUri, width, height);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return bitmap;
    }

    /**
     * Load image from http/https address.
     * @param bitmapUrl
     * @return
     * @throws java.io.IOException
     */
    private Bitmap getBitmapFromWeb(final String bitmapUrl, final int width, final int height) {
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            final URL url = new URL(bitmapUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(HTTP_TIMEOUT);
            connection.setConnectTimeout(HTTP_TIMEOUT);
            connection.connect();
            // fix a known android bug: code.google.com/p/android/issues/detail?id=6066
            in = new FlushedInputStream(connection.getInputStream());
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG, "getBitmapFromUri: " + bitmapUrl , e);
            return null;
        } finally {
            if (in != null)
            {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            if (connection != null)
                connection.disconnect();
        }
    }

    private static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(final InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while ( totalBytesSkipped < n ) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if ( bytesSkipped == 0L ) {
                    int readByte = read();
                    if (readByte <0)
                        break;
                    else
                        bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    /**
     * Load images from disk cache.
     */
    public void loadFromDiskCache() {
        mImageCache.loadFromDiskCache();
    }

    /**
     * Inner class to support loading image asynchronously.
     * @author abhishekshukla
     *
     */
    private class LoadImageRunnable implements Runnable{
        private final ImageLoaderSubscriber mSubscriber;
        private final String mUri;
        private final int mWidth;
        private final int mHeight;
        private final String mkey;
        private final boolean mForceReload;

        public LoadImageRunnable(final String uri, final int width, final int height, final ImageLoaderSubscriber subscriber, final String asin, final boolean forceReload)
        {
            mUri = uri;
            mSubscriber = subscriber;
            mWidth = width;
            mHeight = height;
            mkey = asin;
            mForceReload = forceReload;
        }

        @Override
        public void run() {

            if ( mSubscriber != null )
                mSubscriber.onLoadStarted();
            Bitmap bitmap = loadImage(mUri, mWidth, mHeight, mkey, mForceReload);
            mImageLoadingCounter.decrementAndGet();
            if ( mSubscriber != null ) {
                if (bitmap == null)
                    mSubscriber.onLoadFailed();
                else
                    mSubscriber.onLoadCompleted(bitmap);
            }
        }
    }

    public static interface ImageLoaderSubscriber {
        public void onLoadStarted();
        public void onLoadCompleted(final Bitmap bitmap);
        public void onLoadFailed();
    }
}
