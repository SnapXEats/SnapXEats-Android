package com.snapxeats.ui.restaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
 * Created by Prajakta Patil on 14/2/18.
 */
public class RestImagesAdapter extends PagerAdapter {
    private Context mContext;
    private List<RestaurantPics> restaurantPicsList;
    private LayoutInflater layoutInflater;

    public RestImagesAdapter(Context mContext, List<RestaurantPics> restaurantPicsList) {
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

        Glide.with(mContext)
                .load(restaurantPicsList.get(position).getDish_image_url())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                        .dontAnimate()
                        .dontTransform())
                .thumbnail(THUMBNAIL).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }
}
