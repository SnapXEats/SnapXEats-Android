package com.snapxeats.ui.foodstack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.snapxeats.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
public class SwipeFoodStackAdapter extends ArrayAdapter<FoodStackData> {
    private Map<String, List<String>> listHashMap;

    SwipeFoodStackAdapter(Context context/*, Map<String, List<String>> listHashMap*/) {
        super(context, 0);
        this.listHashMap = listHashMap;
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup parent) {
       /* ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_foodstack_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        for (Map.Entry<String, List<String>> entry : listHashMap.entrySet()) {
            String key = entry.getKey();
            holder.mTxtDishName.setText(key);

            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                Picasso.with(getContext()).load(values.get(i)).placeholder(R.drawable.ic_cuisine_placeholder).into(holder.imgDishes);
            }
        }*/

        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_foodstack_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        FoodStackData spot = getItem(position);

        holder.mTxtDishName.setText(spot.name);
//        holder.city.setText(spot.city);

        Glide.with(getContext()).load(spot.url).into(holder.imgDishes);

        return contentView;
    }

    private static class ViewHolder {
        TextView mTxtDishName;
         ImageView imgDishes;

         ViewHolder(View view) {
            this.mTxtDishName = view.findViewById(R.id.txt_card_rest_name);
            this.imgDishes = view.findViewById(R.id.txt_card_rest_image);
        }
    }
}
