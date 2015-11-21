package com.example.abhishekshukla.shopapp.activity.cart.listview;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekshukla.shopapp.UserCart;
import com.example.abhishekshukla.shopapp.dto.CartItem;
import com.example.abhishekshukla.shopapp.util.ImageLoader;
import com.example.abhishekshukla.shopapp.util.ImageSub;
import com.example.abhishekshukla.shopapp.R;

public class GoogleCardsAdapter extends ArrayAdapter<CartItem> {
	
	private LayoutInflater mInflater;

	public GoogleCardsAdapter(Context context, List<CartItem> items) {
		super(context, 0, items);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cart_item , parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        final CartItem item = getItem(position);
        Bitmap bitmap =  ImageLoader.getInstance().loadImageAsync("http:" + item.getImageUrl(), new ImageSub(holder.image, (Activity) getContext()), "" + item.getId(), false);
		if(null != bitmap)
        {
            holder.image.setImageBitmap(bitmap);
        }

        final TextView itemCount = (TextView) convertView.findViewById(R.id.item_count);
        itemCount.setText("" + item.getItemCount());

        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        title.setText(item.getTitle());

        TextView price = (TextView) convertView.findViewById(R.id.item_price);
        price.setText(item.getSellingPrice() + " Rs/-");

        final TextView upArrow = (TextView)  convertView.findViewById(R.id.add_button);
        upArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count = item.getItemCount();
                count += 1;
                item.setItemCount(count);
                UserCart.getInstance().saveOrUpdateItemFromDb(item);
                itemCount.setText("" + count);
            }
        });

        TextView downArrow = (TextView)  convertView.findViewById(R.id.subtract_button);
        downArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count = item.getItemCount();
                count-= 1;
                if(count > 0) {
                    item.setItemCount(count);
                    UserCart.getInstance().saveOrUpdateItemFromDb(item);
                    itemCount.setText("" + count);
                }
            }
        });

        return convertView;
	}
	
	private static class ViewHolder {
		public ImageView image;
	}
}
