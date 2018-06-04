package com.snapxeats.ui.restaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.R;
import com.snapxeats.common.model.restaurantInfo.RestaurantPics;
import com.snapxeats.ui.home.HomeDbHelper;

import java.util.List;

import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
public class RestImagesAdapter extends PagerAdapter {
    private Context mContext;
    private List<RestaurantPics> restaurantPicsList;
    private LayoutInflater layoutInflater;
    private OnViewpagerTap onViewpagerTap;

    HomeDbHelper homeDbHelper;

    public RestImagesAdapter(Context mContext,
                             HomeDbHelper homeDbHelper,
                             List<RestaurantPics> restaurantPicsList,
                             OnViewpagerTap onViewpagerTap) {
        this.mContext = mContext;
        this.restaurantPicsList = restaurantPicsList;
        this.onViewpagerTap = onViewpagerTap;
        this.homeDbHelper = homeDbHelper;
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
        ImageView mImgTextReview = itemView.findViewById(R.id.img_text_review);
        ImageView mImgAudioReview = itemView.findViewById(R.id.img_audio_review);
        ImageView mImgDownload = itemView.findViewById(R.id.img_download);
        ImageView imageView = itemView.findViewById(R.id.img_restaurant_pics);
        LinearLayout layoutControls = itemView.findViewById(R.id.layout_controls);

        RestaurantPics restaurantPic = restaurantPicsList.get(position);
        Glide.with(mContext)
                .load(restaurantPicsList.get(position).getDish_image_url())
                .placeholder(R.drawable.ic_rest_info_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).thumbnail(THUMBNAIL)
                .into(imageView);

        //Check duplicate entry for dish to download
        if (null != homeDbHelper) {
            if (homeDbHelper.isDuplicateSmartPhoto(restaurantPic.getRestaurant_dish_id())) {
                mImgDownload.setVisibility(View.GONE);
            } else {
                mImgDownload.setVisibility(View.VISIBLE);
            }
        }

        if (null != restaurantPic.getAudio_review_url() && !restaurantPic.getAudio_review_url().isEmpty()) {
            mImgAudioReview.setVisibility(View.VISIBLE);
        } else {
            mImgAudioReview.setVisibility(View.GONE);
        }

        if (null != restaurantPic.getText_review() && !restaurantPic.getText_review().isEmpty()) {
            mImgTextReview.setVisibility(View.VISIBLE);
        } else {
            mImgTextReview.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(v -> {
            if (onViewpagerTap != null) {
                onViewpagerTap.onImageTap(restaurantPic, itemView);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }

  public  interface OnViewpagerTap{
        void onImageTap(RestaurantPics restaurantPics,View itemView);
    }
}
