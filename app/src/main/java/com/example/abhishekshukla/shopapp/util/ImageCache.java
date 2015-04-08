package com.example.abhishekshukla.shopapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import com.example.abhishekshukla.shopapp.ShopApplication;

/**
 * 
 * Image cache using android LruCache to cache in memory.
 * 
 * Cache every newly received image in disk, and remove it from disk when it's purged in memory cache.
 * 
 * @author abhishekshukla
 */
class ImageCache extends LruCache<String, Bitmap>{

    private static final String TAG = ImageCache.class.getSimpleName();
    private static final int DEFAULT_IMAGECACHE_CAPACITY = 30;
    private static final String IMAGE_FILE_POSTFIX = ".png";
    private final boolean mIsDiskCache;


    public ImageCache(final int imageCacheCapacity, final boolean isDiskCache ) {
        super(imageCacheCapacity);
        mIsDiskCache = isDiskCache;
    }

    public ImageCache(final boolean isDiskCache ) {
        this(DEFAULT_IMAGECACHE_CAPACITY, isDiskCache);
    }
    

    @Override
    public void entryRemoved(final boolean evicted, final String key, final Bitmap oldValue, final Bitmap newValue) {
        if (newValue == null) {
            try {
                new File(ShopApplication.getInstance().getFilesDir(), key + IMAGE_FILE_POSTFIX).delete();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return 1;
    }

    private void saveImageToDisk(final String key, final Bitmap value) {

        FileOutputStream fos = null;
        try {
            fos = ShopApplication.getInstance().openFileOutput(key + IMAGE_FILE_POSTFIX, Context.MODE_WORLD_READABLE);
            value.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if ( fos != null )
                    fos.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
    
    private synchronized String generateDigest(final String data) {
        return Base64.encodeToString(data.getBytes(), Base64.NO_WRAP).replace('/', '_');
    }
    

    public Bitmap putImage(final String key, final Bitmap value) {

        final String digestKey = generateDigest(key);
        if (mIsDiskCache)
            saveImageToDisk(digestKey, value);
        return put(digestKey, value);
    }
    

    public Bitmap getImage(final String key) {
        final String digestKey = generateDigest(key);
        return get(digestKey);
    }

    public void loadFromDiskCache() {
        File[] images = ShopApplication.getInstance().getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if ( filename.endsWith(IMAGE_FILE_POSTFIX) )
                    return true;
                else
                    return false;
            }
        });
        if ( !Util.isEmpty(images) ) {
            for ( File image : images ) {
                final String fileName = image.getName();
                final String key = fileName.substring(0, fileName.indexOf(IMAGE_FILE_POSTFIX));
                final Bitmap value = BitmapFactory.decodeFile(image.getAbsolutePath());
                if ( key != null && value != null )
                    put(key, value);
                else {
                    if ( key == null )
                        Log.e(TAG, "loadFromDiskCache: key is null, " + fileName);
                    if ( value == null )
                        Log.e(TAG, "loadFromDiskCache: value is null, " + fileName);
                }
            }
        }
    }
}
