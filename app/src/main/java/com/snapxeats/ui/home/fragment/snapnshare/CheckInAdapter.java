package com.snapxeats.ui.home.fragment.snapnshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pkmmte.view.CircularImageView;
import com.snapxeats.R;
import com.snapxeats.common.OnRecyclerItemClickListener;
import com.snapxeats.common.model.checkin.RestaurantInfo;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;

/**
 * Created by Snehal Tembare on 9/4/18.
 */

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.ViewHolder> {

    private Context mContext;
    private List<RestaurantInfo> mRestaurantList;
    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public CheckInAdapter(Context mContext,
                          List<RestaurantInfo> mRestaurantList,
                          OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mContext = mContext;
        this.mRestaurantList = mRestaurantList;
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_checkin, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantInfo restaurantInfo = mRestaurantList.get(position);
        holder.setItem(restaurantInfo);
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.parent_layout)
        LinearLayout mParentLayout;

        @BindView(R.id.img_restaurant)
        CircularImageView mImgRestaurant;

        @BindView(R.id.txt_rest_name)
        TextView mTxtRestName;

        @BindView(R.id.txt_food_category)
        TextView mTxtFoodCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setItem(RestaurantInfo restaurantInfo) {
            if (restaurantInfo.isSelected()) {
                mParentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.check_in_rest_selected));
            } else {
                mParentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            }
            Glide.with(mContext)
                    .load(restaurantInfo.getRestaurant_logo())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .dontTransform())
                    .thumbnail(THUMBNAIL)
                    .into(mImgRestaurant);

            mTxtRestName.setText(restaurantInfo.getRestaurant_name());
            mTxtFoodCategory.setText(restaurantInfo.getRestaurant_type());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnRecyclerItemClickListener.onClick(position, true);
        }
    }
}
