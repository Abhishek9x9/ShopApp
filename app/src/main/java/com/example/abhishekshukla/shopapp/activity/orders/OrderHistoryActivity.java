package com.example.abhishekshukla.shopapp.activity.orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.UserCart;
import com.example.abhishekshukla.shopapp.activity.review.CartItemReviewAdapter;
import com.example.abhishekshukla.shopapp.activity.review.CartReviewActivity;
import com.example.abhishekshukla.shopapp.dto.CartItem;
import com.example.abhishekshukla.shopapp.dto.OrderSummary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class OrderHistoryActivity  extends Activity {

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.cart_items);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        List<OrderSummary> orderSummaries = new ArrayList<>();
        for(int i = 0 ; i < 4; i++) {
            Random random = new Random();
            OrderSummary orderSummary = new OrderSummary(random.nextInt(999999 - 100000 + 1) + 100000,
                    random.nextInt(3000 - 500 + 1) + 500,
                    new Date(System.currentTimeMillis() - random.nextInt(999999)),
                    random.nextInt(20 - 5 + 1) + 5);
            orderSummaries.add(orderSummary);
        }
        ArrayAdapter<OrderSummary> adapter = new OrderAdapter(this, orderSummaries);

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

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;
                // ListView Clicked item value
                Object  itemValue    = listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CartReviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
