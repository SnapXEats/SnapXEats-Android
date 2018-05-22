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

import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
public class FoodStackAdapter extends ArrayAdapter<FoodStackData> {
    private List<FoodStackData> dataList;

    FoodStackAdapter(Context context, List<FoodStackData> dataList) {
        super(context, 0);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (null == contentView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_foodstack_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        FoodStackData stackData = dataList.get(position);
        holder.mTxtDishName.setText(stackData.getName());
        Glide.with(getContext())
                .load(stackData.getUrl().get(position))
                .thumbnail(THUMBNAIL)
                .into(holder.mImgDishes);
        return contentView;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    private static class ViewHolder {
        TextView mTxtDishName;
        ImageView mImgDishes;

        ViewHolder(View view) {
            mTxtDishName = view.findViewById(R.id.txt_card_rest_name);
            mImgDishes = view.findViewById(R.id.txt_card_rest_image);
        }
    }
}
