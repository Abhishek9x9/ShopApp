package com.example.abhishekshukla.shopapp;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abhishekshukla.shopapp.dto.Product;
import com.example.abhishekshukla.shopapp.util.UIUtil;
import com.example.abhishekshukla.shopapp.provider.*;
/**
 *  @author abhishek
 */
public class ResultsManager {

    private static final int ISS_LOADER = 1;
    private final SearchActivity mParentActivity;
    private final ListView mListView;
    private final StoreSearchLoader mStoreSearchLoader;

    public static final String PRODUCT_CLICKED = "product.clicked";

    public ResultsManager(SearchActivity parentActivity, ListView listView) {
        mParentActivity = parentActivity;
        mListView = listView;
        mListView.setDividerHeight(0);
        mStoreSearchLoader = new StoreSearchLoader ();
    }
    
    /**
     * Close all pending search loaders
     */
    public void closePendingLoaders (){
        mStoreSearchLoader.close();
    }
    
    /**
     * Notify data updated when performing Store search
     * @param cursor
     *        The cursor of results.
     */
    public void notifyDataUpdated (Cursor cursor) {
        StoreSearchAdapter adapter = new StoreSearchAdapter (mParentActivity, cursor, false);
        mParentActivity.setHeaderVisibility((cursor != null && cursor.getCount() > 0) ? View.VISIBLE : View.GONE);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(adapter);
    }

    public void updateSearchQuery(String query) {
        mStoreSearchLoader.load(query);                
    }
    

    /**
     * Store search loader will retrieve A9 search suggestions in background
     * thread.
     * 
     */
    private class StoreSearchLoader implements LoaderCallbacks<Cursor> {
        
        private String mQuery;
        public void load (String query){
            mQuery = query;
            LoaderManager lm =  mParentActivity.getLoaderManager();
            lm.restartLoader(ISS_LOADER, null, this);
        }
        
        public void close() {
            mParentActivity.getLoaderManager().destroyLoader(ISS_LOADER);
        }

        //----------------------------------
        // Implementation of LoaderCallbacks
        //----------------------------------
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            if (id == ISS_LOADER) {
                Log.d("com.shopapp", "searching for query " + mQuery);
                Uri.Builder builder = Uri.parse("content://" + SuggestionsProvider.AUTHORITY + "/"+ SearchManager.SUGGEST_URI_PATH_QUERY).buildUpon();
                builder.appendQueryParameter("limit", String.valueOf(SuggestionsProvider.DEFAULT_LIMIT));
                return new CursorLoader(mParentActivity, builder.build(), SuggestionsProvider.COLUMNS, null, new String[]{mQuery}, null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            notifyDataUpdated(cursor);
            Log.d("com.shopapp", "loader finished " + mQuery);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d("com.shopapp", "loader reset " + mQuery);
        }
    }
    private static final String UTF_8 = "UTF-8";
    private class StoreSearchAdapter extends CursorAdapter implements OnItemClickListener {
        
        private Cursor mCursor;
        private Context mContext;
        public StoreSearchAdapter(Context context, Cursor cursor, boolean autoRequery) {
            super(context, cursor, autoRequery);
            mCursor = cursor;
            mContext = context;
        }

        /**
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UIUtil.closeSoftKeyboard(view);
            mCursor.moveToPosition(position);

            Product product = new Product();
            product.setId(Long.parseLong(mCursor.getString(0)));
            product.setTitle(mCursor.getString(1));
            product.setBrand(mCursor.getString(2));
            product.setImageUrl(mCursor.getString(3));
            product.setPrice(mCursor.getString(4));
            product.setAbout(mCursor.getString(5));

            Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
            intent.putExtra(PRODUCT_CLICKED, product);
            view.getContext().startActivity(intent);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.store_search_item, parent, false);
            updateItemView(context, cursor, layout);
            return layout;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            LinearLayout layout = (LinearLayout)view;
            updateItemView(context, cursor, layout);
        }
        
        private void updateItemView(Context context, Cursor cursor, LinearLayout layout) {
            String title = cursor.getString(1);
            String about = cursor.getString(5);

            TextView titleTextView =  (TextView)layout.findViewById(R.id.search_dropdown_item_text);
            titleTextView.setText(title);

            TextView aboutTextView =  (TextView)layout.findViewById(R.id.search_dropdown_store_name);
            aboutTextView.setText(about);
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            return null;
        }
    }
}
