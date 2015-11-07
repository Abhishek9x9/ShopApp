package com.example.abhishekshukla.shopapp.activity.orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.activity.review.CartReviewActivity;
import com.example.abhishekshukla.shopapp.dto.CartItem;
import com.example.abhishekshukla.shopapp.dto.OrderSummary;

import java.util.List;

/**
 * Created by nishantgupta on 24/9/15.
 */
public class OrderAdapter extends ArrayAdapter<OrderSummary> {

    private LayoutInflater mInflater;

    public OrderAdapter(Context context, List<OrderSummary> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getOrderId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.order_summary , parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderSummary item = getItem(position);

        final TextView orderDate = (TextView) convertView.findViewById(R.id.order_date);
        orderDate.setText(item.getOrderDate().toString());

        TextView title = (TextView) convertView.findViewById(R.id.order_id);
        title.setText("Order #" + item.getOrderId());

        TextView price = (TextView) convertView.findViewById(R.id.order_amount);
        price.setText(item.getOrderValue() + " Rs/- ");

        TextView totalPrice = (TextView) convertView.findViewById(R.id.item_count);
        totalPrice.setText(item.getOrderItemCount() +  " Items ");

        ImageView orderDetails = (ImageView) convertView.findViewById(R.id.order_details);
        orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CartReviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;
    }
}
