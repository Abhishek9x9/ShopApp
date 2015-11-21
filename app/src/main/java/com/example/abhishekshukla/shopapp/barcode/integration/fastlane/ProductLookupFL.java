package com.example.abhishekshukla.shopapp.barcode.integration.fastlane;

import android.os.StrictMode;
import android.util.Log;

import com.example.abhishekshukla.shopapp.cache.ProductCache;
import com.example.abhishekshukla.shopapp.dto.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import lombok.Data;

/**
 * Created by nishantgupta on 22/11/15.
 */
public class ProductLookupFL {

    private static final String TAG = ProductLookupFL.class.getSimpleName();
    private static final String PRODUCT_SEARCH_URL = "http://ec2-52-91-149-37.compute-1.amazonaws.com:8080/curator/inventory/281/current/";

    private static final Gson gson = new Gson();

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

            try {
                URL url = new URL(PRODUCT_SEARCH_URL + barcode);
                Log.v(TAG, url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(in, writer, "UTF-8");
                    String bodyContent = writer.toString();
                    Log.v(TAG, bodyContent);
                    ProductResponse productResponse = gson.fromJson(bodyContent, ProductResponse.class);
                    Map<String, ProductFL> productsUpdatedItemsJsonMap = productResponse.getUpdatedItems();
                    Map<String, ProductFL> productsAddedItemsJsonMap = productResponse.getAddedItems();
                    Map<String, ProductFL> productsJsonMap = productsAddedItemsJsonMap.values().size() > 0 ?
                            productsAddedItemsJsonMap : productsUpdatedItemsJsonMap;
                    if (productsJsonMap != null && productsJsonMap.values() != null && productsJsonMap.values().size() > 0) {
                        ProductFL productJson = productsJsonMap.values().iterator().next();
                        if (productJson != null) {
                            product = new Product();
                            product.setTitle(productJson.getProductInfo().getName());
                            product.setCategory("Dummy Category");
                            product.setImageUrl(productJson.getProductInfo().getImageJSON());
                            product.setBrand(productJson.getProductInfo().getBrandName());
                            double price = (int) productJson.getItemPrice().getPrice();
                            if (productJson.getItemPrice().isDiscount()) {
                                product.setSaving(productJson.getItemPrice().getDiscountDetail());
                            }
                            if (productJson.getItemPrice().isMRP()) {
                                product.setOriginalPrice(productJson.getItemPrice().getMrp());
                            } else {
                                product.setOriginalPrice(price);
                            }
                            product.setSellingPrice(price);
                            product.setId(Long.parseLong(productJson.getBarcode()));
                        }
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }

            if (product != null) {
                ProductCache.getInstance().updateCache(barcode, product);
            }
            return product;
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            return null;
        }
    }

    @Data
    private static class ProductResponse {
        private Map<String, ProductFL> updatedItems;
        private Map<String, ProductFL> addedItems;
        private boolean isError;
    }

    @Data
    private static class ItemPrice {
        private boolean isMRP;
        private double mrp;
        private double price;
        private boolean isBasePrice;
        private double basePrice;
        private boolean isDiscount;
        private String discountDetail;
    }

    @Data
    private static class ProductInfo {
        private String name;
        private String imageJSON;
        private String brandName;
    }

    @Data
    private static class ProductFL {
        private String itemCode;
        private String barcode;
        private ItemPrice itemPrice;
        private ProductInfo productInfo;
    }
}

