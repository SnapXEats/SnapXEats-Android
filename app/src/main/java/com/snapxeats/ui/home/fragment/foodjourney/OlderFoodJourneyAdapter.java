package com.snapxeats.ui.home.fragment.foodjourney;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.snapxeats.R;
import com.snapxeats.common.model.foodJourney.UserPastHistory;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prajakta Patil on 14/5/18.
 */
public class OlderFoodJourneyAdapter extends BaseAdapter {

    Context mContext;
    List<UserPastHistory> foodJourneys;

    OlderFoodJourneyAdapter(Context context, List<UserPastHistory> foodJourney) {
        this.mContext = context;
        this.foodJourneys = foodJourney;
    }

    @Override
    public int getCount() {
        return foodJourneys.size();
    }

    @Override
    public Object getItem(int position) {
        return foodJourneys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_older_food_journey, null);
            new OlderFoodJourneyAdapter.ViewHolder(convertView);
        }
        OlderFoodJourneyAdapter.ViewHolder holder = (OlderFoodJourneyAdapter.ViewHolder) convertView.getTag();
        holder.setData(foodJourneys.get(position));
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.image_view)
        ImageView mImageView;

        @BindView(R.id.txt_restaurant_name)
        TextView mTxtRestaurantName;

        @BindView(R.id.txt_address)
        TextView mTxtAddress;

        @BindView(R.id.txt_reward_points)
        TextView mTxtPoints;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }

        void setData(UserPastHistory item) {
            Picasso.with(mContext).load(item.getRestaurant_image_url()).into(mImageView);
            mTxtRestaurantName.setText(item.getRestaurant_name());
            mTxtAddress.setText(item.getRestaurant_address());
            mTxtPoints.setText(item.getReward_point());
        }
    }
}