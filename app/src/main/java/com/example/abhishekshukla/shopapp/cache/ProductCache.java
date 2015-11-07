package com.example.abhishekshukla.shopapp.cache;

import android.content.Context;
import android.util.Log;

import com.example.abhishekshukla.shopapp.auth.UserInfo;
import com.example.abhishekshukla.shopapp.dto.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nishantgupta on 7/11/15.
 */
public class ProductCache {
    private static Map<String, Product> cache = new HashMap<>();
    private static Gson gson = new Gson();
    private static final String productInfoFile = "product_cache";

    private ProductCache() {
        //
    }

    public Product getProduct(String barCode){
        return cache.get(barCode);
    }

    public void updateCache(String barCode, Product product){
        cache.put(barCode, product);
    }

    public static ProductCache _instance = new ProductCache();

    public static ProductCache getInstance() {
        return _instance;
    }

    public void initCache(Context context) {
        Log.v(ProductCache.class.getSimpleName() + "@initCache", "");
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = context.openFileInput(productInfoFile);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine();
            }
            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Type mapType = new TypeToken<Map<String, Product>>() {}.getType();
        if(datax.toString() != null && datax.toString() != "") {
            cache = gson.fromJson(datax.toString(), mapType);
        }
        Log.v(ProductCache.class.getSimpleName() + "@initCache:done", datax.toString());
    }

    public void saveProductCache(Context context) {
        String cacheGson = gson.toJson(cache);
        Log.v(ProductCache.class.getSimpleName() + "@saveProductCache", cacheGson);
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(productInfoFile, Context.MODE_WORLD_READABLE);
            outputStream.write(cacheGson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
