package com.example.abhishekshukla.shopapp;

/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 import android.content.Intent;
 import android.content.res.Resources;
 import android.os.Bundle;
 import android.support.annotation.NonNull;
 import android.support.v7.app.ActionBarActivity;
 import android.util.Log;
 import android.util.TypedValue;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.ListView;
 import android.widget.TextView;
 import android.widget.Toast;
 import android.app.Activity;


 import com.example.abhishekshukla.shopapp.adapter.GoogleCardsAdapter;

 import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
 import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
 import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

public class CartActivity extends Activity implements OnDismissCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;

    private GoogleCardsAdapter mGoogleCardsAdapter;

    private TextView cartTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_view);
        cartTextView = (TextView) findViewById(R.id.cartTextView);
        ListView listView = (ListView) findViewById(R.id.list_view);

        mGoogleCardsAdapter = new GoogleCardsAdapter(this, UserCart.getInstance().getAllProducts());
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);

        listView.setClipToPadding(false);
        listView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());
        listView.setDividerHeight(px);
        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
        listView.setPadding(px, px, px, px);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setAdapter(swingBottomInAnimationAdapter);


        Button placeOrderButton = (Button)findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddressActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected  void  onResume () {
        super.onResume();
        cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView,
                          @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            Log.d("Card position", "" + position);
            UserCart.getInstance().removeItem(mGoogleCardsAdapter.getItem(position).getId());
            mGoogleCardsAdapter.remove(mGoogleCardsAdapter.getItem(position));
            cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
        }
    }
}