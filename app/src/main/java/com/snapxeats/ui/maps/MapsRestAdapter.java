package com.snapxeats.ui.maps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snapxeats.R;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Prajakta Patil on 1/4/18.
 */
public class MapsRestAdapter extends RecyclerView.Adapter<MapsRestAdapter.ViewHolder> {

    private RootCuisinePhotos stackData;
    private Context mContext;
    private static final double DIST_IN_MILES = 1609;
    private static final String LATITUDE = "40.4862157";
    private static final String LONGITUDE = "-74.4518188";

    MapsRestAdapter(Context context, RootCuisinePhotos stackData) {
        this.mContext = context;
        this.stackData = stackData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_maps_rest_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (0 != stackData.getDishesInfo().get(position).getRestaurantDishes().size()) {
            Picasso.with(holder.itemView.getContext())
                    .load(String.valueOf(stackData.getDishesInfo().get(position).getRestaurantDishes().get(0).getDish_image_url()))
                    .placeholder(R.drawable.foodstack_placeholder)
                    .into(holder.imgRestaurant);
        }
        holder.txtRestName.setText(stackData.getDishesInfo().get(position).getRestaurant_name());
        String price = stackData.getDishesInfo().get(position).getRestaurant_price();

        switch (price) {
            case "1":
                holder.txtPrice.setText(mContext.getString(R.string.price_one));
                break;
            case "2":
                holder.txtPrice.setText(mContext.getString(R.string.price_two));
                break;
            case "3":
                holder.txtPrice.setText(mContext.getString(R.string.price_three));
                break;
            case "4":
                holder.txtPrice.setText(mContext.getString(R.string.price_four));
                break;
            default:
                holder.txtPrice.setText(mContext.getString(R.string.price_one));
        }

        //TODO latlng are hardcoded for now
        double distVal = distInMiles(Double.parseDouble(LATITUDE),
                Double.parseDouble(LONGITUDE)
                , Double.parseDouble(stackData.getDishesInfo().get(position).getLocation_lat())
                , Double.parseDouble(stackData.getDishesInfo().get(position).getLocation_long()));

        String distance = String.valueOf(distVal).substring(0,3);
        holder.txtDistance.setText(distance+" "+mContext.getString(R.string.mi));
    }

    /*calculate distance in miles*/
    private double distInMiles(double srcLat, double srcLng, double destLat, double destLng) {
        double theta = srcLng - destLng;
        double dist = Math.sin(deg2rad(srcLat))
                * Math.sin(deg2rad(destLat))
                + Math.cos(deg2rad(srcLat))
                * Math.cos(deg2rad(destLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 / DIST_IN_MILES;
        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {
        return stackData.getDishesInfo().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRestaurant;
        private TextView txtPrice, txtDistance, txtRestName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgRestaurant = itemView.findViewById(R.id.img_rest_maps);
            txtPrice = itemView.findViewById(R.id.txt_price_maps);
            txtDistance = itemView.findViewById(R.id.txt_distance_maps);
            txtRestName = itemView.findViewById(R.id.txt_rest_maps);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Intent intent = new Intent(mContext, RestaurantInfoActivity.class);
                intent.putExtra(mContext.getString(R.string.intent_foodstackRestInfoId),
                        stackData.getDishesInfo().get(position).getRestaurant_info_id());
                mContext.startActivity(intent);
            });
        }
    }
}