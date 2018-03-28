package com.snapxeats.ui.restaurantInfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.snapxeats.R;
import com.snapxeats.common.model.restaurantDetails.RestaurantPics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
public class RestaurantInfoAdapter extends PagerAdapter {
    private Context mContext;
    private List<RestaurantPics> restaurantPicsList;
    private LayoutInflater layoutInflater;

    public RestaurantInfoAdapter(Context mContext, List<RestaurantPics> restaurantPicsList) {
        this.mContext = mContext;
        this.restaurantPicsList = restaurantPicsList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return restaurantPicsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.layout_rest_pics, container, false);

        ImageView imageView = itemView.findViewById(R.id.img_restaurant_pics);

        Picasso.with(mContext).load(restaurantPicsList.get(position).getDish_image_url()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }
}

