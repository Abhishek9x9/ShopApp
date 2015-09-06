package com.example.abhishekshukla.shopapp.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.example.abhishekshukla.shopapp.util.Util;
import com.example.abhishekshukla.shopapp.dto.Product;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishekshukla on 30/3/15.
 */

public class SuggestionsProvider extends ContentProvider {
    private static final String TAG = SuggestionsProvider.class.getSimpleName();

    // the authority for this content provider
    public static final String AUTHORITY = "com.example.abhishekshukla.shopapp.provider.SuggestionsProvider";

    // search suggestion request
    private static final int SEARCH_SUGGEST = 0;

    // default query limit
    public static final int DEFAULT_LIMIT = 10;

    private static final String SUGGEST_URL = "http://10.14.124.103:9000/dummy/";

    // the URI matcher
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String[] COLUMNS = {
            "_id",
            "title",
            "brand",
            "image_url",
            "price",
            "about"
    };

    static {
        sURIMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        sURIMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String query = selectionArgs[0];
        String limitString = uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT);
        int limit = DEFAULT_LIMIT;
        if (!Util.isEmpty(limitString)) {
            try {
                limit = Integer.valueOf(limitString);
            } catch (NumberFormatException nfe) {
                limit = DEFAULT_LIMIT;
            }
        }

        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                return getSuggestions(query, limit);
            default:
                throw new IllegalArgumentException("Invalid URI " + uri);
        }
    }

    private Cursor getSuggestions(String query, int limit) {
        MatrixCursor cursor = new MatrixCursor(COLUMNS);
        if (query == null || query.length() == 0) {
            return cursor;
        }

        return getSuggestionsFromService(query, limit, cursor);
    }

    private Cursor getSuggestionsFromService(String query, int limit, MatrixCursor cursor) {
      //  try {
            int numberOfRows = 0;
            //Gson gson = new Gson();
            //URL url = new URL(SUGGEST_URL + query);
            //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //try {
              //  InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //StringWriter writer = new StringWriter();
                //IOUtils.copy(in, writer, "UTF-8");
                //String bodyContent = writer.toString();
                //List<Product> products = gson.fromJson(bodyContent, new TypeToken<List<Product>>(){}.getType());
                List<Product> products= new ArrayList<>();
                for(int i = 0 ; i < 4 ; i++){
                    Product product = new Product();
                    product.setId(i);
                    product.setAbout("This is about : " + i);
                    product.setBrand("Brand: " + i);
                    product.setPrice("" + i );
                    product.setTitle("Title" + i);
                    product.setImageUrl("https://dtgxwmigmg3gc.cloudfront.net/files/54100714c566d7637d001751-icon-256x256.png");

                    products.add(product);
                }
                for(Product product : products) {
                    if(numberOfRows >= limit)
                        return cursor;
                    cursor.addRow(createRows(product));
                    numberOfRows++;
                }
          //  }
         //   finally {
                 //   urlConnection.disconnect();
        //    }
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG, "Error", e);
//        } catch (IOException e) {
//            Log.e(TAG, "Error", e);
//        }
        return cursor;
    }

    private Object[] createRows(Product product) {

        return new String[] {
                Long.toString(product.getId()),
                product.getTitle(),
                product.getBrand(),
                product.getImageUrl(),
                product.getPrice(),
                product.getAbout()
        };
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectArgs) {
        throw new UnsupportedOperationException();
    }
}
