package com.snapxeats.ui.home.fragment.snapnshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.R;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import java.util.List;

import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;

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
        //Load image using Glide
        Glide.with(mContext)
                .load(mImageUrl.get(position).getDish_image_url())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                        .dontAnimate()
                        .dontTransform())
                .thumbnail(THUMBNAIL)
                .into(imageView);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
