package com.example.abhishekshukla.shopapp.barcode.integration.eandata;

import android.os.StrictMode;
import android.util.Log;

import com.example.abhishekshukla.shopapp.cache.ProductCache;
import com.example.abhishekshukla.shopapp.dto.Product;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by nishantgupta on 24/10/15.
 * out of milk: http://api.outofmilkapp.com/v7/ProductCode.asmx/Lookup?upc=8901277008442
 * barcode1 : https://barcode1.co.uk/barcodelookup/lib/scraper/barcodeLookupService.php?source=amazon&q=8901030340987
 * eandata: http://eandata.com/feed/?v=3&keycode=7DC55A1A562B7421&mode=json&find=8901277008442
 */
public class ProductLookup {

    private static final String TAG = ProductLookup.class.getSimpleName();
    private static final String PRODUCT_SEARCH_URL = "http://eandata.com/feed/?v=3&keycode=7DC55A1A562B7421&mode=json&find=";
    private static final int retryCount = 2;

    public static Product getProductByBarCode(String barcode) {
        try {
            Product cachedProduct = ProductCache.getInstance().getProduct(barcode);
            if (cachedProduct != null) {
                return cachedProduct;
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            int retry = 0;

            Product product = null;
            while (retry < retryCount && product == null) {
                try {
                    URL url = new URL(PRODUCT_SEARCH_URL + barcode);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        StringWriter writer = new StringWriter();
                        IOUtils.copy(in, writer, "UTF-8");
                        String bodyContent = writer.toString();
                        Log.v(ProductLookup.class.getSimpleName(), bodyContent);
                        JSONObject jsonObject = new JSONObject(bodyContent);
                        if ((((JSONObject) jsonObject.get("status")).get("code").equals("200"))) {
                            JSONObject productJson = (JSONObject) jsonObject.get("product");
                            JSONObject productAttributesJson = (JSONObject) productJson.get("attributes");
                            if (productAttributesJson != null) {
                                product = new Product();
                                product.setTitle(productAttributesJson.get("product").toString());
                                product.setCategory(productAttributesJson.get("category_text").toString());
                                product.setImageUrl(productJson.get("image").toString());
                                product.setBrand(productAttributesJson.get("category_text").toString());
                                Integer price = (int) (Double.parseDouble(productAttributesJson.get("price_new").toString()) * 60);
                                Random rand = new Random();
                                int saving = price - rand.nextInt((price.intValue() - 1) + 1) + 1;
                                product.setOriginalPrice(price);
                                product.setSaving(saving + " Rs/-");
                                product.setSellingPrice((price - saving));
                                product.setId(Long.parseLong(productJson.get("EAN13").toString()));
                            }
                        }
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Error", e);
                } catch (IOException e) {
                    Log.e(TAG, "Error", e);
                } finally {
                    Log.d(ProductLookup.class.getSimpleName(), "retry count: " + retry);
                    retry = retry + 1;
                }
            }
            if (product != null) {
                ProductCache.getInstance().updateCache(barcode, product);
            }
            return product;
        }catch (Exception e){
            Log.e(TAG, "Error", e);
            return null;
        }
    }
}
