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
 import android.os.Parcelable;
 import android.support.annotation.NonNull;
 import android.support.v7.app.ActionBarActivity;
 import android.util.Log;
 import android.util.TypedValue;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.Window;
 import android.widget.BaseAdapter;
 import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.ListView;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import android.widget.Toast;
 import android.app.Activity;


 import com.example.abhishekshukla.shopapp.adapter.GoogleCardsAdapter;

 import com.example.abhishekshukla.shopapp.auth.RegistrationActivity;
 import com.example.abhishekshukla.shopapp.carousel.CartDetailCarouselAcitivity;
 import com.example.abhishekshukla.shopapp.dto.CartItem;
 import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
 import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SwipeUndoAdapter;
 import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoCallback;
 import com.nhaarman.listviewanimations.util.ListViewWrapper;

public class CartActivity extends Activity implements UndoCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;

    private GoogleCardsAdapter mGoogleCardsAdapter;
    private SwipeUndoAdapter swipeUndoAdapter;

    private TextView cartTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_view);
        cartTextView = (TextView) findViewById(R.id.cartTextView);
        ListView listView = (ListView) findViewById(R.id.list_view);

        ImageView caraoselView = (ImageView) findViewById(R.id.item_image_carousel);

        caraoselView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CartDetailCarouselAcitivity.class);
                view.getContext().startActivity(intent);
            }
        });

        mGoogleCardsAdapter = new GoogleCardsAdapter(this, UserCart.getInstance().getAllProducts());
        swipeUndoAdapter = new SwipeUndoAdapter(mGoogleCardsAdapter, this) {
            @Override
            public void setListViewWrapper(@NonNull ListViewWrapper listViewWrapper) {
                super.setListViewWrapper(listViewWrapper);
            }
        };
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(swipeUndoAdapter);
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
                Toast.makeText(v.getContext(), "Checkout is clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
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
        for (final int position : reverseSortedPositions) {
            Log.d("Card position", "" + position);
            final CartItem cartItem = mGoogleCardsAdapter.getItem(position);
            UserCart.getInstance().removeItem(mGoogleCardsAdapter.getItem(position).getId());
            mGoogleCardsAdapter.remove(cartItem);
            UserCart.getInstance().removeItem(cartItem.getId());
            cartTextView.setText(Integer.toString(UserCart.getInstance().getCartSize()));
        }
    }

    @NonNull
    @Override
    public View getPrimaryView(@NonNull View view) {
        return view.findViewById(R.id.main_item_view);
    }

    @NonNull
    @Override
    public View getUndoView(@NonNull View view) {
        return view.findViewById(R.id.undoBar);
    }

    @Override
    public void onUndoShown(@NonNull final View view, final int position) {
        view.findViewById(R.id.undo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.undoBar).setVisibility(View.GONE);
                swipeUndoAdapter.undo(view);
            }
        });

        view.findViewById(R.id.item_delete_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeUndoAdapter.dismiss(position);
            }
        });
    }

    @Override
    public void onUndo(@NonNull View view, int position) {
    }

    @Override
    public void onDismiss(@NonNull View view, int position) {

    }
}