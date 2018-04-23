package com.snapxeats.ui.home.fragment.snapnshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.snapxeats.R;
import com.snapxeats.common.model.restaurantDetails.RestaurantPics;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Snehal Tembare on 17/4/18.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<RestaurantPics> mImageUrl;

    public ViewPagerAdapter(Context mContext, List<RestaurantPics> restaurant_pics) {
        this.mContext = mContext;
        mImageUrl = restaurant_pics;
    }

    @Override
    public int getCount() {
        return mImageUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.layout_rest_pics, container, false);
        ImageView imageView = view.findViewById(R.id.img_restaurant_pics);
        //Load image using picasso
        Picasso.with(mContext).load(mImageUrl.get(position).getDish_image_url())
                .placeholder(R.drawable.ic_cuisine_placeholder).into(imageView);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
