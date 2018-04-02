package com.snapxeats.ui.home.fragment.wishlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snapxeats.R;
import com.snapxeats.common.OnRecyclerItemClickListener;
import com.snapxeats.common.model.foodGestures.Wishlist;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.ui.home.fragment.wishlist.WishlistFragment.isMultipleDeleted;

/**
 * Created by Snehal Tembare on 27/3/18.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private Context mContext;
    List<Wishlist> wishlists;
    private OnRecyclerItemClickListener clickListener;

    WishlistAdapter(Context mContext,
                    List<Wishlist> wishlists,
                    OnRecyclerItemClickListener clickListener) {
        this.mContext = mContext;
        this.wishlists = wishlists;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_wishlist, parent,
                false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(wishlists.get(position));
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.view_foreground)
        LinearLayout mLayoutFg;

        @BindView(R.id.image_view)
        ImageView mImageView;

        @BindView(R.id.txt_restaurant_name)
        TextView mTxtRestaurantName;

        @BindView(R.id.txt_location_date)
        TextView mTxtLocationDate;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setItem(Wishlist item) {
            if (item.isDeleted()) {
                mLayoutFg.setBackground(mContext.getDrawable(R.drawable.custom_edittext));
            } else {
                mLayoutFg.setBackground(mContext.getDrawable(R.drawable.wishlist_layout_white));
            }
            Picasso.with(mContext).load(item.getDish_image_url()).into(mImageView);
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

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (isMultipleDeleted) {
                clickListener.onClickToDelete(position, wishlists.get(position));
            } else {
                clickListener.onClick(wishlists.get(position));
            }
        }
    }
}
