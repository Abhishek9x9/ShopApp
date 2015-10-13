package com.example.abhishekshukla.shopapp.review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.dto.CartItem;

import java.util.List;

/**
 * Created by nishantgupta on 24/9/15.
 */
public class CartItemReviewAdapter extends ArrayAdapter<CartItem> {

    private LayoutInflater mInflater;

    public CartItemReviewAdapter(Context context, List<CartItem> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_item_review , parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CartItem item = getItem(position);

        final TextView itemCount = (TextView) convertView.findViewById(R.id.item_count);
        itemCount.setText("" + item.getItemCount());

        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        title.setText(item.getTitle());

        TextView price = (TextView) convertView.findViewById(R.id.item_price);
        price.setText(item.getPrice());

        TextView totalPrice = (TextView) convertView.findViewById(R.id.item_total_price);
        totalPrice.setText(item.getPrice());

        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;
    }
}
