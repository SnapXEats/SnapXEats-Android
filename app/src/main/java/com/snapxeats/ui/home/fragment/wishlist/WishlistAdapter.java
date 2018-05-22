package com.snapxeats.ui.home.fragment.wishlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.snapxeats.R;
import com.snapxeats.common.model.foodGestures.Wishlist;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;

/**
 * Created by Snehal Tembare on 2/4/18.
 */

public class WishlistAdapter extends BaseAdapter {

    Context mContext;
    List<Wishlist> wishlist;

    WishlistAdapter(Context context,
                    List<Wishlist> wishlist) {
        this.mContext = context;
        this.wishlist = wishlist;
    }

    @Override
    public int getCount() {
        return wishlist.size();
    }

    @Override
    public Object getItem(int position) {
        return wishlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_wishlist, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.setItem(wishlist.get(position));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.view_foreground)
        LinearLayout mLayoutFg;

        @BindView(R.id.image_view)
        ImageView mImageView;

        @BindView(R.id.txt_restaurant_name)
        TextView mTxtRestaurantName;

        @BindView(R.id.txt_location_date)
        TextView mTxtLocationDate;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }

        void setItem(Wishlist item) {
            if (item.isDeleted()) {
                mLayoutFg.setBackground(mContext.getDrawable(R.drawable.custom_edittext));
            } else {
                mLayoutFg.setBackground(mContext.getDrawable(R.drawable.wishlist_layout_white));
            }
            Glide.with(mContext).load(item.getDish_image_url())
                    .thumbnail(THUMBNAIL)
                    .into(mImageView);

            mTxtRestaurantName.setText(item.getRestaurant_name());
            DateFormat simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.yyyy_MM_dd));
            Date date = null;
            String stringDate = null;
            try {
                date = simpleDateFormat.parse(item.getCreated_at());
                SimpleDateFormat dateFormat = new SimpleDateFormat(mContext.getString(R.string.dd_MMM_yyyy));
                stringDate = dateFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            mTxtLocationDate.setText(item.getRestaurant_address() + "  |  " + stringDate);
        }
    }
}
