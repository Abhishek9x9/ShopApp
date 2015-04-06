package com.example.abhishekshukla.shopapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.util.Util;
import com.example.abhishekshukla.shopapp.util.UIUtil;

public class SearchActivity extends Activity {
    
    private TextView mSuggestionsLabel;
    private ListView mListView;
    private SearchEditText mQueryEditBox;
    private TextView cartTextView;
    private ImageButton mSearchButton;
    private ResultsManager mResultsManager;
    
    private static final String SEARCH_QUERY_KEY = "com.fv.search.QUERY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_layout);
        mSearchButton = (ImageButton) findViewById(R.id.search_button);
        mSuggestionsLabel = (TextView) findViewById(R.id.suggestions_label);
        cartTextView = (TextView) findViewById(R.id.cartTextView);
        mListView = (ListView) findViewById(R.id.search_list_view);
        mListView.setVerticalFadingEdgeEnabled(true);
        mResultsManager = new ResultsManager(this, mListView);
        setupQueryEditBox();

        mSearchButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                String keyWord = mQueryEditBox.getText().toString().trim();
                if (!Util.isEmpty(keyWord)) {
                    // do action here
                }
            }
        });

        if (savedInstanceState != null && !Util.isEmpty(savedInstanceState.getString(SEARCH_QUERY_KEY))) {
            String initialQuery = savedInstanceState.getString(SEARCH_QUERY_KEY);
            mQueryEditBox.setText(initialQuery);
            mQueryEditBox.setSelection(initialQuery.length());
        }
        cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
    }

    @Override
    protected  void  onResume () {
        super.onResume();
        cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_QUERY_KEY, mQueryEditBox.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        // Close all pending search loaders
        mResultsManager.closePendingLoaders();
        super.onDestroy();
    }


    private void setupQueryEditBox() {

        mQueryEditBox = (SearchEditText) findViewById(R.id.search_box);
        mQueryEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = mQueryEditBox.getText().toString();
                mResultsManager.updateSearchQuery(query);
            }
        });
        
        // Intercept the Go key event from the IME when performing store search and
        // launch shop app with the input query
        mQueryEditBox.setOnEditorActionListener(new OnEditorActionListener(){  

            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO ) { 
                    String keyWord = editText.getText().toString().trim();
                    if (!Util.isEmpty(keyWord)) {
                        UIUtil.closeSoftKeyboard(editText);
                        // do action here
                    }
                    return true;
                } 
                return false; 
            } 
        });
    }

    public void setHeaderVisibility(int visibility) {
        mSuggestionsLabel.setVisibility(visibility);
    }
}
